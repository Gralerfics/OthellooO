package tech.gralerfics.controller;

import tech.gralerfics.controller.glfw.GlfwApplication;
import tech.gralerfics.domain.Handles;
import tech.gralerfics.domain.HitRst;
import tech.gralerfics.domain.Ray;
import tech.gralerfics.gamelogic.Game;
import tech.gralerfics.gamelogic.Player;
import tech.gralerfics.managers.rendering.RenderManager;
import tech.gralerfics.persistence.options.OptionsManager;
import tech.gralerfics.raytracer.utils.BVHTreeUtils;
import tech.gralerfics.utils.maths.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL31.glTexBuffer;
import static tech.gralerfics.raytracer.scenes.SceneManager.*;

public class GlfwController extends Controller {
    private static final float CURSOR_EPS = 1.1f;
    private GlfwApplication glfwApp;
    private Handles handles;

    private boolean isWdown = false, isSdown = false, isAdown = false, isDdown = false;
    float[] lpinf = new float[8 * 8 * 3];

    public GlfwController(String title, int hres, int vres, Mouse mouse, Keyboard keyboard) {
        // 创建应用程序
        this.setTitle(title);
        this.setHres(hres);
        this.setVres(vres);
        this.setMouse(mouse);
        this.setKeyboard(keyboard);
        glfwApp = new GlfwApplication(this);
        handles = new Handles();
        // 设置回调函数
        this.glfwApp.getWindow().setGlfwWindowSizeCallback((window, width, height) -> {
            this.setHres(width);
            this.setVres(height);
            this.glfwApp.getWindow().setWidth(width);
            this.glfwApp.getWindow().setHeight(height);

            this.handles.getRenderPipe1().cAtt.clear();
            this.handles.getRenderPipe1().cAtt.add(RenderManager.genRGB32F(this.getHres(), this.getVres()));
            this.handles.getRenderPipe1().cAtt.add(RenderManager.genRGB32F(this.getHres(), this.getVres()));
            this.handles.getRenderPipe1().cAtt.add(RenderManager.genRGB32F(this.getHres(), this.getVres()));
            this.handles.getRenderPipe1().initBuffer(false);

            this.handles.getRenderPipe1().shaderProgram.use();
            this.handles.getRenderPipe1().shaderProgram.setInt("hres", this.getHres());
            this.handles.getRenderPipe1().shaderProgram.setInt("vres", this.getVres());
            this.handles.getRenderPipe1().shaderProgram.deuse();

            this.handles.setLastFrame(RenderManager.genRGB32F(this.getHres(), this.getVres()));
            this.handles.getRenderPipe2().cAtt.clear();
            this.handles.getRenderPipe2().cAtt.add(this.handles.getLastFrame());
            this.handles.getRenderPipe2().initBuffer(false);

            glViewport(0, 0, width, height);
        });
        this.getMouse().setGlfwMouseButtonCallback((window, button, action, mods) -> {
            if ((button == GLFW_MOUSE_BUTTON_LEFT || button == GLFW_MOUSE_BUTTON_RIGHT) && action == GLFW_PRESS) {
                if (button == GLFW_MOUSE_BUTTON_LEFT) this.getMouse().setLeftButtonDown(true);
                if (button == GLFW_MOUSE_BUTTON_RIGHT) this.getMouse().setRightButtonDown(true);
                this.getMouse().setLastDownX(this.getMouse().getLastX());
                this.getMouse().setLastDownY(this.getMouse().getLastY());
            } else if ((button == GLFW_MOUSE_BUTTON_LEFT || button == GLFW_MOUSE_BUTTON_RIGHT) && action == GLFW_RELEASE) {
                if (button == GLFW_MOUSE_BUTTON_LEFT) this.getMouse().setLeftButtonDown(false);
                if (button == GLFW_MOUSE_BUTTON_RIGHT) this.getMouse().setRightButtonDown(false);
                if (button == GLFW_MOUSE_BUTTON_LEFT && Math.abs(this.getMouse().getLastDownX() - this.getMouse().getLastX()) < CURSOR_EPS && Math.abs(this.getMouse().getLastDownY() - this.getMouse().getLastY()) < CURSOR_EPS) {
                    onClick(window);
                    this.handles.setFrameCnt(1);
                }
            }
        });
        this.getMouse().setGlfwScrollCallback((window, x, y) -> {
            this.handles.setFrameCnt(1);
            if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "1")) {
                this.handles.getCamera().lookAt = Vector3f.add(this.handles.getCamera().lookAt, this.handles.getCamera().w.scaler((float) (y * 0.6f)));
            }
            this.handles.getCamera().r -= (float) (y * 0.6f);
            if (this.handles.getCamera().r < 0.01f) this.handles.getCamera().r = 0.01f;
//            this.handles.getCamera().eye = Vector3f.add(this.handles.getCamera().eye, this.handles.getCamera().w.scaler((float) (y * 0.6f)));
        });
        this.getMouse().setGlfwCursorPosCallback((window, x, y) -> {
            if (this.getMouse().isLeftButtonDown()) {
                this.handles.setFrameCnt(1);
                if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "1")) {
                    this.handles.getCamera().rotateAngle += 2.618f * (x - this.getMouse().getLastX()) / 512.0f;
                    this.handles.getCamera().upAngle += 2.618f * (y - this.getMouse().getLastY()) / 512.0f;
                    this.handles.getCamera().upAngle = Math.min(this.handles.getCamera().upAngle, 1.55f);
                    this.handles.getCamera().upAngle = Math.max(this.handles.getCamera().upAngle, -1.55f);
                } else if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "2")) {
                    this.handles.getCamera().rotateAngle += 2.618f * (x - this.getMouse().getLastX()) / 512.0f;
                    this.handles.getCamera().upAngle += 2.618f * (y - this.getMouse().getLastY()) / 512.0f;
                    this.handles.getCamera().upAngle = Math.min(this.handles.getCamera().upAngle, 1.55f);
                    this.handles.getCamera().upAngle = Math.max(this.handles.getCamera().upAngle, -1.55f);
                } else if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "3")) {
                    this.handles.getCamera().rotateAngle += 2.618f * (x - this.getMouse().getLastX()) / 512.0f;
                    this.handles.getCamera().upAngle += 2.618f * (y - this.getMouse().getLastY()) / 512.0f;
                    this.handles.getCamera().upAngle = Math.min(this.handles.getCamera().upAngle, 1.55f);
                    this.handles.getCamera().upAngle = Math.max(this.handles.getCamera().upAngle, -1.55f);
                }
            }
            if (this.getMouse().isRightButtonDown()) {
                this.handles.setFrameCnt(1);
                float factor = 0.02f;
                if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "1")) {
                    this.handles.getCamera().eye = Vector3f.substract(this.handles.getCamera().eye, this.handles.getCamera().u.scaler((float) (x - this.getMouse().getLastX()) * factor));
                    this.handles.getCamera().eye = Vector3f.substract(this.handles.getCamera().eye, this.handles.getCamera().v.scaler((float) (y - this.getMouse().getLastY()) * factor));
                    this.handles.getCamera().lookAt = Vector3f.substract(this.handles.getCamera().lookAt, this.handles.getCamera().u.scaler((float) (x - this.getMouse().getLastX()) * factor));
                    this.handles.getCamera().lookAt = Vector3f.substract(this.handles.getCamera().lookAt, this.handles.getCamera().v.scaler((float) (y - this.getMouse().getLastY()) * factor));
                }
            }
            this.getMouse().setLastX((float) x);
            this.getMouse().setLastY((float) y);
        });
        this.getKeyboard().setGlfwKeyCallback((window, key, scancode, action, mods) -> {
            float dis = 0.2f;
            if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "2")) {
                if (key == GLFW_KEY_W) {
                    if (action == GLFW_PRESS) {
                        isWdown = true;
                    } else if (action == GLFW_RELEASE) {
                        isWdown = false;
                    }
                }
                if (key == GLFW_KEY_S) {
                    if (action == GLFW_PRESS) {
                        isSdown = true;
                    } else if (action == GLFW_RELEASE) {
                        isSdown = false;
                    }
                }
                if (key == GLFW_KEY_A) {
                    if (action == GLFW_PRESS) {
                        isAdown = true;
                    } else if (action == GLFW_RELEASE) {
                        isAdown = false;
                    }
                }
                if (key == GLFW_KEY_D) {
                    if (action == GLFW_PRESS) {
                        isDdown = true;
                    } else if (action == GLFW_RELEASE) {
                        isDdown = false;
                    }
                }
                if (isWdown) {
                    this.handles.setFrameCnt(1);
                    this.handles.getCamera().lookAt = Vector3f.add(this.handles.getCamera().lookAt, this.handles.getCamera().w.scaler(dis));
                }
                if (isSdown) {
                    this.handles.setFrameCnt(1);
                    this.handles.getCamera().lookAt = Vector3f.substract(this.handles.getCamera().lookAt, this.handles.getCamera().w.scaler(dis));
                }
                if (isAdown) {
                    this.handles.setFrameCnt(1);
                    this.handles.getCamera().lookAt = Vector3f.substract(this.handles.getCamera().lookAt, this.handles.getCamera().u.scaler(dis));
                }
                if (isDdown) {
                    this.handles.setFrameCnt(1);
                    this.handles.getCamera().lookAt = Vector3f.add(this.handles.getCamera().lookAt, this.handles.getCamera().u.scaler(dis));
                }
            }
