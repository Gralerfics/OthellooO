package tech.gralerfics.utils.maths;

public class MathUtils {
    /**
     * 生成投影变换矩阵
     */
    public static Matrix4f transformMatrix(Vector3f scaler, Vector3f rotation, Vector3f translation) {
        Matrix4f rst = new Matrix4f();
        // 0、1 单位矩阵、缩放
        for (int i = 1; i <= 3; i ++) rst.set(i, i, scaler.getElementByIndex(i - 1));
        rst.set(4, 4, 1.0f);
        // 2 旋转
        Matrix4f rot;
        rot = Matrix4f.identity();
        rot.set(2, 2, (float) Math.cos(rotation.x));
        rot.set(3, 3, (float) Math.cos(rotation.x));
        rot.set(3, 2, (float) Math.sin(rotation.x));
        rot.set(2, 3, (float) - Math.sin(rotation.x));
        rst = Matrix4f.multiply(rot, rst);
        rot = Matrix4f.identity();
        rot.set(1, 1, (float) Math.cos(rotation.y));
        rot.set(3, 3, (float) Math.cos(rotation.y));
        rot.set(3, 1, (float) Math.sin(rotation.y));
        rot.set(1, 3, (float) - Math.sin(rotation.y));;
        rst = Matrix4f.multiply(rot, rst);
        rot = Matrix4f.identity();
        rot.set(1, 1, (float) Math.cos(rotation.z));
        rot.set(2, 2, (float) Math.cos(rotation.z));
        rot.set(2, 1, (float) Math.sin(rotation.z));
        rot.set(1, 2, (float) - Math.sin(rotation.z));;
        rst = Matrix4f.multiply(rot, rst);
        // 3 平移
        Matrix4f tr = Matrix4f.identity();
        for (int i = 1; i <= 3; i ++) tr.set(i, 4, translation.getElementByIndex(i - 1));
        rst = Matrix4f.multiply(tr, rst);
        // 返回
        return rst;
    }
}
