package tech.gralerfics.raytracer.scenes;

import com.github.ivelate.JavaHDR.HDREncoder;
import com.github.ivelate.JavaHDR.HDRImage;
import tech.gralerfics.controller.GlfwController;
import tech.gralerfics.domain.*;
import tech.gralerfics.gamelogic.Game;
import tech.gralerfics.managers.rendering.RenderManager;
import tech.gralerfics.managers.resource.ResourceManager;
import tech.gralerfics.persistence.options.OptionsManager;
import tech.gralerfics.raytracer.utils.BVHTreeUtils;
import tech.gralerfics.utils.maths.Vector3f;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.GL_RG32F;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL31.glTexBuffer;

/**
 * 场景数据管理（不允许空场景）
 */
public final class SceneManager {
    public static final int START_SCENE = 0;
    public static final int GAME_SCENE = 1;

    public static int triTextureBuffer, bvhTextureBuffer, dytTextureBuffer, surTextureBuffer, dtrTextureBuffer, hdrMap, hdrCache, lpinfBuffer;

    public static ArrayList<Triangle> triangles;
    public static ArrayList<BVHNode> tree;
    public static ArrayList<Triangle> dytris = new ArrayList<>();
    public static ArrayList<BVHNode> dytree = new ArrayList<>();
    public static ArrayList<Vector3f> encoded2DPoints = new ArrayList<>();

    public static boolean updateText = false;
    public static Game currentGame;

