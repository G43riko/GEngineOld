package com.engine.rendering;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.engine.core.CoreEngine;
import com.engine.core.Screen;
import com.engine.gui.Gui;
import com.engine.rendering.shader.EntityShader;
import com.engine.rendering.shader.GuiShader;
import com.engine.rendering.shader.WaterShader;
import com.engine.water.WaterTile;

import ggllib.core.Camera;
import ggllib.entity.Entity;
import ggllib.entity.component.ModelAndTextureComponent;
import ggllib.entity.component.light.PointLightComponent;
import ggllib.object.light.PointLight;
import ggllib.render.material.Material;
import ggllib.render.model.BorderedModel;
import ggllib.render.model.MaterialedModel;
import ggllib.render.model.Model;
import ggllib.render.shader.GBasicShader;
import ggllib.utils.Maths;
import glib.util.GLog;
import glib.util.vector.GMatrix4f;
import glib.util.vector.GVector2f;
import glib.util.vector.GVector3f;
import glib.util.vector.GVector4f;

public class RenderingEngine {
	private Map<String, GBasicShader> shaders = new HashMap<String, GBasicShader>();
	private Map<MaterialedModel, List<Entity>> entities = new HashMap<MaterialedModel, List<Entity>>();
	private List<Gui> guis = new ArrayList<Gui>();
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	private List<Entity> pointLights = new ArrayList<Entity>();
	private CoreEngine parent;
	private Camera actCamera  = new Camera();
	private GVector4f plane = new GVector4f(0, -1, 0, 15);
	private GMatrix4f viewMatrix; 
	
	
	public RenderingEngine(CoreEngine parent){
		this.parent = parent;
		shaders.put("entityShader", new EntityShader());
		shaders.put("waterShader", new WaterShader());
		shaders.put("guiShader", new GuiShader());
		
		setProjectionMatrix(actCamera.getProjectionMatrix());
		
		GVector3f bg = parent.getOptions().getBackgroundColor();
		glClearColor(bg.getX(), bg.getY(), bg.getZ(), 1);

//		GL11.glViewport(0, 0, parent.getWindow().getWidth(), parent.getWindow().getHeight());
		/*
		 * EntityShader
		 * ParticleShader
		 * WaterShader
		 * TerrainShader
		 * SkyShader
		 * GuiShader
		 */
	}
	
	//ADDERS
	
	public void add(Gui gui){
		guis.add(gui);
	}
	
	public void add(Entity entity){
		if(entity.hasComponent(ModelAndTextureComponent.class)){
			MaterialedModel model = entity.getMaterialedModel();
			if(!entities.containsKey(model))
				entities.put(model, new ArrayList<Entity>());
			
			entities.get(model).add(entity);
		}
		if(entity.hasComponent(PointLightComponent.class)){
			pointLights.add(entity);
		}
	}

	public void add(WaterTile water){
		waters.add(water);
	}
	
//	public void add(PointLight light){
//		pointLights.add(light);
//	}
	
	//UTILS
	
	private void init3D(){
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void prepare(){
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		setViewMatrix(Maths.createViewMatrix(actCamera.getPosition(), actCamera.getRotation()));
		init3D();
	}
	
	protected void disableVertex(int num){
		for(int i=0 ; i<num ; i++)
			GL20.glDisableVertexAttribArray(i);
		
		GL30.glBindVertexArray(0);
	}

	private void prepareMaterialedModel(int num, MaterialedModel model, GBasicShader shader) {
		setMaterial(model.getMaterial());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getMaterial().getDiffuse().getId());
		
		if(model.getMaterial().getNormal() != null){
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getMaterial().getNormal().getId());
		}
		shader.updateUniform("specularIntensity", model.getMaterial().getSpecularIntensity());
		shader.updateUniform("specularPower", model.getMaterial().getSpecularPower());
		
		GL30.glBindVertexArray(model.getModel().getVaoID());
		
