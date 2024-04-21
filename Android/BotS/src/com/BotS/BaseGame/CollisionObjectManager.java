package com.BotS.BaseGame;

import java.util.ArrayList;

import org.andengine.util.debug.Debug;

import com.google.fpl.liquidfun.Body;

public class CollisionObjectManager {

	private static final CollisionObjectManager INSTANCE = new CollisionObjectManager();

	private ArrayList<CollisionObject> objs = new ArrayList<CollisionObject>();// must be changed
																				// atomically
	private CollisionObject undefinedCollisionObject = new CollisionObject() {

		@Override
		public CollisionObjectType getType() {
			return CollisionObjectType.UNDEFINED;
		}

		@Override
		public Body getBody() {
			return null;
		}
	};

	private CollisionObjectManager() {
		// private constructor
		addToList(undefinedCollisionObject);
	}

	private void clearPrivate() {
		objs.clear();
		addToList(undefinedCollisionObject);
	}

	synchronized private int addToList(CollisionObject obj) {
		objs.add(obj);
		return objs.size() - 1;
	}

	private int registerPrivate(CollisionObject obj) {
		int id = addToList(obj);
		obj.getBody().setUserDataInt(id);
		return id;
	}

	private CollisionObject getPrivate(int id) {
		if (id < 0 || id >= objs.size()) {
			Debug.e("Game Object not found for id: " + id);
			return null;
		}
		return objs.get(id);
	}

	public static int register(CollisionObject obj) {
		return INSTANCE.registerPrivate(obj);
	}

	public static CollisionObject get(int id) {
		return INSTANCE.getPrivate(id);
	}

	public static void clear() {
		INSTANCE.clearPrivate();
	}

}
