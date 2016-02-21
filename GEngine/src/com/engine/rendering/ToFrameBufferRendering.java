package com.engine.rendering;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import ggllib.render.material.Texture2D;
import glib.util.GLog;
import glib.util.vector.GVector2f;

public class ToFrameBufferRendering {
	private Texture2D texture;
	private int framebufferID;
	private int depthRenderBufferID;

	public ToFrameBufferRendering(){
		this(new GVector2f(256, 256));
	}
	
	public ToFrameBufferRendering(GVector2f resolution){
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			GLog.write("FBO nieje podporovaný!!!");
			System.exit(0);
		}
		else {
			framebufferID = glGenFramebuffers();
			
			glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);

			texture = initTexture(resolution);

			glBindFramebuffer(GL_FRAMEBUFFER, 0);
		}
	}
	
	private int initDepthBuffer(GVector2f resolution){
		int depthBuffer = glGenRenderbuffers();
		
		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, resolution.getXi(), resolution.getYi());
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
		
		return depthBuffer;

	}
	
	private Texture2D initTexture(GVector2f resolution){
		Texture2D txt = new Texture2D("FBO", glGenTextures(), resolution);
		
		glBindTexture(GL_TEXTURE_2D, txt.getId());
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, resolution.getXi(), resolution.getYi(), 0, GL_RGBA, GL_INT, (ByteBuffer) null);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, txt.getId(), 0);
		return txt;
	}
	
	private Texture2D createDepthTextureAttachment(GVector2f resolution){
		Texture2D txt = new Texture2D("FBO", glGenTextures(), resolution);
		
        glBindTexture(GL_TEXTURE_2D, txt.getId());
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, resolution.getXi(), resolution.getYi(), 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, txt.getId(), 0);
        return txt;
    }
	
	public void startRenderToFrameBuffer(){
		glDepthFunc (GL_LEQUAL);
		glEnable (GL_DEPTH_TEST);
		glShadeModel (GL_SMOOTH);
		glHint (GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 

		glViewport (0, 0, texture.getSize().getXi(), texture.getSize().getYi());
	
		glBindTexture(GL_TEXTURE_2D, 0);
		
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);

		glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void stopRenderToFrameBuffer(){
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport (0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public void cleanUp() {//call when closing the game
    	GL30.glDeleteFramebuffers(framebufferID);
        GL11.glDeleteTextures(texture.getId());
        GL30.glDeleteRenderbuffers(depthRenderBufferID);
    }

	public Texture2D getTexture() {
		return texture;
	}
}