package com.BotS.BaseGame;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.Physics.PhysicsFactory;
import com.BotS.Physics.PhysicsWorld;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;

public class Castle extends Sprite implements GameConstants, CollisionObject {

	private Body body;
	private float centerX;
	private float centerY;
	protected final Player owner;
	private float shieldAlpha = 1;
	private float overlayAlpha = 1;
	private float angle, orignialAngle;
	private Sprite shield;
	private Sprite overlay;
	private long curFrame = 0;
	private ShieldState shieldState;
	private State state;
	private long framesPast;

	public enum ShieldState {
		HIT, FADE
	};

	public enum State {
		NORMAL, ATTACKED, AFTERATTACKED, NEARDEAD, AFTERNEARDEAD, DEAD, AFTERDEAD
	};

	public Castle(float pX, float pY, final ITextureRegion pTextureRegion, VertexBufferObjectManager vbo,
			PhysicsWorld physicsWorld, Player player, Sprite shield, Sprite overlay) {
		super(pX, pY, pTextureRegion, vbo);
		this.shield = shield;
		this.shield.setVisible(false);
		this.overlay = overlay;
		this.overlay.setVisible(false);
		owner = player;
		centerX = pX;
		centerY = pY;
		angle = 1.5f;
		orignialAngle = getRotation();
		createPhysics(physicsWorld);
		owner.registerCastle(this);
		CollisionObjectManager.register(this);

	}

	public void showShield() {

		shieldState = ShieldState.HIT;

	}

	public void showOverlay() {

		state = State.ATTACKED;

	}

	public void setDead() {

		state = State.DEAD;

	}

	public void setReset() {

		overlay.setVisible(false);

	}

	@Override
	public Body getBody() {
		return body;
	}

	public void createPhysics(PhysicsWorld physicsWorld) {

		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0.7f, 0);
		Filter filter = new Filter();

		if (owner.getTeam().isLeft()) {
			filter.setCategoryBits(LEFT_TEAM_CASTLE_COLLISION_CATEGORY);
			filter.setMaskBits(RIGHT_TEAM_FIREBALL_COLLISION_CATEGORY);
		} else {
			filter.setCategoryBits(RIGHT_TEAM_CASTLE_COLLISION_CATEGORY);
			filter.setMaskBits(LEFT_TEAM_FIREBALL_COLLISION_CATEGORY);
		}
		fixtureDef.setFilter(filter);
		body = PhysicsFactory.createCircleBody(physicsWorld, centerX - (owner.getTeam().isLeft() ? 0 : 6), centerY,
				130, BodyType.staticBody, fixtureDef);
		body.setActive(true);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		++curFrame;

		if (getOwner().getLife() == INITIAL_LIFE) {
			overlay.setVisible(false);
		}

		if (getOwner().getLife() == 1) {
			if (state != State.AFTERNEARDEAD) {
				state = State.NEARDEAD;
			}
		}

		if (getOwner().getLife() == 0) {
			if (state != State.AFTERDEAD) {
				state = State.DEAD;
			}
		}

		if (shieldState == ShieldState.HIT) {

			shield.setVisible(true);
			shieldState = ShieldState.FADE;
			shieldAlpha = 1f;
		}

		if (shieldState == ShieldState.FADE) {

			shieldAlpha -= 0.02;

			if (shieldAlpha <= 0.02) {
				shield.setVisible(false);
			} else {
				shield.setAlpha(shieldAlpha);

			}

		}

		if (state == State.ATTACKED) {

			overlay.setVisible(true);
			state = State.AFTERATTACKED;
			overlayAlpha = 0.8f;
			setRotation(getRotation() - angle);
			overlay.setRotation(getRotation() - angle);
			angle = -angle;
		}

		if (state == State.AFTERATTACKED) {

			overlayAlpha -= 0.04;
			setRotation(getRotation() - angle);
			overlay.setRotation(getRotation() - angle);
			angle = -angle;

			if (overlayAlpha <= 0) {
				setRotation(orignialAngle);
				overlay.setRotation(orignialAngle);
				overlay.setVisible(false);
			} else {
				overlay.setAlpha(overlayAlpha);

			}
		}

		if (state == State.NEARDEAD) {
			overlay.setVisible(true);
			state = State.AFTERNEARDEAD;
			overlayAlpha = 0.8f;
			setRotation(getRotation() - angle);
			overlay.setRotation(getRotation() - angle);
			angle = -angle;
			framesPast = curFrame;

		}

		if (state == State.AFTERNEARDEAD) {
			overlayAlpha -= 0.03;

			if (overlayAlpha <= 0.1) {
				overlayAlpha = 0.8f;
			} else {
				overlay.setAlpha(overlayAlpha);
			}
			if (curFrame - framesPast <= 25) {
				setRotation(getRotation() - angle);
				overlay.setRotation(getRotation() - angle);
				angle = -angle;
			} else {
				setRotation(orignialAngle);
				overlay.setRotation(orignialAngle);
			}

		}

		if (state == State.DEAD) {

			overlayAlpha = 0.9f;
			overlay.setAlpha(overlayAlpha);
			state = State.AFTERDEAD;
			setRotation(getRotation() - angle);
			overlay.setRotation(getRotation() - angle);
			angle = -angle;
			framesPast = curFrame;

		}

		if (state == State.AFTERDEAD) {

			overlayAlpha -= 0.005;

			if (overlayAlpha <= 0.005) {
				overlay.setVisible(false);
			} else {
				overlay.setAlpha(overlayAlpha);

			}
			if (curFrame - framesPast <= 25) {
				setRotation(getRotation() - angle);
				overlay.setRotation(getRotation() - angle);
				angle = -angle;
			} else {
				setRotation(orignialAngle);
				overlay.setRotation(orignialAngle);
			}

		}

	}

	public void destroy() {

	}

	public Player getOwner() {

		return owner;

	}

	@Override
	public CollisionObjectType getType() {

		return CollisionObjectType.CASTLE;

	}
}
