package com.BotS.BaseGame;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

public class FireballParticleSystem extends BatchedPseudoSpriteParticleSystem {

	private VelocityParticleInitializer<Entity> V;
	private ExpireParticleInitializer<Entity> LF;
	private float initialX;
	private float initialY;

	public FireballParticleSystem(VertexBufferObjectManager vbo, float X, float Y, final ITextureRegion pTextureRegion) {
		super(new CircleParticleEmitter(X, Y, 20), 40, 50, 100, pTextureRegion, vbo);

		setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		V = new VelocityParticleInitializer<Entity>(0, 0, 0, 0);
		LF = new ExpireParticleInitializer<Entity>(0.0f);
		addParticleInitializer(V);
		addParticleInitializer(LF);
		addParticleInitializer(new AccelerationParticleInitializer<Entity>(-100, 100, -100, 100));
		addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));

		addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f, 0.5f));
		addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 1.0f, 0.0f));

		initialX = X;
		initialY = Y;
	}

	public void updateV(float X, float Y) {

		V.setVelocityX(X);
		V.setVelocityY(Y);

	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		float emitterX = ((BaseParticleEmitter) getParticleEmitter()).getCenterX();
		float emitterY = ((BaseParticleEmitter) getParticleEmitter()).getCenterX();

		if (Math.abs(emitterX - initialX) > 20 && Math.abs(emitterY - initialY) > 20) {
			LF.setLifeTime(1f);
		} else {
			LF.setLifeTime(0f);
		}

	}

	public void destory() {
		reset();

	}

}
