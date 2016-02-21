#version 400 core

const int maxLights = 8;

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVector[maxLights];

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition[maxLights];

const float tiling = 6.0;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);

	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	textureCoords = vec2(position.x / 2 + 0.5, position.y / 2 + 0.5) * tiling;
	gl_Position = clipSpace;
	toCameraVector = cameraPosition - worldPosition.xyz;
	
	for(int i=0 ; i<maxLights ; i++)
		fromLightVector[i] = worldPosition.xyz - lightPosition[i];
 
}