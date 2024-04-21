package com.BotS.BaseGame;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.BaseGame.Shooter.State;

public class Player implements GameConstants {

	private Set<Boat> activeBoats = new HashSet<Boat>();
	private Set<Boat> inactiveBoats = new HashSet<Boat>();
	private int life = INITIAL_LIFE;
	private Player opponent;
	private Team mTeam;
	private Fireball fireball;
	private Castle castle;
	private int tokenLevelOneCounter = 0;
	private TokenIndicator tokenLevelOne1;
	private TokenIndicator tokenLevelOne2;
	private TokenIndicator tokenLevelOne3;
	private TokenIndicator tokenLevelTwo1;
	private TokenIndicator tokenLevelTwo2;
	private TokenIndicator tokenLevelThree;
	private LifeIndicator lifeIndicator;
	private Shooter shooter;
	private int roundsWon, destFriendly, destEnemies, shotsFired, specialsUsed, fireballsOnTarget;

	public Player(Team pTeam, int indicatorXPos, int indicatorYPos, GameScene gameScene) {
		mTeam = pTeam;

		if (mTeam.isLeft()) {
			tokenLevelOne1 = new TokenIndicator(indicatorXPos, indicatorYPos, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_blue_l_region, 1, mTeam);
			tokenLevelOne1.setVisible(false);
			tokenLevelOne2 = new TokenIndicator(indicatorXPos, indicatorYPos - 50, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_blue_l_region, 1, mTeam);
			tokenLevelOne2.setVisible(false);
			tokenLevelOne3 = new TokenIndicator(indicatorXPos, indicatorYPos - 100, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_blue_l_region, 1, mTeam);
			tokenLevelOne3.setVisible(false);

			tokenLevelTwo1 = new TokenIndicator(indicatorXPos - 50, indicatorYPos - 25, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_green_l_region, 2, mTeam);
			tokenLevelTwo1.setVisible(false);
			tokenLevelTwo2 = new TokenIndicator(indicatorXPos - 50, indicatorYPos - 75, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_green_l_region, 2, mTeam);
			tokenLevelTwo2.setVisible(false);
			tokenLevelThree = new TokenIndicator(indicatorXPos - 100, indicatorYPos - 50, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_purple_l_region, 3, mTeam);
			tokenLevelThree.setVisible(false);
		}

		else {
			tokenLevelOne1 = new TokenIndicator(indicatorXPos, indicatorYPos, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_blue_r_region, 1, mTeam);
			tokenLevelOne1.setVisible(false);
			tokenLevelOne2 = new TokenIndicator(indicatorXPos, indicatorYPos - 50, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_blue_r_region, 1, mTeam);
			tokenLevelOne2.setVisible(false);
			tokenLevelOne3 = new TokenIndicator(indicatorXPos, indicatorYPos - 100, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_blue_r_region, 1, mTeam);
			tokenLevelOne3.setVisible(false);

			tokenLevelTwo1 = new TokenIndicator(indicatorXPos + 50, indicatorYPos - 25, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_green_r_region, 2, mTeam);
			tokenLevelTwo1.setVisible(false);
			tokenLevelTwo2 = new TokenIndicator(indicatorXPos + 50, indicatorYPos - 75, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_green_r_region, 2, mTeam);
			tokenLevelTwo2.setVisible(false);
			tokenLevelThree = new TokenIndicator(indicatorXPos + 100, indicatorYPos - 50, gameScene.vbom,
					ResourcesManager.getInstance().fireBall_purple_r_region, 3, mTeam);
			tokenLevelThree.setVisible(false);

		}

		lifeIndicator = new LifeIndicator(this, INITIAL_LIFE, gameScene.vbom);
		lifeIndicator.attach(gameScene);
	}

	public void registerCastle(Castle castle) {
		this.castle = castle;
	}

	public Castle getCastle() {
		return castle;
	}

	public Team getTeam() {
		return mTeam;
	}

