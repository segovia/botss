package com.BotS.BaseGame;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import com.google.fpl.liquidfun.ParticleSystem;

public class WaterEntity extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final WaterVertexBufferObject mWaterVertexBufferObject;
	private int mParticleCount;
	private ParticleSystem mParticleSystem;
	private RenderTexture renderTexture;

	// ===========================================================
	// Constructors
	// ===========================================================

	public WaterEntity(final VertexBufferObjectManager pVertexBufferObjectManager, final ParticleSystem pParticleSystem) {
		super(0, 0, 0, 0, WaterShader.getInstance());
		mParticleSystem = pParticleSystem;
		mParticleCount = mParticleSystem.getParticleCount();
		renderTexture = new RenderTexture(ResourcesManager.getInstance().textureManager,
				(int) ResourcesManager.getInstance().camera.getWidth(),
				(int) ResourcesManager.getInstance().camera.getHeight());
		mWaterVertexBufferObject = new WaterVertexBufferObject(pVertexBufferObjectManager, mParticleCount);
		mWaterVertexBufferObject.setDirtyOnHardware();
		setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IVertexBufferObject getVertexBufferObject() {
		return mWaterVertexBufferObject;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		onUpdateVertices();
	}

	@Override
	protected void onUpdateVertices() {
		mParticleSystem.getPositionAndVelocityBuffer(0, mParticleCount, mWaterVertexBufferObject.getByteBuffer());
		mWaterVertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		mWaterVertexBufferObject.bind(pGLState, mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		mWaterVertexBufferObject.draw(DrawMode.POINTS.getDrawMode(), mParticleCount);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		mWaterVertexBufferObject.unbind(pGLState, mShaderProgram);
		super.postDraw(pGLState, pCamera);
	}

	public TextureRegion getTexture() {
		return TextureRegionFactory.extractFromTexture(renderTexture);
	}

	@Override
	protected void onManagedDraw(GLState pGLState, Camera pCamera) {
		if (!renderTexture.isInitialized()) {
			renderTexture.init(pGLState);
		}
		renderTexture.begin(pGLState, false, true, Color.TRANSPARENT);
		{
			super.onManagedDraw(pGLState, pCamera);
		}
		renderTexture.end(pGLState);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}