//            if (currentScene == GAME_SCENE) {
//                if (key == GLFW_KEY_LEFT_SHIFT || key == GLFW_KEY_RIGHT_SHIFT) {
//                    if (action == GLFW_RELEASE) {
//                        isShiftDown = false;
//                    } else if (action == GLFW_PRESS) {
//                        isShiftDown = true;
//                    }
//                }
//                if (key == GLFW_KEY_LEFT_CONTROL || key == GLFW_KEY_RIGHT_CONTROL) {
//                    if (action == GLFW_RELEASE) {
//                        isCtrlDown = false;
//                    } else if (action == GLFW_PRESS) {
//                        isCtrlDown = true;
//                    }
//                }
//                if (key == GLFW_KEY_SLASH && action == GLFW_RELEASE && !isCommanding) {
//                    isCommanding = true;
//                    command.setLength(0);
//                }
//                if (isCommanding && action == GLFW_RELEASE) {
//                    if (key >= GLFW_KEY_A && key <= GLFW_KEY_Z) {
//                        command.append((char) (key + ((isShiftDown) ? 0 : 32)));
//                    } else if (key >= GLFW_KEY_0 && key <= GLFW_KEY_9) {
//                        command.append((char) key);
//                    } else if (key >= GLFW_KEY_KP_0 && key <= GLFW_KEY_KP_9) {
//                        command.append((char) (key - 272));
//                    } else if (key == GLFW_KEY_SLASH) {
//                        command.append('/');
//                    } else if (key == GLFW_KEY_COMMA) {
//                        command.append(',');
//                    } else if (key == GLFW_KEY_PERIOD) {
//                        command.append('.');
//                    } else if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
//                        isCommanding = false;
//                        executeCommand(command.toString(), window);
//                    } else if (key == GLFW_KEY_BACKSPACE) {
//                        if (command.length() > 0) command.setLength(command.length() - 1);
//                    } else if (key == GLFW_KEY_SPACE) {
//                        command.append(' ');
//                    }
//                    updateText = true;
//                    frameCnt = 1;
//                } else {
//                    if (isCtrlDown) {
//                        if (key == GLFW_KEY_Z && action == GLFW_RELEASE) {
//                            Judger.backHistory();
//                            frameCnt = 1;
//                        } else if (key == GLFW_KEY_A && action == GLFW_RELEASE) {
//                            // 从 AI 获取.
////                            int rs = AIProcess.getNext(Judger.contents, Judger.curChess);
////                            int i = rs / 10, j = rs % 10;
////                            putChess(i, j);
//                            frameCnt = 1;
//                        } else if (key == GLFW_KEY_R && action == GLFW_RELEASE) {
//                            Judger.init();
//                            frameCnt = 1;
//                        } else if (key == GLFW_KEY_V && action == GLFW_RELEASE) {
//                            camera.lookAt = new Vector3f(0.0f, 0.0f, 0.0f);
//                            frameCnt = 1;
//                        }
//                    }
//                }
//            }
        });
        glfwApp.initCallbacks(this);
    }

    public GlfwApplication getGlfwApp() {
        return glfwApp;
    }

    public Handles getHandles() {
        return handles;
    }

    /**
     * 单机左键
     */
    private void onClick(long window) {
        if (currentGame != null && currentGame.white != null && currentGame.black != null) {
            if (this.getMouse().getSelected() >= 0 && this.getMouse().getSelected() <= 77) {
                Player curPlayer = (currentGame.judger.curChess == 1) ? currentGame.black : currentGame.white;
                if (curPlayer.tag == 0) {
                    curPlayer.nextStep = this.getMouse().getSelected();
                }
                this.handles.setFrameCnt(1);
            }
        }
    }

    public void run() {
        glfwApp.run(this);
    }

    public void init() {
        // 默认局
        currentGame = new Game();

        // 相机对正
        this.handles.getCamera().lookAt = new Vector3f(0.0f, 4.0f, 9.0f);

        // 初始化场景
        initScene();

        // 管线配置
        this.handles.getRenderPipe1().shaderProgram.addShader(GL_VERTEX_SHADER , "rpVertexShader.vs");
        this.handles.getRenderPipe1().shaderProgram.addShader(GL_FRAGMENT_SHADER, "rp1FragmentShader.fs");
        this.handles.getRenderPipe1().shaderProgram.reload();
        this.handles.getRenderPipe1().cAtt.add(RenderManager.genRGB32F(this.getHres(), this.getVres()));
        this.handles.getRenderPipe1().cAtt.add(RenderManager.genRGB32F(this.getHres(), this.getVres()));
        this.handles.getRenderPipe1().cAtt.add(RenderManager.genRGB32F(this.getHres(), this.getVres()));
        this.handles.getRenderPipe1().cAtt.add(RenderManager.genRGB32F(this.getHres(), this.getVres()));
        this.handles.getRenderPipe1().initBuffer(false);

        this.handles.getRenderPipe1().shaderProgram.use();
        this.handles.getRenderPipe1().shaderProgram.setInt("hres", this.getHres());
        this.handles.getRenderPipe1().shaderProgram.setInt("vres", this.getVres());
        this.handles.getRenderPipe1().shaderProgram.deuse();

        this.handles.getRenderPipe2().shaderProgram.addShader(GL_VERTEX_SHADER , "rpVertexShader.vs");
        this.handles.getRenderPipe2().shaderProgram.addShader(GL_FRAGMENT_SHADER, "rp2FragmentShader.fs");
        this.handles.getRenderPipe2().shaderProgram.reload();
        this.handles.setLastFrame(RenderManager.genRGB32F(this.getHres(), this.getVres()));
        this.handles.getRenderPipe2().cAtt.add(this.handles.getLastFrame());
        this.handles.getRenderPipe2().initBuffer(false);

        this.handles.getRenderPipe3().shaderProgram.addShader(GL_VERTEX_SHADER , "rpVertexShader.vs");
        this.handles.getRenderPipe3().shaderProgram.addShader(GL_FRAGMENT_SHADER, "rp3FragmentShader.fs");
        this.handles.getRenderPipe3().shaderProgram.reload();
        this.handles.getRenderPipe3().initBuffer(true);
    }

    private void update() {
        // 相机设置
        if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "1") || Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "2")) {
            handles.getCamera().eye = new Vector3f(
                    (float) (-0.01f * Math.sin(handles.getCamera().rotateAngle) * Math.cos(handles.getCamera().upAngle) + handles.getCamera().lookAt.x),
                    (float) (0.01f * Math.sin(handles.getCamera().upAngle)) + handles.getCamera().lookAt.y,
                    (float) (0.01f * Math.cos(handles.getCamera().rotateAngle) * Math.cos(handles.getCamera().upAngle)) + handles.getCamera().lookAt.z
            );
        } else if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "3")) {
            handles.getCamera().eye = new Vector3f(
                    (float) (-handles.getCamera().r * Math.sin(handles.getCamera().rotateAngle) * Math.cos(handles.getCamera().upAngle) + handles.getCamera().lookAt.x),
                    (float) (handles.getCamera().r * Math.sin(handles.getCamera().upAngle)) + handles.getCamera().lookAt.y,
                    (float) (handles.getCamera().r * Math.cos(handles.getCamera().rotateAngle) * Math.cos(handles.getCamera().upAngle)) + handles.getCamera().lookAt.z
            );
            handles.getCamera().lookAt = new Vector3f(0.0f, 0.0f, 0.0f);
        } else if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "4")) {
            handles.getCamera().eye = new Vector3f(0.0f, 7.0f, 0.01f);
            handles.getCamera().lookAt = new Vector3f(0.0f, 0.0f, 0.0f);
        }
        handles.getCamera().up = new Vector3f(0.0f, 1.0f, 0.0f);
        handles.getCamera().computeUVW();

        // 计算 selected 选取状态
        Ray ray = new Ray();
        ray.origin = handles.getCamera().eye;
        float mx = this.getMouse().getLastX() / this.getHres() * 2.0f - 1.0f;
        float my = this.getMouse().getLastY() / this.getVres() * 2.0f - 1.0f;
        ray.direction = Vector3f.add(
                handles.getCamera().w, Vector3f.add(
                        handles.getCamera().u.scaler(mx),
                        handles.getCamera().v.scaler(my * 1.0f * this.getVres() / this.getHres())
                )
        ).normalize();
        HitRst hst = BVHTreeUtils.hitInterval(ray, 0, triangles.size() - 1, triangles);
        HitRst tst = BVHTreeUtils.hitInterval(ray, 0, dytris.size() - 1, dytris);
        if (tst.isHit && tst.t < hst.t) hst = tst;
        if (hst.isHit) {
            this.getMouse().setSelected(hst.material.tag);
        } else {
            this.getMouse().setSelected(-1);
        }
        if (this.getMouse().getSelected() != this.getMouse().getLastSelected()) {
            this.handles.setFrameCnt(1);
        }

        // 动态场景更新
        updateScene(this);

        // 游戏循环
        if (currentGame != null) {
            currentGame.loop();
        }

        // 同步 selected
        this.getMouse().setLastSelected(this.getMouse().getSelected());

        // 计算并同步 legal places
        Arrays.fill(lpinf, 0.0f);
        int cnt = 0;
        if (currentGame != null && currentGame.white != null && currentGame.black != null && OptionsManager.options.getProperty("showLegalPlace").equals("on")) {
            Player curPlayer = (currentGame.judger.curChess == 1) ? currentGame.black : currentGame.white;
            if (curPlayer.tag == 0) {
                for (int i = 0; i < 8; i ++) {
                    for (int j = 0; j < 8; j ++) {
                        if (currentGame.judger.check(i, j, currentGame.judger.curChess)) {
                            lpinf[cnt ++] = 1.0f;
                            cnt += 2;
                        } else {
                            cnt += 3;
                        }
                    }
                }
            }
        }
        int TBO = glGenBuffers();
        glBindBuffer(GL_TEXTURE_BUFFER, TBO);
        glBufferData(GL_TEXTURE_BUFFER, lpinf, GL_DYNAMIC_DRAW);
        lpinfBuffer = glGenTextures();
        glBindTexture(GL_TEXTURE_BUFFER, lpinfBuffer);
        glTexBuffer(GL_TEXTURE_BUFFER, GL_RGB32F, TBO);
    }

    public void loop() {
        // Fps 计算及帧累积
        this.handles.setT2(System.currentTimeMillis());
        float fps = 1000.0f * 1 / (this.handles.getT2() - this.handles.getT1());
        this.handles.setT1(this.handles.getT2());
        this.handles.setFrameCnt(this.handles.getFrameCnt() + 1);

        // 更新
        update();

        // Uniform 传值
        this.handles.getRenderPipe1().shaderProgram.use();
        this.handles.getRenderPipe1().shaderProgram.setInt("trianglesNum", triangles.size());
        this.handles.getRenderPipe1().shaderProgram.setInt("dytrisNum", dytris.size());
        this.handles.getRenderPipe1().shaderProgram.setInt("pointNum", encoded2DPoints.size());
        Random rdr = new Random(System.currentTimeMillis());
        this.handles.getRenderPipe1().shaderProgram.setFloat("rdSeed[0]", rdr.nextFloat());
        this.handles.getRenderPipe1().shaderProgram.setFloat("rdSeed[1]", rdr.nextFloat());
        this.handles.getRenderPipe1().shaderProgram.setFloat("rdSeed[2]", rdr.nextFloat());
        this.handles.getRenderPipe1().shaderProgram.setFloat("rdSeed[3]", rdr.nextFloat());
        this.handles.getRenderPipe1().shaderProgram.setVector3f("eye", this.handles.getCamera().eye.x, this.handles.getCamera().eye.y, this.handles.getCamera().eye.z);
        this.handles.getRenderPipe1().shaderProgram.setVector3f("lookAt", this.handles.getCamera().lookAt.x, this.handles.getCamera().lookAt.y, this.handles.getCamera().lookAt.z);
        this.handles.getRenderPipe1().shaderProgram.setVector3f("up", this.handles.getCamera().up.x, this.handles.getCamera().up.y, this.handles.getCamera().up.z);
        this.handles.getRenderPipe1().shaderProgram.setVector3f("u", this.handles.getCamera().u.x, this.handles.getCamera().u.y, this.handles.getCamera().u.z);
        this.handles.getRenderPipe1().shaderProgram.setVector3f("v", this.handles.getCamera().v.x, this.handles.getCamera().v.y, this.handles.getCamera().v.z);
        this.handles.getRenderPipe1().shaderProgram.setVector3f("w", this.handles.getCamera().w.x, this.handles.getCamera().w.y, this.handles.getCamera().w.z);
        this.handles.getRenderPipe1().shaderProgram.setInt("frameCnt", (int) this.handles.getFrameCnt());
        this.handles.getRenderPipe1().shaderProgram.setInt("hdfRes", Integer.parseInt(OptionsManager.options.getProperty("hdrResolution")));
        this.handles.getRenderPipe1().shaderProgram.setInt("selected", this.getMouse().getSelected());
        int lastp = -1;
        if (OptionsManager.options.getProperty("showLastPlace").equals("on")) {
            lastp = (currentGame.judger.stepList.isEmpty()) ? (-1) : (currentGame.judger.stepList.get(currentGame.judger.stepList.size() - 1).getI() * 10 + currentGame.judger.stepList.get(currentGame.judger.stepList.size() - 1).getJ());
        }
        this.handles.getRenderPipe1().shaderProgram.setInt("lastpos", lastp);
        this.handles.getRenderPipe1().shaderProgram.setInt("TDEPTH", Integer.parseInt(OptionsManager.options.getProperty("rayTracingDepth")));
        this.handles.getRenderPipe1().shaderProgram.setFloat("lightFactor", Float.parseFloat(OptionsManager.options.getProperty("hdrLightFactor")));
        this.handles.getRenderPipe1().shaderProgram.setBoolean("hdrClosed", OptionsManager.options.getProperty("hdrFilename").equals(""));
        this.handles.getRenderPipe1().shaderProgram.setBoolean("chessSpClosed", !OptionsManager.options.getProperty("chessBoardSpecular").equals("on"));

        // Uniform 传值 - 加载当前场景
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_BUFFER, triTextureBuffer);
        this.handles.getRenderPipe1().shaderProgram.setInt("triangles", 0);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_BUFFER, bvhTextureBuffer);
        this.handles.getRenderPipe1().shaderProgram.setInt("tree", 1);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_BUFFER, dytTextureBuffer);
        this.handles.getRenderPipe1().shaderProgram.setInt("dytris", 2);
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_BUFFER, dtrTextureBuffer);
        this.handles.getRenderPipe1().shaderProgram.setInt("dytree", 3);
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_BUFFER, surTextureBuffer);
        this.handles.getRenderPipe1().shaderProgram.setInt("surpnter", 4);
        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, this.handles.getLastFrame());
        this.handles.getRenderPipe1().shaderProgram.setInt("lastFrame", 5);
        glActiveTexture(GL_TEXTURE6);
        glBindTexture(GL_TEXTURE_2D, hdrMap);
        this.handles.getRenderPipe1().shaderProgram.setInt("hdrMap", 6);
        glActiveTexture(GL_TEXTURE7);
        glBindTexture(GL_TEXTURE_2D, hdrCache);
        this.handles.getRenderPipe1().shaderProgram.setInt("hdrCache", 7);
        glActiveTexture(GL_TEXTURE8);
        glBindTexture(GL_TEXTURE_BUFFER, lpinfBuffer);
        this.handles.getRenderPipe1().shaderProgram.setInt("lpinf", 8);
        this.handles.getRenderPipe1().shaderProgram.deuse();

        // 渲染画布
        this.handles.getRenderPipe1().render(new ArrayList<>());
        this.handles.getRenderPipe2().render(this.handles.getRenderPipe1().cAtt);
        this.handles.getRenderPipe3().render(this.handles.getRenderPipe2().cAtt);
    }
}
