package tech.gralerfics.controller;

import tech.gralerfics.controller.wind.*;
import tech.gralerfics.managers.resource.ImageManager;
import tech.gralerfics.persistence.options.GUIConstants;
import tech.gralerfics.persistence.options.OptionsManager;
import tech.gralerfics.persistence.profiles.PlayerManager;
import tech.gralerfics.persistence.profiles.ProfileManager;

import java.awt.*;

public class Main {
    public static Mouse glfwMouse = new Mouse();
    public static Keyboard glfwKeyboard = new Keyboard();
    public static GlfwController glfwCtrl = new GlfwController("OthellooO", GUIConstants.GLFWWINDOW_DEFAULT_HRES, GUIConstants.GLFWWINDOW_DEFAULT_VRES, glfwMouse, glfwKeyboard);
    public static PlayerFrame playerFrame = new PlayerFrame();
    public static ProfileFrame profileFrame = new ProfileFrame();
    public static StatusFrame statusFrame = new StatusFrame();
    public static MainFrame mainFrame = new MainFrame();

    public static void confirmToClose(Window owner) {
        boolean rst = (new ConfirmDialog(owner)).show("Confirm to leave OthellooO?");
        if (rst) {
            System.exit(0);
        }
    }

    /**
     * 程序入口
     */
    public static void main(String[] args) {
        // 初始化管理器
        OptionsManager.loadOption();
        PlayerManager.loadPlayers();
        ProfileManager.loadProfiles();
        ImageManager.init();

        // 主窗口加载
        Main.mainFrame.setVisible(true);

        // GLFW 窗口加载
        glfwCtrl.run();
    }
}
