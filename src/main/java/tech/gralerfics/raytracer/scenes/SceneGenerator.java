package tech.gralerfics.raytracer.scenes;

import tech.gralerfics.domain.Material;
import tech.gralerfics.domain.Triangle;
import tech.gralerfics.managers.resource.ResourceManager;
import tech.gralerfics.persistence.options.OptionsManager;
import tech.gralerfics.utils.maths.MathUtils;
import tech.gralerfics.utils.maths.Vector3f;

import java.util.ArrayList;
import java.util.Random;

public final class SceneGenerator {
    /**
     * 添加一个占位方块（防止场景崩溃）
     */
    public static void addNull(ArrayList<Triangle> triangles) {
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                new Material(),
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(1000.0f,  1000.0f, 1000.0f)
                ),
                false,
                false
        ));
    }

    /**
     * 添加棋盘格
     */
    public static void addPlane(ArrayList<Triangle> triangles) {
        Material bbMat = new Material(), wbMat = new Material();
        bbMat.plainColor = new Vector3f(0.73f, 0.56f, 0.29f);
        wbMat.plainColor = new Vector3f(0.90f, 0.80f, 0.50f);
        bbMat.roughness = wbMat.roughness = 0.1f;
        bbMat.specular = wbMat.specular = 0.4f;
        bbMat.metallic = wbMat.metallic = 0.2f;
        for (int i = -4; i < 4; i ++) {
            for (int j = -4; j < 4; j ++) {
                wbMat.tag = bbMat.tag = (i + 4) * 10 + (j + 4);
                triangles.addAll(ResourceManager.loadObjAsTriangles(
                        "models/block.obj",
                        ((i + j) % 2 == 0) ? new Material(wbMat) : new Material(bbMat),
                        MathUtils.transformMatrix(
                                new Vector3f(1.0f, 1.5f, 1.0f),
                                new Vector3f(0.0f, 0.0f, 0.0f),
                                new Vector3f(1.0f + i * 2.0f, - 1.74f, 1.0f + j * 2.0f)
                        ),
                        false, false
                ));
            }
        }
    }

    /**
     * 依据棋盘添加棋子
     */
    public static void addChess(ArrayList<Triangle> triangles, int[][] contents) {
        boolean lifeSaver = OptionsManager.options.getProperty("simplifyChess").equals("on");
        boolean lightup = OptionsManager.options.getProperty("lightOfChess").equals("on");
        Material bbMat = new Material(), wbMat = new Material();
        bbMat.plainColor = new Vector3f(0.0f, 0.0f, 0.0f);
        wbMat.plainColor = new Vector3f(1.0f, 1.0f, 1.0f);
        if (lightup) bbMat.emissive = new Vector3f(0.05f, 0.05f, 0.05f);
        if (lightup) wbMat.emissive = new Vector3f(1.5f, 1.5f, 1.5f);
        bbMat.subsurface = wbMat.subsurface = 1.0f;
        bbMat.roughness = wbMat.roughness = 0.2f;
        bbMat.specular = wbMat.specular = 0.5f;
        Random rander = new Random();
        for (int i = -4; i < 4; i ++) {
            for (int j = -4; j < 4; j ++) {
                if (contents[i + 4][j + 4] != 0) {
                    wbMat.tag = bbMat.tag = (i + 4) * 10 + (j + 4);
                    float rdr = 0.0f; // rander.nextFloat();
                    triangles.addAll(ResourceManager.loadObjAsTriangles(
                            (lifeSaver) ? "models/chessreduced.obj" : "models/chess.obj",
                            (contents[i + 4][j + 4] == 1) ? new Material(bbMat) : new Material(wbMat),
                            MathUtils.transformMatrix(
                                    new Vector3f(0.4f, 0.4f, 0.4f),
                                    new Vector3f(0.0f, (float) ((float) rdr * Math.PI), 0.0f),
                                    new Vector3f(1.0f + i * 2.0f,  (lifeSaver) ? (-1.6f) : (-1.5f), 1.0f + j * 2.0f)
                            ),
                            false, false
                    ));
                }
            }
        }

        if (!OptionsManager.options.getProperty("simplifyBoard").equals("on")) {
            boolean lightupu = OptionsManager.options.getProperty("lightOfBoard").equals("on");
            Material boardMat = new Material();
            boardMat.plainColor = new Vector3f(0.83f, 0.76f, 0.48f);
            boardMat.roughness = 0.02f;
            boardMat.specular = 0.4f;
            boardMat.metallic = 0.2f;
            if (lightupu) boardMat.emissive = new Vector3f(0.2f, 0.2f, 0.2f);
            boardMat.tag = -1;
            triangles.addAll(ResourceManager.loadObjAsTriangles(
                    "models/board.obj",
                    boardMat,
                    MathUtils.transformMatrix(
                            new Vector3f(1.0f, 1.0f, 1.0f),
                            new Vector3f(0.0f, 0.0f, 0.0f),
                            new Vector3f(0.0f, - 1.75f, 0.0f)
                    ),
                    false,
                    false
            ));
        }
    }

    /**
     * 添加启动界面标题
     */
    public static void addTitle(ArrayList<Triangle> triangles) {
        boolean lifeSaver = OptionsManager.options.getProperty("simplifyTitle").equals("on");
        Material mat = new Material();
        mat.plainColor = new Vector3f(0.1f, 0.1f, 0.1f);
        mat.roughness = 0.4f;
        mat.metallic = 0.8f;
        mat.subsurface = 1.0f;
//        mat.sheen = 1.0f;
//        mat.sheenTint = 0.5f;
        mat.tag = -1;
        float offset = -6.4f, wid = 1.7f, offerror = -0.7f, oy = 6.0f, oz = -7.5f;
        // O
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/oupperreduced.obj" : "models/oupper.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 0 * wid + 0 * offerror,  oy, oz)
                ),
                false, false
        ));
        // t
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/treduced.obj" : "models/t.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 1 * wid + 0 * offerror,  oy, oz)
                ),
                false, false
        ));
        // h
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/hreduced.obj" : "models/h.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 2 * wid + 0 * offerror,  oy, oz)
                ),
                false, false
        ));
        // e
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/ereduced.obj" : "models/e.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 3 * wid + 0 * offerror,  oy, oz)
                ),
                false, false
        ));
        // double l
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/lreduced.obj" : "models/l.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 4 * wid + 0 * offerror,  oy, oz)
                ),
                false, false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/lreduced.obj" : "models/l.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 5 * wid + 1 * offerror,  oy, oz)
                ),
                false, false
        ));
        // double o
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/olowerreduced.obj" : "models/olower.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 6 * wid + 2 * offerror,  oy, oz)
                ),
                false, false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/olowerreduced.obj" : "models/olower.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 7 * wid + 2 * offerror,  oy, oz)
                ),
                false, false
        ));
        // O
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                (lifeSaver) ? "models/oupperreduced.obj" : "models/oupper.obj", mat,
                MathUtils.transformMatrix(
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(offset + 8 * wid + 1 * offerror,  oy, oz)
                ),
                false, false
        ));
    }

    /**
     * 0 康奈尔盒测试
     */
    public static void cornellBox(ArrayList<Triangle> triangles) {
        Material lWall = new Material();
        Material rWall = new Material();
        Material oWall = new Material();
        Material fWall = new Material();
        Material light = new Material();
        Material cube = new Material();
        lWall.tag = rWall.tag = oWall.tag = fWall.tag = light.tag = cube.tag = -1;
        lWall.plainColor = new Vector3f(0.85f, 0.15f, 0.15f);
        rWall.plainColor = new Vector3f(0.15f, 0.85f, 0.15f);
        oWall.plainColor = new Vector3f(0.70f, 0.70f, 0.70f);
        light.plainColor = new Vector3f(1.0f, 1.0f, 1.0f);
        cube.plainColor = new Vector3f(0.1f, 0.8f, 0.1f);

        float offsetY = 5.5f;

        lWall.metallic = rWall.metallic = oWall.metallic = 0.0f;
        lWall.roughness = rWall.roughness = oWall.roughness = 0.25f;
        lWall.subsurface = rWall.subsurface = oWall.subsurface = 1.0f;
        fWall.specular = 0.5f;
//        fWall.metallic = 0.5f;
        light.emissive = new Vector3f(5.0f, 5.0f, 5.0f);
        cube.specular = 0.95f;
        cube.subsurface = 1.0f;
        cube.roughness = 0.1f;

        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                lWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f(0.0f, 0.0f, (float) Math.PI / 2),
                        new Vector3f(-8.5f, 0.0f + offsetY, 0.0f)
                ),
                false,
                false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                rWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f(0.0f, 0.0f, (float) Math.PI / 2),
                        new Vector3f(8.5f, 0.0f + offsetY, 0.0f)
                ),
                false,
                false
        ));
