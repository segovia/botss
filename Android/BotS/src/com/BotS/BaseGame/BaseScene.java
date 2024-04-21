package com.BotS.BaseGame;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import com.BotS.BaseGame.SceneManager.SceneType;

public abstract class BaseScene extends Scene {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	protected Engine engine;
	protected Activity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera camera;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public BaseScene() {
		resourcesManager = ResourcesManager.getInstance();
		engine = resourcesManager.engine;
		activity = resourcesManager.activity;
		vbom = resourcesManager.vbom;
		camera = resourcesManager.camera;
		createScene();
	}

	// ---------------------------------------------
	// ABSTRACTION
	// ---------------------------------------------

	public abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();
}