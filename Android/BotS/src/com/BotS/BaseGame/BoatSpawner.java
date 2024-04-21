package com.BotS.BaseGame;

import org.andengine.entity.sprite.Sprite;

import com.BotS.BaseGame.GameScene.Layer;
import com.BotS.Physics.Vector2;

public class BoatSpawner implements GameConstants {

	private long lastFrameBoatCreated = -10000;
	private GameScene gameScene;
	private Vector2<Integer> leftSpawnPoint;
	private Vector2<Integer> rightSpawnPoint;
	private long curFrame = 0;
	private float initialWindForce;

	public BoatSpawner(GameScene pGameScene, Vector2<Integer> pLeftSpawnPoint, Vector2<Integer> pRightSpawnPoint) {
		gameScene = pGameScene;
		leftSpawnPoint = pLeftSpawnPoint;
		rightSpawnPoint = pRightSpawnPoint;
	}

	public void setInitialWindForce(float force) {

		initialWindForce = force;

	}

	public void onUpdate() {
		++curFrame;
		if (gameScene == null) {
			throw new NullPointerException("GameScene null, did you call init before using this object?");
		}

		if (curFrame - lastFrameBoatCreated > 120) {
			spawnBoat(gameScene.leftPlayer, leftSpawnPoint);
			spawnBoat(gameScene.rightPlayer, rightSpawnPoint);
			lastFrameBoatCreated = curFrame;
		}
	}

	private void spawnBoat(Player player, Vector2<Integer> point) {
		if (player.getActiveBoats().size() >= MAX_BOATS_PER_TEAM) {
			return;
		}
		if (player.getInactiveBoats().isEmpty()) {
			if (player.getTeam().isLeft()) {
				Sprite shield = new Sprite(point.x, point.y, ResourcesManager.getInstance().boatShield_region1,
						gameScene.vbom);
				Boat b = new Boat(point.x, point.y, gameScene.vbom, gameScene.physicsWorld, player,
						ResourcesManager.getInstance().boat_l_region, shield);
				b.setWindForce(initialWindForce);
				gameScene.getLayer(Layer.BOAT).attachChild(b);
				gameScene.getLayer(Layer.BOAT).attachChild(shield);
			} else {
				Sprite shield = new Sprite(point.x, point.y, ResourcesManager.getInstance().boatShield_region2,
						gameScene.vbom);
				Boat b = new Boat(point.x, point.y, gameScene.vbom, gameScene.physicsWorld, player,
						ResourcesManager.getInstance().boat_r_region, shield);
				b.setWindForce(initialWindForce);
				gameScene.getLayer(Layer.BOAT).attachChild(b);
				gameScene.getLayer(Layer.BOAT).attachChild(shield);
			}

		} else {
			Boat b = player.getInactiveBoats().iterator().next();
			b.revive(point.x, point.y);
		}
	}
}
