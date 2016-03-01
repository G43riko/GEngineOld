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
		bindAttribute(1, "modelViewMatrix");
		bindAttribute(5, "textOffsets");
		bindAttribute(6, "blendFactor");
	}

	@Override
	public void getAllUniformsLocations(){
		uniforms.put("modelViewMatrix", super.getUniformLocation("modelViewMatrix"));
		uniforms.put("projectionMatrix", super.getUniformLocation("projectionMatrix"));
//		uniforms.put("textOffset1", super.getUniformLocation("textOffset1"));
//		uniforms.put("textOffset2", super.getUniformLocation("textOffset2"));
//		uniforms.put("textCoords", super.getUniformLocation("textCoords"));
		uniforms.put("numberOfRows", super.getUniformLocation("numberOfRows"));
	}

}
