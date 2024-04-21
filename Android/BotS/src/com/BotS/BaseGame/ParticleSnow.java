package com.BotS.BaseGame;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

public class ParticleSnow extends BatchedPseudoSpriteParticleSystem implements GameConstants {

	public ParticleSnow(final float pCenterX, final float pCenterY, final float pWidth, final float pHeight,
			VertexBufferObjectManager vbo) {
		super(new RectangleParticleEmitter(pCenterX, pCenterY, pWidth, pHeight), 5, 15, 100, ResourcesManager
				.getInstance().snow_region, vbo);

		setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		addParticleInitializer(new VelocityParticleInitializer<Entity>(-3, 3, -20, -40));
		addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, 3, -3, -5));
		addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
		addParticleInitializer(new ExpireParticleInitializer<Entity>(10f));
		addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f, 0.8f));
		/*
		 * addParticleInitializer(new SwingEntityModifierInitializer<Entity>(50f, 10f, (float) Math.PI * 8 * 5, 10f,
		 * 50f, false));
		 */
	}

	@Override
	public BaseParticleEmitter getParticleEmitter() {
		return (BaseParticleEmitter) super.getParticleEmitter();
	}

}
