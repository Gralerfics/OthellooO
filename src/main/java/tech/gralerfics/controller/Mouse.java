package tech.gralerfics.controller;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

public class Mouse {
    private float x, y;
    private float lastX, lastY;
    private float lastDownX, lastDownY;
    private boolean isLeftButtonDown, isRightButtonDown;

    private int selected, lastSelected;

    private GLFWScrollCallbackI glfwScrollCallback;
    private GLFWCursorPosCallbackI glfwCursorPosCallback;
    private GLFWMouseButtonCallbackI glfwMouseButtonCallback;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public float getLastDownX() {
        return lastDownX;
    }

    public void setLastDownX(float lastDownX) {
        this.lastDownX = lastDownX;
    }

    public float getLastDownY() {
        return lastDownY;
    }

    public void setLastDownY(float lastDownY) {
        this.lastDownY = lastDownY;
    }

    public boolean isLeftButtonDown() {
        return isLeftButtonDown;
    }

    public void setLeftButtonDown(boolean leftButtonDown) {
        isLeftButtonDown = leftButtonDown;
    }

    public boolean isRightButtonDown() {
        return isRightButtonDown;
    }

    public void setRightButtonDown(boolean rightButtonDown) {
        isRightButtonDown = rightButtonDown;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getLastSelected() {
        return lastSelected;
    }

    public void setLastSelected(int lastSelected) {
        this.lastSelected = lastSelected;
    }

    public GLFWScrollCallbackI getGlfwScrollCallback() {
        return glfwScrollCallback;
    }

    public void setGlfwScrollCallback(GLFWScrollCallbackI glfwScrollCallback) {
        this.glfwScrollCallback = glfwScrollCallback;
    }

    public GLFWCursorPosCallbackI getGlfwCursorPosCallback() {
        return glfwCursorPosCallback;
    }

    public void setGlfwCursorPosCallback(GLFWCursorPosCallbackI glfwCursorPosCallback) {
        this.glfwCursorPosCallback = glfwCursorPosCallback;
    }

    public GLFWMouseButtonCallbackI getGlfwMouseButtonCallback() {
        return glfwMouseButtonCallback;
    }

    public void setGlfwMouseButtonCallback(GLFWMouseButtonCallbackI glfwMouseButtonCallback) {
        this.glfwMouseButtonCallback = glfwMouseButtonCallback;
    }
}