	public int getLife() {
		return life;
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	public Player getOpponent() {
		return opponent;
	}

	public void addActiveBoat(Boat b) {
		inactiveBoats.remove(b);
		activeBoats.add(b);
	}

	public void removeActiveBoat(Boat b) {
		activeBoats.remove(b);
		inactiveBoats.add(b);
	}

	// mod access should be done through addActiveBoat removeActiveBoat
	public Set<Boat> getBoats() {
		return Collections.unmodifiableSet(activeBoats);

	}

	public Sprite getTokenLevelOne1() {
		return tokenLevelOne1;
	}

	public Sprite getTokenLevelOne2() {
		return tokenLevelOne2;
	}

	public Sprite getTokenLevelOne3() {
		return tokenLevelOne3;
	}

	public Sprite getTokenLevelTwo1() {
		return tokenLevelTwo1;
	}

	public Sprite getTokenLevelTwo2() {
		return tokenLevelTwo2;
	}

	public Sprite getTokenLevelThree() {
		return tokenLevelThree;
	}

	public Fireball getFireball() {
		return fireball;
	}

	public void setFireball(Fireball a_fireball) {
		fireball = a_fireball;
	}

	public Shooter getShooter() {
		return shooter;
	}

	public void setShooter(Shooter shooter) {
		this.shooter = shooter;
	}

	public void addToken() {
		if (tokenLevelOneCounter == 3) {
			return; // do nothing if already have token
		}

		tokenLevelOneCounter++;

	}

	public int getTokenLevelOneCount() {

		return tokenLevelOneCounter;

	}

	public void setTokenLevelOneCount(int number) {

		tokenLevelOneCounter = number;

	}

	/*
	 * public float getBirdSpriteX() { return tokenIndicator.getX(); }
	 * 
	 * public float getBirdSpriteY() { return tokenIndicator.getY(); }
	 */
	public void removeLife() {
		life--;
		lifeIndicator.setLife(life);
	}

	public void reset() {
		life = INITIAL_LIFE;
		lifeIndicator.setLife(life);
		tokenLevelOne1.setVisible(false);
		tokenLevelOne2.setVisible(false);
		tokenLevelOne3.setVisible(false);
		tokenLevelTwo1.setVisible(false);
		tokenLevelTwo2.setVisible(false);
		tokenLevelThree.setVisible(false);
		tokenLevelOneCounter = 0;
		resetDestEnemies();
		resetDestFriendly();
		resetShotsFired();
		resetSpecialsUsed();
		resetFireballsOnTarget();
	}

	public enum Team {
		LEFT(true), RIGHT(false);
		private boolean mIsLeft;

		private Team(boolean pIsLeft) {
			mIsLeft = pIsLeft;
		}

		public boolean isLeft() {
			return mIsLeft;
		}
	}

	public Set<Boat> getInactiveBoats() {
		return inactiveBoats;
	}

	private class TokenIndicator extends Sprite {

		private int level;

		public TokenIndicator(float pX, float pY, VertexBufferObjectManager vbom, final ITextureRegion pTextureRegion,
				int level, Team team) {
			super(pX, pY, pTextureRegion, vbom);
			this.level = level;
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (shooter.state == State.DRAGGING || shooter.state == State.FOLLOWING) {
				return false;
			}
			if (isVisible() && pSceneTouchEvent.isActionUp()) {

				if (level == 1 && tokenLevelOneCounter >= 1) {
					tokenLevelOneCounter--;
				}

				if (level == 2 && tokenLevelOneCounter >= 2) {
					tokenLevelOneCounter -= 2;
				}

				if (level == 3 && tokenLevelOneCounter >= 3) {
					tokenLevelOneCounter -= 3;
				}

				getFireball().resetToStart();
				getFireball().setSpellLevel(level);
				getFireball().setflagSpell(true);
			}

			return true;
		}
	}

	public Set<Boat> getActiveBoats() {
		return activeBoats;
	}

	public int getRoundsWon() {
		return roundsWon;
	}

	public void increaseRoundsWon() {
		roundsWon++;
	}

	public void resetRoundsWon() {
		roundsWon = 0;
	}

	public int getDestFriendly() {
		return destFriendly;
	}

	public void increaseDestFriendly() {
		destFriendly++;
	}

	public void resetDestFriendly() {
		destFriendly = 0;
	}

	public int getDestEnemies() {
		return destEnemies;
	}

	public void increaseDestEnemies() {
		destEnemies++;
	}

	public void resetDestEnemies() {
		destEnemies = 0;
	}

	public int getShotsFired() {
		return shotsFired;
	}

	public void increaseShotsFired() {
		shotsFired++;
	}

	public void resetShotsFired() {
		shotsFired = 0;
	}

	public int getSpecialsUsed() {
		return specialsUsed;
	}

	public void increaseSpecialsUsed() {
		specialsUsed++;
	}

	public void resetSpecialsUsed() {
		specialsUsed = 0;
	}

	public void increaseFireballsOnTarget() {
		fireballsOnTarget++;
	}

	public int getFireballsOnTarget() {
		return fireballsOnTarget;
	}

	public void resetFireballsOnTarget() {
		fireballsOnTarget = 0;
	}

}
