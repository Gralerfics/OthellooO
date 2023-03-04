#version 430 core

in vec3 pos;
out vec4 fragColor;

uniform sampler2D tp0, tp1, tp2, tp3, tp4, tp5, tp6;

void main() {
    gl_FragData[0] = vec4(texture2D(tp0, pos.xy * 0.5 + 0.5).rgb, 1.0);
}
