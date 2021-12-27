package tech.gralerfics.domain;

import tech.gralerfics.managers.rendering.RenderManager;

public class Handles {
    private long frameCnt, T1, T2;
    private RenderManager renderPipe1 = new RenderManager();
    private RenderManager renderPipe2 = new RenderManager();
    private RenderManager renderPipe3 = new RenderManager();
    private Camera camera = new Camera();
    private int lastFrame;

    public long getFrameCnt() {
        return frameCnt;
    }

    public void setFrameCnt(long frameCnt) {
        this.frameCnt = frameCnt;
    }

    public long getT1() {
        return T1;
    }

    public void setT1(long t1) {
        T1 = t1;
    }

    public long getT2() {
        return T2;
    }

    public void setT2(long t2) {
        T2 = t2;
    }

    public RenderManager getRenderPipe1() {
        return renderPipe1;
    }

    public void setRenderPipe1(RenderManager renderPipe1) {
        this.renderPipe1 = renderPipe1;
    }

    public RenderManager getRenderPipe2() {
        return renderPipe2;
    }

    public void setRenderPipe2(RenderManager renderPipe2) {
        this.renderPipe2 = renderPipe2;
    }

    public RenderManager getRenderPipe3() {
        return renderPipe3;
    }

    public void setRenderPipe3(RenderManager renderPipe3) {
        this.renderPipe3 = renderPipe3;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public int getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(int lastFrame) {
        this.lastFrame = lastFrame;
    }
}
