package helper;

import java.awt.*;
import java.io.File;

public class Asset {
    public static Image getImage(String path) {
        String pathString = System.getProperty("user.dir") + File.separator + "assets" + File.separator + path;
        return Toolkit.getDefaultToolkit().createImage(pathString);
    }

    public static Image getShip(int id) {
        return getImage("Ship" + id + ".png");
    }

    public static Image getAmmo(int id) {
        return getImage("Ship" + id + "_ammo.png");
    }

    public static File getAudioFile(String path) {
        String pathString = System.getProperty("user.dir") + File.separator + "assets/sound" + File.separator + path;
        return new File(pathString);
    }
}