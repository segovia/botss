package com.BotS.BaseGame;

import org.andengine.entity.sprite.Sprite;

import com.BotS.BaseGame.GameScene.Layer;
import com.BotS.Physics.PhysicsConstants;
import com.BotS.Physics.PhysicsFactory;
import com.BotS.Physics.PhysicsWorld;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;

public class SunRay extends Sprite implements GameConstants {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Body body;
	private SunRayBody b1, b2, b3;
	private int frameReleased, curFrame;
	private boolean rayReleased, fired, rainbowSet;
	private float hitX, hitY;
	private Sprite sun, rainbow;
	private int spellLevel;
	private GameScene gameScene;
	private SunRayEasterEgg easterEgg;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public SunRay(float pX, float pY, GameScene pGameScene) {

		super(pX, pY, ResourcesManager.getInstance().sunRay_region, pGameScene.vbom);
		createPhysics(pGameScene.physicsWorld);
		setVisible(false);
		sun = pGameScene.getSun();
		rainbow = new Sprite(pX, pY, ResourcesManager.getInstance().rainbow_region, pGameScene.vbom);
		rainbow.setVisible(false);
		gameScene = pGameScene;
		gameScene.getLayer(Layer.BACKGROUND).attachChild(rainbow);
		easterEgg = gameScene.getSunEasterEgg();
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void createPhysics(PhysicsWorld physicsWorld) {
		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0.1f, 0, 0);
		Filter filter = new Filter();
		filter.setCategoryBits(SUNRAY_COLLISION_CATEGORY);
		filter.setMaskBits(DEFAULT_COLLISION_CATEGORY | LEFT_TEAM_BOAT_COLLISION_CATEGORY
				| RIGHT_TEAM_BOAT_COLLISION_CATEGORY);

		fixtureDef.setFilter(filter);
		/*
		 * ArrayList<Vector2<Float>> vertices = new ArrayList<Vector2<Float>>(); vertices.add(new Vector2<Float>(-75.0f,
		 * 150.0f)); vertices.add(new Vector2<Float>(75.0f, 150.0f)); vertices.add(new Vector2<Float>(0.0f, 0.0f)); body
		 * = PhysicsFactory.createPolygonBody(physicsWorld, this,vertices, BodyType.dynamicBody, fixtureDef);
		 */
		b1 = new SunRayBody(150, 1000, physicsWorld, fixtureDef);
		b2 = new SunRayBody(300, 1000, physicsWorld, fixtureDef);
		b3 = new SunRayBody(600, 1000, physicsWorld, fixtureDef);

		// = PhysicsFactory.createCircleBody(physicsWorld, 5000 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
		// 5000 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 100, 100, BodyType.staticBody, fixtureDef);

		// body.setFixedRotation(true);

		// physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
		// @Override
		// public void onUpdate(float pSecondsElapsed) {
		// super.onUpdate(pSecondsElapsed);
		// camera.onUpdate(0.1f);
		//
		// // Debug.d(body.getUserDataStr());
		// }
		// });

		// body.setTransform(5000 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
		// 5000 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
		b1.getBody().setActive(false);
		b2.getBody().setActive(false);
		b3.getBody().setActive(false);
		body = b1.getBody();
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		/*
		 * if (body.getPosY() <= 0) { onDie(); }
		 */
		++curFrame;
		if (rayReleased) {
			setVisible(!rainbowSet);
			rainbow.setVisible(rainbowSet);
			float sunX = sun.getX();
			// float sunX = 800;
			float sunY = sun.getY();
			float vX = (hitX - sunX) / 2f;

			float vY = (hitY - sunY) / 2f;
			// float rayY = (0f - sunY) / 2f;

			float unitX = 0.0f;
			float unitY = 1.0f;
			double sgn = Math.signum(vX);
			double angle = sgn
					* Math.toDegrees(Math.acos((vX * unitX + vY * unitY)
							/ Math.sqrt(Math.pow(vX, 2.0) + Math.pow(vY, 2.0))));

			double entryAngle = 180 - Math.abs(angle);
			float xdiff = (float) (sgn * (Math.tan(Math.toRadians(entryAngle)) * hitY));

			float rayX = (hitX + xdiff - sunX) / 2f;
			vX = rayX;
			float rayY = (0 - sunY) / 2f;
			vY = rayY;
			rayX += sunX;
			rayY += sunY;
			setX(rayX);
			setY(rayY);
			rainbow.setX(rayX);
			rainbow.setY(rayY);

			double length = Math.sqrt(Math.pow(vX * 2, 2) + Math.pow(vY * 2, 2));

			// float scaling = (float) length / Math.abs(sunY);
			float scaling = (float) length / 611;
			float scalingRainbow = (float) length / 545;
			setScale(1.0f, scaling);
			setRotation((float) angle);
			rainbow.setFlippedVertical(true);
			rainbow.setScale(1.0f, scalingRainbow);
			rainbow.setRotation((float) angle);
			if (rainbowSet) {
				easterEgg.releaseRay((float) angle);
			}

			if (curFrame - frameReleased > 50 && !fired) {
				fired = true;
				vX = -xdiff;
				sgn = Math.signum(vX);
				vY = hitY;
				double bodyangle = -sgn
						* Math.toDegrees(Math.acos((vX * unitX + vY * unitY)
								/ Math.sqrt(Math.pow(vX, 2.0) + Math.pow(vY, 2.0))));
				releaseRayBody(hitX + xdiff / 2, hitY / 2, (float) bodyangle);
			}

			if (curFrame - frameReleased > 60 && fired) {
				onDie();
			}

		}
	}

	public void releaseRay(float x, float y, int spelllvl) {
		setIgnoreUpdate(false);
		rainbowSet = gameScene.getSunEasterEgg().isSunTouched();
		gameScene.getSunEasterEgg().resetSun();
		spellLevel = spelllvl;
		fired = false;
		rayReleased = true;
		hitX = x;
		hitY = y;
		frameReleased = curFrame;
	}

	public void releaseRayBody(float x, float y, float angle) {
		ResourcesManager.getInstance().SummerSunRayExp.play();
		switch (spellLevel) {
		case 1:
			body = b1.getBody();
			break;
		case 2:
			body = b2.getBody();
			break;
		case 3:
			body = b3.getBody();
			break;
		}
		body.setActive(true);
		body.setTransform(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, y
				/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (float) Math.toRadians(angle));

		// body.setTransform(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f, 0.0f);
		/*
		 * Vec2 v = new Vec2(0, 50); body.setLinearVelocity(v); body.setGravityScale(100); v.delete();
		 */
	}

	public boolean isReleased() {
		return rayReleased;
	}

	public void onDie() {
		destroy();
	}

	public void destroy() {
		body.setTransform(5000 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				5000 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0.0f);
		rayReleased = false;
		setIgnoreUpdate(true);
		setVisible(false);
		rainbow.setVisible(false);
		easterEgg.resetSun();
		body.setActive(false);
	}

	private class SunRayBody implements CollisionObject {

		private Body Sbody;

		public SunRayBody(int width, int height, PhysicsWorld physicsWorld, FixtureDef fixtureDef) {
			Sbody = PhysicsFactory.createBoxBody(physicsWorld, 5000 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					5000 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, width, height, BodyType.staticBody,
					fixtureDef);
			CollisionObjectManager.register(this);

		}

		@Override
		public Body getBody() {
			return Sbody;
		}

		@Override
		public CollisionObjectType getType() {
			return CollisionObjectType.SUNRAY;
		}

	}
}
