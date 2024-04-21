package com.BotS.Physics;

import java.util.List;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.util.Constants;
import org.andengine.util.math.MathUtils;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.ParticleFlag;
import com.google.fpl.liquidfun.ParticleGroup;
import com.google.fpl.liquidfun.ParticleGroupDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Shape.Type;

public final class PhysicsFactory {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final float PIXEL_TO_METER_RATIO_DEFAULT = 32.0f;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private PhysicsFactory() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public static FixtureDef createFixtureDef(final float pDensity, final float pElasticity, final float pFriction) {
		return PhysicsFactory.createFixtureDef(pDensity, pElasticity, pFriction, false);
	}

	public static FixtureDef createFixtureDef(final float pDensity, final float pElasticity, final float pFriction,
			final boolean pSensor) {
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.setDensity(pDensity);
		fixtureDef.setRestitution(pElasticity);
		fixtureDef.setFriction(pFriction);
		fixtureDef.setIsSensor(pSensor);
		return fixtureDef;
	}

	public static Body createBoxBody(final PhysicsWorld pPhysicsWorld, final IEntity pEntity, final BodyType pBodyType,
			final FixtureDef pFixtureDef) {
		return PhysicsFactory.createBoxBody(pPhysicsWorld, pEntity, pBodyType, pFixtureDef,
				PIXEL_TO_METER_RATIO_DEFAULT);
	}

	@SuppressWarnings("deprecation")
	public static Body createBoxBody(final PhysicsWorld pPhysicsWorld, final IEntity pEntity, final BodyType pBodyType,
			final FixtureDef pFixtureDef, final float pPixelToMeterRatio) {
		final float[] sceneCenterCoordinates = pEntity.getSceneCenterCoordinates();
		final float centerX = sceneCenterCoordinates[Constants.VERTEX_INDEX_X];
		final float centerY = sceneCenterCoordinates[Constants.VERTEX_INDEX_Y];
		return PhysicsFactory.createBoxBody(pPhysicsWorld, centerX, centerY, pEntity.getWidthScaled(),
				pEntity.getHeightScaled(), pEntity.getRotation(), pBodyType, pFixtureDef, pPixelToMeterRatio);
	}

	public static Body createBoxBody(final PhysicsWorld pPhysicsWorld, final float pCenterX, final float pCenterY,
			final float pWidth, final float pHeight, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		return PhysicsFactory.createBoxBody(pPhysicsWorld, pCenterX, pCenterY, pWidth, pHeight, 0, pBodyType,
				pFixtureDef, PIXEL_TO_METER_RATIO_DEFAULT);
	}

	public static Body createBoxBody(final PhysicsWorld pPhysicsWorld, final float pCenterX, final float pCenterY,
			final float pWidth, final float pHeight, final float pRotation, final BodyType pBodyType,
			final FixtureDef pFixtureDef) {
		return PhysicsFactory.createBoxBody(pPhysicsWorld, pCenterX, pCenterY, pWidth, pHeight, pRotation, pBodyType,
				pFixtureDef, PIXEL_TO_METER_RATIO_DEFAULT);
	}

	public static Body createBoxBody(final PhysicsWorld pPhysicsWorld, final float pCenterX, final float pCenterY,
			final float pWidth, final float pHeight, final BodyType pBodyType, final FixtureDef pFixtureDef,
			final float pPixelToMeterRatio) {
		return PhysicsFactory.createBoxBody(pPhysicsWorld, pCenterX, pCenterY, pWidth, pHeight, 0, pBodyType,
				pFixtureDef, pPixelToMeterRatio);
	}

	public static Body createBoxBody(final PhysicsWorld pPhysicsWorld, final float pCenterX, final float pCenterY,
			final float pWidth, final float pHeight, final float pRotation, final BodyType pBodyType,
			final FixtureDef pFixtureDef, final float pPixelToMeterRatio) {
		final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.setType(pBodyType);

		boxBodyDef.setPosition(pCenterX / pPixelToMeterRatio, pCenterY / pPixelToMeterRatio);

		final Body boxBody = pPhysicsWorld.createBody(boxBodyDef);

		final PolygonShape boxPoly = new PolygonShape();

		final float halfWidth = pWidth * 0.5f / pPixelToMeterRatio;
		final float halfHeight = pHeight * 0.5f / pPixelToMeterRatio;

		boxPoly.setAsBox(halfWidth, halfHeight);
		pFixtureDef.setShape(boxPoly);

		boxBody.createFixture(pFixtureDef);

		boxPoly.delete();

		boxBody.setTransform(boxBody.getWorldCenter(), MathUtils.degToRad(pRotation));

		return boxBody;
	}

