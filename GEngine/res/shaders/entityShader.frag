#version 140

const int maxLights = 8;

in vec2 pass_textureCoords;
in vec3 toLightVector[maxLights];
in vec3 toCameraVector;

out vec4 out_Color;

uniform vec3 lightColor[maxLights];
uniform vec3 attenuation[maxLights];
uniform vec3 color;
uniform float specularIntensity;
uniform float specularPower;
uniform sampler2D textureSampler;
uniform sampler2D normalSampler;

void main(){

	vec4 normalMapValue = 2.0 * texture(normalSampler, pass_textureCoords) - 1.0; 
	vec3 unitNormal = normalize(normalMapValue.rgb);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i=0 ; i<maxLights ; i++){
		if(lightColor[i] == vec3(0))
			break;
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + attenuation[i].y * distance + attenuation[i].z * distance * distance;
		vec3 unitLightVector = normalize(toLightVector[i]);
	
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
	
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, specularPower);
		
		totalDiffuse  += (brightness * lightColor[i]) / attFactor;
		totalSpecular += (dampedFactor * specularIntensity * lightColor[i]) / attFactor;
	}
	
	totalDiffuse = max(totalDiffuse, 0.1);
	
	
	out_Color = vec4(totalDiffuse, 1.0) * texture(textureSampler, pass_textureCoords) + vec4(totalSpecular, 1.0);
	//out_Color = vec4(toLightVector[0], 1.0);
	
	if(color != vec3(0, 0, 0))
		out_Color = vec4(color, 1);
}