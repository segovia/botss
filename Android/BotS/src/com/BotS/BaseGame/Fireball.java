package com.BotS.BaseGame;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.Physics.PhysicsConstants;
import com.BotS.Physics.PhysicsFactory;
import com.BotS.Physics.PhysicsWorld;
import com.BotS.Physics.Vector2;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;

public class Fireball extends Sprite implements GameConstants, CollisionObject {

	private static final int ROTATION_OFFSET = 10;
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Body body;
	private FireballSensor fireballSensor;
	private FireballParticleSystem tail1;
	private FireballParticleSystem tail2;
	private FireballParticleSystem tail3;
	private FireballParticleSystem tail4;
	public Sprite fireballSpellLevel1;
	public Sprite fireballSpellLevel2;
	public Sprite fireballSpellLevel3;

	private boolean flagSpell = false, hitEnemyBoat = false;
	private int spellLevel;
	private float iX, iY;
	private Player owner;
	private State state;

	public enum State {
		READY, FIRED
	};

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Fireball(float pX, float pY, VertexBufferObjectManager vbom, PhysicsWorld physicsWorld, Player pOwner,
			Sprite fireballSpellLevel1Sprite, Sprite fireballSpellLevel2Sprite, Sprite fireballSpellLevel3Sprite) {
		super(pX, pY, ResourcesManager.getInstance().fireBall_red_r_region, vbom);

		iX = pX;
		iY = pY;
		owner = pOwner;
		fireballSpellLevel1 = fireballSpellLevel1Sprite;
		fireballSpellLevel1.setVisible(false);
		if (getOwner().getTeam().isLeft()) {
			fireballSpellLevel1.setScaleX(-1);
		}

		fireballSpellLevel2 = fireballSpellLevel2Sprite;
		fireballSpellLevel2.setVisible(false);
		if (getOwner().getTeam().isLeft()) {
			fireballSpellLevel2.setScaleX(-1);
		}

		fireballSpellLevel3 = fireballSpellLevel3Sprite;
		fireballSpellLevel3.setVisible(false);
		if (getOwner().getTeam().isLeft()) {
			fireballSpellLevel3.setScaleX(-1);
		}

		tail1 = new FireballParticleSystem(vbom, iX, iY, ResourcesManager.getInstance().fire_red_region);
		tail2 = new FireballParticleSystem(vbom, iX, iY, ResourcesManager.getInstance().fire_blue_region);
		tail3 = new FireballParticleSystem(vbom, iX, iY, ResourcesManager.getInstance().fire_green_region);
		tail4 = new FireballParticleSystem(vbom, iX, iY, ResourcesManager.getInstance().fire_purple_region);

		createPhysics(physicsWorld);
		CollisionObjectManager.register(this);
		CollisionObjectManager.register(fireballSensor);
		resetToStart();

	}

	public FireballParticleSystem getTail1() {

		return tail1;

	}

	public FireballParticleSystem getTail2() {

		return tail2;

	}

	public FireballParticleSystem getTail3() {

		return tail3;

	}

