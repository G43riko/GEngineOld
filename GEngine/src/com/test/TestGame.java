package com.test;

import com.engine.core.Controllable;
import com.engine.core.GameAble;
import com.engine.gui.Gui;
import com.engine.rendering.RenderingEngine;
import com.engine.water.WaterFrameBuffers;

import ggllib.audio.GAudio;
import ggllib.core.Camera;
import ggllib.core.Input;
import ggllib.entity.Entity;
import ggllib.entity.component.ModelAndTextureComponent;
import ggllib.entity.component.PosRotScaleComponent;
import ggllib.entity.component.light.PointLightComponent;
import ggllib.render.material.Material;
import ggllib.render.material.Texture2D;
import ggllib.render.model.MaterialedModel;
import ggllib.render.model.Model;
import glib.data.good.GScene;
import glib.math.GIntersects;
import glib.math.GMath;
import glib.network.tcp_server_client.GClient;
import glib.network.tcp_server_client.GServer;
import glib.util.Utils;
import glib.util.vector.GVector2f;
import glib.util.vector.GVector3f;

public class TestGame implements GameAble{
	private GScene<Entity> scene;
	private Controllable parent;
//	private Model model;
	private GAudio audio;
//	private WaterTile water;
	private Entity dragon;
	
	public TestGame(Controllable parent) {
		this.parent = parent;
		init();
	}
	
	@Override
	public void init() {
		scene = new GScene<Entity>(a -> parent.getRenderingEngine().add(a));
		
//		float[] vertices = {
//				-0.5f, 0.5f, 0,
//				-0.5f,-0.5f, 0,
//				 0.5f,-0.5f, 0,
//				 0.5f, 0.5f, 0
//		};
//		int[] indices = {
//				0, 1, 3,
//				3, 1, 2
//		};
//		model = parent.getContentManager().getLoader().loadToVAO(vertices, indices);
//		parent.getRenderingEngine().add(model);
		
		
//		renderingEngine.add(new Gui(getContentManager().getLoader(), 
//							new Texture2D("fbos", fbos.getReflectionTexture(), new GVector2f(800, 600)),
//							new GVector2f(-0.5f, 0.5f), new GVector2f(0.25f, 0.25f)));
//		
//		renderingEngine.add(new Gui(getContentManager().getLoader(), 
//							new Texture2D("fbos", fbos.getRefractionTexture(), new GVector2f(800, 600)), 
//							new GVector2f(0.5f, 0.5f), new GVector2f(0.25f, 0.25f)));
		
		
		
		
//		water = new WaterTile(this, 20, 20, 5, fbos);
//		renderingEngine.add(water);
		
//		renderingEngine.add(new PointLight(new GVector3f( 20, 10, 10), new GVector3f(1), new GVector3f(1, 0.1, 0.002)));
//		renderingEngine.add(new PointLight(new GVector3f(-20, 10, 10), new GVector3f(1), new GVector3f(1, 0.1, 0.002)));
		Entity light;
		
		light = new Entity();
		light.addComponent(new PosRotScaleComponent(new GVector3f( 20, 10, 10)));
		light.addComponent(new PointLightComponent(new GVector3f(0, 0, 1), new GVector3f(1, 0.1, 0.002)));
		scene.add(light);
		
		light = new Entity();
		light.addComponent(new PosRotScaleComponent(new GVector3f(-20, 10, 10)));
		light.addComponent(new PointLightComponent(new GVector3f(1, 0, 0), new GVector3f(1, 0.1, 0.002)));
		scene.add(light);
		
		light = new Entity();
		light.addComponent(new PosRotScaleComponent(new GVector3f(0, 10, -40)));
		light.addComponent(new PointLightComponent(new GVector3f(0, 1, 0), new GVector3f(1, 0.1, 0.002)));
		scene.add(light);
		
		
		audio = parent.getContentManager().loadAudio("air_raid.wav", parent.getRenderingEngine().getActCamera());
		audio.play();
		
		
		MaterialedModel m = new MaterialedModel(parent.getContentManager().loadModel("dragon.obj"), 
												parent.getContentManager().loadMaterial("texture.png"));
		dragon = new Entity();
		dragon.addComponent(new PosRotScaleComponent());
		dragon.addComponent(new ModelAndTextureComponent(m));
		scene.add(dragon);

	}
	
	@Override
	public void update(float delta) {
		scene.foreach(a -> a.update(delta));
		audio.update(delta);
		
		Camera camera = parent.getRenderingEngine().getActCamera();
		GVector3f ray = camera.getMousePicker().getCurrentRay();
		
		float y = -camera.getPosition().getY() / ray.getY();
		float x = y * ray.getX() + camera.getPosition().getX();
		float z = y * ray.getZ() + camera.getPosition().getZ();
		
		dragon.setPosition(new GVector3f(x, 0, z));
	}
	
	
	@Override
	public void input() {
		scene.foreach(a -> a.input());
	}
}