//        triangles.addAll(ResourceManager.loadObjAsTriangles(
//                "models/block.obj",
//                oWall,
//                MathUtils.transformMatrix(
//                        new Vector3f(8.5f, 1.0f, 8.5f),
//                        new Vector3f(0.0f, 0.0f, 0.0f),
//                        new Vector3f(0.0f, 8.5f + offsetY, 0.0f)
//                ),
//                false,
//                false
//        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                fWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f((float) Math.PI / 2, 0.0f, 0.0f),
                        new Vector3f(0.0f, 0.0f + offsetY, -8.5f)
                ),
                false,
                false
        ));
//        triangles.addAll(ResourceManager.loadObjAsTriangles(
//                "models/block.obj",
//                fWall,
//                MathUtils.transformMatrix(
//                        new Vector3f(8.5f, 1.0f, 8.5f),
//                        new Vector3f((float) Math.PI / 2, 0.0f, 0.0f),
//                        new Vector3f(0.0f, 0.0f + offsetY, 8.5f)
//                ),
//                false,
//                false
//        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                light,
                MathUtils.transformMatrix(
                        new Vector3f(3.0f, 1.0f, 3.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(0.0f, 8.49f + offsetY, 0.0f)
                ),
                false,
                false
        ));
//        triangles.addAll(ResourceManager.loadObjAsTriangles(
//                "models/bunny.obj",
//                cube,
//                MathUtils.transformMatrix(
//                        new Vector3f(3.0f, 3.0f, 3.0f),
//                        new Vector3f(0.0f, 0.0f, 0.0f),
//                        new Vector3f(0.7f, -1.8f, 0.0f)
//                ),
//                false,
//                true
//        ));
    }

    /**
     * 1 镜子盒测试
     */
    public static void MirrorBox(ArrayList<Triangle> triangles) {
        Material oWall = new Material();
        Material fWall = new Material();
        Material light = new Material();
        oWall.tag = fWall.tag = light.tag = -1;
        oWall.plainColor = new Vector3f(0.40f, 0.40f, 0.40f);
        light.plainColor = new Vector3f(1.0f, 1.0f, 1.0f);

        float offsetY = 5.5f;

        oWall.metallic = 0.0f;
        oWall.roughness = 0.25f;
        oWall.subsurface = 1.0f;
        fWall.specular = 0.5f;
        fWall.metallic = 0.5f;
        light.emissive = new Vector3f(5.0f, 5.0f, 5.0f);

        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                fWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f(0.0f, 0.0f, (float) Math.PI / 2),
                        new Vector3f(-8.5f, 0.0f + offsetY, 0.0f)
                ),
                false,
                false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                fWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f(0.0f, 0.0f, (float) Math.PI / 2),
                        new Vector3f(8.5f, 0.0f + offsetY, 0.0f)
                ),
                false,
                false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                oWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(0.0f, 8.5f + offsetY, 0.0f)
                ),
                false,
                false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                oWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(0.0f, -8.5f + offsetY, 0.0f)
                ),
                false,
                false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                fWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f((float) Math.PI / 2, 0.0f, 0.0f),
                        new Vector3f(0.0f, 0.0f + offsetY, -8.5f)
                ),
                false,
                false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                fWall,
                MathUtils.transformMatrix(
                        new Vector3f(8.5f, 1.0f, 8.5f),
                        new Vector3f((float) Math.PI / 2, 0.0f, 0.0f),
                        new Vector3f(0.0f, 0.0f + offsetY, 8.5f)
                ),
                false,
                false
        ));
        triangles.addAll(ResourceManager.loadObjAsTriangles(
                "models/block.obj",
                light,
                MathUtils.transformMatrix(
                        new Vector3f(3.0f, 1.0f, 3.0f),
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(0.0f, 8.49f + offsetY, 0.0f)
                ),
                false,
                false
        ));
    }
}
