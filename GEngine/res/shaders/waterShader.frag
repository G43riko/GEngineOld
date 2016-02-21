#version 400 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;

const float waveStrength = 0.02;

uniform float moveFactor;

void main(void) {

	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2 + 0.5f;
	
	vec2 refractTextCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTextCoords = vec2(ndc.x, -ndc.y);
	
	vec2 distortion1 = (texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2.0 - 1) * waveStrength;
	vec2 distortion2 = (texture(dudvMap, vec2(-textureCoords.x + moveFactor, textureCoords.y+ moveFactor)).rg * 2.0 - 1) * waveStrength;
	
	vec2 distortion = distortion1 + distortion2;
	
	reflectTextCoords += distortion;
	//reflectTextCoords = clamp(reflectTextCoords, 0.001, 0.009);
	
	refractTextCoords += distortion;
	
	vec4 reflectColor = texture(reflectionTexture, reflectTextCoords);
	vec4 refractColor = texture(refractionTexture, refractTextCoords);
	
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, vec3(0, 1, 0));
	
	
	out_Color = mix(reflectColor, refractColor, refractiveFactor);
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2);
}