	public static ParticleGroup createParticleBox(final PhysicsWorld pPhysicsWorld, final float pCenterX,
			final float pCenterY, final float pWidth, final float pHeight, final float pRotation,
			final BodyType pBodyType, final FixtureDef pFixtureDef) {
		final float pPixelToMeterRatio = PIXEL_TO_METER_RATIO_DEFAULT;
		final PolygonShape boxPoly = new PolygonShape();

		final float halfWidth = pWidth * 0.5f / pPixelToMeterRatio;
		final float halfHeight = pHeight * 0.5f / pPixelToMeterRatio;

		boxPoly.setAsBox(halfWidth, halfHeight, pCenterX / pPixelToMeterRatio, pCenterY / pPixelToMeterRatio, 0);
		ParticleGroupDef pd = new ParticleGroupDef();
		// pd.setPosition(pCenterX / pPixelToMeterRatio, pCenterY / pPixelToMeterRatio);
		pd.setFlags(ParticleFlag.waterParticle);
		pd.setShape(boxPoly);
		ParticleGroup particleGroup = pPhysicsWorld.getParticleSystem().createParticleGroup(pd);

		// particleGroup.setTransform(pPhysicsWorld.getWorld()., MathUtils.degToRad(pRotation));

		boxPoly.delete();
		pd.delete();
		return particleGroup;
	}

	public static Body createCircleBody(final PhysicsWorld pPhysicsWorld, final IEntity pEntity,
			final BodyType pBodyType, final FixtureDef pFixtureDef) {
		return PhysicsFactory.createCircleBody(pPhysicsWorld, pEntity, pBodyType, pFixtureDef,
				PIXEL_TO_METER_RATIO_DEFAULT);
	}

	@SuppressWarnings("deprecation")
	public static Body createCircleBody(final PhysicsWorld pPhysicsWorld, final IEntity pEntity,
			final BodyType pBodyType, final FixtureDef pFixtureDef, final float pPixelToMeterRatio) {
		final float[] sceneCenterCoordinates = pEntity.getSceneCenterCoordinates();
		final float centerX = sceneCenterCoordinates[Constants.VERTEX_INDEX_X];
		final float centerY = sceneCenterCoordinates[Constants.VERTEX_INDEX_Y];
		return PhysicsFactory.createCircleBody(pPhysicsWorld, centerX, centerY, pEntity.getWidthScaled() * 0.5f,
				pEntity.getRotation(), pBodyType, pFixtureDef, pPixelToMeterRatio);
	}

	public static Body createCircleBody(final PhysicsWorld pPhysicsWorld, final float pCenterX, final float pCenterY,
			final float pRadius, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		return createCircleBody(pPhysicsWorld, pCenterX, pCenterY, pRadius, 0, pBodyType, pFixtureDef,
				PIXEL_TO_METER_RATIO_DEFAULT);
	}

	public static Body createCircleBody(final PhysicsWorld pPhysicsWorld, final float pCenterX, final float pCenterY,
			final float pRadius, final float pRotation, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		return createCircleBody(pPhysicsWorld, pCenterX, pCenterY, pRadius, pRotation, pBodyType, pFixtureDef,
				PIXEL_TO_METER_RATIO_DEFAULT);
	}

	public static Body createCircleBody(final PhysicsWorld pPhysicsWorld, final float pCenterX, final float pCenterY,
			final float pRadius, final BodyType pBodyType, final FixtureDef pFixtureDef, final float pPixelToMeterRatio) {
		return createCircleBody(pPhysicsWorld, pCenterX, pCenterY, pRadius, 0, pBodyType, pFixtureDef,
				pPixelToMeterRatio);
	}

