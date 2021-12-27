package tech.gralerfics.domain;

import tech.gralerfics.utils.maths.Vector3f;

public class Camera {
    public Vector3f eye;
    public Vector3f lookAt;
    public Vector3f up;
    public Vector3f u, v, w;
    public float upAngle = 0.8f;
    public float rotateAngle = 0.0f;
    public float r = 5.0f;

    // 计算相机系基底
    public void computeUVW() {
        this.w = Vector3f.substract(this.lookAt, this.eye).normalize();
        this.u = Vector3f.cross(this.w, this.up).normalize();
        this.v = Vector3f.cross(this.w, this.u);
    }
}
