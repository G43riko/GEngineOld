package com.engine.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.engine.entity.Entity;
import com.engine.entity.components.RenderComponent;

import ggllib.render.model.Model;
import ggllib.render.shader.GBasicShader;

public class RenderingEngine {
	private Map<String, GBasicShader> shaders = new HashMap<String, GBasicShader>();
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Model> models = new ArrayList<Model>();
	
	public RenderingEngine(){
		/*
		 * EntityShader
		 * ParticleShader
		 * WaterShader
		 * TerrainShader
		 * SkyShader
		 * GuiShader
		 */
	}
	
	public void add(Entity e){
		if(e.getComponent(RenderComponent.class) != null)
			entities.add(e);
	}
	public void add(Model m){
		models.add(m);
	}

	public void render(){
		for(Model m : models)
			render(m);
	}
	
	public void render(Model model){
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		System.out.println("juchuuu");
	}
}
