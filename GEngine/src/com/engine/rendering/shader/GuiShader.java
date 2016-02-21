package com.engine.rendering.shader;

import ggllib.render.shader.GBasicShader;

public class GuiShader extends GBasicShader{

	public GuiShader(){
		super("guiShader");
	}

	@Override
	protected void bindAttributes(){
		bindAttribute(0, "position");
	}

	@Override
	public void getAllUniformsLocations(){
		uniforms.put("transformationMatrix", super.getUniformLocation("transformationMatrix"));
	}

}
