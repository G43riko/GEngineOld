#version 130



in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 plane;

uniform int fakeLight;

void main(){
	vec4 worldPosition = transformationMatrix * vec4(position,1);
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	
	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	gl_Position = projectionMatrix * positionRelativeToCamera;
	
	pass_textureCoords = textureCoords;
}