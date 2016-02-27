#version 130

const float lowerLimit = -100.00;
const float upperLimit = 700.00;

in vec2 pass_textureCoords;
in vec3 pass_position;

out vec4 out_Color;

struct DirectionalLight{
    vec3 color;
    float intensity;
    vec3 direction;
};

uniform DirectionalLight directionalLight;
uniform sampler2D textureSampler;
uniform int view;
uniform int fog;
uniform vec3 color;
uniform vec3 ambient;

void main(){
	if(view == 0){
		out_Color = vec4(ambient,1) * texture(textureSampler, vec2(pass_textureCoords.x,pass_textureCoords.y+1));
		if(fog == 1){
			float factor = (pass_position.y - lowerLimit) / (upperLimit - lowerLimit);
			factor = clamp(factor, 0.0 , 1.0);
			out_Color = mix(vec4(ambient,1.0), out_Color, factor);
		}
	}
	else if(view == 1){
		out_Color = vec4(1-texture(textureSampler, pass_textureCoords).xyz,1);
	}
	else if(view == 2){
		vec4 text = texture(textureSampler, pass_textureCoords);
		float average = (text.x + text.y + text.z)/3; 
		out_Color = vec4(average, average, average,1);
	}
	else if(view == 3){
		if(color != vec3(0,0,0)){
			out_Color = vec4(color,1);
		}
		else
			out_Color = vec4(0.5, 0.5, 0.5, 1);
	}
}