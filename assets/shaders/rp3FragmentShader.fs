#version 430 core

in vec3 pos;
out vec4 fragColor;

uniform sampler2D tp0, tp1, tp2, tp3, tp4, tp5, tp6;

vec3 mapping(in vec3 c, float li) {
    float lu = 0.299 * c.x + 0.587 * c.y + 0.114 * c.z;
    return c * 1.0 / max(0.001, 1.0 + lu / li);
}

void main() {
    vec3 color = texture2D(tp0, pos.xy * 0.5 + 0.5).rgb;
    color = pow(mapping(color, 1.5), vec3(1.0 / 2.2));
    fragColor = vec4(color, 1.0);
}
