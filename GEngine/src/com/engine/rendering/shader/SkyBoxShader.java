package com.engine.rendering.shader;

import ggllib.render.shader.GBasicShader;

public class SkyBoxShader extends GBasicShader{

	public SkyBoxShader(){
		super("skyBoxShader");
	}

	@Override
	protected void bindAttributes(){
		bindAttribute(0, "position");
	}

	@Override
	public void getAllUniformsLocations(){
		uniforms.put("fogColor", super.getUniformLocation("fogColor"));
		uniforms.put("viewMatrix", super.getUniformLocation("viewMatrix"));
		uniforms.put("projectionMatrix", super.getUniformLocation("projectionMatrix"));
		
		
	}

}
