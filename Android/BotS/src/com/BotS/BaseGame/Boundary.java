package com.BotS.BaseGame;

import java.util.List;

import com.BotS.Physics.PhysicsFactory;
import com.BotS.Physics.PhysicsWorld;
import com.BotS.Physics.Vector2;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;

public class Boundary implements CollisionObject, GameConstants {
	private Body body;

	public Boundary(PhysicsWorld physicsWorld, float x, float y, float radius, int collisionCategory) {
		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		Filter filter = new Filter();
		filter.setCategoryBits(collisionCategory);
		fixtureDef.setFilter(filter);
		body = PhysicsFactory.createCircleBody(physicsWorld, x, y, radius, 0, BodyType.staticBody, fixtureDef);
		fixtureDef.delete();
		CollisionObjectManager.register(this);
	}

	public Boundary(PhysicsWorld physicsWorld, List<Vector2<Float>> vertices, int collisionCategory) {
		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		Filter filter = new Filter();
		filter.setCategoryBits(collisionCategory);
		fixtureDef.setFilter(filter);
		body = PhysicsFactory.createPolygonBody(physicsWorld, 0.0f, 0.0f, vertices, BodyType.staticBody, fixtureDef);
		fixtureDef.delete();
		CollisionObjectManager.register(this);
	}

	public Boundary(PhysicsWorld physicsWorld, float x, float y, float width, float height, int collisionCategory) {
		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		Filter filter = new Filter();
		filter.setCategoryBits(collisionCategory);
		fixtureDef.setFilter(filter);
		body = PhysicsFactory.createBoxBody(physicsWorld, x, y, width, height, 0, BodyType.staticBody, fixtureDef);
		fixtureDef.delete();
		CollisionObjectManager.register(this);
	}

	@Override
	public CollisionObjectType getType() {
		return CollisionObjectType.BOUNDARY;
	}

	@Override
	public Body getBody() {
		return body;
	}

}
