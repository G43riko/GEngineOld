package com.engine.core;


import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

import java.awt.event.WindowEvent;

import com.engine.rendering.RenderingEngine;

import ggllib.GGLConfig;
import ggllib.core.Input;
import ggllib.core.Window;
import ggllib.utils.ContentManager;
import glib.GConfig;
import glib.cycle.GLoop;
import glib.interfaces.InteractableGL;
import glib.util.GOptions;
import glib.util.analytics.Performance;

public abstract class CoreEngine extends GLoop implements InteractableGL, Controllable{
	private ContentManager 	contentManager 	= new ContentManager();
	private Performance 	performance 	= new Performance();
	private GOptions 		options 		= new GOptions();
	private RenderingEngine renderingEngine;
	private Window 			window;
	
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
		renderingEngine = new RenderingEngine(this);
		performance.start();
		setVSync(false);
	}

	public void cleanUp() {
		System.out.println("cleanuje sa");
		contentManager.cleanUp();

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
		renderingEngine.prepare();
		renderingEngine.render();
	}

	private void localUpdate(float delta){
		Input.update();
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
