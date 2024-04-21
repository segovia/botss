package com.BotS.BaseGame;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.Physics.PhysicsWorld;

public class Lightning extends Sprite implements GameConstants {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private int frameCreated, curFrame;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Lightning(float pX, float pY, VertexBufferObjectManager vbo, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourcesManager.getInstance().lightning_region, vbo);
		setVisible(false);
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		++curFrame;
		if (curFrame - frameCreated > 25) {
			onDie();
		}

	}

	public void spawnLightning(float x, float y) {
		setPosition(x, y);
		frameCreated = curFrame;
		setIgnoreUpdate(false);
		setVisible(true);
		ResourcesManager.getInstance().FallLightning.play();
	}

	public void onDie() {
		destroy();
	}

	public void destroy() {
		setVisible(false);
		setIgnoreUpdate(true);
	}
}
