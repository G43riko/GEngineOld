package com.engine.rendering;

import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.engine.core.CoreEngine;
import com.engine.rendering.shader.EntityShader;

import ggllib.core.Camera;
import ggllib.entity.Entity;
import ggllib.entity.component.RenderComponent;
import ggllib.render.material.Material;
import ggllib.render.model.BorderedModel;
import ggllib.render.model.MaterialedModel;
import ggllib.render.model.Model;
import ggllib.render.shader.GBasicShader;
import ggllib.utils.Maths;
import glib.util.GLog;
import glib.util.vector.GMatrix4f;
import glib.util.vector.GVector3f;

public class RenderingEngine {
	private Map<String, GBasicShader> shaders = new HashMap<String, GBasicShader>();
	private Map<MaterialedModel, List<Entity>> entities = new HashMap<MaterialedModel, List<Entity>>();
	private CoreEngine parent;
	private Camera actCamera;
	
	public RenderingEngine(CoreEngine parent){
		this.parent = parent;
		
		shaders.put("entityShader", new EntityShader());
		
		actCamera = new Camera();
		setProjectionMatrix(actCamera.getProjectionMatrix());
		GVector3f bg = parent.getOptions().getBackgroundColor();
		glClearColor(bg.getX(), bg.getY(), bg.getZ(), 1);
//		GL11.glViewport(0, 0, parent.getWindow().getWidth(), parent.getWindow().getHeight());
		/*
		 * EntityShader
		 * ParticleShader
		 * WaterShader
		 * TerrainShader
		 * SkyShader
		 * GuiShader
		 */
	}
	
	public void add(Entity entity){
		MaterialedModel model = entity.getMaterialedModel();
		if(!entities.containsKey(model))
			entities.put(model, new ArrayList<Entity>());
		
		entities.get(model).add(entity);
		
	}

	public void render(){
		GBasicShader shader = getShader("entityShader"); 
		shader.bind();
		
		for(MaterialedModel model : entities.keySet()){
			prepareMaterialedModel(3, model);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch){
				prepareInstance(shader, entity);
				
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(),GL11.GL_UNSIGNED_INT, 0);	
			}
			
			disableVertex(3);
		}
	}
	
	//UTILS
	
	public void prepare(){
		setViewMatrix(Maths.createViewMatrix(actCamera.getPosition(), actCamera.getRotation()));
	}
	
	protected void disableVertex(int num){
		for(int i=0 ; i<num ; i++)
			GL20.glDisableVertexAttribArray(i);
		
		GL30.glBindVertexArray(0);
	}

	private void prepareMaterialedModel(int num, MaterialedModel model) {
		setMaterial(model.getMaterial());
		
		GL30.glBindVertexArray(model.getModel().getVaoID());
		
		for(int i=0 ; i<num ; i++)
			GL20.glEnableVertexAttribArray(i);
	}
	
	private void prepareInstance(GBasicShader shader, Entity entity) {
		shader.updateUniform("transformationMatrix", entity.getTransformationMatrix());
	}
	
	//RENDERERS
	
	public void renderObject(Entity entity) {
		GBasicShader shader = getShader("objectShader"); 
		
		shader.bind();

		shader.updateUniform("transformationMatrix", entity.getTransformationMatrix());
		setMaterial(entity.getMaterial());
		
		disableVertex(3);
	}
	
	public void render(Model model){
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	//GETTERS
	
	public GBasicShader getShader(String shader){
		return shaders.get(shader);
	}

	//SETTERS
	
	private void setMaterial(Material material) {
		getShader("objectShader").connectTextures();
		
		if(material.getDiffuse() != null){
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			material.getDiffuse().bind();
		}
		
		
		getShader("objectShader").updateUniform("specularPower", material.getSpecularPower());
		getShader("objectShader").updateUniform("specularIntensity", material.getSpecularIntensity());
	}
	
	private void setViewMatrix(GMatrix4f matrix) {
		shaders.forEach((key,val) -> {
			if(val.hasUniform("viewMatrix")){
				val.bind();
				val.updateUniform("viewMatrix", matrix);
			}
		});
		
	}

	private void setProjectionMatrix(GMatrix4f projectionMatrix) {
		shaders.forEach((key,val) -> {
			if(val.hasUniform("projectionMatrix")){
				val.bind();
				val.updateUniform("projectionMatrix", projectionMatrix);
			}
		});
	}
	
}
