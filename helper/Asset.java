package helper;

import java.awt.*;
import java.awt.Image;
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
}