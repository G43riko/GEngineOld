package com.engine.rendering;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.engine.water.WaterFrameBuffers;

import ggllib.core.Camera;
import ggllib.entity.Entity;
import ggllib.render.material.Material;
import ggllib.render.model.BorderedModel;
import ggllib.render.model.MaterialedModel;
import ggllib.render.shader.GBasicShader;
import ggllib.utils.Maths;
import glib.util.vector.GMatrix4f;
import glib.util.vector.GVector3f;
import glib.util.vector.GVector4f;

public abstract class GRenderingEngine {
	protected Map<MaterialedModel, List<Entity>> entities = new HashMap<MaterialedModel, List<Entity>>();
	private WaterFrameBuffers 	fbos;
	protected GMatrix4f viewMatrix; 
	private Map<String, GBasicShader> shaders = new HashMap<String, GBasicShader>();
	private Map<String, Camera> cameras = new HashMap<String, Camera>();
	private Camera actCamera  = new Camera();
	private GVector4f plane = new GVector4f(0, -1, 0, 15);
	
	//CONSTRUCTORS
	
	public GRenderingEngine(){
		if(GLContext.getCapabilities().GL_EXT_framebuffer_object)
			fbos = new WaterFrameBuffers();
		
		setProjectionMatrix(getActCamera().getProjectionMatrix());
		
		cameras.put("main", actCamera);
	}
	
	//UTILS
	
	protected GMatrix4f updateModelViewMatrix(GVector3f position, float rotation, float scale){
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(new Vector3f(position.getX(), position.getY(), position.getZ()), modelMatrix, modelMatrix);
		modelMatrix.m00 = viewMatrix.get(0, 0);
	    modelMatrix.m01 = viewMatrix.get(1, 0);
	    modelMatrix.m02 = viewMatrix.get(2, 0);
	    modelMatrix.m10 = viewMatrix.get(0, 1);
	    modelMatrix.m11 = viewMatrix.get(1, 1);
	    modelMatrix.m12 = viewMatrix.get(2, 1);
	    modelMatrix.m20 = viewMatrix.get(0, 2);
	    modelMatrix.m21 = viewMatrix.get(1, 2);
	    modelMatrix.m22 = viewMatrix.get(2, 2);
	    
	    Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
	    Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
	    
	    return Maths.MatrixToGMatrix(Matrix4f.mul(Maths.GMatrixToMatrix(viewMatrix), modelMatrix, null));
	}
	
	protected void init3D(){
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	protected void disableVertex(int num){
		for(int i=0 ; i<num ; i++)
			GL20.glDisableVertexAttribArray(i);
		
		GL30.glBindVertexArray(0);
	}
	
	//PREPARERS
	
	public void prepare(){
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		setViewMatrix(Maths.createViewMatrix(actCamera.getPosition(), actCamera.getRotation()));
		init3D();
	}
	
	protected void prepareInstance(GBasicShader shader, Entity entity) {
		shader.updateUniform("transformationMatrix", entity.getTransformationMatrix());
	}
	
	protected void prepareModel(BorderedModel model, int num){
		GL30.glBindVertexArray(model.getVaoID());
		
		for(int i=0 ; i<num ; i++)
			GL20.glEnableVertexAttribArray(i);
	}
	
	protected void prepareMaterial(GBasicShader shader, Material material) {
		shader.connectTextures();
		
		if(material.getDiffuse() != null)
			material.getDiffuse().bind(GL13.GL_TEXTURE0);

		if(material.getNormal() != null)
			material.getNormal().bind(GL13.GL_TEXTURE1);
		
		
		shader.updateUniform("specularPower", material.getSpecularPower());
		shader.updateUniform("specularIntensity", material.getSpecularIntensity());
	}
	
	//OTHERS
	
	public void cleanUp(){
		if(fbos != null)
			fbos.cleanUp();
	}
	
	//GETTERS
	
	public Camera getActCamera() {
		return actCamera;
	}
	
	protected GBasicShader getShader(String key){
		return shaders.get(key);
	}

	public GVector4f getPlane(){
		return plane;
	}
	
	protected GVector3f getEyeSpacePosition(Entity light){
		GVector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.getX(), position.getY(), position.getZ(), 1f);
        Matrix4f.transform(Maths.GMatrixToMatrix(viewMatrix), eyeSpacePos, eyeSpacePos);
        return new GVector3f(eyeSpacePos.getX(), eyeSpacePos.getY(), eyeSpacePos.getZ());
	}
	
	//SETTERS
	
	protected void setViewMatrix(GMatrix4f matrix) {
		this.viewMatrix = matrix;
		shaders.forEach((key,val) -> {
			if(val.hasUniform("viewMatrix")){
				val.bind();
				val.updateUniform("viewMatrix", matrix);
			}
		});
	}
	
	protected void setProjectionMatrix(GMatrix4f projectionMatrix) {
		shaders.forEach((key, val) -> {
			if(val.hasUniform("projectionMatrix")){
				val.bind();
				val.updateUniform("projectionMatrix", projectionMatrix);
			}
		});
	}
	
	public void setActCamera(String camera){
		actCamera = cameras.get(camera);
		setProjectionMatrix(getActCamera().getProjectionMatrix());
	}
	
	//ADDERS
	
	public void addCamera(String name, Camera camera){
		cameras.put(name, camera);
	}
	
	public void addShader(String key, GBasicShader value){
		shaders.put(key, value);
	}
}
