package com.BotS.Physics;

public class FixedStepPhysicsWorld extends PhysicsWorld {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int STEPSPERSECOND_DEFAULT = 60;

	// ===========================================================
	// Fields
	// ===========================================================

	@SuppressWarnings("unused")
	private final int mMaximumStepsPerUpdate;
	@SuppressWarnings("unused")
	private float mSecondsElapsedAccumulator;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FixedStepPhysicsWorld(final int pStepsPerSecond, final Vector2<Float> gravity,
			final Vector2<Float> gravityCenter, final boolean pAllowSleep) {
		this(pStepsPerSecond, Integer.MAX_VALUE, gravity, gravityCenter, pAllowSleep);
	}

	public FixedStepPhysicsWorld(final int pStepsPerSecond, final int pMaximumStepsPerUpdate,
			final Vector2<Float> gravity, final Vector2<Float> gravityCenter, final boolean pAllowSleep) {
		super(gravity, gravityCenter, pAllowSleep, 1.0f / pStepsPerSecond);
		mMaximumStepsPerUpdate = pMaximumStepsPerUpdate;
		mWorld.addMyContactListener();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		mRunnableHandler.onUpdate(pSecondsElapsed);
		mSecondsElapsedAccumulator += pSecondsElapsed;
		doStep();
		mPhysicsConnectorManager.onUpdate(pSecondsElapsed);
	}

	public void doStep() {
		mWorld.step(mTimeStep, mVelocityIterations, mPositionIterations, mParticleIterations);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
