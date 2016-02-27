#version 130

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 pass_position;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(){
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position,1);
	pass_position = (projectionMatrix * transformationMatrix * vec4(position,1)).xyz;
	pass_textureCoords = textureCoords;
}