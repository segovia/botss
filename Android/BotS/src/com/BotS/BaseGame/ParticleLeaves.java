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

public class ParticleLeaves extends BatchedPseudoSpriteParticleSystem implements GameConstants {

	public ParticleLeaves(final float pCenterX, final float pCenterY, final float pWidth, final float pHeight,
			VertexBufferObjectManager vbo) {
		super(new RectangleParticleEmitter(pCenterX, pCenterY, pWidth, pHeight), 0, 1.5f, 100, ResourcesManager
				.getInstance().leaf_region, vbo);

		setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		addParticleInitializer(new VelocityParticleInitializer<Entity>(-30, -5, 0, 0));
		addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, -1, 0, 0));
		addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 40.0f));
		addParticleInitializer(new ExpireParticleInitializer<Entity>(40f));
		addParticleInitializer(new ScaleParticleInitializer<Entity>(0.1f, 0.4f));
		addParticleInitializer(new SwingEntityModifierInitializer<Entity>(50f, 5f, (float) Math.PI * 8 * 2, 10f, 50f,
				true));
	}

	@Override
	public BaseParticleEmitter getParticleEmitter() {
		return (BaseParticleEmitter) super.getParticleEmitter();
	}

}