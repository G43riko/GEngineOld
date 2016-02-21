package com.engine.water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.engine.rendering.shader.WaterShader;

import ggllib.core.Camera;
import ggllib.render.model.BorderedModel;
import ggllib.utils.Loader;
import ggllib.utils.Maths;
import glib.util.vector.GMatrix4f;

public class WaterRenderer {

	private BorderedModel quad;
	private WaterShader shader;

	public WaterRenderer(Loader loader, WaterShader shader, GMatrix4f projectionMatrix) {
		this.shader = shader;
		shader.bind();
		shader.updateUniform("projectionMatrix", projectionMatrix);
		shader.unbind();
		
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera) {
		prepareRender(camera);	
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), 
																				 tile.getHeight(), 
																				 tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
			
			shader.updateUniform("modelMatrix", Maths.MatrixToGMatrix(modelMatrix));
			
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera){
		shader.bind();
		
		shader.updateUniform("viewMatrix", Maths.createViewMatrix(camera.getPosition(), camera.getRotation()));
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}
	
	private void unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.unbind();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
