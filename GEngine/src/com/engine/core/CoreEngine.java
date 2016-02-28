package com.engine.core;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

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
import ggllib.entity.Entity;
import ggllib.entity.component.ModelAndTextureComponent;
import ggllib.entity.component.PosRotScaleComponent;
import ggllib.object.light.PointLight;
import ggllib.render.material.Material;
import ggllib.render.material.Texture2D;
import ggllib.render.model.MaterialedModel;
import ggllib.utils.ContentManager;
import glib.GConfig;
import glib.cycle.GLoop;
import glib.interfaces.InteractableGL;
import glib.util.GOptions;
import glib.util.analytics.Performance;
import glib.util.vector.GVector2f;
import glib.util.vector.GVector3f;
import sun.misc.FloatingDecimal;

public abstract class CoreEngine extends GLoop implements InteractableGL, Controllable{
	private ContentManager 		contentManager 	= new ContentManager();
	private Performance 		performance 	= new Performance();
	private GOptions 			options 		= new GOptions();
	private RenderingEngine 	renderingEngine;
	private Window 				window;
	private Screen				screen;
	
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
		
		if(GLContext.getCapabilities().GL_EXT_framebuffer_object)
			screen = new Screen(contentManager);
		
		
		renderingEngine = new RenderingEngine(this);
		
		performance.start();
		setVSync(false);
		
	}

	public void cleanUp() {
		System.out.println("cleanuje sa");
		contentManager.cleanUp();
		
		renderingEngine.cleanUp();
		
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
		
		
		if(GGLConfig.ENGINE_POST_FX && screen != null)
			screen.startRenderToScreen();
		
		renderingEngine.render();
		
		
		if(GGLConfig.ENGINE_POST_FX && screen != null){
			screen.stopRenderToScreen();
			renderingEngine.renderScreen(screen);
		}
		

//		renderingEngine.renderGuis();
	}

	private void localUpdate(float delta){
		Input.update();
		renderingEngine.update(delta);
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
