package tech.gralerfics.raytracer.utils;

import tech.gralerfics.domain.BVHNode;
import tech.gralerfics.domain.HitRst;
import tech.gralerfics.domain.Ray;
import tech.gralerfics.domain.Triangle;
import tech.gralerfics.utils.maths.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BVHTreeUtils {
    private static final float INF = 1e9f;
    private static final float EPS = 1e-5f;

    /**
     * 依据提供的 triangles 和建树于 tree 中, 需要预清空 tree
     */
    public static int buildTree(ArrayList<Triangle> triangles, ArrayList<BVHNode> tree, int l, int r, int n) {
        if (l > r) {
            return -1;
        }
        tree.add(new BVHNode(
                0, 0, 0, 0,
                new Vector3f(INF, INF, INF),
                new Vector3f(-INF, -INF, -INF)
        ));
        int id = tree.size() - 1;

        // 计算包围盒
        for (int i = l; i <= r; i ++) {
            float minX = Math.min(triangles.get(i).p1.x, Math.min(triangles.get(i).p2.x, triangles.get(i).p3.x));
            float minY = Math.min(triangles.get(i).p1.y, Math.min(triangles.get(i).p2.y, triangles.get(i).p3.y));
            float minZ = Math.min(triangles.get(i).p1.z, Math.min(triangles.get(i).p2.z, triangles.get(i).p3.z));
            tree.get(id).AA.x = Math.min(tree.get(id).AA.x, minX);
            tree.get(id).AA.y = Math.min(tree.get(id).AA.y, minY);
            tree.get(id).AA.z = Math.min(tree.get(id).AA.z, minZ);
            float maxX = Math.max(triangles.get(i).p1.x, Math.max(triangles.get(i).p2.x, triangles.get(i).p3.x));
            float maxY = Math.max(triangles.get(i).p1.y, Math.max(triangles.get(i).p2.y, triangles.get(i).p3.y));
            float maxZ = Math.max(triangles.get(i).p1.z, Math.max(triangles.get(i).p2.z, triangles.get(i).p3.z));
            tree.get(id).BB.x = Math.max(tree.get(id).BB.x, maxX);
            tree.get(id).BB.y = Math.max(tree.get(id).BB.y, maxY);
            tree.get(id).BB.z = Math.max(tree.get(id).BB.z, maxZ);
        }

        // 数量足够少, 划分为叶节点
        if (r - l + 1 <= n) {
            tree.get(id).left = tree.get(id).right = -1;
            tree.get(id).n = r - l + 1;
            tree.get(id).index = l;
            return id;
        }

        float Value = INF;
        int Ax = 0, Sp = (l + r) / 2;
        for (int ax = 0; ax < 3; ax ++) {
            if (ax == 0) sortByX(triangles.subList(l, r + 1));
            if (ax == 1) sortByY(triangles.subList(l, r + 1));
            if (ax == 2) sortByZ(triangles.subList(l, r + 1));

            // 前缀最大值
            Vector3f[] lMax = new Vector3f[r - l + 1];
            Vector3f[] lMin = new Vector3f[r - l + 1];
            for (int i = l; i <= r; i ++) {
                lMax[i - l] = new Vector3f(-INF, -INF, -INF);
                lMin[i - l] = new Vector3f(INF, INF, INF);
            }
            for (int i = l; i <= r; i ++) {
                Triangle tri = triangles.get(i);
                int offset = 1;
                if (i == l) offset = 0;
                lMax[i - l].x = Math.max(lMax[i - l - offset].x, Math.max(tri.p1.x, Math.max(tri.p2.x, tri.p3.x)));
                lMax[i - l].y = Math.max(lMax[i - l - offset].y, Math.max(tri.p1.y, Math.max(tri.p2.y, tri.p3.y)));
                lMax[i - l].z = Math.max(lMax[i - l - offset].z, Math.max(tri.p1.z, Math.max(tri.p2.z, tri.p3.z)));

                lMin[i - l].x = Math.min(lMax[i - l - offset].x, Math.min(tri.p1.x, Math.min(tri.p2.x, tri.p3.x)));
                lMin[i - l].y = Math.min(lMax[i - l - offset].y, Math.min(tri.p1.y, Math.min(tri.p2.y, tri.p3.y)));
                lMin[i - l].z = Math.min(lMax[i - l - offset].z, Math.min(tri.p1.z, Math.min(tri.p2.z, tri.p3.z)));
            }

            // 后缀最大值
            Vector3f[] rMax = new Vector3f[r - l + 1];
            Vector3f[] rMin = new Vector3f[r - l + 1];
            for (int i = l; i <= r; i ++) {
                rMax[i - l] = new Vector3f(-INF, -INF, -INF);
                rMin[i - l] = new Vector3f(INF, INF, INF);
            }
            for (int i = r; i >= l; i --) {
                Triangle tri = triangles.get(i);
                int offset = 1;
                if (i == r) offset = 0;
                rMax[i - l].x = Math.max(rMax[i - l + offset].x, Math.max(tri.p1.x, Math.max(tri.p2.x, tri.p3.x)));
                rMax[i - l].y = Math.max(rMax[i - l + offset].y, Math.max(tri.p1.y, Math.max(tri.p2.y, tri.p3.y)));
                rMax[i - l].z = Math.max(rMax[i - l + offset].z, Math.max(tri.p1.z, Math.max(tri.p2.z, tri.p3.z)));

                rMin[i - l].x = Math.min(rMin[i - l + offset].x, Math.min(tri.p1.x, Math.min(tri.p2.x, tri.p3.x)));
                rMin[i - l].y = Math.min(rMin[i - l + offset].y, Math.min(tri.p1.y, Math.min(tri.p2.y, tri.p3.y)));
                rMin[i - l].z = Math.min(rMin[i - l + offset].z, Math.min(tri.p1.z, Math.min(tri.p2.z, tri.p3.z)));
            }

            // 遍历分割
            float value = INF;
            int sp = l;
            for (int i = l; i < r; i ++) {
                float lX, lY, lZ;

                // [l, i]
                Vector3f lAA = lMin[i - l];
                Vector3f lBB = lMax[i - l];
                lX = lBB.x - lAA.x;
                lY = lBB.y - lAA.y;
                lZ = lBB.z - lAA.z;
                float lV = 2.0f * (lX * lY + lX * lZ + lY * lZ) * (i - l + 1);

                // [i + 1, r]
                Vector3f rAA = rMin[i + 1 - l];
                Vector3f rBB = rMax[i + 1 - l];
                lX = rBB.x - rAA.x;
                lY = rBB.y - rAA.y;
                lZ = rBB.z - rAA.z;
                float rV = 2.0f * (lX * lY + lX * lZ + lY * lZ) * (r - i);

                if (lV + rV < value) {
                    value = lV + rV;
                    sp = i;
                }
            }
            if (value < Value) {
                Value = value;
                Ax = ax;
                Sp = sp;
            }
        }

        // 按最佳轴排序
        if (Ax == 0) sortByX(triangles.subList(l, r + 1));
        if (Ax == 1) sortByY(triangles.subList(l, r + 1));
        if (Ax == 2) sortByZ(triangles.subList(l, r + 1));

        // 递归建树
        tree.get(id).left = buildTree(triangles, tree, l, Sp, n);
        tree.get(id).right = buildTree(triangles, tree, Sp + 1, r, n);

        return id;
    }

    /**
     * 光线与三角形求交 - 光标选取用
     */
    public static HitRst hitTriangle(Ray ray, Triangle tri) {
        HitRst rst = new HitRst();
        rst.t = INF;
        rst.isHit = false;

        Vector3f normal = Vector3f.cross(
                Vector3f.substract(tri.p2, tri.p1),
                Vector3f.substract(tri.p3, tri.p1)
        ).normalize();
        if (Math.abs(Vector3f.dot(normal, ray.direction)) < EPS) {
            return rst;
        }

        float DDN = Vector3f.dot(ray.direction, normal);
        if (DDN == 0.0f) DDN = 0.001f;
        float t = (Vector3f.dot(normal, tri.p1) - Vector3f.dot(ray.origin, normal)) / DDN;
        if (t < EPS) return rst;
        Vector3f I = Vector3f.add(ray.origin, ray.direction.scaler(t));

        Vector3f d1 = Vector3f.cross(Vector3f.substract(tri.p2, tri.p1), Vector3f.substract(I, tri.p1));
        Vector3f d2 = Vector3f.cross(Vector3f.substract(tri.p3, tri.p2), Vector3f.substract(I, tri.p2));
        Vector3f d3 = Vector3f.cross(Vector3f.substract(tri.p1, tri.p3), Vector3f.substract(I, tri.p3));
        if (Vector3f.dot(d1, normal) < 0 && Vector3f.dot(d2, normal) < 0 && Vector3f.dot(d3, normal) < 0
         || Vector3f.dot(d1, normal) > 0 && Vector3f.dot(d2, normal) > 0 && Vector3f.dot(d3, normal) > 0) {
            rst.isHit = true;
            rst.t = t;
            rst.material = tri.material;
        }
        return rst;
    }

    /**
     * 光线与三角形数组区间求交 - 光标选取用
     */
    public static HitRst hitInterval(Ray ray, int l, int r, ArrayList<Triangle> list) {
        HitRst rst = new HitRst();
        rst.isHit = false;
        rst. t = INF;
        for (int i = l; i <= r; i ++) {
            HitRst rs = hitTriangle(ray, list.get(i));
            if (rs.isHit && rs.t < rst.t) {
                rst = rs;
            }
        }
        return rst;
    }

    /**
     * 光线与 bvhTree 求交 - 光标选取用 - 未编写
     */
    public static HitRst hitBVHTree() {
        HitRst rst = new HitRst();
        rst.isHit = false;
        rst.t = INF;

        return rst;
    }

    /**
     * 按 x 排序三角形列表 - 建树用
     */
    private static void sortByX(List<Triangle> list) {
        list.sort(new Comparator<Triangle>() {
            @Override
            public int compare(Triangle o1, Triangle o2) {
                float x = o1.p1.x + o1.p2.x + o1.p3.x;
                float y = o2.p1.x + o2.p2.x + o2.p3.x;
                return Float.compare(x, y);
            }
        });
    }

    /**
     * 按 y 排序三角形列表 - 建树用
     */
    private static void sortByY(List<Triangle> list) {
        list.sort(new Comparator<Triangle>() {
            @Override
            public int compare(Triangle o1, Triangle o2) {
                float x = o1.p1.y + o1.p2.y + o1.p3.y;
                float y = o2.p1.y + o2.p2.y + o2.p3.y;
                return Float.compare(x, y);
            }
        });
    }

    /**
     * 按 z 排序三角形列表 - 建树用
     */
    private static void sortByZ(List<Triangle> list) {
        list.sort(new Comparator<Triangle>() {
            @Override
            public int compare(Triangle o1, Triangle o2) {
                float x = o1.p1.z + o1.p2.z + o1.p3.z;
                float y = o2.p1.z + o2.p2.z + o2.p3.z;
                return Float.compare(x, y);
            }
        });
    }
}
