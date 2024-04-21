package com.BotS.BaseGame;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.andengine.entity.Entity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.BaseGame.GameScene.Layer;
import com.BotS.Physics.PhysicsWorld;

public class IcebergSpawner {

	private GameScene gameScene;
	private Set<Iceberg> activeIcebergs = new HashSet<Iceberg>();
	private Set<Iceberg> inactiveIcebergs = new HashSet<Iceberg>();

	public IcebergSpawner(GameScene a_gameScene, VertexBufferObjectManager a_vbo, PhysicsWorld a_physicsWorld) {
		gameScene = a_gameScene;
	}

	public void spawnIceberg(float x, float y, long lifetime) {
		Iceberg iceb;
		if (inactiveIcebergs.isEmpty()) {
			iceb = new Iceberg(0.0f, 0.0f, gameScene, lifetime);
			Entity layer = gameScene.getLayer(Layer.FIREBALL);
			layer.attachChild(iceb);
			activeIcebergs.add(iceb);
		} else {
			iceb = inactiveIcebergs.iterator().next();
			inactiveIcebergs.remove(iceb);
			activeIcebergs.add(iceb);
		}
		iceb.spawnIceberg(x, y, lifetime);
	}

	public void onUpdate() {
		// Check for all icebergs in active if still running
		Iterator<Iceberg> iter = activeIcebergs.iterator();
		while (iter.hasNext()) {
			Iceberg iceb = iter.next();
			if (!iceb.isVisible()) {
				iter.remove();
				inactiveIcebergs.add(iceb);
			}
		}
	}

}