	public static Body createCircleBody(final PhysicsWorld pPhysicsWorld, final float pCenterX, final float pCenterY,
			final float pRadius, final float pRotation, final BodyType pBodyType, final FixtureDef pFixtureDef,
			final float pPixelToMeterRatio) {
		final BodyDef circleBodyDef = new BodyDef();
		circleBodyDef.setType(pBodyType);

		circleBodyDef.setPosition(pCenterX / pPixelToMeterRatio, pCenterY / pPixelToMeterRatio);

		circleBodyDef.setAngle(MathUtils.degToRad(pRotation));

		final Body circleBody = pPhysicsWorld.createBody(circleBodyDef);

		final CircleShape circlePoly = new CircleShape();
		pFixtureDef.setShape(circlePoly);

		final float radius = pRadius / pPixelToMeterRatio;
		circlePoly.setRadius(radius);

		circleBody.createFixture(pFixtureDef);

		circlePoly.delete();

		return circleBody;
	}

	public static Body createLineBody(final PhysicsWorld pPhysicsWorld, final Line pLine, final FixtureDef pFixtureDef) {
		return PhysicsFactory.createLineBody(pPhysicsWorld, pLine, pFixtureDef, PIXEL_TO_METER_RATIO_DEFAULT);
	}

	public static Body createLineBody(final PhysicsWorld pPhysicsWorld, final Line pLine, final FixtureDef pFixtureDef,
			final float pPixelToMeterRatio) {
		final float[] sceneCoordinates = pLine.convertLocalCoordinatesToSceneCoordinates(0, 0);
		final float x1 = sceneCoordinates[Constants.VERTEX_INDEX_X];
		final float y1 = sceneCoordinates[Constants.VERTEX_INDEX_Y];

		pLine.convertLocalCoordinatesToSceneCoordinates(pLine.getX2() - pLine.getX1(), pLine.getY2() - pLine.getY1());
		final float x2 = sceneCoordinates[Constants.VERTEX_INDEX_X];
		final float y2 = sceneCoordinates[Constants.VERTEX_INDEX_Y];

		return PhysicsFactory.createLineBody(pPhysicsWorld, x1, y1, x2, y2, pFixtureDef, pPixelToMeterRatio);
	}

	public static Body createLineBody(final PhysicsWorld pPhysicsWorld, final float pX1, final float pY1,
			final float pX2, final float pY2, final FixtureDef pFixtureDef) {
		return PhysicsFactory.createLineBody(pPhysicsWorld, pX1, pY1, pX2, pY2, pFixtureDef,
				PIXEL_TO_METER_RATIO_DEFAULT);
	}

	public static Body createLineBody(final PhysicsWorld pPhysicsWorld, final float pX1, final float pY1,
			final float pX2, final float pY2, final FixtureDef pFixtureDef, final float pPixelToMeterRatio) {
		final BodyDef lineBodyDef = new BodyDef();
		lineBodyDef.setType(BodyType.staticBody);

		final Body boxBody = pPhysicsWorld.createBody(lineBodyDef);

		final PolygonShape linePoly = new PolygonShape();

		System.out.println("createLineBody needs to be fixed if we want to use it");
		// linePoly.setAsEdge(new Vector2(pX1 / pPixelToMeterRatio, pY1 / pPixelToMeterRatio), new Vector2(pX2
		// / pPixelToMeterRatio, pY2 / pPixelToMeterRatio));
		pFixtureDef.setShape(linePoly);

		boxBody.createFixture(pFixtureDef);

		linePoly.delete();

		return boxBody;
	}

	/**
	 * @param pPhysicsWorld
	 * @param pEntity
	 * @param pVertices
	 *            are to be defined relative to the center of the pEntity and have the
	 *            {@link PhysicsConstants#PIXEL_TO_METER_RATIO_DEFAULT} applied.
	 * @param pBodyType
	 * @param pFixtureDef
	 * @return
	 */
	public static Body createPolygonBody(final PhysicsWorld pPhysicsWorld, final IEntity pEntity,
			final List<Vector2<Float>> pVertices, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		final float[] sceneCenterCoordinates = pEntity.getSceneCenterCoordinates();
		return PhysicsFactory.createPolygonBody(pPhysicsWorld, sceneCenterCoordinates[Constants.VERTEX_INDEX_X],
				sceneCenterCoordinates[Constants.VERTEX_INDEX_Y], pVertices, pBodyType, pFixtureDef,
				PIXEL_TO_METER_RATIO_DEFAULT);
	}