	public FireballParticleSystem getTail4() {

		return tail4;

	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public CollisionObjectType getType() {
		return CollisionObjectType.FIREBALL;
	}

	public Player getOwner() {
		return owner;
	}

	public void setEnemyHitBoat(boolean hit) {
		hitEnemyBoat = hit;
	}

	public boolean hasHitEnemyBoat() {
		return hitEnemyBoat;
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void createPhysics(PhysicsWorld physicsWorld) {

		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(30f, 0, 0);
		Filter filter = new Filter();
		if (owner.getTeam().isLeft()) {
			filter.setCategoryBits(LEFT_TEAM_FIREBALL_COLLISION_CATEGORY);
			filter.setMaskBits(DEFAULT_COLLISION_CATEGORY | RIGHT_TEAM_FIREBALL_COLLISION_CATEGORY
					| PLANET_COLLISION_CATEGORY | RIGHT_TEAM_CASTLE_COLLISION_CATEGORY);
		} else {
			filter.setCategoryBits(RIGHT_TEAM_FIREBALL_COLLISION_CATEGORY);
			filter.setMaskBits(DEFAULT_COLLISION_CATEGORY | LEFT_TEAM_FIREBALL_COLLISION_CATEGORY
					| PLANET_COLLISION_CATEGORY | LEFT_TEAM_CASTLE_COLLISION_CATEGORY);
		}

		fixtureDef.setFilter(filter);
		body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.dynamicBody, fixtureDef);
		body.setFixedRotation(true);
		body.setGravityScale(1.3f);
		body.setActive(false);
		fireballSensor = new FireballSensor(physicsWorld);
		fireballSensor.body.setActive(false);

	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		fireballSpellLevel1.setPosition(getX(), getY());
		fireballSpellLevel2.setPosition(getX(), getY());
		fireballSpellLevel3.setPosition(getX(), getY());

		if (state != State.READY) {
			final float worldX = body.getPosX();
			final float worldY = body.getPosY();
			setX(worldX * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			setY(worldY * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			fireballSensor.getBody().setTransform(worldX, worldY, 0);

			((BaseParticleEmitter) tail1.getParticleEmitter()).setCenter(getX(), getY());
			((BaseParticleEmitter) tail2.getParticleEmitter()).setCenter(getX(), getY());
			((BaseParticleEmitter) tail3.getParticleEmitter()).setCenter(getX(), getY());
			((BaseParticleEmitter) tail4.getParticleEmitter()).setCenter(getX(), getY());
			float factor = 1;
			float veloX = getBody().getLinearVelocity().getX();
			float veloY = getBody().getLinearVelocity().getY();
			tail1.updateV(-veloX * factor, -veloY * factor);
			tail2.updateV(-veloX * factor, -veloY * factor);
			tail3.updateV(-veloX * factor, -veloY * factor);
			tail4.updateV(-veloX * factor, -veloY * factor);

			int sign = veloX > 0 ? -1 : 1;
			setScaleX(sign);
			setRotation((float) Math.toDegrees(Math.atan2(-veloY, veloX)) + (veloX > 0 ? 0 : 180) + sign
					* ROTATION_OFFSET);

		}

		else {

			if (getflagSpell() == true) {
				if (getSpellLevel() == 1) {
					setVisible(false);
					tail1.setParticlesSpawnEnabled(false);
					tail2.setParticlesSpawnEnabled(true);
					tail3.setParticlesSpawnEnabled(false);
					tail4.setParticlesSpawnEnabled(false);
					fireballSpellLevel1.setVisible(true);
					fireballSpellLevel2.setVisible(false);
					fireballSpellLevel3.setVisible(false);
				}

				if (getSpellLevel() == 2) {
					setVisible(false);
					tail1.setParticlesSpawnEnabled(false);
					tail2.setParticlesSpawnEnabled(false);
					tail3.setParticlesSpawnEnabled(true);
					tail4.setParticlesSpawnEnabled(false);
					fireballSpellLevel1.setVisible(false);
					fireballSpellLevel2.setVisible(true);
					fireballSpellLevel3.setVisible(false);
				}

				if (getSpellLevel() == 3) {
					setVisible(false);
					tail1.setParticlesSpawnEnabled(false);
					tail2.setParticlesSpawnEnabled(false);
					tail3.setParticlesSpawnEnabled(false);
					tail4.setParticlesSpawnEnabled(true);
					fireballSpellLevel1.setVisible(false);
					fireballSpellLevel2.setVisible(false);
					fireballSpellLevel3.setVisible(true);
				}

			} else {

				setVisible(true);
				tail1.setParticlesSpawnEnabled(true);
				tail2.setParticlesSpawnEnabled(false);
				tail3.setParticlesSpawnEnabled(false);
				tail4.setParticlesSpawnEnabled(false);

				fireballSpellLevel1.setVisible(false);
				fireballSpellLevel2.setVisible(false);
				fireballSpellLevel3.setVisible(false);

			}
		}
		fireballSpellLevel1.setScaleX(getScaleX());
		fireballSpellLevel2.setScaleX(getScaleX());
		fireballSpellLevel3.setScaleX(getScaleX());
		fireballSpellLevel1.setRotation(getRotation());
		fireballSpellLevel2.setRotation(getRotation());
		fireballSpellLevel3.setRotation(getRotation());
	}

	public void activatePhysics(boolean activate) {
		body.setActive(activate);
		fireballSensor.body.setActive(activate);
		if (activate) {
			final float worldX = iX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
			final float worldY = iY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
			body.setTransform(worldX, worldY, 0.0f);
			fireballSensor.body.setTransform(worldX, worldY, 0.0f);
		}
	}

	public void fire(Vector2<Float> velocity) {
		if (state == State.READY) {
			state = State.FIRED;
			activatePhysics(true);
			body.setLinearVelocity(velocity.x, velocity.y);
			ResourcesManager.getInstance().ShootFireball.play();
			if (!flagSpell) {
				owner.increaseShotsFired();
			}
		}
	}

	public State getState() {
		return state;
	}

	public void resetToStart() {

		state = State.READY;
		body.setActive(false);
		fireballSensor.body.setActive(false);
		setPosition(iX, iY);
		if (hitEnemyBoat) {
			owner.increaseFireballsOnTarget();
		}
		hitEnemyBoat = false;
		if (owner.getTeam().isLeft()) {
			setScaleX(-1.0f); // make fireball face the other way
			setRotation(-30);
		} else {
			setScaleX(1.0f);
			setRotation(30);
		}
		((BaseParticleEmitter) getTail1().getParticleEmitter()).setCenter(getX(), getY());
		getTail1().updateV(0, 0);
		((BaseParticleEmitter) getTail2().getParticleEmitter()).setCenter(getX(), getY());
		getTail2().updateV(0, 0);
		((BaseParticleEmitter) getTail3().getParticleEmitter()).setCenter(getX(), getY());
		getTail3().updateV(0, 0);
		((BaseParticleEmitter) getTail4().getParticleEmitter()).setCenter(getX(), getY());
		getTail4().updateV(0, 0);
		ResourcesManager.getInstance().ShootFireball.stop();

	}

	public void setflagSpell(boolean value) {

		flagSpell = value;
	}

	public boolean getflagSpell() {

		return flagSpell;
	}

	public void setSpellLevel(int level) {

		spellLevel = level;
	}

	public int getSpellLevel() {

		return spellLevel;
	}

	public class FireballSensor implements CollisionObject {
		private Body body;

		public FireballSensor(PhysicsWorld physicsWorld) {
			// Sensor body
			FixtureDef fixtureDefSensor = PhysicsFactory.createFixtureDef(0, 0, 0);
			fixtureDefSensor.setDensity(0.2f);
			fixtureDefSensor.setIsSensor(true);
			body = PhysicsFactory.createCircleBody(physicsWorld, Fireball.this, BodyType.dynamicBody, fixtureDefSensor);
		}

		@Override
		public CollisionObjectType getType() {
			return CollisionObjectType.FIREBALL_SENSOR;
		}

		@Override
		public Body getBody() {
			return body;
		}

		public Fireball getFireball() {
			return Fireball.this;
		}
	}

}
