package com.engine.core;

import com.engine.rendering.RenderingEngine;

import ggllib.GGLConfig;
import ggllib.core.Input;
import ggllib.core.Window;
import glib.GConfig;
import glib.cycle.GLoop;
import glib.interfaces.InteractableGL;
import glib.util.analytics.Performance;

public abstract class CoreEngine extends GLoop implements InteractableGL, Controllable{
	private Window window;
	private RenderingEngine renderingEngine;
	private Performance performance = new Performance();
	
	public CoreEngine(){
		super(GGLConfig.ENGINE_FPS);
	}
	
	public void init(){
		window = new Window(this, GConfig.WINDOW_TITLE, GConfig.WINDOW_SIZE, GGLConfig.WINDOW_FULLSCREEN);
		renderingEngine = new RenderingEngine();
		performance.start();;
	}

	protected void cleanUp() {
		window.cleanUp();
	}
	
	@Override
	protected void loop(float delta) {
		localInput();
		performance.endInput();
		localUpdate(delta);
		performance.endUpdate();
		localRender();
		performance.endRender();
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
		renderingEngine.render();
	}

	private void localUpdate(float delta){
		Input.update();
		update(delta);
	}

	private void localInput(){
		if(Input.isKeyDown(Input.KEY_ESCAPE))
			stop();
		
		input();
	}
	
	//GETTERS
	
	@Override
	public RenderingEngine getRenderingEngine() {return renderingEngine;}
	public Performance getPerformance() {return performance;}
	public Window getWindow() {return window;}
}
