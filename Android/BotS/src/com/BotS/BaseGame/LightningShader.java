package com.BotS.BaseGame;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

import com.BotS.Physics.PhysicsConstants;

public class LightningShader extends ShaderProgram implements PhysicsConstants, GameConstants {

	private static LightningShader INSTANCE;

	//@formatter:off
	
	public static final String VERTEXSHADER = "uniform mat4 "
			+ ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" + "attribute vec4 "
			+ ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" + "attribute vec4 "
			+ ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" + "attribute vec2 "
			+ ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" + "varying vec4 "
			+ ShaderProgramConstants.VARYING_COLOR + ";\n" + "varying vec2 "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" + "void main() {\n" + "	"
			+ ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR  + " * 1000.0 ;\n" + "	"
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = "
			+ ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" + "	gl_Position = "
			+ ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * "
			+ ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" + "}";

	public static final String FRAGMENTSHADER = "precision lowp float;\n" + "uniform sampler2D "
			+ ShaderProgramConstants.UNIFORM_TEXTURE_0
			+ ";\n"
			+ "uniform vec2 u_texRegionMin;\n"
			+ "uniform vec2 u_texRegionMax;\n"
			+ "uniform float u_time;\n"
			+ "varying lowp vec4 "
			+ ShaderProgramConstants.VARYING_COLOR
			+ ";\n"
			+ "varying mediump vec2 "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES
			+ ";\n"
			+ "void main() {\n"
			// + "	gl_FragColor = "
			// + ShaderProgramConstants.VARYING_COLOR + " * texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 +
			// ", "
			// + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ");\n"
			+ "	vec2 normTexCoord = (v_textureCoordinates - u_texRegionMin)/(u_texRegionMax-u_texRegionMin);\n"
			+ "	vec2 distFromCenter = normTexCoord - vec2(0.5);\n"
			+ "	float distSq = dot(distFromCenter, distFromCenter);\n"
			+ " float max =  0.10 * (abs(u_time-500.0) / 500.0);\n"
			+ " const float blurSize = 0.0005;\n"
			+ "	vec4 sum = vec4(0.0);	\n"
			+ "	float t1 = 0.5;	\n"
			+ "	float t2 = 0.06;	\n"
			+ "	float t3 = 0.03;	\n"
			+
			// "if(texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2(" +
			// ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + blurSize, " +
			// ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)).w > t) {" +

			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 2 * blurSize)) * 1.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 2 * blurSize)) * 4.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 2 * blurSize)) * 7.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 2 * blurSize)) * 4.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 2 * blurSize)) * 1.0/273.0; \n" + 
			
			
			//////////
			
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 1 * blurSize)) * 4.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 1 * blurSize)) * 16.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 1 * blurSize)) * 26.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 1 * blurSize)) * 16.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y - 1 * blurSize)) * 4.0/273.0; \n" + 
			
			////////
			
			
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)) * 7.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)) * 26.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)) * 41.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)) * 26.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y)) * 7.0/273.0; \n" + 
			
			
			///////
			
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 1 * blurSize)) * 4.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 1 * blurSize)) * 16.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 1 * blurSize)) * 26.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 1 * blurSize)) * 16.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 1 * blurSize)) * 4.0/273.0; \n" + 
			
			///////
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 2 * blurSize)) * 1.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x - 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 2 * blurSize)) * 4.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 2 * blurSize)) * 7.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 1 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 2 * blurSize)) * 4.0/273.0; \n" + 
			" sum += texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", vec2("
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".x + 2 * blurSize, "
			+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ".y + 2 * blurSize)) * 1.0/273.0; \n"  
			
			+ "	gl_FragColor = sum * 5.0;	\n"
			// + "	gl_FragColor.r = gl_FragColor.b;\n"
			// + "	gl_FragColor.b = gl_FragColor.b ;\n"
			// + "	gl_FragColor.g = gl_FragColor.g ;\n"
			// + "	gl_FragColor.a = 1.0/distSq * 100000;\n"
			+ "}";

	//@formatter:on

	public LightningShader() {
		super(LightningShader.VERTEXSHADER, LightningShader.FRAGMENTSHADER);
	}

	public static LightningShader getInstance() {
		if (LightningShader.INSTANCE == null) {
			LightningShader.INSTANCE = new LightningShader();
		}
		return LightningShader.INSTANCE;
	}

	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexRegionMinLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexRegionMaxLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTimeLocation = ShaderProgramConstants.LOCATION_INVALID;

	@Override
	protected void link(final GLState pGLState) throws ShaderProgramLinkException {

		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_POSITION);
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_COLOR);
		GLES20.glBindAttribLocation(mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,
				ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

		super.link(pGLState);

		LightningShader.sUniformModelViewPositionMatrixLocation = getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		LightningShader.sUniformTexture0Location = getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
		// LightningShader.sUniformTexRegionMinLocation = getUniformLocation("u_texRegionMin");
		// LightningShader.sUniformTexRegionMaxLocation = getUniformLocation("u_texRegionMax");
		// LightningShader.sUniformTimeLocation = getUniformLocation("u_time");

	}

	@Override
	public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(LightningShader.sUniformModelViewPositionMatrixLocation, 1, false,
				pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(LightningShader.sUniformTexture0Location, 0);

		// ((HighPerformanceTiledSpriteVertexBufferObject)pVertexBufferObjectAttributes).

	}
}