    public static void initHdr() {
        HDRImage hdrRes = null;
        try {
            if (OptionsManager.options.getProperty("hdrFilename").equals("")) {
                return;
            }
            hdrRes = HDREncoder.readHDR(new File("assets/hdr/" + OptionsManager.options.getProperty("hdrFilename") + ".hdr"), true);
            hdrMap = RenderManager.genRGB32F(hdrRes.getWidth(), hdrRes.getHeight());
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, hdrRes.getWidth(), hdrRes.getHeight(), 0, GL_RGB, GL_FLOAT, hdrRes.getInternalData());

            hdrCache = RenderManager.genRGB32F(hdrRes.getWidth(), hdrRes.getHeight());
            float[] cache = ResourceManager.calculateHdrCache(hdrRes.getInternalData(), hdrRes.getWidth(), hdrRes.getHeight());;
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, hdrRes.getWidth(), hdrRes.getHeight(), 0, GL_RGB, GL_FLOAT, cache);
        } catch (IOException e) {
            OptionsManager.options.setProperty("hdrFilename", "");
        }
    }

    /**
     * 编码初始静态场景 - triangles & bvhTree, 并生成 Texture
     */
    public static void encodeStaticScene(ArrayList<Triangle> en_triangles, ArrayList<BVHNode> en_tree) {
        // 编码 Triangles
        ArrayList<EncodedTriangle> encodedTriangles = new ArrayList<>();
        for (Triangle tri : en_triangles) {
            Material mat = tri.material;
            encodedTriangles.add(new EncodedTriangle(
                    tri.p1, tri.p2, tri.p3,
                    tri.n1, tri.n2, tri.n3,
                    new EncodedMaterial(
                            mat.emissive, mat.plainColor,
                            new Vector3f(mat.subsurface, mat.metallic, mat.specular),
                            new Vector3f(mat.specularTint, mat.roughness, mat.anisotropic),
                            new Vector3f(mat.sheen, mat.sheenTint, mat.clearcoat),
                            new Vector3f(mat.clearcoatGloss, mat.IOR, mat.tag) // mat.transmission)
                    )
            ));
        }

        // 编码 BVHNode
        ArrayList<EncodedBVHNode> encodedBVHNodes = new ArrayList<>();
        for (BVHNode no : en_tree) {
            encodedBVHNodes.add(new EncodedBVHNode(
                    new Vector3f(no.left, no.right, 0),
                    new Vector3f(no.n, no.index, 0),
                    no.AA, no.BB
            ));
        }

        // 生成 Texture
        int TBO0 = glGenBuffers();
        glBindBuffer(GL_TEXTURE_BUFFER, TBO0);
        glBufferData(GL_TEXTURE_BUFFER, ResourceManager.encodedTrianglesToFloats(encodedTriangles), GL_STATIC_DRAW);
        triTextureBuffer = glGenTextures();
        glBindTexture(GL_TEXTURE_BUFFER, triTextureBuffer);
        glTexBuffer(GL_TEXTURE_BUFFER, GL_RGB32F, TBO0);

        int TBO1 = glGenBuffers();
        glBindBuffer(GL_TEXTURE_BUFFER, TBO1);
        glBufferData(GL_TEXTURE_BUFFER, ResourceManager.encodedBVHNodesToFloats(encodedBVHNodes), GL_STATIC_DRAW);
        bvhTextureBuffer = glGenTextures();
        glBindTexture(GL_TEXTURE_BUFFER, bvhTextureBuffer);
        glTexBuffer(GL_TEXTURE_BUFFER, GL_RGB32F, TBO1);

        // HDR
        initHdr();
    }

    /**
     * 动态更新场景
     */
    public static void updateScene(GlfwController glfwCtrl) {
        // 更新动态场景
        dytris.clear();
        SceneGenerator.addNull(dytris);
//        SceneGenerator.addTitle(dytris);

        if (currentGame != null) {
            SceneGenerator.addChess(dytris, currentGame.judger.contents);
        }
        if (OptionsManager.options.getProperty("cornellBox").equals("on")) {
            SceneGenerator.cornellBox(dytris);
        }
        if (OptionsManager.options.getProperty("mirrorBox").equals("on")) {
            SceneGenerator.MirrorBox(dytris);
        }
        if (updateText) {
            encoded2DPoints.clear();
            encoded2DPoints.addAll(TextGenerator.genSentence(5, 5, glfwCtrl.getKeyboard().getCommand().toString(), 1, 2));
            updateText = false;
        }

        // 建树
        dytree.clear();
        BVHTreeUtils.buildTree(dytris, dytree, 0, dytris.size() - 1, 8);

        // 编码 Dynamic Triangles
        ArrayList<EncodedTriangle> encodedDyTris = new ArrayList<>();
        for (Triangle tri : dytris) {
            Material mat = tri.material;
            encodedDyTris.add(new EncodedTriangle(
                    tri.p1, tri.p2, tri.p3,
                    tri.n1, tri.n2, tri.n3,
                    new EncodedMaterial(
                            mat.emissive, mat.plainColor,
                            new Vector3f(mat.subsurface, mat.metallic, mat.specular),
                            new Vector3f(mat.specularTint, mat.roughness, mat.anisotropic),
                            new Vector3f(mat.sheen, mat.sheenTint, mat.clearcoat),
                            new Vector3f(mat.clearcoatGloss, mat.IOR, mat.tag) // mat.transmission)
                    )
            ));
        }

        // 编码 BVHNode
        ArrayList<EncodedBVHNode> encodedDytNodes = new ArrayList<>();
        for (BVHNode no : dytree) {
            encodedDytNodes.add(new EncodedBVHNode(
                    new Vector3f(no.left, no.right, 0),
                    new Vector3f(no.n, no.index, 0),
                    no.AA, no.BB
            ));
        }

        // 生成 Texture
        int TBO0 = glGenBuffers();
        glBindBuffer(GL_TEXTURE_BUFFER, TBO0);
        glBufferData(GL_TEXTURE_BUFFER, ResourceManager.encodedTrianglesToFloats(encodedDyTris), GL_DYNAMIC_DRAW);
        dytTextureBuffer = glGenTextures();
        glBindTexture(GL_TEXTURE_BUFFER, dytTextureBuffer);
        glTexBuffer(GL_TEXTURE_BUFFER, GL_RGB32F, TBO0);

        int TBO1 = glGenBuffers();
        glBindBuffer(GL_TEXTURE_BUFFER, TBO1);
        glBufferData(GL_TEXTURE_BUFFER, ResourceManager.encodedBVHNodesToFloats(encodedDytNodes), GL_DYNAMIC_DRAW);
        dtrTextureBuffer = glGenTextures();
        glBindTexture(GL_TEXTURE_BUFFER, dtrTextureBuffer);
        glTexBuffer(GL_TEXTURE_BUFFER, GL_RGB32F, TBO1);

        int TBO2 = glGenBuffers();
        glBindBuffer(GL_TEXTURE_BUFFER, TBO2);
        glBufferData(GL_TEXTURE_BUFFER, ResourceManager.encoded2DPointsToFloats(encoded2DPoints), GL_DYNAMIC_DRAW);
        surTextureBuffer = glGenTextures();
        glBindTexture(GL_TEXTURE_BUFFER, surTextureBuffer);
        glTexBuffer(GL_TEXTURE_BUFFER, GL_RG32F, TBO2);
    }

    /**
     * 初始化游戏界面
     */
    public static void initScene() {
        // 初始场景数据
        triangles = new ArrayList<>();
        SceneGenerator.addPlane(triangles);

        // 建树
        tree = new ArrayList<>();
        BVHTreeUtils.buildTree(triangles, tree, 0, triangles.size() - 1, 8);

        // 编码并生成 Texture
        encodeStaticScene(triangles, tree);
    }
}
