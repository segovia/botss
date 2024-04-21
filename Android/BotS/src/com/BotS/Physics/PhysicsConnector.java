package com.BotS.Physics;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.util.math.MathUtils;

import com.google.fpl.liquidfun.Body;

public class PhysicsConnector implements IUpdateHandler, PhysicsConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IEntity mEntity;
	protected final Body mBody;

	protected boolean mUpdatePosition;
	protected boolean mUpdateRotation;
	protected final float mPixelToMeterRatio;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PhysicsConnector(final IEntity pEntity, final Body pBody) {
		this(pEntity, pBody, true, true);
	}

	public PhysicsConnector(final IEntity pEntity, final Body pBody, final float pPixelToMeterRatio) {
		this(pEntity, pBody, true, true, pPixelToMeterRatio);
	}

	public PhysicsConnector(final IEntity pEntity, final Body pBody, final boolean pUdatePosition,
			final boolean pUpdateRotation) {
		this(pEntity, pBody, pUdatePosition, pUpdateRotation, PIXEL_TO_METER_RATIO_DEFAULT);
	}

	public PhysicsConnector(final IEntity pEntity, final Body pBody, final boolean pUdatePosition,
			final boolean pUpdateRotation, final float pPixelToMeterRatio) {
		mEntity = pEntity;
		mBody = pBody;

		mUpdatePosition = pUdatePosition;
		mUpdateRotation = pUpdateRotation;
		mPixelToMeterRatio = pPixelToMeterRatio;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IEntity getEntity() {
		return mEntity;
	}

	public Body getBody() {
		return mBody;
	}

	public boolean isUpdatePosition() {
		return mUpdatePosition;
	}

	public boolean isUpdateRotation() {
		return mUpdateRotation;
	}

	public void setUpdatePosition(final boolean pUpdatePosition) {
		mUpdatePosition = pUpdatePosition;
	}

	public void setUpdateRotation(final boolean pUpdateRotation) {
		mUpdateRotation = pUpdateRotation;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		final IEntity entity = mEntity;
		final Body body = mBody;

		if (mUpdatePosition) {
			final float pixelToMeterRatio = mPixelToMeterRatio;
			entity.setPosition(body.getPosX() * pixelToMeterRatio, body.getPosY() * pixelToMeterRatio);
		}

		if (mUpdateRotation) {
			final float angle = body.getAngle();
			entity.setRotation(-MathUtils.radToDeg(angle));
		}
	}

	@Override
	public void reset() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
