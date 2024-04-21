package com.BotS.BaseGame;

import java.nio.ByteBuffer;

import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

public class WaterVertexBufferObject extends VertexBufferObject implements GameConstants {

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(
			2)
			.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2,
					GLES20.GL_FLOAT, false)
			.add(ATTRIBUTE_VELOCITY_LOCATION, ATTRIBUTE_VELOCITY, 2, GLES20.GL_FLOAT, false).build();

	public WaterVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int particleCount) {
		super(pVertexBufferObjectManager, particleCount * 2 * 2, DrawType.DYNAMIC, true,
				WaterVertexBufferObject.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
	}

	ByteBuffer getByteBuffer() {
		return mByteBuffer;
	}

	@Override
	public int getHeapMemoryByteSize() {
		return getByteCapacity();
	}

	@Override
	public int getNativeHeapMemoryByteSize() {
		return getByteCapacity();
	}

	@Override
	protected void onBufferData() {
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mByteBuffer.limit(), mByteBuffer, mUsage);
	}

}
