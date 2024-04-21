package com.BotS.BaseGame;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.andengine.entity.Entity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.BotS.BaseGame.GameScene.Layer;
import com.BotS.Physics.PhysicsWorld;

public class LightningSpawner {

	private GameScene gameScene;
	private Set<Lightning> activeLightnings = new HashSet<Lightning>();
	private Set<Lightning> inactiveLightnings = new HashSet<Lightning>();
	private VertexBufferObjectManager vbo;
	private PhysicsWorld physicsWorld;

	public LightningSpawner(GameScene a_gameScene, VertexBufferObjectManager a_vbo, PhysicsWorld a_physicsWorld) {
		gameScene = a_gameScene;
		vbo = a_vbo;
		physicsWorld = a_physicsWorld;
	}

	public void spawnLightning(float x, float y) {
		Lightning light;
		if (inactiveLightnings.isEmpty()) {
			light = new Lightning(x, y, vbo, physicsWorld);
			Entity layer = gameScene.getLayer(Layer.FIREBALL);
			layer.attachChild(light);
			activeLightnings.add(light);
		} else {
			light = inactiveLightnings.iterator().next();
			inactiveLightnings.remove(light);
			activeLightnings.add(light);
		}
		light.spawnLightning(x, y);
	}

	public void onUpdate() {
		// Check for all icebergs in active if still running
		Iterator<Lightning> iter = activeLightnings.iterator();
		while (iter.hasNext()) {
			Lightning light = iter.next();
			if (!light.isVisible()) {
				iter.remove();
				inactiveLightnings.add(light);
			}
		}
	}

}