	public static Body createPolygonBody(final PhysicsWorld pPhysicsWorld, final float posX, final float posY,
			final List<Vector2<Float>> pVertices, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		return PhysicsFactory.createPolygonBody(pPhysicsWorld, posX, posY, pVertices, pBodyType, pFixtureDef,
				PIXEL_TO_METER_RATIO_DEFAULT);
	}

	/**
	 * @param pPhysicsWorld
	 * @param pEntity
	 * @param pVertices
	 *            are to be defined relative to the center of the pEntity.
	 * @param pBodyType
	 * @param pFixtureDef
	 * @return
	 */
	public static Body createPolygonBody(final PhysicsWorld pPhysicsWorld, final float posX, final float posY,
			final List<Vector2<Float>> pVertices, final BodyType pBodyType, final FixtureDef pFixtureDef,
			final float pPixelToMeterRatio) {
		final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.setType(pBodyType);

		boxBodyDef.setPosition(posX / pPixelToMeterRatio, posY / pPixelToMeterRatio);

		final Body boxBody = pPhysicsWorld.createBody(boxBodyDef);

		final PolygonShape boxPoly = new PolygonShape();
		boxPoly.setType(Type.POLYGON);

		float[] points = new float[pVertices.size() * 2];
		for (int i = 0; i < pVertices.size(); ++i) {
			points[2 * i] = pVertices.get(i).x / pPixelToMeterRatio;
			points[2 * i + 1] = pVertices.get(i).y / pPixelToMeterRatio;
		}
		boxPoly.set(points, pVertices.size());

		pFixtureDef.setShape(boxPoly);

		boxBody.createFixture(pFixtureDef);

		boxPoly.delete();

		return boxBody;
	}

	/**
	 * @param pPhysicsWorld
	 * @param pEntity
	 * @param pTriangleVertices
	 *            are to be defined relative to the center of the pEntity and have the
	 *            {@link PhysicsConstants#PIXEL_TO_METER_RATIO_DEFAULT} applied.
	 * @param pBodyType
	 * @param pFixtureDef
	 * @return
	 */
	public static Body createTrianglulatedBody(final PhysicsWorld pPhysicsWorld, final IEntity pEntity,
			final List<Vector2<Float>> pTriangleVertices, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		return PhysicsFactory.createTrianglulatedBody(pPhysicsWorld, pEntity, pTriangleVertices, pBodyType,
				pFixtureDef, PIXEL_TO_METER_RATIO_DEFAULT);
	}

	/**
	 * @param pPhysicsWorld
	 * @param pEntity
	 * @param pTriangleVertices
	 *            are to be defined relative to the center of the pEntity and have the
	 *            {@link PhysicsConstants#PIXEL_TO_METER_RATIO_DEFAULT} applied. The vertices will be triangulated and
	 *            for each triangle a {@link Fixture} will be created.
	 * @param pBodyType
	 * @param pFixtureDef
	 * @return
	 */
	public static Body createTrianglulatedBody(final PhysicsWorld pPhysicsWorld, final IEntity pEntity,
			final List<Vector2<Float>> pTriangleVertices, final BodyType pBodyType, final FixtureDef pFixtureDef,
			final float pPixelToMeterRatio) {
		final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.setType(pBodyType);

		final float[] sceneCenterCoordinates = pEntity.getSceneCenterCoordinates();
		boxBodyDef.setPosition(sceneCenterCoordinates[Constants.VERTEX_INDEX_X] / pPixelToMeterRatio,
				sceneCenterCoordinates[Constants.VERTEX_INDEX_Y] / pPixelToMeterRatio);

		final Body boxBody = pPhysicsWorld.createBody(boxBodyDef);

		final int vertexCount = pTriangleVertices.size();
		for (int i = 0; i < vertexCount; /* */) {
			final PolygonShape boxPoly = new PolygonShape();
			boxPoly.setType(Type.POLYGON);

			float[] points = new float[6];
			for (int j = 0; j < 3; ++j) {
				points[2 * j] = pTriangleVertices.get(i).x / pPixelToMeterRatio;
				points[2 * j + 1] = pTriangleVertices.get(i++).y / pPixelToMeterRatio;
			}
			boxPoly.set(points, 3);
			pFixtureDef.setShape(boxPoly);

			boxBody.createFixture(pFixtureDef);

			boxPoly.delete();
		}

		return boxBody;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
