package com.engine.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ggllib.render.model.BorderedModel;
import ggllib.utils.ContentManager;

public class ParticleManager {
	public static BorderedModel PARTICLE_MODEL;
	private List<Particle> particles = new ArrayList<Particle>();

	//CONSTRUCTORS
	
	public ParticleManager(ContentManager manager){
		if(PARTICLE_MODEL == null)
			PARTICLE_MODEL = manager.loadParticlesQuad();
	}
	
	//OTHERS
	
	public void update(float delta){
		Iterator<Particle> iterator = particles.iterator();
		
		while(iterator.hasNext()){
			Particle p = iterator.next();
			if(!p.update(delta))
				iterator.remove();
		}
	}
	
	public void add(Particle particle){
		particles.add(particle);
	}

	//GETTERS
	
	public List<Particle> getParticles(){
		return particles;
	}
}
