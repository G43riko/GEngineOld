package com.test;

import com.engine.core.Controllable;
import com.engine.core.GameAble;
import com.engine.gui.Gui;
import com.engine.gui.PointLightViewer;
import com.engine.gui.PosRotScaleViewer;
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
import glib.swing.windows.Viewer;
import glib.swing.windows.WindowViewer;
import glib.util.Utils;
import glib.util.vector.GVector2f;
import glib.util.vector.GVector3f;

public class TestGame extends DefaultTest implements GameAble{
	private GScene<Entity> scene;
	private Controllable parent;
//	private Model model;
	private GAudio audio;
//	private WaterTile water;
	private Entity dragon, entity;
	private Viewer viewer;
	private WindowViewer window;
	
	public TestGame(Controllable parent) {
		this.parent = parent;
		init();
	}
	
	@Override
	public void init() {
		scene = new GScene<Entity>(a -> parent.getRenderingEngine().add(a));

		Entity light;
		
		light = new Entity();
		light.addComponent(new PosRotScaleComponent(new GVector3f(2, 0, 3)));
		light.addComponent(new PointLightComponent(new GVector3f(1)/*, new GVector3f(1, 0.1, 0.002)*/));
		scene.add(light);

		
		
		audio = parent.getContentManager().loadAudio("air_raid.wav", parent.getRenderingEngine().getActCamera());
		audio.play();
		
		
		MaterialedModel m = new MaterialedModel(parent.getContentManager().loadModel("dragon.obj"), 
												parent.getContentManager().loadMaterial("texture.png"));
		dragon = new Entity();
		dragon.addComponent(new PosRotScaleComponent());
		dragon.addComponent(new ModelAndTextureComponent(m));
		scene.add(dragon);
		
		
		m = new MaterialedModel(parent.getContentManager().loadModel("a.obj"),
								new Material(parent.getContentManager().loadTexture("texture.png"),
							  				 parent.getContentManager().loadTexture("textureNormal.png")));
		entity = new Entity();
		entity.addComponent(new PosRotScaleComponent());
		entity.addComponent(new ModelAndTextureComponent(m));
		scene.add(entity);
		
		
//		viewer = new PosRotScaleViewer(light.getComponent(PosRotScaleComponent.class));
		viewer = new PointLightViewer(light.getComponent(PointLightComponent.class));
		window = new WindowViewer(viewer);
		window.setVisible(true);
	}
	
	@Override
	public void update(float delta) {
		scene.foreach(a -> a.update(delta));
		audio.update(delta);
		
		entity.rotate(new GVector3f(0, 0.1, 0));
		
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
