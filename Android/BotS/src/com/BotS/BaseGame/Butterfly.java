package com.BotS.BaseGame;

import java.util.Random;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Butterfly extends AnimatedSprite implements GameConstants {

	long lastTime;
	private int scale = 50;
	private PhysicsHandler handler;
	private float originalX;
	private float originalY;

	public Butterfly(float pX, float pY, VertexBufferObjectManager vbo, int type) {
		super(pX, pY, ResourcesManager.getInstance().butterfly_region, vbo);
		originalX = pX;
		originalY = pY;
		lastTime = System.currentTimeMillis();

		setScaleX(-1);

		if (type == 1) {
			setAnimation1();
		}
		if (type == 2) {
			setAnimation2();
		}
		if (type == 3) {
			setAnimation3();
		}
		handler = new PhysicsHandler(this);
		registerUpdateHandler(handler);
		if (isVisible()) {
			handler.setVelocity(-3, 0);
		}

	}

	public void changeDirection() {

		float movementX = handler.getVelocityX();
		float movementY = handler.getVelocityY();

		double length = Math.sqrt(Math.pow(movementX, 2) + Math.pow(movementY, 2));
		float circleCenterX = 0;
		float circleCenterY = 0;
		if (length > 0) {
			circleCenterX = getX() + movementX / (float) length * (float) Math.sqrt(2);
			circleCenterY = getY() + movementY / (float) length * (float) Math.sqrt(2);
		} else {
			circleCenterX = getX() + 1 * (float) Math.sqrt(2);
			circleCenterY = getY() + 1 * (float) Math.sqrt(2);
		}

		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(scale);

		float t = (float) (2 * Math.PI / scale * randomInt);
		float moveX = circleCenterX + (float) Math.cos(t) - getX();
		float moveY = circleCenterY + (float) Math.sin(t) - getY();
		float factor = 8f;

		if (moveX > 0) {
			moveX = -moveX;
		}

		handler.setVelocity(-3f + moveX * factor, moveY * factor);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (isVisible()) {
			long curTime = System.currentTimeMillis();
			if (curTime - lastTime > 1000) {
				changeDirection();
				lastTime = curTime;
			}

			if (getX() < 0) {
				handler.setVelocity(10, handler.getVelocityY());
				backToOrigin();
			}
			if (getX() > GameConstants.RESOLUTION.x) {
				handler.setVelocity(-10, handler.getVelocityY());
			}
			if (getY() < GameConstants.RESOLUTION.y * 0.3) {
				handler.setVelocity(handler.getVelocityX(), 10);
			}
			if (getY() > GameConstants.RESOLUTION.y * 0.55) {
				handler.setVelocity(handler.getVelocityX(), -10);
			}
		} else {
			backToOrigin();
		}

	}

	public void setAnimation1() {

		long frameTime = 100;
		final long[] frameTimes = new long[] { frameTime, frameTime, frameTime, frameTime };
		final int[] frames = new int[] { 0, 1, 2, 3 };

		this.animate(frameTimes, frames, true);

	}

	public void setAnimation2() {

		long frameTime = 100;
		final long[] frameTimes = new long[] { frameTime, frameTime, frameTime };
		final int[] frames = new int[] { 24, 25, 26 };

		this.animate(frameTimes, frames, true);

	}

	public void setAnimation3() {

		long frameTime = 100;
		final long[] frameTimes = new long[] { frameTime, frameTime, frameTime };
		final int[] frames = new int[] { 48, 49, 50 };

		this.animate(frameTimes, frames, true);

	}

	public void backToOrigin() {

		this.setPosition(originalX, originalY);
		setVisible(false);

	}
}
