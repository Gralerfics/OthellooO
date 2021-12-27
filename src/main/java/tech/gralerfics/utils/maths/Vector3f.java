package tech.gralerfics.utils.maths;

public class Vector3f {
    public float x, y, z;

    public static final float INF = 1e9f;

    public Vector3f() {}
    public Vector3f(float a) {
        this.x = a;
        this.y = a;
        this.z = a;
    }
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3f(Matrix4f M) {
        this.x = M.get(1, 1);
        this.y = M.get(2, 1);
        this.z = M.get(3, 1);
    }
    public Vector3f(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public float getElementByIndex(int idx) {
        if (idx == 0) return this.x;
        if (idx == 1) return this.y;
        if (idx == 2) return this.z;
        return INF;
    }

    public static Vector3f add(Vector3f u, Vector3f v) {
        return new Vector3f(u.x + v.x, u.y + v.y, u.z + v.z);
    }

    public static Vector3f substract(Vector3f u, Vector3f v) {
        return new Vector3f(u.x - v.x, u.y - v.y, u.z - v.z);
    }

    public static Vector3f negative(Vector3f v) {
        return new Vector3f(- v.x, - v.y, - v.z);
    }

    public static float dot(Vector3f u, Vector3f v) {
        return u.x * v.x + u.y * v.y + u.z * v.z;
    }

    public static Vector3f cross(Vector3f u, Vector3f v) {
        return new Vector3f(
                u.y * v.z - u.z * v.y,
                u.z * v.x - u.x * v.z,
                u.x * v.y - u.y * v.x
        );
    }

    public Vector3f scaler(float k) {
        return new Vector3f(k * this.x, k * this.y, k * this.z);
    }

    public Vector3f division(float k) {
        float inv = 1.0f / k;
        return new Vector3f(inv * this.x, inv * this.y, inv * this.z);
    }

    public float length2() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float length() {
        return (float) Math.sqrt(this.length2());
    }

    public Vector3f normalize() {
        float inv = 1.0f / this.length();
        return new Vector3f(this.x * inv, this.y * inv, this.z * inv);
    }

    @Override
    public String toString() {
        return "Vector3f{" + x + ", " + y + ", " + z + '}';
    }
}
