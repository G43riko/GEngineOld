package com.engine.rendering;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.engine.GLine;
import com.engine.core.CoreEngine;
import com.engine.core.Screen;
import com.engine.gui.Gui;
import com.engine.particles.Particle;
import com.engine.particles.ParticleManager;
import com.engine.particles.ParticleTexture;
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
import ggllib.render.model.MaterialedModel;
import ggllib.render.shader.GBasicShader;
import ggllib.utils.Maths;
import glib.util.vector.GMatrix4f;
import glib.util.vector.GVector3f;

public class RenderingEngine extends GRenderingEngine{
	private List<Gui> guis = new ArrayList<Gui>();
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	private List<Entity> pointLights = new ArrayList<Entity>();
	private ParticleManager particles;
	private SkyBox skybox;
	private CoreEngine parent;
	private GLine line;
	
	public RenderingEngine(CoreEngine parent){
		this.parent = parent;
		
		line = new GLine(new GVector3f(), new GVector3f(0, 100, 0), parent.getContentManager().getLoader());
		
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

	public void renderLine(GLine line) {
		prepareModel(line.getModel(), 1);
		GL11.glDrawElements(GL11.GL_LINE_STRIP, line.getModel().getVertexCount(),GL11.GL_UNSIGNED_INT, 0);
		disableVertex(1);
		
	}
	
	public void renderParticles(){
		GBasicShader shader = getShader("particleShader").bind();
		
		prepareModel(ParticleManager.PARTICLE_MODEL, 6);

		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
		
		for(ParticleTexture texture : particles.getParticles().keySet()){
			texture.getTexture().bind(GL13.GL_TEXTURE0);
			List<Particle> particleList = particles.getParticles().get(texture);
			pointer = 0;
			float[] vboData = new float[particleList.size() * ParticleManager.INSTANCE_DATA_LENGTH];
			shader.updateUniform("numberOfRows", texture.getNumberOfRows());
			for(Particle p : particleList){
				shader.updateUniform("modelViewMatrix", updateModelViewMatrix(p.getPosition(), p.getRotation(), p.getScale(), vboData));
				updateTextCoordsInfo(p, vboData);
			}
			parent.getContentManager().getLoader().updateVBO(particles.getVbo(), vboData, ParticleManager.getBuffer());
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, ParticleManager.PARTICLE_MODEL.getVertexCount(), particleList.size());
			System.out.println(particleList.size());
		}

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		disableVertex(6);
	}
	
	public void renderSkyBox(SkyBox sky){
		GBasicShader shader = getShader("renderSkyBox").bind();
		
		GMatrix4f vm = new GMatrix4f(viewMatrix);
		vm.set(3, 0, 0);
		vm.set(3, 1, 0);
		vm.set(3, 2, 0);
		
		shader.updateUniform("viewMatrix", vm);
		shader.updateUniform("fogColor", sky.getFogColor());
		
		prepareModel(sky.getModel(), 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, sky.getTexture());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, sky.getModel().getVertexCount());
		
		disableVertex(1);
		
	}
	
	private void renderWaters(){
		if(waters.isEmpty())
			return;
		
		GBasicShader shader = getShader("waterShader").bind();
		prepareModel(WaterTile.getQuad(), 1);
		
		shader.connectTextures();
		shader.updateUniform("viewMatrix", Maths.createViewMatrix(getActCamera().getPosition(), getActCamera().getRotation()));
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
		disableVertex(1);
	}
	
	public void renderScreen(Screen screen) {
		GBasicShader shader = getShader("postFxShader").bind();
		
		prepareModel(screen.getModel(), 1);

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		screen.getTexture().bind(GL13.GL_TEXTURE0);
		
		shader.updateUniform("transformationMatrix", screen.getTransformationMatrix());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, screen.getModel().getVertexCount());
		
//		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		disableVertex(1);
	}
	
	public void renderGuis(){
		if(guis.isEmpty())
			return;
		
		GBasicShader shader = getShader("guiShader").bind();
		prepareModel(Gui.getQuad(), 1);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		//render
		for(Gui gui : guis){
			gui.getTexture().bind(GL13.GL_TEXTURE0);
			
			shader.updateUniform("transformationMatrix", gui.getTransformationMatrix());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, Gui.getQuad().getVertexCount());
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		disableVertex(1);
	}
	
	private void renderEntities(){
		GBasicShader shader = getShader("entityShader").bind(); 
		
		shader.updateUniform("plane", getPlane());
		shader.connectTextures();
		loadLights(shader);

		
		for(MaterialedModel model : entities.keySet()){
			prepareMaterial(shader, model.getMaterial());
			prepareModel(model.getModel(), 4);
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
		
//		startRenderWireframes();
		
		getPlane().set(0, -1, 0, 500000);
		init3D();
		renderLine(line);
		renderWaters();
		renderEntities();
		
		if(skybox != null)
			renderSkyBox(skybox);
		
		renderParticles();
		
//		stopRenderWireframes();
	}

	
	public void update(float delta){
		getActCamera().update(delta);
		particles.update(delta, getActCamera());
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

