package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile PLAYER(boolean right) {
        if (right) {
            return new TETile('S', Color.black, Color.black, "playerRight", "playerright.png");
        } else {
            return new TETile('S', Color.black, Color.black, "playerLight", "playerleft.png");
        }
    }
    public static final TETile PR = new TETile('S', Color.black, Color.black, "playerR", "playerright.png");
    public static final TETile PL = new TETile('S', Color.black, Color.black, "playerL", "playerleft.png");
    public static final TETile ENEMY = new TETile('S', Color.black, Color.black, "player", "enemyright.png");
    public static final TETile WALL = new TETile('|', new Color(216, 128, 128), Color.darkGray,
            "wall", "wall.png");
    public static final TETile FLOOR = new TETile('·', Color.black, Color.black, "floor", "grass.jpeg");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing", "stone.jpg");
    public static final TETile DIAMOND = new TETile('"', Color.green, Color.black, "diamond", "diamond.png");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower", "flower.png");
}


