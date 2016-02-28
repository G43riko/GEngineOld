#version 140

in vec2 position;

out vec2 textureCoords1;
out vec2 textureCoords2;
out float blend;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

uniform vec2 textOffset1;
uniform vec2 textOffset2;
uniform vec2 textCoords;

void main(void){
	vec2 textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y; 
	textureCoords /= textCoords.x;
	
	textureCoords1 = textureCoords + textOffset1;
	textureCoords2 = textureCoords + textOffset2;
	blend = textCoords.y;
	
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}