package tech.gralerfics.utils.maths;

// ÐÐÖ÷Ðò´æ´¢
public class Matrix4f {
    private float[][] data = new float[4][4];

    public Matrix4f() {}
    public Matrix4f(float[][] m) {
        this.data = m;
    }
    public Matrix4f(Vector3f v) {
        this.data[0][0] = v.x;
        this.data[1][0] = v.y;
        this.data[2][0] = v.z;
        this.data[3][0] = 1.0f;
    }

    public float[][] getData() {
        return this.data;
    }

    public float get(int r, int c) {
        return this.data[r - 1][c - 1];
    }

    public void set(int r, int c, float value) {
        this.data[r - 1][c - 1] = value;
    }

    public static Matrix4f identity() {
        Matrix4f rst = new Matrix4f();
        for (int i = 1; i <= 4; i ++) {
            rst.set(i, i, 1.0f);
        }
        return rst;
    }

    public static Matrix4f multiply(Matrix4f L, Matrix4f R) {
        Matrix4f rst = new Matrix4f();
        for (int i = 1; i <= 4; i ++) {
            for (int j = 1; j <= 4; j ++) {
                float sum = 0.0f;
                for (int k = 1; k <= 4; k ++) {
                    sum += L.get(i, k) * R.get(k, j);
                    rst.set(i, j, sum);
                }
            }
        }
        return rst;
    }
}
