package com.BotS.BaseGame;

import com.google.fpl.liquidfun.Body;

public interface CollisionObject {

	public enum CollisionObjectType {
		UNDEFINED(0), FIREBALL(1), BOAT(2), WATER(3), SUNRAY(4), BIRD(5), CASTLE_BOUNDARY(6), ICEBERG(7), BOUNDARY(8), FIREBALL_SENSOR(
				9), CASTLE(10);

		private int mTypeId;

		CollisionObjectType(int pTypeId) {
			mTypeId = pTypeId;
		}

		public int getTypeId() {
			return mTypeId;
		}

	}

	public CollisionObjectType getType();

	public Body getBody();
}
