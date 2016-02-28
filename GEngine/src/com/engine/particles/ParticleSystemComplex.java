//package com.engine.particles;
//
//import java.util.Random;
//
//import org.lwjgl.util.vector.Matrix4f;
//import org.lwjgl.util.vector.Vector3f;
//import org.lwjgl.util.vector.Vector4f;
//
//import glib.util.vector.GVector3f;
//import glib.util.vector.GVector4f;
//
//public class ParticleSystemComplex {
//	 
//    private float pps, averageSpeed, gravityComplient, averageLifeLength, averageScale;
// 
//    private float speedError, lifeError, scaleError = 0;
//    private boolean randomRotation = false;
//    private GVector3f direction;
//    private float directionDeviation = 0;
// 
//    private Random random = new Random();
// 
//    public ParticleSystemComplex(float pps, float speed, float gravityComplient, float lifeLength, float scale) {
//        this.pps = pps;
//        this.averageSpeed = speed;
//        this.gravityComplient = gravityComplient;
//        this.averageLifeLength = lifeLength;
//        this.averageScale = scale;
//    }
// 
//    /**
//     * @param direction - The average direction in which particles are emitted.
//     * @param deviation - A value between 0 and 1 indicating how far from the chosen direction particles can deviate.
//     */
//    public void setDirection(GVector3f direction, float deviation) {
//        this.direction = new GVector3f(direction);
//        this.directionDeviation = (float) (deviation * Math.PI);
//    }
// 
//    public void randomizeRotation() {
//        randomRotation = true;
//    }
// 
//    /**
//     * @param error
//     *            - A number between 0 and 1, where 0 means no error margin.
//     */
//    public void setSpeedError(float error) {
//        this.speedError = error * averageSpeed;
//    }
// 
//    /**
//     * @param error
//     *            - A number between 0 and 1, where 0 means no error margin.
//     */
//    public void setLifeError(float error) {
//        this.lifeError = error * averageLifeLength;
//    }
// 
//    /**
//     * @param error
//     *            - A number between 0 and 1, where 0 means no error margin.
//     */
//    public void setScaleError(float error) {
//        this.scaleError = error * averageScale;
//    }
// 
//    public void generateParticles(GVector3f systemCenter, float delta) {
//        float particlesToCreate = pps * delta;
//        int count = (int) Math.floor(particlesToCreate);
//        float partialParticle = particlesToCreate % 1;
//        for (int i = 0; i < count; i++) 
//            emitParticle(systemCenter);
//        
//        if (Math.random() < partialParticle) 
//            emitParticle(systemCenter);
//        
//    }
// 
//    private void emitParticle(GVector3f center) {
//        GVector3f velocity = null;
//        if(direction!=null){
//            velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
//        }else{
//            velocity = generateRandomUnitVector();
//        }
//        velocity.Normalized();
//        velocity.mul(generateValue(averageSpeed, speedError));
//        float scale = generateValue(averageScale, scaleError);
//        float lifeLength = generateValue(averageLifeLength, lifeError);
//        new Particle(new GVector3f(center), velocity, gravityComplient, lifeLength, generateRotation(), scale);
//    }
// 
//    private float generateValue(float average, float errorMargin) {
//        float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
//        return average + offset;
//    }
// 
//    private float generateRotation() {
//        if (randomRotation) {
//            return random.nextFloat() * 360f;
//        } else {
//            return 0;
//        }
//    }
// 
//    private static GVector3f generateRandomUnitVectorWithinCone(GVector3f coneDirection, float angle) {
//        float cosAngle = (float) Math.cos(angle);
//        Random random = new Random();
//        float theta = (float) (random.nextFloat() * 2f * Math.PI);
//        float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
//        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
//        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
//        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
// 
//        Vector4f direction = new Vector4f(x, y, z, 1);
//        if (coneDirection.getX() != 0 || coneDirection.getY() != 0 || (coneDirection.getZ() != 1 && coneDirection.getZ() != -1)) {
//            GVector3f rotateAxis = coneDirection.cross(new GVector3f(0, 0, 1));
//            rotateAxis.Normalized();
//            float rotateAngle = (float) Math.acos(coneDirection.dot(new GVector3f(0, 0, 1)));
//            Matrix4f rotationMatrix = new Matrix4f();
//            rotationMatrix.rotate(-rotateAngle, new Vector3f(rotateAxis.getX(), rotateAxis.getY(), rotateAxis.getZ()));
//            direction = Matrix4f.transform(rotationMatrix, direction, direction);
//        } else if (coneDirection.getZ() == -1) 
//            direction.z *= -1;
//        
//        return new GVector3f(direction.x, direction.y, direction.z);
//    }
//     
//    private GVector3f generateRandomUnitVector() {
//        float theta = (float) (random.nextFloat() * 2f * Math.PI);
//        float z = (random.nextFloat() * 2) - 1;
//        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
//        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
//        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
//        return new GVector3f(x, y, z);
//    }
// 
//}
