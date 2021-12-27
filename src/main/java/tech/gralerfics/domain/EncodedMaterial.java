package tech.gralerfics.domain;

import tech.gralerfics.utils.maths.Vector3f;

public class EncodedMaterial {
    public Vector3f emissive;
    public Vector3f plainColor;
    public Vector3f param0, param1, param2, param3;

    public static final int SIZE = 18;

    public EncodedMaterial() {}
    public EncodedMaterial(Vector3f emissive, Vector3f plainColor, Vector3f param0, Vector3f param1, Vector3f param2, Vector3f param3) {
        this.emissive = emissive;
        this.plainColor = plainColor;
        this.param0 = param0;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }
}
