package tech.gralerfics.controller;

import org.lwjgl.glfw.GLFWKeyCallbackI;

public class Keyboard {
    private boolean isShiftDown, isCtrlDown;
    private boolean isCommanding = false;
    private StringBuilder command = new StringBuilder();
    private GLFWKeyCallbackI glfwKeyCallback;

    public boolean isShiftDown() {
        return isShiftDown;
    }

    public void setShiftDown(boolean shiftDown) {
        isShiftDown = shiftDown;
    }

    public boolean isCtrlDown() {
        return isCtrlDown;
    }

    public void setCtrlDown(boolean ctrlDown) {
        isCtrlDown = ctrlDown;
    }

    public boolean isIsCommanding() {
        return isCommanding;
    }

    public void setIsCommanding(boolean isCommanding) {
        this.isCommanding = isCommanding;
    }

    public StringBuilder getCommand() {
        return command;
    }

    public void setCommand(StringBuilder command) {
        this.command = command;
    }

    public GLFWKeyCallbackI getGlfwKeyCallback() {
        return glfwKeyCallback;
    }

    public void setGlfwKeyCallback(GLFWKeyCallbackI glfwKeyCallback) {
        this.glfwKeyCallback = glfwKeyCallback;
    }
}
