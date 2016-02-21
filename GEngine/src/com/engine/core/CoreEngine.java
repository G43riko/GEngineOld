package com.engine.core;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;

import java.awt.event.WindowEvent;

import com.engine.gui.Gui;
import com.engine.rendering.RenderingEngine;
import com.engine.rendering.ToFrameBufferRendering;
import com.engine.water.WaterFrameBuffers;
import com.engine.water.WaterTile;

import ggllib.GGLConfig;
import ggllib.core.Input;
import ggllib.core.Window;
import ggllib.render.material.Texture2D;
import ggllib.utils.ContentManager;
import glib.GConfig;
import glib.cycle.GLoop;
import glib.interfaces.InteractableGL;
import glib.util.GOptions;
import glib.util.analytics.Performance;
import glib.util.vector.GVector2f;
import sun.misc.FloatingDecimal;

public abstract class CoreEngine extends GLoop implements InteractableGL, Controllable{
	private ContentManager 		contentManager 	= new ContentManager();
	private Performance 		performance 	= new Performance();
	private GOptions 			options 		= new GOptions();
	private RenderingEngine 	renderingEngine;
	private Window 				window;
	private WaterFrameBuffers 	fbos;
	private Screen				screen;
	private WaterTile 			water;
	
	public ContentManager getContentManager() {
		return contentManager;
	}

	public CoreEngine(){
		super(GConfig.ENGINE_FPS);
	}
	
	public void init(){
		options.load(GConfig.getData(), true);
		options.load(GGLConfig.getData(), true);
		
		window = new Window(this, GConfig.WINDOW_TITLE, GConfig.WINDOW_SIZE, GConfig.WINDOW_FULLSCREEN);
		
		fbos = new WaterFrameBuffers();
		screen = new Screen(contentManager.getLoader());
		renderingEngine = new RenderingEngine(this);
		
//		renderingEngine.add(new Gui(getContentManager().getLoader(), 
//									screen.getTexture(), 
//									new GVector2f(0.5f, 0.5f), new GVector2f(0.25f, 0.25f)));
		
		renderingEngine.add(new Gui(getContentManager().getLoader(), 
							new Texture2D("fbos", fbos.getReflectionTexture(), new GVector2f(800, 600)), 
							new GVector2f(-0.5f, 0.5f), new GVector2f(0.25f, 0.25f)));
		
		renderingEngine.add(new Gui(getContentManager().getLoader(), 
							new Texture2D("fbos", fbos.getRefractionTexture(), new GVector2f(800, 600)), 
							new GVector2f(0.5f, 0.5f), new GVector2f(0.25f, 0.25f)));
		
		
		water = new WaterTile(this, 20, 20, 5, fbos);
		renderingEngine.add(water);
		
		performance.start();
		setVSync(false);
		
	}

	public void cleanUp() {
		System.out.println("cleanuje sa");
		contentManager.cleanUp();

		fbos.cleanUp();
		
		window.cleanUp();
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
	
	@Override
	protected void loop(float delta) {
		localInput();
		performance.endInput();
		localUpdate(delta);
		performance.endUpdate();
		localRender();
		performance.endRender();
		Display.update();
	}
	
	@Override
	protected final void endSecond() {
		performance.endSecond();
	}

	@Override
	protected final void endLoop() {
		performance.endLoop();
	}
	
	private void localRender() {

		
		
		glEnable(GL30.GL_CLIP_DISTANCE0);
		
		
		//REFLECTION
		float dist = 2 * (renderingEngine.getActCamera().getPosition().getY() - 5);
		
		renderingEngine.getActCamera().getPosition().addToY(-dist);
		renderingEngine.getActCamera().invertPitch();
		renderingEngine.getPlane().set(0, 1, 0, -5);

		renderingEngine.prepare();
		
		fbos.bindReflectionFrameBuffer();
		renderingEngine.render();
		fbos.unbindCurrentFrameBuffer();
		
		renderingEngine.getActCamera().invertPitch();
		renderingEngine.getActCamera().getPosition().addToY(dist);
		
		//REFRACTION
		renderingEngine.prepare();
		
		renderingEngine.getPlane().set(0, -1, 0, 5);
		fbos.bindRefractionFrameBuffer();
		renderingEngine.render();
		fbos.unbindCurrentFrameBuffer();
		
		glDisable(GL30.GL_CLIP_DISTANCE0);
		
		
		//NORMAL
		renderingEngine.getPlane().set(0, -1, 0, 500000);
//		screen.startRenderToScreen();
//		renderingEngine.render();
//		screen.stopRenderToScreen();
		
		renderingEngine.renderWaters();
		
		renderingEngine.render();
		renderingEngine.renderGuis();
	}

	private void localUpdate(float delta){
		Input.update();
		water.update(delta);
		renderingEngine.getActCamera().update(delta);
		update(delta);
	}

	private void localInput(){
		renderingEngine.getActCamera().input();
		
		if(Input.isKeyDown(Input.KEY_ESCAPE))
			stop();
		
		input();
	}
	
	//GETTERS
	
	@Override
	public RenderingEngine getRenderingEngine() {return renderingEngine;}
	public Performance getPerformance() {return performance;}
	public GOptions getOptions(){return options; }
	public Window getWindow() {return window;}
}
