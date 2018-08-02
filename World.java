//Most recent upload
package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.util.Random;

public class World extends Game {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static TERenderer ter = new TERenderer();
    //Player coordinates
    int playerX;
    int playerY;
    static int getFlower = 0;
    static int zombies = 10;
    TETile[][] world = new TETile[WIDTH][HEIGHT];
    long seed;
    String fatWASDstring;
    public static int initialx;
    public static int initialy;

    private void base() {
        ter.initialize(WIDTH, HEIGHT);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void room(Random randomseed) {
        //Amount of rooms
        int n = 30;

        while (n > 0) {
            //Creating room sizes, width and height
            int roomw = randomseed.nextInt(5) + 2;
            int roomh = randomseed.nextInt(5) + 1;
            //Creating the coordinates, room's x and y
            int roomx = randomseed.nextInt(WIDTH - 2 - roomw) + 1;
            int roomy = randomseed.nextInt(HEIGHT - 2 - roomh) + 1;
            for (int x1 = roomx; x1 < roomw + roomx - 1; x1 += 1) {
                for (int y1 = roomy; y1 < roomh + roomy; y1 += 1) {
                    world[x1][y1] = Tileset.FLOOR;
                }
            }
            n--;
        }
    }

    private void halls() {
        int range = 15;
        for (int x = 0; x < WIDTH - 1; x++) {
            for (int y = 0; y < HEIGHT - 1; y++) {
                //looking to see if its on the edge of a room
                if (world[x][y] == Tileset.FLOOR) {
                    if ((world[x + 1][y] == Tileset.NOTHING
                            && world[x][y + 1] == Tileset.NOTHING)) {
                        //upper right corner tile coordinates
                        int room1x = x;
                        int room1y = y;
                        //checking if another room is within a range of tiles UPWARDS right
                        //increment x1 until it hits a new room
                        for (int x1 = x; x1 < range + x + 1 && x1 < WIDTH && x1 > 0; x1++) {
                            //increments y for each x
                            for (int y1 = y; y1 < range + y + 1 && y1 < HEIGHT && y1 > 0; y1++) {
                                if (world[x1][y1] == Tileset.FLOOR) {
                                    int room2x = x1;
                                    int room2y = y1;
                                    while (room1y < room2y) {
                                        world[room1x][room1y] = Tileset.FLOOR;
                                        room1y++;
                                    }
                                    while (room1x < room2x) {
                                        world[room1x][room2y] = Tileset.FLOOR;
                                        room1x++;
                                    }
                                }
                            }
                        }
                        //checking if another room is within a range of tiles DOWNWARDS right
                        for (int x1 = x; x1 < range + x + 1 && x1 < WIDTH && x1 > 0; x1++) {
                            for (int y1 = y; y1 > y - range - 1 && y1 > 0 && y1 > 0; y1--) {
                                if (world[x1][y1] == Tileset.FLOOR) {
                                    int room2x = x1;
                                    int room2y = y1;
                                    while (room1x < room2x) {
                                        world[room1x][room1y] = Tileset.FLOOR;
                                        room1x++;
                                    }
                                    while (room1y > room2y) {
                                        world[room1x][room1y] = Tileset.FLOOR;
                                        room1y--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void walls() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                //check for floor
                if (world[x][y] == Tileset.FLOOR) {
                    //check top left
                    if (world[x - 1][y + 1] == Tileset.NOTHING) {
                        world[x - 1][y + 1] = Tileset.WALL;
                    }
                    //check top
                    if (world[x][y + 1] == Tileset.NOTHING) {
                        world[x][y + 1] = Tileset.WALL;
                    }
                    //check top right
                    if (world[x + 1][y + 1] == Tileset.NOTHING) {
                        world[x + 1][y + 1] = Tileset.WALL;
                    }
                    //check right
                    if (world[x + 1][y] == Tileset.NOTHING) {
                        world[x + 1][y] = Tileset.WALL;
                    }
                    //check bottom right
                    if (world[x + 1][y - 1] == Tileset.NOTHING) {
                        world[x + 1][y - 1] = Tileset.WALL;
                    }
                    //check bottom
                    if (world[x][y - 1] == Tileset.NOTHING) {
                        world[x][y - 1] = Tileset.WALL;
                    }
                    //check bottom left
                    if (world[x - 1][y - 1] == Tileset.NOTHING) {
                        world[x - 1][y - 1] = Tileset.WALL;
                    }
                    //check left
                    if (world[x - 1][y] == Tileset.NOTHING) {
                        world[x - 1][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private void diamonds(Random r) {
        int count = r.nextInt(15) + 20;
        while (true) {
            int x = r.nextInt(49);
            int y = r.nextInt(49);
            if (world[x][y] == Tileset.NOTHING && count > 0) {
                world[x][y] = Tileset.DIAMOND;
                count = count - 1;
                if (count == 0) {
                    break;
                }
            }
        }
    }
    // First x and y where a floor exists
    public int startingtileX() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (world[x][y] == Tileset.FLOOR) {
                    initialx = x;
                    return x;
                }
            }
        }
        return 0;
    }

    public int startingtileY() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (world[x][y] == Tileset.FLOOR) {
                    initialy = y;
                    return y;
                }
            }
        }
        return 0;
    }

    public void player() {
        this.playerX = startingtileX();
        this.playerY = startingtileY();
        world[playerX][playerY] = Tileset.PR;
    }

    //move player up  "W"
    public boolean playerUp() {
        if (world[playerX][playerY + 1] == Tileset.FLOOR) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerY += 1;
            world[playerX][playerY] = Tileset.PR;
            return true;
        }
        if (world[playerX][playerY + 1] == Tileset.WALL) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX = initialx;
            playerY = initialy;
            world[playerX][playerY] = Tileset.PR;
            return true;
        }
        if (world[playerX][playerY + 1] == Tileset.FLOWER) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerY += 1;
            getFlower += 1;
            world[playerX][playerY] = Tileset.PR;
            return true;
        }
        if (world[playerX][playerY + 1] == Tileset.ENEMY) {
            if (getFlower > 5) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerY += 1;
                world[playerX][playerY] = Tileset.PR;
                zombies -= 1;
                if (zombies == 0) {
                    StdDraw.clear();
                    win(seed, watch.TimeRecorder());
                    StdDraw.show();
                    return false;
                }
                return true;
            } else {
                StdDraw.clear();
                gameOver(seed, watch.TimeRecorder());
                StdDraw.show();
                StdDraw.pause(5000);
                return false;
            }
        }
        return true;
    }

    //move player down "S"
    public boolean playerDown() {
        if (world[playerX][playerY - 1] == Tileset.FLOOR) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerY -= 1;
            world[playerX][playerY] = Tileset.PL;
            return true;
        }
        if (world[playerX][playerY - 1] == Tileset.WALL) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX = initialx;
            playerY = initialy;
            world[playerX][playerY] = Tileset.PR;
            return true;
        }
        if (world[playerX][playerY - 1] == Tileset.FLOWER) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerY -= 1;
            getFlower += 1;
            world[playerX][playerY] = Tileset.PL;
            return true;
        }
        if (world[playerX][playerY - 1] == Tileset.ENEMY) {
            if (getFlower > 5) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerY -= 1;
                world[playerX][playerY] = Tileset.PL;
                zombies -= 1;
                if (zombies == 0) {
                    StdDraw.clear();
                    win(seed, watch.TimeRecorder());
                    StdDraw.show();
                    return false;
                }
                return true;
            } else {
                StdDraw.clear();
                gameOver(seed, watch.TimeRecorder());
                StdDraw.show();
                StdDraw.pause(5000);
                return false;
            }
        }
        return true;
    }

    //move player right "D"
    public boolean playerRight() {
        if (world[playerX + 1][playerY] == Tileset.FLOOR) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX += 1;
            world[playerX][playerY] = Tileset.PR;
            return true;
        }
        if (world[playerX + 1][playerY] == Tileset.WALL) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX = initialx;
            playerY = initialy;
            world[playerX][playerY] = Tileset.PR;
            return true;
        }
        if (world[playerX + 1][playerY] == Tileset.FLOWER) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX += 1;
            getFlower += 1;
            world[playerX][playerY] = Tileset.PR;
            return true;
        }
        if (world[playerX + 1][playerY] == Tileset.ENEMY) {
            if (getFlower > 5) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerX += 1;
                world[playerX][playerY] = Tileset.PR;
                zombies -= 1;
                if (zombies == 0) {
                    StdDraw.clear();
                    win(seed, watch.TimeRecorder());
                    StdDraw.show();
                    return false;
                }
                return true;
            } else {
                StdDraw.clear();
                gameOver(seed, watch.TimeRecorder());
                StdDraw.show();
                StdDraw.pause(5000);
                return false;
            }
        }
        return true;
    }

    //move player left "A"
    public boolean playerLeft() {
        if (world[playerX - 1][playerY] == Tileset.FLOOR) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX -= 1;
            world[playerX][playerY] = Tileset.PL;
            return true;
        }
        if (world[playerX - 1][playerY] == Tileset.WALL) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX = initialx;
            playerY = initialy;
            world[playerX][playerY] = Tileset.PR;
            return true;
        }
        if (world[playerX - 1][playerY] == Tileset.FLOWER) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX -= 1;
            getFlower += 1;
            world[playerX][playerY] = Tileset.PL;
            return true;
        }
        if (world[playerX - 1][playerY] == Tileset.ENEMY) {
            if (getFlower > 5) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerX -= 1;
                world[playerX][playerY] = Tileset.PL;
                zombies -= 1;
                if (zombies == 0) {
                    StdDraw.clear();
                    win(seed, watch.TimeRecorder());
                    StdDraw.show();
                    return false;
                }
                return true;
            } else {
                StdDraw.clear();
                gameOver(seed, watch.TimeRecorder());
                StdDraw.show();
                StdDraw.pause(5000);
                return false;
            }
        }
        return true;
    }

    public void zombieSpawn(Random r) {
        int count = zombies;
        while (true) {
            int x = r.nextInt(49);
            int y = r.nextInt(49);
            if (world[x][y] == Tileset.FLOOR && count > 0) {
                world[x][y] = Tileset.ENEMY;
                count = count - 1;
                if (count == 0) {
                    break;
                }
            }
        }
    }
    public void flowers(Random r) {
        int count = r.nextInt(15) + 15;
        while (true) {
            int x = r.nextInt(49);
            int y = r.nextInt(49);
            if (world[x][y] == Tileset.FLOOR && count > 0) {
                world[x][y] = Tileset.FLOWER;
                count = count - 1;
                if (count == 0) {
                    break;
                }
            }
        }
    }

    public TETile[][] finalWorldgenerator(Random seedRandomizer) {
        base();
        room(seedRandomizer);
        halls();
        walls();
        diamonds(seedRandomizer);
        player();
        zombieSpawn(seedRandomizer);
        flowers(seedRandomizer);
        return world;
    }
}
