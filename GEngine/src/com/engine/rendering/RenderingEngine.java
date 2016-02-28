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

import com.engine.core.CoreEngine;
import com.engine.core.Screen;
import com.engine.gui.Gui;
import com.engine.particles.Particle;
import com.engine.particles.ParticleManager;
import com.engine.rendering.shader.EntityShader;
import com.engine.rendering.shader.GuiShader;
import com.engine.rendering.shader.ParticleShader;
import com.engine.rendering.shader.PostFxShader;
import com.engine.rendering.shader.SkyBoxShader;
import com.engine.rendering.shader.WaterShader;
import com.engine.water.WaterTile;

import ggllib.entity.Entity;
import ggllib.entity.component.ModelAndTextureComponent;
import ggllib.entity.component.light.PointLightComponent;
import ggllib.object.skybox.SkyBox;
import ggllib.render.model.BorderedModel;
import ggllib.render.model.MaterialedModel;
import ggllib.render.shader.GBasicShader;
import ggllib.utils.Maths;
import glib.util.vector.GMatrix4f;
import glib.util.vector.GVector3f;

public class RenderingEngine extends GRenderingEngine{
	private Map<MaterialedModel, List<Entity>> entities = new HashMap<MaterialedModel, List<Entity>>();
	private List<Gui> guis = new ArrayList<Gui>();
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	private List<Entity> pointLights = new ArrayList<Entity>();
	private ParticleManager particles;
	private SkyBox skybox;
//	private CoreEngine parent;
	
	
	public RenderingEngine(CoreEngine parent){
//		this.parent = parent;
		addShader("entityShader", new EntityShader());
		addShader("waterShader", new WaterShader());
		addShader("guiShader", new GuiShader());
		addShader("postFxShader", new PostFxShader());
		addShader("particleShader", new ParticleShader());
//		addShader("skyShader", new SkyShader());
		addShader("renderSkyBox", new SkyBoxShader());
		
		particles = new ParticleManager(parent.getContentManager());
		
//		skybox = new SkyBox(getActCamera(), parent.getContentManager().getLoader());
		skybox = new SkyBox(parent.getContentManager(), new GVector3f(1,0,1));
		
		setProjectionMatrix(getActCamera().getProjectionMatrix());
		
		GVector3f bg = parent.getOptions().getBackgroundColor();
		glClearColor(bg.getX(), bg.getY(), bg.getZ(), 1);

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
	
	public void add(Particle particle){
		particles.add(particle);
	}
	
//	public void add(PointLight light){
//		pointLights.add(light);
//	}
	
	//UTILS
	
	private void loadLights(GBasicShader shader){
		for(int i=0 ; i<EntityShader.MAX_LIGHTS ; i++){
			if(i < pointLights.size()){
				shader.updateUniform("lightPositionEyeSpace[" + i +"]", getEyeSpacePosition(pointLights.get(i)));
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
	
	//RENDERERS

	public void renderParticles(){
		GBasicShader shader = getShader("particleShader");
		shader.bind();
		
		GMatrix4f vm = Maths.createViewMatrix(getActCamera().getPosition(), getActCamera().getRotation());
		
		GL30.glBindVertexArray(ParticleManager.PARTICLE_MODEL.getVaoID());	
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
		
		for(Particle p : particles.getParticles()){
			shader.updateUniform("modelViewMatrix", updateModelViewMatrix(p.getPosition(), p.getRotation(), p.getScale(), vm));
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, ParticleManager.PARTICLE_MODEL.getVertexCount());
		}

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		disableVertex(1);
	}
	
//	public void renderSky(SkyBox sky){
//		GL11.glDisable(GL11.GL_CULL_FACE);
////		GL11.glCullFace(GL11.GL_BACK);
//		
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		GBasicShader shader = getShader("skyShader");
//		shader.bind();
//		
//		
//		shader.updateUniform("transformationMatrix", sky.getTransformationMatrix());
//		
//		prepareMaterialedModel(2, sky.getModel(), shader);
//		
//		GL11.glDrawElements(GL11.GL_TRIANGLES, sky.getModel().getModel().getVertexCount(),GL11.GL_UNSIGNED_INT, 0);
//
//		GL11.glEnable(GL11.GL_CULL_FACE);
//		GL11.glCullFace(GL11.GL_BACK);
//		disableVertex(2);
//	}
//	
	public void renderSkyBox(SkyBox sky){
		GBasicShader shader = getShader("renderSkyBox");
		shader.bind();
		viewMatrix.set(3, 0, 0);
		viewMatrix.set(3, 1, 0);
		viewMatrix.set(3, 2, 0);
		shader.updateUniform("viewMatrix", viewMatrix);
		shader.updateUniform("fogColor", sky.getFogColor());
		
		
		GL30.glBindVertexArray(sky.getModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, sky.getTexture());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, sky.getModel().getVertexCount());
		
		disableVertex(1);
		
	}
	
	private void renderWaters(){
		if(waters.isEmpty())
			return;
		
		GBasicShader shader = getShader("waterShader");
		shader.bind();
		
		shader.connectTextures();
		
		shader.updateUniform("viewMatrix", Maths.createViewMatrix(getActCamera().getPosition(), getActCamera().getRotation()));
		GL30.glBindVertexArray(WaterTile.getQuad().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		shader.updateUniform("cameraPosition", getActCamera().getPosition());
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
		GBasicShader shader = getShader("postFxShader");
		shader.bind();
		
		GL30.glBindVertexArray(screen.getModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		screen.getTexture().bind();
		
		shader.updateUniform("transformationMatrix", screen.getTransformationMatrix());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, screen.getModel().getVertexCount());
		
//		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		disableVertex(1);
	}
	
	public void renderGuis(){
		if(guis.isEmpty())
			return;
		
		GBasicShader shader = getShader("guiShader");
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
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		disableVertex(1);
	}
	
	private void renderEntities(){
		GBasicShader shader = getShader("entityShader"); 
		shader.bind();
		
		shader.updateUniform("plane", getPlane());
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
	
	public void render(){
//		renderToBuffers(null);
		getPlane().set(0, -1, 0, 500000);
		init3D();
		renderWaters();
		renderEntities();
		
		if(skybox != null)
			renderSkyBox(skybox);
		
		renderParticles();
		
	}

	
	public void update(float delta){
		getActCamera().update(delta);
		particles.update(delta);
		
	}
	
//	private void renderToBuffers(WaterTile water){
//		if(fbos == null)
//			return;
//		
//		//REFLECTION
//		float dist = 2 * (actCamera.getPosition().getY() - water.getHeight());
//		
//		actCamera.getPosition().addToY(-dist);
//		actCamera.invertPitch();
//		setViewMatrix(Maths.createViewMatrix(actCamera.getPosition(), actCamera.getRotation()));
//
//		getPlane().set(0, 1, 0, -water.getHeight());
//		
//		fbos.bindReflectionFrameBuffer();
//		renderEntities();
//		fbos.unbindCurrentFrameBuffer();
//		
//		
//		//REFRACTION
//		actCamera.invertPitch();
//		actCamera.getPosition().addToY(dist);
//		setViewMatrix(Maths.createViewMatrix(actCamera.getPosition(), actCamera.getRotation()));
//		
//		getPlane().set(0, -1, 0, water.getHeight());
//		
//		fbos.bindRefractionFrameBuffer();
//		renderEntities();
//		fbos.unbindCurrentFrameBuffer();
//		
//		glDisable(GL30.GL_CLIP_DISTANCE0);
//	}
}

