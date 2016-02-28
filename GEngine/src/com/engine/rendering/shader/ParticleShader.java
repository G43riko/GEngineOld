package com.engine.rendering.shader;

import ggllib.render.shader.GBasicShader;

public class ParticleShader extends GBasicShader{

	public ParticleShader(){
		super("particleShader");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindAttributes(){
		bindAttribute(0, "position");
	}

	@Override
	public void getAllUniformsLocations(){
		uniforms.put("modelViewMatrix", super.getUniformLocation("modelViewMatrix"));
		uniforms.put("projectionMatrix", super.getUniformLocation("projectionMatrix"));
		uniforms.put("textOffset1", super.getUniformLocation("textOffset1"));
		uniforms.put("textOffset2", super.getUniformLocation("textOffset2"));
		uniforms.put("textCoords", super.getUniformLocation("textCoords"));
	}

}