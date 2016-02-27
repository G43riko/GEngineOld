package com.engine.rendering.shader;

import ggllib.render.shader.GBasicShader;

public class PostFxShader extends GBasicShader{
	public PostFxShader() {
		super("postFxShader");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	public void getAllUniformsLocations() {
		uniforms.put("transformationMatrix", super.getUniformLocation("transformationMatrix"));
		uniforms.put("mouseMove", super.getUniformLocation("mouseMove"));
		
		uniforms.put("useCameraBlur", super.getUniformLocation("useCameraBlur"));
		uniforms.put("useAntiAliasing", super.getUniformLocation("useAntiAliasing"));
		
		uniforms.put("typeOfView", super.getUniformLocation("typeOfView"));
	}
}