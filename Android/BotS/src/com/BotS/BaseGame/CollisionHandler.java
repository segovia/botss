package com.BotS.BaseGame;

import com.BotS.BaseGame.CollisionObject.CollisionObjectType;
import com.BotS.BaseGame.Fireball.FireballSensor;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

public class CollisionHandler {

	private MagicHandler magicHandler;
	private GameScene gameScene;
	private ParticleSystem particleSystem;
	private World world;

	public CollisionHandler(GameScene pGameScene) {
		gameScene = pGameScene;
		world = gameScene.physicsWorld.getWorld();
		particleSystem = gameScene.physicsWorld.getParticleSystem();
		magicHandler = new MagicHandler(gameScene);
	}

	public void onUpdate() {
		handleObjectToWaterCollisions();
		handleObjectToObjectCollisions();
	}

	private void handleObjectToWaterCollisions() {
		int bodyOnWaterCount = particleSystem.getUniqueBodyContactCount(30);
		if (bodyOnWaterCount > 0) {
			int[] bodyContacts = new int[bodyOnWaterCount];
			particleSystem.populateUniqueBodyContacts(bodyContacts);
			for (int i = 0; i < bodyOnWaterCount; ++i) {
				int bodyId = bodyContacts[i];
				CollisionObject collisionObject = CollisionObjectManager.get(bodyId);
				if (collisionObject == null) {
					continue;
				}
				if (collisionObject.getType() == CollisionObjectType.FIREBALL) {
					Fireball fireball = (Fireball) collisionObject;
					if (fireball.getflagSpell() == false) {
						handleFireballOnWater((Fireball) collisionObject);

					}
					if (fireball.getflagSpell() == true) {
						handleSpellFireballOnWater((Fireball) collisionObject);
					}
				}
			}
		}
	}

	private void handleObjectToObjectCollisions() {
		int beginContactsArrayCount = world.getBeginContactsArraySize();
		if (beginContactsArrayCount > 0) {
			int[] beginContactsArray = new int[beginContactsArrayCount];
			world.populateBeginContacts(beginContactsArray);
			for (int i = 0; i < beginContactsArrayCount / 2; ++i) {
				int bodyAId = beginContactsArray[2 * i];
				int bodyBId = beginContactsArray[2 * i + 1];
				CollisionObject collisionObjectA = CollisionObjectManager.get(bodyAId);
				CollisionObject collisionObjectB = CollisionObjectManager.get(bodyBId);
				if (collisionObjectA == null || collisionObjectB == null) {
					continue;
				}
				// Fireball x other object
				if (collisionObjectA.getType() == CollisionObjectType.FIREBALL) {
					handleFireballCollisionReal((Fireball) collisionObjectA, collisionObjectB);
				} else if (collisionObjectB.getType() == CollisionObjectType.FIREBALL) {
					handleFireballCollisionReal((Fireball) collisionObjectB, collisionObjectA);
				} else if (collisionObjectA.getType() == CollisionObjectType.FIREBALL_SENSOR) {
					handleFireballCollision(((FireballSensor) collisionObjectA).getFireball(), collisionObjectB);
				} else if (collisionObjectB.getType() == CollisionObjectType.FIREBALL_SENSOR) {
					handleFireballCollision(((FireballSensor) collisionObjectB).getFireball(), collisionObjectA);
				} else if (collisionObjectA.getType() == CollisionObjectType.BOAT) {
					handleBoatCollision((Boat) collisionObjectA, collisionObjectB);
				} else if (collisionObjectB.getType() == CollisionObjectType.BOAT) {
					handleBoatCollision((Boat) collisionObjectB, collisionObjectA);
				}
			}
		}
	}

	private void handleFireballOnWater(Fireball fireball) {
		ResourcesManager.getInstance().FireballHitWater.play();
		fireball.resetToStart();
	}

	private void handleSpellFireballOnWater(Fireball fireball) {
		magicHandler.magicHandle(fireball, fireball.getSpellLevel());
	}

	public void handleFireballCollisionReal(Fireball fireball, CollisionObject other) {

		if (other.getType() == CollisionObjectType.CASTLE) {

			Castle castle = (Castle) other;
			ResourcesManager.getInstance().FireballHitShield.play();
			castle.showShield();

		}

	}

	public void handleFireballCollision(Fireball fireball, CollisionObject other) {
		if (other.getType() == CollisionObjectType.BOAT) {
			Boat boat = (Boat) other;
			if (fireball.getflagSpell() == false) {
				if (fireball.getOwner() == boat.owner) {
					fireball.getOwner().increaseDestFriendly();
				} else {
					fireball.setEnemyHitBoat(true);
					fireball.getOwner().increaseDestEnemies();
				}
				boat.hit();
			}

		} else if (other.getType() == CollisionObjectType.BIRD) {
			MagicBird bird = (MagicBird) other;
			bird.hit();
			fireball.setEnemyHitBoat(true);
			ResourcesManager.getInstance().BirdHit.play();
			fireball.getOwner().addToken();
			bird.setLastFrameHit(gameScene.getCurrFrames());
		} else if (other.getType() == CollisionObjectType.ICEBERG) {

			Iceberg iceberg = (Iceberg) other;
			float scaleFactor = 0.01f;
			Vec2 impluse = new Vec2(scaleFactor * fireball.getBody().getLinearVelocity().getX(), scaleFactor
					* fireball.getBody().getLinearVelocity().getY());
			fireball.resetToStart();
			Vec2 position = new Vec2(iceberg.getX(), iceberg.getY());
			iceberg.getBody().applyLinearImpulse(impluse, position, true);
			impluse.delete();
			position.delete();
		} else if (other.getType() == CollisionObjectType.CASTLE_BOUNDARY
				|| other.getType() == CollisionObjectType.BOUNDARY) {
			fireball.resetToStart();
		}

	}

	public void handleBoatCollision(Boat boat, CollisionObject other) {
		if (other.getType() == CollisionObjectType.CASTLE_BOUNDARY) {
			CastleBoundary castleBoundary = (CastleBoundary) other;
			if (boat.owner != castleBoundary.getOwner()) {
				castleBoundary.getOwner().getCastle().showOverlay();
				castleBoundary.getOwner().removeLife();
				ResourcesManager.getInstance().BoatHitCastle.play();
				if (gameScene.getMenu().getFinalSoundSelectionIndex() == 2) {
					gameScene.engine.vibrate(100);
				}
				boat.destroy();
			}
		} else if (other.getType() == CollisionObjectType.BOUNDARY) {
			boat.setDeadState();
		} else if (other.getType() == CollisionObjectType.SUNRAY) {
			boat.setDeadState();
		}
	}

}
