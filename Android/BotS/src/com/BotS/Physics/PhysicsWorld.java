package com.BotS.Physics;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.runnable.RunnableHandler;
import org.andengine.util.debug.Debug;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.ParticleSystemDef;
import com.google.fpl.liquidfun.World;

public class PhysicsWorld implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VELOCITY_ITERATIONS_DEFAULT = 6;
	public static final int POSITION_ITERATIONS_DEFAULT = 2;
	public static final int PARTICLE_ITERATIONS_DEFAULT = 1;
	public static final float PARTICLE_RADIUS_DEFAULT = 0.15f;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final PhysicsConnectorManager mPhysicsConnectorManager = new PhysicsConnectorManager();
	protected final RunnableHandler mRunnableHandler = new RunnableHandler();
	protected final World mWorld;

	protected int mVelocityIterations = VELOCITY_ITERATIONS_DEFAULT;
	protected int mPositionIterations = POSITION_ITERATIONS_DEFAULT;
	protected int mParticleIterations = PARTICLE_ITERATIONS_DEFAULT;
	protected final float mTimeStep;

	protected final ParticleSystem mParticleSystem;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PhysicsWorld(final Vector2<Float> gravity, final Vector2<Float> gravityCenter, final boolean pAllowSleep,
			float timeStep) {
		mWorld = new World(gravity.x, gravity.y); // , pAllowSleep ?
		if (gravityCenter != null) {
			mWorld.setGravityCenter(gravityCenter.x, gravityCenter.y);
		}
		mTimeStep = timeStep;
		mParticleIterations = PARTICLE_ITERATIONS_DEFAULT; // not using recommended
		Debug.d("Particle iterations = " + mParticleIterations);
		mVelocityIterations = VELOCITY_ITERATIONS_DEFAULT;
		mPositionIterations = POSITION_ITERATIONS_DEFAULT;
		ParticleSystemDef psd = new ParticleSystemDef();
		mParticleSystem = mWorld.createParticleSystem(psd);
		mParticleSystem.setRadius(PARTICLE_RADIUS_DEFAULT);
		mParticleSystem.setDamping(0.2f);
		psd.delete();
	}

	/**
	 * Might be used in future, code taken from b2Particle.h
	 */
	@SuppressWarnings("unused")
	private void calcRecomendedParticleIteration(Vector2<Float> gravity) {
		float threshold = 0.01f;
		double gravityNorm = Math.sqrt(gravity.x * gravity.x + gravity.y * gravity.y);
		mParticleIterations = (int) Math.ceil(Math.sqrt(gravityNorm / (threshold * PARTICLE_RADIUS_DEFAULT))
				* mTimeStep);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public World getWorld() {
		return mWorld;
	}

	public ParticleSystem getParticleSystem() {
		return mParticleSystem;
	}

	public int getPositionIterations() {
		return mPositionIterations;
	}

	public void setPositionIterations(final int pPositionIterations) {
		mPositionIterations = pPositionIterations;
	}

	public int getVelocityIterations() {
		return mVelocityIterations;
	}

	public void setVelocityIterations(final int pVelocityIterations) {
		mVelocityIterations = pVelocityIterations;
	}

	public PhysicsConnectorManager getPhysicsConnectorManager() {
		return mPhysicsConnectorManager;
	}

	public void clearPhysicsConnectors() {
		mPhysicsConnectorManager.clear();
	}

	public void registerPhysicsConnector(final PhysicsConnector pPhysicsConnector) {
		mPhysicsConnectorManager.add(pPhysicsConnector);
	}

	public void unregisterPhysicsConnector(final PhysicsConnector pPhysicsConnector) {
		mPhysicsConnectorManager.remove(pPhysicsConnector);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		mRunnableHandler.onUpdate(pSecondsElapsed);
		mWorld.step(pSecondsElapsed, mVelocityIterations, mPositionIterations, mParticleIterations);
		mPhysicsConnectorManager.onUpdate(pSecondsElapsed);
	}

	@Override
	public void reset() {
		// TODO Reset all native physics objects !?!??!
		mPhysicsConnectorManager.reset();
		mRunnableHandler.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void postRunnable(final Runnable pRunnable) {
		mRunnableHandler.postRunnable(pRunnable);
	}

	// public void clearForces() {
	// System.out.println("No clear forces");
	// //mWorld.clearForces();
	// }

	public Body createBody(final BodyDef pDef) {
		return mWorld.createBody(pDef);
	}

	// public Joint createJoint(final JointDef pDef) {
	// return mWorld.createJoint(pDef);
	// }

	public void destroyBody(final Body pBody) {
		mWorld.destroyBody(pBody);
	}

	// public void destroyJoint(final Joint pJoint) {
	// mWorld.destroyJoint(pJoint);
	// }

	public void dispose() {
		mWorld.delete();
	}

	// public boolean getAutoClearForces() {
	// return mWorld.getAutoClearForces();
	// }
	//
	// public Iterator<Body> getBodies() {
	// return mWorld.getBodies();
	// }
	//
	// public int getBodyCount() {
	// return mWorld.getBodyCount();
	// }
	//
	// public int getContactCount() {
	// return mWorld.getContactCount();
	// }
	//
	// public List<Contact> getContactList() {
	// return mWorld.getContactList();
	// }
	//
	// public Vector2 getGravity() {
	// return mWorld.getGravity();
	// }
	//
	// public Iterator<Joint> getJoints() {
	// return mWorld.getJoints();
	// }
	//
	// public int getJointCount() {
	// return mWorld.getJointCount();
	// }
	//
	// public int getProxyCount() {
	// return mWorld.getProxyCount();
	// }
	//
	// public boolean isLocked() {
	// return mWorld.isLocked();
	// }
	//
	// public void QueryAABB(final QueryCallback pCallback, final float pLowerX, final float pLowerY, final float
	// pUpperX,
	// final float pUpperY) {
	// mWorld.QueryAABB(pCallback, pLowerX, pLowerY, pUpperX, pUpperY);
	// }
	//
	// public void setAutoClearForces(final boolean pFlag) {
	// mWorld.setAutoClearForces(pFlag);
	// }
	//
	// public void setContactFilter(final ContactFilter pFilter) {
	// mWorld.setContactFilter(pFilter);
	// }
	//
	// public void setContactListener(final ContactListener pListener) {
	// mWorld.setContactListener(pListener);
	// }
	//
	// public void setContinuousPhysics(final boolean pFlag) {
	// mWorld.setContinuousPhysics(pFlag);
	// }
	//
	// public void setDestructionListener(final DestructionListener pListener) {
	// mWorld.setDestructionListener(pListener);
	// }
	//
	// public void setGravity(final Vector2 pGravity) {
	// mWorld.setGravity(pGravity);
	// }
	//
	// public void setWarmStarting(final boolean pFlag) {
	// mWorld.setWarmStarting(pFlag);
	// }
	//
	// public void rayCast(final RayCastCallback pRayCastCallback, final Vector2 pPoint1, final Vector2 pPoint2) {
	// mWorld.rayCast(pRayCastCallback, pPoint1, pPoint2);
	// }

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
