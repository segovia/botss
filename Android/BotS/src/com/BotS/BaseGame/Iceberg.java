package com.BotS.BaseGame;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;

import com.BotS.Physics.PhysicsConnector;
import com.BotS.Physics.PhysicsConstants;
import com.BotS.Physics.PhysicsFactory;
import com.BotS.Physics.Vector2;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;

public class Iceberg extends Sprite implements GameConstants, CollisionObject {

	private Body body;
	private GameScene mGameScene;
	private long curFrame = 0;
	private long createdFrame = 0;
	private long lifeFrame;

	public Iceberg(float pX, float pY, GameScene pGameScene, long mlifeTime) {
		super(pX, pY, ResourcesManager.getInstance().iceberg_region, pGameScene.vbom);
		mGameScene = pGameScene;
		lifeFrame = mlifeTime;
		setVisible(false);
		createPhysics();
		CollisionObjectManager.register(this);
	}

	@Override
	public Body getBody() {
		return body;
	}

	public void setLifeFrame(long life) {

		lifeFrame = life;

	}

	public void spawnIceberg(float x, float y, long a_lifetime) {
		body.setTransform(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, y
				/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
		createdFrame = curFrame;
		setLifeFrame(a_lifetime);
		body.setActive(true);
		setVisible(true);
		setIgnoreUpdate(false);

	}

	@Override
	public CollisionObjectType getType() {
		return CollisionObjectType.ICEBERG;
	}

	public void createPhysics() {

		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0.2f, 0.2f, 0);
		Filter filter = new Filter();
		filter.setCategoryBits(ICEBERG_COLLISION_CATEGORY);
		fixtureDef.setFilter(filter);
		ArrayList<Vector2<Float>> vertices = new ArrayList<Vector2<Float>>();
		float factor = 1.0f;
		vertices.add(new Vector2<Float>(0f, 100f * factor));
		vertices.add(new Vector2<Float>(-70.0f * factor, -40f * factor));
		vertices.add(new Vector2<Float>(50.0f * factor, -40f * factor));

		body = PhysicsFactory.createPolygonBody(mGameScene.physicsWorld, this, vertices, BodyType.dynamicBody,
				fixtureDef);

		body.setFixedRotation(true);

		body.setLinearDamping(30);

		mGameScene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
		body.setActive(false);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		++curFrame;

		if (mGameScene.getSeasonAtPosition(getX(), getY()) != Season.WINTER) {
			destroy();
		}

		if (curFrame - createdFrame > lifeFrame) {
			destroy();
		}

	}

	public void destroy() {
		setVisible(false);
		setIgnoreUpdate(true);
		body.setActive(false);
	}

}
