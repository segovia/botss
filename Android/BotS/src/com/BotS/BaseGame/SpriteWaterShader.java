package com.BotS.BaseGame;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class SpriteWaterShader extends ShaderProgram {
	// ===========================================================
	// Constants
	// ===========================================================

	private static SpriteWaterShader INSTANCE;

	//@formatter:off
	public static final String VERTEXSHADER =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
			"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"	" + ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
			"	" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"}";

	public static final String FRAGMENTSHADER =
			"precision lowp float;\n" +
			"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
			"varying lowp vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"	gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n" +
			"   float size = gl_FragColor.z;\n" +
			"   if (gl_FragColor.w < 0.2) {\n"+
			"      gl_FragColor.w = 0.0;\n" +
			"   } else if (gl_FragColor.w < 0.3) {\n" +
			"      gl_FragColor = vec4(1.0, 1.0, 1.0, 0.9);\n" +
			"   } else {\n" +
			"		gl_FragColor = vec4(0.0, (1.0-size)*3.0 + 0.5, 1.0, 0.7);\n" +
			"   }" +
			"}";
	//@formatter:on
	// ===========================================================
	// Fields
	// ===========================================================

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private SpriteWaterShader() {
		super(SpriteWaterShader.VERTEXSHADER, SpriteWaterShader.FRAGMENTSHADER);
	}

	public static SpriteWaterShader getInstance() {
		if (SpriteWaterShader.INSTANCE == null) {
			SpriteWaterShader.INSTANCE = new SpriteWaterShader();
		}
		return SpriteWaterShader.INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void link(final GLState pGLState) throws ShaderProgramLinkException {
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_POSITION);
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_COLOR);
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

		super.link(pGLState);

		SpriteWaterShader.sUniformModelViewPositionMatrixLocation = getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		SpriteWaterShader.sUniformTexture0Location = getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
	}

	@Override
	public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(SpriteWaterShader.sUniformModelViewPositionMatrixLocation, 1, false,
				pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(SpriteWaterShader.sUniformTexture0Location, 0);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
