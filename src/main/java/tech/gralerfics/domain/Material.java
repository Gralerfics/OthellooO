package tech.gralerfics.domain;

import tech.gralerfics.utils.maths.Vector3f;

public class Material {
    public Vector3f emissive = new Vector3f(0.0f, 0.0f, 0.0f);
    public Vector3f plainColor = new Vector3f(1.0f, 1.0f, 1.0f);
    public float subsurface;
    public float metallic;
    public float specular, specularTint;
    public float roughness;
    public float anisotropic;
    public float sheen, sheenTint;
    public float clearcoat, clearcoatGloss;
    public float IOR = 1.0f;
//    public float transmission;
    public int tag;

    public Material() {}

    public Material(Material mat) {
        this.emissive = mat.emissive;
        this.plainColor = mat.plainColor;
        this.subsurface = mat.subsurface;
        this.metallic = mat.metallic;
        this.specular = mat.specular;
        this.specularTint = mat.specularTint;
        this.roughness = mat.roughness;
        this.anisotropic = mat.anisotropic;
        this.sheen = mat.sheen;
        this.sheenTint = mat.sheenTint;
        this.clearcoat = mat.clearcoat;
        this.clearcoatGloss = mat.clearcoatGloss;
        this.IOR = mat.IOR;
//        this.transmission = mat.transmission;
        this.tag = mat.tag;
    }
}
