package tech.gralerfics.controller.glfw;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;
import tech.gralerfics.controller.Main;
import tech.gralerfics.controller.wind.TipDialog;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

// GLFW 窗口类
public class GlfwWindow {
    private long window;
    private int width;
    private int height;
    private String title = "";
    public boolean isVisible = false;

    private GLFWWindowSizeCallbackI glfwWindowSizeCallback;

    public GlfwWindow(int width, int height) {
        this.width = width;
        this.height = height;
        this.initialize();
    }
    public GlfwWindow(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.initialize();
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public long getWindow() {
        return window;
    }
    public void setWindow(long window) {
        this.window = window;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public GLFWWindowSizeCallbackI getGlfwWindowSizeCallback() {
        return glfwWindowSizeCallback;
    }
    public void setGlfwWindowSizeCallback(GLFWWindowSizeCallbackI glfwWindowSizeCallback) {
        this.glfwWindowSizeCallback = glfwWindowSizeCallback;
    }

    /**
     * 初始化 GLFW 窗口
     */
    private void initialize() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window.");
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            assert vidmode != null;
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwSetWindowCloseCallback(window, (window) -> {
            (new TipDialog(Main.statusFrame)).show("Please quit from the menu.");
            glfwSetWindowShouldClose(window, false);
        });

//        setVisible(true);
        setVisible(false);
    }

    public void setVisible(boolean flag) {
        if (flag) {
            glfwShowWindow(window);
        } else {
            glfwHideWindow(window);
        }
        isVisible = flag;
    }

    public void setKeyCallBack(GLFWKeyCallbackI cbfun) {
        glfwSetKeyCallback(window, cbfun);
    }

    public void setWindowSizeCallBack(GLFWWindowSizeCallbackI cbfun) {
        glfwSetWindowSizeCallback(window, cbfun);
    }

    public void setMouseButtonCallBack(GLFWMouseButtonCallbackI cbfun) {
        glfwSetMouseButtonCallback(window, cbfun);
    }

    public void setCursorPosCallBack(GLFWCursorPosCallbackI cbfun) {
        glfwSetCursorPosCallback(window, cbfun);
    }

    public void setScrollCallBack(GLFWScrollCallbackI cbfun) {
        glfwSetScrollCallback(window, cbfun);
    }
}
