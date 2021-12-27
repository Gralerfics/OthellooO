package tech.gralerfics.managers.resource;

import tech.gralerfics.controller.Main;
import tech.gralerfics.controller.wind.TipDialog;
import tech.gralerfics.domain.EncodedBVHNode;
import tech.gralerfics.domain.EncodedTriangle;
import tech.gralerfics.domain.Material;
import tech.gralerfics.domain.Triangle;
import tech.gralerfics.utils.maths.Matrix4f;
import tech.gralerfics.utils.maths.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ResourceManager {
    private static final String PATH = "";
    private static final float INF = 1e9f;

    /**
     * 简化版读取 .obj 文件, 默认格式非常规范
     */
    public static ArrayList<Triangle> loadObjAsTriangles(String path, Material material, Matrix4f translation, boolean smooth, boolean identify) {
        ArrayList<Triangle> rst = new ArrayList<>();

        ArrayList<Vector3f> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        float maxX = -INF, maxY = -INF, maxZ = -INF;
        float minX = INF, minY = INF, minZ = INF;

//        InputStream objInputStream = RenderManager.class.getClassLoader().getResourceAsStream(PATH + path);
//        System.out.println(objInputStream);
//        if (objInputStream == null) throw new IOException("读取 .obj 文件失败.");

        BufferedReader fin = null;
        String prs = "";

        try {
            fin = new BufferedReader(new FileReader("assets\\" + path));
            int peek;
            StringBuilder sin = new StringBuilder();
            while ((peek = fin.read()) != -1) sin.append((char) peek);
            fin.close();
            prs = sin.toString();
            if (prs.isEmpty()) throw new IOException("读取 .obj 文件失败.");
        } catch (IOException e) {
            (new TipDialog(Main.mainFrame)).show("OBJ Model missing! OthellooO is closing...");
            System.exit(0);
        }

        Scanner sc = new Scanner(prs);
        while (sc.hasNext()) {
            String h = sc.next();
            float x = sc.nextFloat(), y = sc.nextFloat(), z = sc.nextFloat();
            if (h.contains("v")) {
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
                maxZ = Math.max(maxZ, z);
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                minZ = Math.min(minZ, z);
            }
            sc.nextLine();

            if (h.contains("f")) {
                indices.add((int) x - 1);
                indices.add((int) y - 1);
                indices.add((int) z - 1);
            } else {
                vertices.add(new Vector3f(x, y, z));
            }
        }

        float lX = maxX - minX;
        float lY = maxY - minY;
        float lZ = maxZ - minZ;
        float l = Math.max(lX, Math.max(lY, lZ));
        for (Vector3f v : vertices) {
            if (identify) {
                v.x /= l;
                v.y /= l;
                v.z /= l;
            }
            Vector3f u = new Vector3f(Matrix4f.multiply(translation, new Matrix4f(v)));
            v.x = u.x;
            v.y = u.y;
            v.z = u.z;
        }

        Vector3f[] normals = new Vector3f[vertices.size()];
        for (int i = 0; i < vertices.size(); i ++) normals[i] = new Vector3f(0.0f, 0.0f, 0.0f);
        if (smooth) {
            for (int i = 0; i < indices.size(); i += 3) {
                Vector3f p1 = vertices.get(indices.get(i));
                Vector3f p2 = vertices.get(indices.get(i + 1));
                Vector3f p3 = vertices.get(indices.get(i + 2));
                Vector3f n = Vector3f.cross(Vector3f.substract(p2, p1), Vector3f.substract(p3, p1)).normalize();
                normals[indices.get(i)] = Vector3f.add(normals[indices.get(i)], n);
                normals[indices.get(i + 1)] = Vector3f.add(normals[indices.get(i + 1)], n);
                normals[indices.get(i + 2)] = Vector3f.add(normals[indices.get(i + 2)], n);
            }
        }

        for (int i = 0; i < indices.size(); i += 3) {
            Triangle tri = new Triangle();
            tri.p1 = vertices.get(indices.get(i));
            tri.p2 = vertices.get(indices.get(i + 1));
            tri.p3 = vertices.get(indices.get(i + 2));
            if (!smooth) {
                Vector3f n = Vector3f.cross(Vector3f.substract(tri.p2, tri.p1), Vector3f.substract(tri.p3, tri.p1)).normalize();
                tri.n1 = tri.n2 = tri.n3 = n;
            } else {
                tri.n1 = normals[indices.get(i)].normalize();
                tri.n2 = normals[indices.get(i + 1)].normalize();
                tri.n3 = normals[indices.get(i + 2)].normalize();
            }
            tri.material = material;
            rst.add(tri);
        }

        return rst;
    }

    private static int lower_bound(float[] v, float u) {
//        for (int i = 0; i < v.length; i ++) {
//            if (v[i] >= u) {
//                return i;
//            }
//        }
//        return v.length - 1;
        int l = 0, r = v.length - 1;
        while (l < r){
            int m = (l + r) / 2;
            if (v[m] >= u) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        return l;
    }

    /**
     * HDR 采样缓存计算
     */
    public static float[] calculateHdrCache(float[] hdr, int width, int height) {
        float lumSum = 0.0f;

        float[][] pdf = new float[height][width];
        for (int i = 0; i < height; i ++) {
            for (int j = 0; j < width; j ++) {
                float r = hdr[(i * width + j) * 3];
                float g = hdr[(i * width + j) * 3 + 1];
                float b = hdr[(i * width + j) * 3 + 2];
//                float lum = 0.299f * r + 0.587f * g + 0.114f * b;
                float lum = 0.2f * r + 0.7f * g + 0.1f * b;
                pdf[i][j] = lum;
                lumSum += lum;
            }
        }

        float fac = 1.0f / lumSum;
        for (int i = 0; i < height; i ++) {
            for (int j = 0; j < width; j ++) {
                pdf[i][j] *= fac;
            }
        }

        float[] pdf_x = new float[width];
        for (int j = 0; j < width; j ++) {
            for (int i = 0; i < height; i ++) {
                pdf_x[j] += pdf[i][j];
            }
        }

        float[] cdf_x = new float[width];
        cdf_x[0] = pdf_x[0];
        for (int i = 1; i < width; i ++) {
            cdf_x[i] = cdf_x[i - 1] + pdf_x[i];
        }

        float[][] pdf_y = new float[height][width];
        for (int j = 0; j < width; j ++) {
            for (int i = 0; i < height; i ++) {
                pdf_y[i][j] = pdf[i][j] / pdf_x[j];
            }
        }

        float[][] cdf_y_t = new float[height][width];
        for (int j = 0; j < width; j ++) {
            cdf_y_t[0][j] = pdf_y[0][j];
            for (int i = 1; i < height; i ++) {
                cdf_y_t[i][j] = cdf_y_t[i - 1][j] + pdf_y[i][j];
            }
        }

        float[][] cdf_y = new float[width][height];
        for (int j = 0; j < width; j ++) {
            for (int i = 0; i < height; i ++) {
                cdf_y[j][i] = cdf_y_t[i][j];
            }
        }

        float[][] sample_x = new float[height][width];
        float[][] sample_y = new float[height][width];
        float[][] sample_p = new float[height][width];
        for (int j = 0; j < width; j ++) {
            for (int i = 0; i < height; i ++) {
                float x1 = 1.0f * i / height;
                float x2 = 1.0f * j / width;

                int x = lower_bound(cdf_x, x1);
                int y = lower_bound(cdf_y[x], x2);

                sample_x[i][j] = 1.0f * x / width;
                sample_y[i][j] = 1.0f * y / height;
                sample_p[i][j] = pdf[i][j];
            }
        }

        float[] rst = new float[width * height * 3];
        for (int i = 0; i < height; i ++) {
            for (int j = 0; j < width; j ++) {
                rst[(i * width + j) * 3] = sample_x[i][j];
                rst[(i * width + j) * 3 + 1] = sample_y[i][j];
                rst[(i * width + j) * 3 + 2] = sample_p[i][j];
            }
        }

        return rst;
    }

    /**
     * 节点类编码转数组编码
     */
    public static float[] encodedBVHNodesToFloats(ArrayList<EncodedBVHNode> bn) {
        float[] rst = new float[bn.size() * EncodedBVHNode.SIZE];
        int cnt = 0;
        for (EncodedBVHNode n : bn) {
            rst[cnt ++] = n.children.x;
            rst[cnt ++] = n.children.y;
            rst[cnt ++] = n.children.z;
            rst[cnt ++] = n.leafInf.x;
            rst[cnt ++] = n.leafInf.y;
            rst[cnt ++] = n.leafInf.z;
            rst[cnt ++] = n.AA.x;
            rst[cnt ++] = n.AA.y;
            rst[cnt ++] = n.AA.z;
            rst[cnt ++] = n.BB.x;
            rst[cnt ++] = n.BB.y;
            rst[cnt ++] = n.BB.z;
        }
        return rst;
    }

    /**
     * 三角类编码转数组编码
     */
    public static float[] encodedTrianglesToFloats(ArrayList<EncodedTriangle> ts) {
        float[] rst = new float[ts.size() * EncodedTriangle.SIZE];
        int cnt = 0;
        for (EncodedTriangle t : ts) {
            rst[cnt ++] = t.p1.x;
            rst[cnt ++] = t.p1.y;
            rst[cnt ++] = t.p1.z;
            rst[cnt ++] = t.p2.x;
            rst[cnt ++] = t.p2.y;
            rst[cnt ++] = t.p2.z;
            rst[cnt ++] = t.p3.x;
            rst[cnt ++] = t.p3.y;
            rst[cnt ++] = t.p3.z;
            rst[cnt ++] = t.n1.x;
            rst[cnt ++] = t.n1.y;
            rst[cnt ++] = t.n1.z;
            rst[cnt ++] = t.n2.x;
            rst[cnt ++] = t.n2.y;
            rst[cnt ++] = t.n2.z;
            rst[cnt ++] = t.n3.x;
            rst[cnt ++] = t.n3.y;
            rst[cnt ++] = t.n3.z;
            rst[cnt ++] = t.material.emissive.x;
            rst[cnt ++] = t.material.emissive.y;
            rst[cnt ++] = t.material.emissive.z;
            rst[cnt ++] = t.material.plainColor.x;
            rst[cnt ++] = t.material.plainColor.y;
            rst[cnt ++] = t.material.plainColor.z;
            rst[cnt ++] = t.material.param0.x;
            rst[cnt ++] = t.material.param0.y;
            rst[cnt ++] = t.material.param0.z;
            rst[cnt ++] = t.material.param1.x;
            rst[cnt ++] = t.material.param1.y;
            rst[cnt ++] = t.material.param1.z;
            rst[cnt ++] = t.material.param2.x;
            rst[cnt ++] = t.material.param2.y;
            rst[cnt ++] = t.material.param2.z;
            rst[cnt ++] = t.material.param3.x;
            rst[cnt ++] = t.material.param3.y;
            rst[cnt ++] = t.material.param3.z;
        }
        return rst;
    }

    /**
     * 二维点编码转数组编码
     */
    public static float[] encoded2DPointsToFloats(ArrayList<Vector3f> tp) {
        float[] rst = new float[tp.size() * 2];
        int cnt = 0;
        for (Vector3f v : tp) {
            rst[cnt ++] = v.x;
            rst[cnt ++] = v.y;
        }
        return rst;
    }
}
