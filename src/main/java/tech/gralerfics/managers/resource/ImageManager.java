package tech.gralerfics.managers.resource;

import tech.gralerfics.controller.Main;
import tech.gralerfics.controller.wind.TipDialog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageManager {
    public static String path = "assets/guiimages/";

    public static BufferedImage MainFrameBackgroundImage;

    public static void init() {
        try {
            MainFrameBackgroundImage = ImageIO.read(new File(path + "B.png"));
        } catch (IOException e) {
            (new TipDialog(Main.mainFrame)).show("Background image missing!");
        }
    }
}
