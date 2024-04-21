package com.BotS.BaseGame;

import java.util.List;

import com.BotS.Physics.PhysicsWorld;
import com.BotS.Physics.Vector2;

public class CastleBoundary extends Boundary implements CollisionObject {
	private Player owner;

	public CastleBoundary(Player pOwner, PhysicsWorld physicsWorld, float x, float y, float width, float height) {
		super(physicsWorld, x, y, width, height, BOUNDARY_COLLISION_CATEGORY);
		owner = pOwner;
	}

	public CastleBoundary(Player pOwner, PhysicsWorld physicsWorld, List<Vector2<Float>> vertices) {
		super(physicsWorld, vertices, BOUNDARY_COLLISION_CATEGORY);
		owner = pOwner;
	}

	@Override
	public CollisionObjectType getType() {
		return CollisionObjectType.CASTLE_BOUNDARY;
	}

	public Player getOwner() {
		return owner;
	}

}
