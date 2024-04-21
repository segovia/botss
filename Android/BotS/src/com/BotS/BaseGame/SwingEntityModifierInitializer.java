package com.BotS.BaseGame;

import java.util.Random;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;

public class SwingEntityModifierInitializer<T extends IEntity> implements IParticleInitializer<T> {

	private float duration;
	private float minValue;
	private float maxValue;
	private float minMagnitude;
	private float maxMagnitude;
	private boolean Randomize;

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	public SwingEntityModifierInitializer(float pduration, float pminValue, float pmaxValue, float pminMagnitude,
			float pmaxMagnitude, boolean pRandomize) {
		duration = pduration;
		minValue = pminValue;
		maxValue = pmaxValue;
		minMagnitude = pminMagnitude;
		maxMagnitude = pmaxMagnitude;
		Randomize = pRandomize;
	}

	@Override
	public void onInitializeParticle(Particle<T> pParticle) {
		if (Randomize) {
			pParticle.getEntity().registerEntityModifier(
					new SwingModifier(duration, minValue, minValue + RANDOM.nextFloat() * (maxValue - minValue),
							minMagnitude, minMagnitude + RANDOM.nextFloat() * (maxMagnitude - minMagnitude)));
		} else {
			pParticle.getEntity().registerEntityModifier(
					new SwingModifier(duration, minValue, maxValue, minMagnitude, maxMagnitude));
		}
	}

}
