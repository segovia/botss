package com.BotS.BaseGame;

import java.util.HashSet;
import java.util.Set;

public class MagicHandler {

	private GameScene gameScene;

	public MagicHandler(GameScene gameScene) {

		this.gameScene = gameScene;

	}

	public void magicHandle(Fireball fireball, int spellLevel) {

		// Increase number of magic powers used
		fireball.getOwner().increaseSpecialsUsed();

		float X = fireball.getX();
		float Y = fireball.getY();
		fireball.resetToStart();
		Season season = gameScene.getSeasonAtPosition(X, Y);

		fireball.getOwner().getFireball().setflagSpell(false);

		if (season == Season.SPRING) {

			float spellRadius = 0;
			if (spellLevel == 1) {
				spellRadius = 150;
			}
			if (spellLevel == 2) {
				spellRadius = 300;
			}
			if (spellLevel == 3) {
				spellRadius = 1000;
			}

			Set<Boat> boatSet = fireball.getOwner().getBoats();
			ResourcesManager.getInstance().SpringShieldPlaced.play();

			for (Boat b : boatSet) {

				if (Math.sqrt((X - b.getX()) * (X - b.getX()) + (Y - b.getY()) * (Y - b.getY())) < spellRadius) {
					if (b.canBeShield()) {
						b.activateShield();
					}
				}
			}
		}

		if (season == Season.SUMMER) {
			// gameScene.ray.releaseRay(X, Y);
			gameScene.getSunraySpawner().spawnSunRay(X, Y, spellLevel);
			ResourcesManager.getInstance().SummerSunRay.play();
			Set<Boat> boatSet = fireball.getOwner().getOpponent().getBoats();
			Set<Boat> boatSetOwn = fireball.getOwner().getBoats();
			float damageRadius = 100;

			for (Boat b : boatSet) {
				if (Math.sqrt((X - b.getX()) * (X - b.getX()) + (Y - b.getY()) * (Y - b.getY())) < damageRadius) {
					b.setDeadState();
				}
			}
			for (Boat b : boatSetOwn) {
				if (Math.sqrt((X - b.getX()) * (X - b.getX()) + (Y - b.getY()) * (Y - b.getY())) < damageRadius) {
					b.setDeadState();
				}
			}

		}

		if (season == Season.FALL) {

			float spellRadius = 0;
			if (spellLevel == 1) {
				spellRadius = 150;
			}
			if (spellLevel == 2) {
				spellRadius = 300;
			}
			if (spellLevel == 3) {
				spellRadius = 1000;
			}

			Set<Boat> boatSet = fireball.getOwner().getOpponent().getBoats();
			Set<Boat> toDeleteBoatSet = new HashSet<Boat>();

			for (Boat b : boatSet) {
				if (Math.sqrt((X - b.getX()) * (X - b.getX()) + (Y - b.getY()) * (Y - b.getY())) < spellRadius) {
					gameScene.lightningSpawn.spawnLightning(b.getX(), b.getY() + 200);
					toDeleteBoatSet.add(b);
				}
			}

			for (Boat b : toDeleteBoatSet) {
				b.setDeadState();
			}

		}

		if (season == Season.WINTER) {

			long lifeTime = 0;
			if (spellLevel == 1) {
				lifeTime = 500;
			}
			if (spellLevel == 2) {
				lifeTime = 1500;
			}
			if (spellLevel == 3) {
				lifeTime = 10000000;
			}

			gameScene.icebergSpawn.spawnIceberg(X, Y + 60, lifeTime);
			ResourcesManager.getInstance().WinterIcebergPlaced.play();
		}

	}

	public void magicHandle(Fireball fireball, Boat boat) {

	}
}