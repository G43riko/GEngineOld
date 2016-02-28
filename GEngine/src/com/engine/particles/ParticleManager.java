package com.engine.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ggllib.core.Camera;
import ggllib.render.model.BorderedModel;
import ggllib.utils.ContentManager;

public class ParticleManager {
	public static BorderedModel PARTICLE_MODEL;
	private Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();

	//CONSTRUCTORS
	
	public ParticleManager(ContentManager manager){
		if(PARTICLE_MODEL == null)
			PARTICLE_MODEL = manager.loadParticlesQuad();
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
}
