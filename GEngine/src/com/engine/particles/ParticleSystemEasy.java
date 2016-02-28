package com.engine.particles;

import glib.util.vector.GVector3f;

public class ParticleSystemEasy {
    
    private float pps;
    private float speed;
    private float gravityComplient;
    private float lifeLength;
    private ParticleTexture texture;
     
    public ParticleSystemEasy(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength) {
        this.gravityComplient = gravityComplient;
        this.lifeLength = lifeLength;
    	this.texture = texture;
        this.speed = speed;
        this.pps = pps;
    }
     
    public void generateParticles(GVector3f systemCenter, float delta){
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for(int i=0;i<count;i++){
            emitParticle(systemCenter);
        }
        if(Math.random() < partialParticle){
            emitParticle(systemCenter);
        }
    }
     
    private void emitParticle(GVector3f center){
        float dirX = (float) Math.random() * 2f - 1f;
        float dirZ = (float) Math.random() * 2f - 1f;
        GVector3f velocity = new GVector3f(dirX, 1, dirZ);
        velocity.Normalized();
        velocity.mul(speed);
        new Particle(texture, new GVector3f(center), velocity, gravityComplient, lifeLength, 0, 1);
    }
     
     
 
}