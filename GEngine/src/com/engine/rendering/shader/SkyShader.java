package com.engine.rendering.shader;

import ggllib.render.shader.GBasicShader;

public class SkyShader extends GBasicShader{
	public SkyShader() {
		super("skyShader");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
	}

	@Override
	public void getAllUniformsLocations() {
		uniforms.put("view", super.getUniformLocation("view"));
		uniforms.put("transformationMatrix", super.getUniformLocation("transformationMatrix"));
		uniforms.put("projectionMatrix", super.getUniformLocation("projectionMatrix"));
		uniforms.put("viewMatrix", super.getUniformLocation("viewMatrix"));
		uniforms.put("color", super.getUniformLocation("color"));
		uniforms.put("ambient", super.getUniformLocation("ambient"));
		uniforms.put("texture", super.getUniformLocation("texture"));
		uniforms.put("fog", super.getUniformLocation("fog"));
		
	}
}