package tech.gralerfics.managers.rendering;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderManager {
    private int FBO = 0, VAO, VBO;

    public ArrayList<Integer> cAtt = new ArrayList<>();
    public ShaderManager shaderProgram = new ShaderManager();

    /**
     * 初始化管线缓存
     */
    public void initBuffer(boolean finalPass) {
        if (!finalPass) FBO = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);

        float[] square = new float[]{
                -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, -1.0f, 0.0f
        };
        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, square, GL_STATIC_DRAW);

        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, (long)0);
        glEnableVertexAttribArray(0);

        if (!finalPass) {
            int[] att = new int[cAtt.size()];
            for (int i = 0; i < cAtt.size(); i ++) {
                glBindTexture(GL_TEXTURE_2D, cAtt.get(i));
                glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, cAtt.get(i), 0);
                att[i] = GL_COLOR_ATTACHMENT0 + i;
            }
            glDrawBuffers(att);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * 传入前置管线附件并渲染指定管线
     */
    public void render(ArrayList<Integer> texturePass) {
        shaderProgram.use();
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);
        glBindVertexArray(VAO);

        for (int i = 0; i < texturePass.size(); i ++) {
            glActiveTexture(GL_TEXTURE0 + i);
            glBindTexture(GL_TEXTURE_2D, texturePass.get(i));
            shaderProgram.setFloat("tp" + i, i);
        }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        glBindVertexArray(0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glUseProgram(0);
    }

    /**
     * 生成传递数据用纹理
     */
    public static int genRGB32F(int width, int height) {
        int rst = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, rst);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, width, height, 0, GL_RGBA, GL_FLOAT, (float[]) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        return rst;
    }
}
