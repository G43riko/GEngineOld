package com.test;

import com.engine.core.Controllable;
import com.engine.core.GameAble;
import com.engine.gui.Gui;
import com.engine.water.WaterFrameBuffers;

import ggllib.audio.GAudio;
import ggllib.core.Camera;
import ggllib.core.Input;
import ggllib.entity.Entity;
import ggllib.entity.component.ModelAndTextureComponent;
import ggllib.entity.component.PosRotScaleComponent;
import ggllib.render.material.Material;
import ggllib.render.material.Texture2D;
import ggllib.render.model.MaterialedModel;
import ggllib.render.model.Model;
import glib.data.good.GScene;
import glib.math.GMath;
import glib.network.tcp_server_client.GClient;
import glib.network.tcp_server_client.GServer;
import glib.util.Utils;
import glib.util.vector.GVector2f;
import glib.util.vector.GVector3f;

public class TestGame implements GameAble{
	private GScene<Entity> scene;
	private Controllable parent;
	private Model model;
	private GAudio audio;
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
		
		audio = parent.getContentManager().loadAudio("audio/air_raid.wav", parent.getRenderingEngine().getActCamera());

		audio.play();
		
		Texture2D texture = parent.getContentManager().loadTexture("texture.png");
		MaterialedModel m = new MaterialedModel(parent.getContentManager().loadModel("person.obj"), new Material(texture));
//		for(int i=0 ; i<7000 ; i++){
			Entity e = new Entity();
//			e.addComponent(new PosRotScaleComponent(new GVector3f(Math.random() * 300, 0, Math.random() * 300)));
			e.addComponent(new PosRotScaleComponent(new GVector3f()));
			e.addComponent(new ModelAndTextureComponent(m));
			scene.add(e);
//		}

	}
	
	@Override
	public void update(float delta) {
		scene.foreach(a -> a.update(delta));
		audio.update(delta);
//		System.out.println(parent.getPerformance().getLastSecondData());
	}
	
	
	@Override
	public void input() {
		scene.foreach(a -> a.input());
	}
}
