package com.engine.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.engine.entity.Entity;
import com.engine.entity.components.RenderComponent;

import ggllib.render.shader.GBasicShader;

public class RenderingEngine {
	private Map<String, GBasicShader> shaders = new HashMap<String, GBasicShader>();
	private List<Entity> entities = new ArrayList<Entity>();
	
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

	public void render() {
	}
}
