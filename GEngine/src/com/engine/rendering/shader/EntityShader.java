package com.engine.rendering.shader;

import ggllib.render.shader.GBasicShader;

public class EntityShader extends GBasicShader{
	public EntityShader() {
		super("entityShader");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "texCoords");
		bindAttribute(2, "normal");
		bindAttribute(3, "tangent");
	}

	@Override
	public void getAllUniformsLocations() {
		uniforms.put("transformationMatrix", super.getUniformLocation("transformationMatrix"));
		uniforms.put("projectionMatrix", super.getUniformLocation("projectionMatrix"));
		uniforms.put("viewMatrix", super.getUniformLocation("viewMatrix"));
		
		uniforms.put("ambient", super.getUniformLocation("ambient"));
		uniforms.put("plane", super.getUniformLocation("plane"));
		uniforms.put("color", super.getUniformLocation("color"));
		
		for(int i=0 ; i<MAX_LIGHTS ; i++){
			uniforms.put("attenuation[" + i +"]", super.getUniformLocation("attenuation[" + i +"]"));
			uniforms.put("lightPositionEyeSpace[" + i +"]", super.getUniformLocation("lightPositionEyeSpace[" + i +"]"));
			uniforms.put("lightColor[" + i +"]", super.getUniformLocation("lightColor[" + i +"]"));
		}
		
		uniforms.put("specularIntensity", super.getUniformLocation("specularIntensity"));
		uniforms.put("specularPower", super.getUniformLocation("specularPower"));
		
		uniforms.put("eyePos", super.getUniformLocation("eyePos"));
		uniforms.put("normalSampler", super.getUniformLocation("normalSampler"));
		uniforms.put("textureSampler", super.getUniformLocation("textureSampler"));
	}
	
	@Override
	public void connectTextures(){
		updateUniform("textureSampler", 0);
		updateUniform("normalSampler", 1);
	}
}
