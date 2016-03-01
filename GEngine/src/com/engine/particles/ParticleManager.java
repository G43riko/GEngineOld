package com.engine.particles;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;

import ggllib.core.Camera;
import ggllib.render.model.BorderedModel;
import ggllib.utils.ContentManager;
import ggllib.utils.Loader;

public class ParticleManager {
	public static BorderedModel PARTICLE_MODEL;
	private final static int MAX_INSTANCES = 10000;
	public final static int INSTANCE_DATA_LENGTH = 21;
	private final static FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	
	private Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
//	private Loader loader;
	private int vbo;
	private int pointer;
	
	//CONSTRUCTORS
	
	public ParticleManager(ContentManager manager){
		if(PARTICLE_MODEL == null)
			PARTICLE_MODEL = manager.loadParticlesQuad();
		Loader loader = manager.getLoader();
		this.vbo = loader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		loader.addInstanceAttribude(PARTICLE_MODEL.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
		loader.addInstanceAttribude(PARTICLE_MODEL.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
		loader.addInstanceAttribude(PARTICLE_MODEL.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
		loader.addInstanceAttribude(PARTICLE_MODEL.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
		loader.addInstanceAttribude(PARTICLE_MODEL.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
		loader.addInstanceAttribude(PARTICLE_MODEL.getVaoID(), vbo, 6, 1, INSTANCE_DATA_LENGTH, 20);
	}
	
	//OTHERS
	
	public void update(float delta, Camera camera){
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		
		while(mapIterator.hasNext()){
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iterator = list.iterator();
			while(iterator.hasNext()){
				Particle p = iterator.next();
				if(!p.update(delta, camera)){
					iterator.remove();
					if(list.isEmpty())
						mapIterator.remove();
				}
			}
			InsertionSort.sortHighToLow(list);
		}
		
	}
	
	public void add(Particle particle){
		List<Particle> list = particles.get(particle.getTexture());
		if(list == null){
			particles.put(particle.getTexture(), list = new ArrayList<Particle>());
		}
		list.add(particle);
	}

	//GETTERS
	
	public Map<ParticleTexture, List<Particle>> getParticles(){
		return particles;
	}

	public static FloatBuffer getBuffer(){
		return buffer;
	}

	public int getVbo(){
		return vbo;
	}
}
