package com.BotS.BaseGame;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

import android.opengl.GLES20;

import com.BotS.Physics.PhysicsConstants;
import com.BotS.Physics.PhysicsFactory;
import com.BotS.Physics.PhysicsWorld;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;

public class Boat extends AnimatedSprite implements GameConstants, PhysicsConstants, CollisionObject {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	protected PhysicsWorld physicsWorld;
	protected Body body;
	protected final Player owner; // the boats owner will never change
	protected boolean isActive = true;
	protected long lastTimeHit = 0;
	private float windForce;
	private State state;
	private Sprite shield;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public enum State {
		STARTING, NORMAL, SHIELDED, DEAD
	};

	public Boat(float pX, float pY, VertexBufferObjectManager vbo, PhysicsWorld pPhysicsWorld, Player player,
			ITiledTextureRegion sprite, Sprite a_shield) {
		// super(pX, pY, ResourcesManager.getInstance().boat_region, vbo, BoatShader.getInstance());
		super(pX, pY, sprite, vbo);
		state = State.STARTING;
		physicsWorld = pPhysicsWorld;
		owner = player;
		createPhysics(pPhysicsWorld);
		shield = a_shield;
		shield.setVisible(false);
		owner.addActiveBoat(this);
		CollisionObjectManager.register(this);

	}

	public boolean canBeShield() {

		if (state == State.NORMAL || state == State.STARTING) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public CollisionObjectType getType() {
		return CollisionObjectType.BOAT;
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	@SuppressWarnings("deprecation")
	public void createPhysics(PhysicsWorld physicsWorld) {
		final float[] sceneCenterCoordinates = this.getSceneCenterCoordinates();
		final float centerX = sceneCenterCoordinates[Constants.VERTEX_INDEX_X];
		final float centerY = sceneCenterCoordinates[Constants.VERTEX_INDEX_Y];
		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0, 0);
		Filter filter = new Filter();
		int category = owner.getTeam().isLeft() ? LEFT_TEAM_BOAT_COLLISION_CATEGORY
				: RIGHT_TEAM_BOAT_COLLISION_CATEGORY;
		filter.setCategoryBits(category);
		filter.setMaskBits(DEFAULT_COLLISION_CATEGORY | BOUNDARY_COLLISION_CATEGORY | category
				| ICEBERG_COLLISION_CATEGORY | SUNRAY_COLLISION_CATEGORY);
		fixtureDef.setFilter(filter);
		body = PhysicsFactory.createCircleBody(physicsWorld, centerX, centerY, getWidthScaled() * 0.4f, getRotation(),
				BodyType.dynamicBody, fixtureDef, PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

	}

	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		ITextureRegion textureRegion = getTextureRegion();
		GLES20.glUniform2f(BoatShader.sUniformTexRegionMinLocation, textureRegion.getU(), textureRegion.getV());
		GLES20.glUniform2f(BoatShader.sUniformTexRegionMaxLocation, textureRegion.getU2(), textureRegion.getV2());
		long time = System.currentTimeMillis();
		GLES20.glUniform1f(BoatShader.sUniformTimeLocation, time % 1000);

	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (isActive) {
			if (state == State.STARTING || state == State.NORMAL || state == State.SHIELDED) {
				// update boat position
				setPosition(body.getPosX() * PIXEL_TO_METER_RATIO_DEFAULT, body.getPosY()
						* PIXEL_TO_METER_RATIO_DEFAULT);
				shield.setPosition(body.getPosX() * PIXEL_TO_METER_RATIO_DEFAULT, body.getPosY()
						* PIXEL_TO_METER_RATIO_DEFAULT);

				boolean leftTeam = owner.getTeam().isLeft();

				if (state == State.NORMAL || state == State.SHIELDED) {

					float xDir = leftTeam ? 1 : -1;
					float yDir = leftTeam && getX() < RESOLUTION.x / 2 || !leftTeam && getX() > RESOLUTION.x / 2 ? 1
							: -1;
					body.applyForceToCenter(windForce * xDir, 0.5f * windForce * yDir);

				}

				if (state == State.STARTING) {
					int offset = 20;
					if (leftTeam && getX() > LEFT_TOP_CORNER + offset || !leftTeam
							&& getX() < RIGHT_TOP_CORNER - offset) {
						// slow down!
						body.setLinearVelocity(0, 0);

						state = State.NORMAL;
					}
				}
			}
			if (state == State.DEAD && !isAnimationRunning()) {
				destroy();
			}
		}

	}

	public void setWindForce(float force) {

		windForce = force;

	}

	public void setAnimationDestruction() {

		long frameTime = 50;

		this.animate(new long[] { frameTime, frameTime, frameTime, frameTime, frameTime, frameTime, frameTime,
				frameTime, frameTime, frameTime, frameTime, frameTime, frameTime, frameTime, frameTime, frameTime,
				frameTime, frameTime, frameTime, frameTime }, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
				14, 15, 16, 17, 18, 19, 20 }, 0);

	}

	public void setAnimationShield() {

		this.animate(new long[] { 100 }, new int[] { 23 }, true);

	}

	public void setDeadState() {
		if (state == State.DEAD) {
			return; // nothing to do here
		}
		state = State.DEAD;
		setAnimationDestruction();
		shield.setVisible(false);
		body.setActive(false);
	}

	public void activateShield() {
		state = State.SHIELDED;
		// setAnimationShield();
		shield.setVisible(true);
	}

	public void revive(float x, float y) {
		if (isActive) {
			// already alive
			return;
		}
		owner.addActiveBoat(this);
		body.setActive(true);
		setX(x); // avoids flickering when reviving
		setY(y); // avoids flickering when reviving
		body.setTransform(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, y
				/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
		Vec2 v = new Vec2(0, 0);
		body.setLinearVelocity(v);
		v.delete();
		setIgnoreUpdate(false);
		lastTimeHit = 0;
		shield.setVisible(false);
		setVisible(true);
		isActive = true;

	}

	public void destroy() {
		if (!isActive) {
			// already destroyed
			return;
		}
		state = State.STARTING;
		shield.setVisible(false);
		setVisible(false);
		setIgnoreUpdate(true);
		body.setActive(false);
		owner.removeActiveBoat(this);
		isActive = false;
		this.stopAnimation(0);

	}

	public void hit() {
		long time = System.currentTimeMillis();

		if (state == State.SHIELDED) {
			state = State.NORMAL;
			ResourcesManager.getInstance().FireballHitShield.play();
			// this.stopAnimation(0);
			shield.setVisible(false);
			lastTimeHit = time;
		} else {
			if (time - lastTimeHit > 500) {
				ResourcesManager.getInstance().explodeSound.play();
				setDeadState();

			}
		}
	}

}
