package com.engine.rendering.shader;

import ggllib.render.shader.GBasicShader;

public class WaterShader extends GBasicShader {

	public WaterShader() {
		super("waterShader");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}


	@Override
	public void getAllUniformsLocations(){
		uniforms.put("projectionMatrix", super.getUniformLocation("projectionMatrix"));
		uniforms.put("modelMatrix", super.getUniformLocation("modelMatrix"));
		uniforms.put("viewMatrix", super.getUniformLocation("viewMatrix"));

		uniforms.put("reflectionTexture", super.getUniformLocation("reflectionTexture"));
		uniforms.put("refractionTexture", super.getUniformLocation("refractionTexture"));
		uniforms.put("dudvMap", super.getUniformLocation("dudvMap"));
		uniforms.put("normalMap", super.getUniformLocation("normalMap"));
		uniforms.put("depthMap", super.getUniformLocation("depthMap"));
		
		for(int i=0 ; i<MAX_LIGHTS ; i++){
			uniforms.put("attenuation[" + i +"]", super.getUniformLocation("attenuation[" + i +"]"));
			uniforms.put("lightPosition[" + i +"]", super.getUniformLocation("lightPosition[" + i +"]"));
			uniforms.put("lightColor[" + i +"]", super.getUniformLocation("lightColor[" + i +"]"));
		}
		
		uniforms.put("moveFactor", super.getUniformLocation("moveFactor"));
		uniforms.put("cameraPosition", super.getUniformLocation("cameraPosition"));
	}
	
	@Override
	public void connectTextures(){
		updateUniform("reflectionTexture", 0);
		updateUniform("refractionTexture", 1);
		updateUniform("dudvMap", 2);
		updateUniform("normalMap", 3);
		updateUniform("depthMap", 4);
	}
}

