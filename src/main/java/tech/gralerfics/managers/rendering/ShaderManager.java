package tech.gralerfics.managers.rendering;

import tech.gralerfics.controller.Main;
import tech.gralerfics.controller.wind.TipDialog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL43.GL_PROGRAM;

/**
 * 着色器管理类
 */
public class ShaderManager {
    private HashMap<Integer, String> shaderInfo = new HashMap<>();
    private int program = -1;
    private boolean loaded = false;

    private final String PATH = "assets\\shaders\\";
    private final boolean outputCompilerMessagesOn = false;

    public ShaderManager() {}

    public int getProgram() {
        return program;
    }
    public void setProgram(int program) {
        this.program = program;
        this.loaded = false;
    }
    public String getShader(int type) {
        return shaderInfo.get(type);
    }
    public void addShader(int type, String fileName) {
        this.loaded = false;
        shaderInfo.put(type, fileName);
    }
    public void delShader(int type) {
        this.loaded = false;
        shaderInfo.remove(type);
    }

    private void checkCompileErrors(int shader, int type) {
        if (!outputCompilerMessagesOn) return;
        System.out.println("*---------------------------------------------------------------------------*");
        switch (type) {
            case GL_VERTEX_SHADER:
                System.out.println("| Vertex Shader Compilation Information                                     |");
                break;
            case GL_FRAGMENT_SHADER:
                System.out.println("| Fragment Shader Compilation Information                                   |");
                break;
            case GL_PROGRAM:
                System.out.println("| Shader Program Compilation Information                                    |");
                break;
        }
        System.out.println("*---------------------------------------------------------------------------*");
        System.out.println(glGetShaderInfoLog(shader));
    }

    public void reload() {
        if (program != -1) glDeleteProgram(program);

        try {
            if (Objects.equals(shaderInfo.get(GL_VERTEX_SHADER), "")) throw new Exception("未提供顶点着色器.");
            if (Objects.equals(shaderInfo.get(GL_FRAGMENT_SHADER), "")) throw new Exception("未提供片元着色器.");
        } catch (Exception e) {
            (new TipDialog(Main.mainFrame)).show("Shaders missing! OthellooO is closing...");
            System.exit(0);
        }
        String vertexShaderFileName = shaderInfo.get(GL_VERTEX_SHADER);
        String fragmentShaderFileName = shaderInfo.get(GL_FRAGMENT_SHADER);
        String vertexShaderCode = "";
        String fragmentShaderCode = "";

        try {
            BufferedReader fin;
            int peek;
            StringBuilder sin;

            fin = new BufferedReader(new FileReader(PATH + vertexShaderFileName));
            sin = new StringBuilder();
            while ((peek = fin.read()) != -1) sin.append((char) peek);
            fin.close();
            vertexShaderCode = sin.toString();

            fin = new BufferedReader(new FileReader(PATH + fragmentShaderFileName));
            sin = new StringBuilder();
            while ((peek = fin.read()) != -1) sin.append((char) peek);
            fin.close();
            fragmentShaderCode = sin.toString();
        } catch (Exception e) {
            (new TipDialog(Main.mainFrame)).show("Shader codes missing! OthellooO is closing...");
            System.exit(0);
        }

        if (vertexShaderCode.isEmpty())  throw new RuntimeException("Failed to load vertex shader codes.");
        if (fragmentShaderCode.isEmpty())  throw new RuntimeException("Failed to load fragment shader codes.");

        int vertexShader, fragmentShader;
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertexShader, vertexShaderCode);
        glShaderSource(fragmentShader, fragmentShaderCode);
        glCompileShader(vertexShader);
        glCompileShader(fragmentShader);
        checkCompileErrors(vertexShader, GL_VERTEX_SHADER);
        checkCompileErrors(fragmentShader, GL_FRAGMENT_SHADER);

        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        checkCompileErrors(program, GL_PROGRAM);
        this.loaded = true;

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void use() {
        try {
            if ( !loaded ) throw new Exception("尚未绑定或更新绑定着色器程序.");
        } catch (Exception e) {
            (new TipDialog(Main.mainFrame)).show("Shader not binded! OthellooO is closing...");
            System.exit(0);
        }
        glUseProgram(program);
    }

    public void deuse() {
        glUseProgram(0);
    }

    public void setInt(String name, int value) {
        use();
        glUniform1i(glGetUniformLocation(program, name), value);
    }

    public void setBoolean(String name, boolean value) {
        setInt(name, (value) ? 1 : 0);
    }

    public void setFloat(String name, float value) {
        use();
        glUniform1f(glGetUniformLocation(program, name), value);
    }

    public void setFloats(String name, float[] values) { // ?
        use();
        glUniform1fv(glGetUniformLocation(program, name), values);
    }

    public void setVec2f(String name, float value0, float value1) {
        use();
        glUniform2f(glGetUniformLocation(program, name), value0, value1);
    }

    public void setVector3f(String name, float value0, float value1, float value2) {
        use();
        glUniform3f(glGetUniformLocation(program, name), value0, value1, value2);
    }

    public void setVec4f(String name, float value0, float value1, float value2, float value3) {
        use();
        glUniform4f(glGetUniformLocation(program, name), value0, value1, value2, value3);
    }
}
