#version 400 core

const int maxLights = 8;

const float waveStrength = 0.02;
const float specularPower = 20;
const float specularIntensity = 0.6;

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector[maxLights];

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;

uniform float moveFactor;
uniform vec3 lightColor[maxLights];

void main(void) {
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2 + 0.5f;
	vec2 refractTextCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTextCoords = vec2(ndc.x, -ndc.y);
	
	
	float near = 0.1;
	float far = 1000.0;
	float depth = texture(depthMap, refractTextCoords).r;
	float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
	
	depth = gl_FragCoord.z;
	float waterDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
	
	float waterDepth = floorDistance - waterDistance;
	
	vec2 distortedTextCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 0.1;
	distortedTextCoords = textureCoords + vec2(distortedTextCoords.x, distortedTextCoords.y + moveFactor);
	vec2 distortion = (texture(dudvMap, distortedTextCoords).rg * 2.0 - 1.0) * waveStrength * clamp(waterDepth / 20, 0.0, 1.0);
	
	
	reflectTextCoords += distortion;
	refractTextCoords += distortion;
	
	vec4 reflectColor = texture(reflectionTexture, reflectTextCoords);
	vec4 refractColor = texture(refractionTexture, refractTextCoords);
	
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, vec3(0, 1, 0));
	
	vec4 normalMapColor = texture(normalMap, distortedTextCoords);
	vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b, normalMapColor.g * 2.0 - 1.0);
	normal = normalize(normal);
	
	vec3 specularHighlights = vec3(0.0);
	
	for(int i=0 ; i<maxLights ; i++){
		if(lightColor[i] == vec3(0))
			break;
		vec3 reflectedLight = reflect(normalize(fromLightVector[i]), normal);
		float specular = max(dot(reflectedLight, viewVector), 0.0);
		specular = pow(specular, specularPower);
		specularHighlights += lightColor[i] * specular * specularIntensity;
	}
	
	specularHighlights *= clamp(waterDepth / 10, 0.0, 1.0);
	
	out_Color = mix(reflectColor, refractColor, refractiveFactor);
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.1) + vec4(specularHighlights, 0.0);
	out_Color.a = clamp(waterDepth / 5, 0.0, 1.0);
}
