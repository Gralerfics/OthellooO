package tech.gralerfics.domain;

import tech.gralerfics.utils.maths.Vector3f;

public class EncodedTriangle {
    public Vector3f p1, p2, p3;
    public Vector3f n1, n2, n3;
    public EncodedMaterial material;

    public static final int SIZE = 18 + EncodedMaterial.SIZE;

    public EncodedTriangle() {}
    public EncodedTriangle(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f n1, Vector3f n2, Vector3f n3, EncodedMaterial material) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.material = material;
    }
}
