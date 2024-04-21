package com.BotS.BaseGame;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.Physics.PhysicsConnector;
import com.BotS.Physics.PhysicsConstants;
import com.BotS.Physics.PhysicsFactory;
import com.BotS.Physics.PhysicsWorld;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;

public class MagicBird extends AnimatedSprite implements GameConstants, PhysicsConstants, CollisionObject {

	private Body body;
	private int lastFrameHit = 0;
	private int lastTime, appeared, currFrames;
	private int scale = 50;
	private boolean appearing, disappearing;

	private float maxX = 850;
	private float maxY = 700;
	private float minX = 480;
	private float minY = 550;

	public MagicBird(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourcesManager.getInstance().bird_region, vbo);
		setAnimation();
		createPhysics(camera, physicsWorld);
		CollisionObjectManager.register(this);
		lastTime = 0;
		currFrames = 0;
		appeared = lastTime;
		setVisible(false);
		body.setActive(false);
	}

	@Override
	public CollisionObjectType getType() {
		return CollisionObjectType.BIRD;
	}

	@Override
	public Body getBody() {
		return body;
	}

	public void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		Filter filter = new Filter();
		filter.setCategoryBits(BIRD_COLLISION_CATEGORY);
		fixtureDef.setFilter(filter);
		body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.kinematicBody, fixtureDef);
		body.setFixedRotation(true);
		Vec2 v = new Vec2(0, -2);
		body.setLinearVelocity(v);
		v.delete();
		appearing = true;

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				// Debug.d(body.getUserDataStr());
			}
		});
	}

	public void hit() {
		setVisible(false);
		body.setActive(false);
	}

	public void appears() {
		if (!isVisible()) {
			appeared = currFrames;
			appearing = true;
			disappearing = false;
			setPosition(800, 750);
			body.setTransform(640 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					750 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
			setVisible(true);
			Vec2 v = new Vec2(0, -2);
			body.setLinearVelocity(v);
			v.delete();
			body.setActive(true);
		}
	}

	public void disappears() {
		disappearing = true;
		Vec2 v = new Vec2(0, 2);
		body.setLinearVelocity(v);
		v.delete();
	}

	public int getLastFrameHit() {
		return lastFrameHit;
	}

	public void setLastFrameHit(int lastFrameHit) {
		this.lastFrameHit = lastFrameHit;
	}

	public void changeDirection() {
		float movementX = body.getLinearVelocity().getX();
		float movementY = body.getLinearVelocity().getY();
		double length = Math.sqrt(Math.pow(movementX, 2) + Math.pow(movementY, 2));
		float circleCenterX = 0;
		float circleCenterY = 0;
		if (length > 0) {
			circleCenterX = body.getPosX() + movementX / (float) length * (float) Math.sqrt(2);
			circleCenterY = body.getPosY() + movementY / (float) length * (float) Math.sqrt(2);
		} else {
			circleCenterX = body.getPosX() + 1 * (float) Math.sqrt(2);
			circleCenterY = body.getPosY() + 1 * (float) Math.sqrt(2);
		}
		float moveX = 0.0f;
		float moveY = 0.0f;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(scale);
		float t = (float) (2 * Math.PI / scale * randomInt);
		moveX = circleCenterX + (float) Math.cos(t) - body.getPosX();
		moveY = circleCenterY + (float) Math.sin(t) - body.getPosY();
		Vec2 v = new Vec2(moveX, moveY);
		body.setLinearVelocity(v);
		v.delete();
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		currFrames++;
		float moveX = body.getLinearVelocity().getX();
		float moveY = body.getLinearVelocity().getY();

		if (appearing) {
			if (body.getPosY() * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT < 650) {
				appearing = false;
			}
		} else if (disappearing) {
			if (body.getPosY() * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT > 760) {
				disappearing = false;
				hit();
				setLastFrameHit(currFrames);
			}
		} else {
			if (currFrames - appeared > BIRDSHOWTIME && isVisible()) {
				disappears();
			} else {
				if (currFrames - lastTime > 25) {
					lastTime = currFrames;
					/*
					 * Random randomGenerator = new Random(); int randomInt = randomGenerator.nextInt(50) - 50; moveX =
					 * randomInt * 0.1f; randomInt = randomGenerator.nextInt(50) - 50; moveY = randomInt * 0.1f; Vec2 v
					 * = new Vec2(moveX, moveY); body.setLinearVelocity(v); v.delete();
					 */
					changeDirection();
					moveX = body.getLinearVelocity().getX();
					moveY = body.getLinearVelocity().getY();
				}
				if (body.getPosX() * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT > maxX
						|| body.getPosX() * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT < minX
						|| body.getPosY() * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT > 700
						|| body.getPosY() * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT < 550) {
					float centerX = (minX + (maxX - minX) / 2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
					float centerY = (minY + (maxY - minY) / 2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
					moveX = centerX - body.getPosX();
					moveY = centerY - body.getPosY();
					Vec2 v = new Vec2(0.5f * moveX, 0.5f * moveY);
					body.setLinearVelocity(v);
					v.delete();
				}
				if (body.getLinearVelocity().getX() > 0.0) {
					setFlippedHorizontal(true);
				} else {
					setFlippedHorizontal(false);
				}
			}
		}
	}

	public void setAnimation() {

		final long[] PLAYER_ANIMATE = new long[] { 100, 100 };

		animate(PLAYER_ANIMATE, 0, 1, true);
	}

}
