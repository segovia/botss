package com.BotS.BaseGame;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.andengine.entity.Entity;

import com.BotS.BaseGame.GameScene.Layer;

public class SunraySpawner {

	private GameScene gameScene;
	private Set<SunRay> activeSunRays = new HashSet<SunRay>();
	private Set<SunRay> inactiveSunRays = new HashSet<SunRay>();

	public SunraySpawner(GameScene a_gameScene) {
		gameScene = a_gameScene;

	}

	public void spawnSunRay(float x, float y, int spellLevel) {
		SunRay ray;
		if (inactiveSunRays.isEmpty()) {
			ray = new SunRay(0.0f, 0.0f, gameScene);
			Entity layer = gameScene.getLayer(Layer.BACKGROUND);
			layer.attachChild(ray);
			activeSunRays.add(ray);
		} else {
			ray = inactiveSunRays.iterator().next();
			inactiveSunRays.remove(ray);
			activeSunRays.add(ray);
		}
		ray.releaseRay(x, y, spellLevel);
	}

	public void onUpdate() {
		// Check for all rays in active if still running
		Iterator<SunRay> iter = activeSunRays.iterator();
		while (iter.hasNext()) {
			SunRay ray = iter.next();
			if (!ray.isReleased()) {
				iter.remove();
				inactiveSunRays.add(ray);
			}
		}
	}

}
