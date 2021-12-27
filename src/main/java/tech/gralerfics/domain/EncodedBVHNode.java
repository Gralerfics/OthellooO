package tech.gralerfics.domain;

import tech.gralerfics.utils.maths.Vector3f;

public class EncodedBVHNode {
    public Vector3f children; // left, right, null
    public Vector3f leafInf; // n, index, null
    public Vector3f AA, BB;

    public static final int SIZE = 12;

    public EncodedBVHNode() {}
    public EncodedBVHNode(Vector3f children, Vector3f leafInf, Vector3f AA, Vector3f BB) {
        this.children = children;
        this.leafInf = leafInf;
        this.AA = AA;
        this.BB = BB;
    }
}