		for(int i=0 ; i<num ; i++)
			GL20.glEnableVertexAttribArray(i);
		
	}
	
	private void prepareInstance(GBasicShader shader, Entity entity) {
		shader.updateUniform("transformationMatrix", entity.getTransformationMatrix());
	}
	
	private void loadLights(GBasicShader shader){
		for(int i=0 ; i<EntityShader.MAX_LIGHTS ; i++){
			if(i < pointLights.size()){
				shader.updateUniform("lightPositionEyeSpace[" + i +"]", getEyeSpacePosition(pointLights.get(i), viewMatrix));
				shader.updateUniform("lightColor[" + i +"]", pointLights.get(i).getColor());
				shader.updateUniform("attenuation[" + i +"]", pointLights.get(i).getAttenuation());
			}
			else{
				shader.updateUniform("lightPositionEyeSpace[" + i +"]", new GVector3f());
				shader.updateUniform("lightColor[" + i +"]", new GVector3f());
				shader.updateUniform("attenuation[" + i +"]", new GVector3f(1, 0, 0));
			}
		}
	}
	
	private GVector3f getEyeSpacePosition(Entity light, GMatrix4f viewMatrix){
		GVector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.getX(), position.getY(), position.getZ(), 1f);
        Matrix4f.transform(Maths.GMatrixToMatrix(viewMatrix), eyeSpacePos, eyeSpacePos);
        return new GVector3f(eyeSpacePos.getX(), eyeSpacePos.getY(), eyeSpacePos.getZ());
	}
	
	//RENDERERS

	public void renderWaters(){
		if(waters.isEmpty())
			return;
		
		GBasicShader shader = shaders.get("waterShader");
		shader.bind();
		
		shader.connectTextures();
		
		shader.updateUniform("viewMatrix", Maths.createViewMatrix(actCamera.getPosition(), actCamera.getRotation()));
		GL30.glBindVertexArray(WaterTile.getQuad().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		shader.updateUniform("cameraPosition", actCamera.getPosition());
		loadLights(shader);
		
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);;
		
		for (WaterTile tile : waters) {
			shader.updateUniform("moveFactor", tile.getMoveFactor());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tile.getFbos().getReflectionTexture());
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tile.getFbos().getRefractionTexture());
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tile.getDudvTexture().getId());
			GL13.glActiveTexture(GL13.GL_TEXTURE3);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tile.getNormalTexture().getId());
			GL13.glActiveTexture(GL13.GL_TEXTURE4);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tile.getFbos().getRefractionDepthTexture());
			
			
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), 
																				 tile.getHeight(), 
																				 tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
			
			shader.updateUniform("modelMatrix", Maths.MatrixToGMatrix(modelMatrix));
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, WaterTile.getQuad().getVertexCount());
		}
		shader.unbind();
	}
	
	public void renderScreen(Screen screen) {
		shaders.get("postFXShader").bind();
		GL30.glBindVertexArray(screen.getModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		screen.getTexture().bind();
		
		shaders.get("postFXShader").updateUniform("transformationMatrix", screen.getTransformationMatrix());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, screen.getModel().getVertexCount());
		
//		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public void renderGuis(){
		if(guis.isEmpty())
			return;
		
		GBasicShader shader = shaders.get("guiShader");
		shader.bind();
		GL30.glBindVertexArray(Gui.getQuad().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		//render
		for(Gui gui : guis){
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			gui.getTexture().bind();
			shader.updateUniform("transformationMatrix", gui.getTransformationMatrix());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, Gui.getQuad().getVertexCount());
		}
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		disableVertex(1);
	}
	
	public void render(){
		GBasicShader shader = getShader("entityShader"); 
		shader.bind();
		
		shader.updateUniform("plane", plane);
		shader.connectTextures();
		loadLights(shader);
		
		for(MaterialedModel model : entities.keySet()){
			prepareMaterialedModel(4, model, shader);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch){
				prepareInstance(shader, entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);	
			}
			
			disableVertex(4);
		}
	}
	
//	public void renderObject(Entity entity) {
//		GBasicShader shader = getShader("entityShader"); 
//		
//		shader.bind();
//
//		shader.updateUniform("transformationMatrix", entity.getTransformationMatrix());
//		setMaterial(entity.getMaterial());
//		
//		disableVertex(3);
//	}
	
	public GVector4f getPlane(){
		return plane;
	}

	public void render(Model model){
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	//GETTERS
	
	public GBasicShader getShader(String shader){
		return shaders.get(shader);
	}

	//SETTERS
	
	public Camera getActCamera() {
		return actCamera;
	}

	private void setMaterial(Material material) {
		getShader("entityShader").connectTextures();
		
		if(material.getDiffuse() != null){
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			material.getDiffuse().bind();
		}
		
		
		getShader("entityShader").updateUniform("specularPower", material.getSpecularPower());
		getShader("entityShader").updateUniform("specularIntensity", material.getSpecularIntensity());
	}
	
	private void setViewMatrix(GMatrix4f matrix) {
		this.viewMatrix = matrix;
		shaders.forEach((key,val) -> {
			if(val.hasUniform("viewMatrix")){
				val.bind();
				val.updateUniform("viewMatrix", matrix);
			}
		});
	}

	private void setProjectionMatrix(GMatrix4f projectionMatrix) {
		shaders.forEach((key,val) -> {
			if(val.hasUniform("projectionMatrix")){
				val.bind();
				val.updateUniform("projectionMatrix", projectionMatrix);
			}
		});
	}
	
}

