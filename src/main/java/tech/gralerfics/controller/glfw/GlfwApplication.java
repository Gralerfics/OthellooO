package tech.gralerfics.controller.glfw;

import org.lwjgl.opengl.GL;
import tech.gralerfics.controller.GlfwController;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.glClearColor;

public final class GlfwApplication {
    private GlfwWindow window;

    public GlfwApplication(GlfwController glfwCtrl) {
        window = new GlfwWindow(glfwCtrl.getTitle(), glfwCtrl.getHres(), glfwCtrl.getVres());
    }

    public GlfwWindow getWindow() {
        return window;
    }

    public void initCallbacks(GlfwController glfwCtrl) {
        window.setWindowSizeCallBack(window.getGlfwWindowSizeCallback());
        window.setMouseButtonCallBack(glfwCtrl.getMouse().getGlfwMouseButtonCallback());
        window.setCursorPosCallBack(glfwCtrl.getMouse().getGlfwCursorPosCallback());
        window.setScrollCallBack(glfwCtrl.getMouse().getGlfwScrollCallback());
        window.setKeyCallBack(glfwCtrl.getKeyboard().getGlfwKeyCallback());
    }

    public void release() {
        glfwFreeCallbacks(window.getWindow());
        glfwDestroyWindow(window.getWindow());
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        System.exit(0);
    }

    public void run(GlfwController glfwCtrl) {
        GL.createCapabilities();
        glfwCtrl.init();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        while (!glfwWindowShouldClose(window.getWindow())) {
            if (window.isVisible) glfwCtrl.loop();
            glfwPollEvents();
            glfwSwapBuffers(window.getWindow());
        }
        release();
    }
}
