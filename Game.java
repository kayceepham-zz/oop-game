package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Game {
    //    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private static TERenderer ter = new TERenderer();
    Random seedRandomizer;
    long seedValue;
    String fatWASDstring = "";

    private long seedKeeper(String input) {
        String seedVal = "";
        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            if (x >= 48 && x < 58) {
                seedVal += (x + "");

            }
        }
        seedValue = Long.parseLong(seedVal);
        return seedValue;
    }
    //reads the seed value and turns it into a randomizer
    public Random seedMaker(String input) {
        String seeD = "";
        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            if (x >= 48 && x < 58) {
                seeD += (x + "");

            }
        }
        seedValue = Long.parseLong(seeD);
        return new Random(seedValue);
    }

    //reads the movement keys
    private String[] stringReader(String input) {
        String[] movementString = new String[input.length()];
        int index = 0;
        boolean s = true;
        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            //if the seed is AWSD
            if (x == 65 || x == 97 || x == 87 || x == 119 || x == 83
                    || x == 115 || x == 68 || x == 100 || x == 58 || x == 81 || x == 113) {
                if ((x == 83 || x == 115) && s) {
                    s = false;
                } else {
                    movementString[index] = Character.toString(x);
                    index++;
                }
            }
        }
        int notNull = 0;
        for (int j = 0; j < movementString.length; j++) {
            if (movementString[j] != null) {
                notNull += 1;
            }
        }
        String[] movementStringNoNull = new String[notNull];
        for (int j = 0; j < movementStringNoNull.length; j++) {
            movementStringNoNull[j] = movementString[j];
        }

        return movementStringNoNull;
    }

    private void liveKeyReader(World worldObj) {
        //Maximum 100 key movements to win
        String[] sA = new String[1];
        String s;
        int index = 0;
        KeyReader read = new KeyReader();
        while (true) {
            mouseRead(worldObj, watch.TimeRecorder());
            if (StdDraw.hasNextKeyTyped()) {
                //Single character string of typed input
                s = Character.toString(StdDraw.nextKeyTyped());
                //records the player movement into a single array
                sA[index] = s;
                boolean b = read.reader(sA, worldObj);
                worldObj.fatWASDstring += s;
                if (s.equals("q") || s.equals("Q") || !b) {
                    break;
                }
                ter.renderFrame(worldObj.world);
            }
        }
    }
    private long seed;
    private Random nSeedRead() {
        String s;
        String seedString = "";
        StdDraw.pause(100);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                s = Character.toString(StdDraw.nextKeyTyped());
                //checks to see if it is a number and begins to read numbers for the seed
                if (s.charAt(0) >= 48 && s.charAt(0) < 58) {
                    //continues to loop until we hit "s"
                    seedString += s;
                    while (true) {
                        drawFrame(seedString);
                        if (StdDraw.hasNextKeyTyped()) {
                            //reads the next characters with each while loop
                            s = Character.toString(StdDraw.nextKeyTyped());
                            if (s.equals("s") || s.equals("S")) {
                                StdDraw.pause(100);
                                gameStart();
                                break;
                            }
                            StdDraw.pause(10);
                            seedString += s;
                        }

                    }
                    this.seed = seedKeeper(seedString);
                    return seedMaker(seedString);
                }
            }
        }
    }

    //Creating the canvas
    public void gameInterface() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    //Starting menu
    public void drawMenu() {
        //Take the string and display it in the center of the screen
        //If game is not over, display relevant game information at the top of the screen
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.text(25 * 16, 40 * 16, "CS61B: THE GAME");
        Font smallfont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(smallfont);
        StdDraw.text(25 * 16, 30 * 16, "New Game (N)");
        StdDraw.text(25 * 16, 28 * 16, "Load Game (L)");
        StdDraw.text(25 * 16, 26 * 16, "Quit (Q)");
        Font smallerfont = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(smallerfont);
        StdDraw.text(25 * 16, 23 * 16, "=== R u l e s ===");
        StdDraw.text(25 * 16, 22 * 16, "1. Don't touch zombies until you eat at least 6 flowers, otherwise you'll lose.");
        StdDraw.text(25 * 16, 21 * 16, "2. Touching walls will put you back at the start.");
        StdDraw.text(25 * 16, 20 * 16, "3. Eat all 10 zombies to win. Have fun!");
        StdDraw.show();
    }

    //TYPE IN YOUR SEED Screen to pop up after clicking the starting 'n'
    private void afterNmenu() {
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.text(25 * 16, 40 * 16, "TYPE IN YOUR SEED");
        StdDraw.show();
    }

    //GAME LOADING Screen to pop up after clicking the starting 's'
    private void gameStart() {
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.text(25 * 16, 40 * 16, "GAME LOADING...");
        StdDraw.show();
    }

    //Drawing method, used to display seed input
    public void drawFrame(String s) {
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(25 * 16, 40 * 16, s);
        StdDraw.text(25 * 16, 35 * 16, "Type in S after you are done typing the seed.");
        StdDraw.show();
    }

    //indicate that the game has been saved
    public void gameSaved(String time) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(25 * 16, 35 * 16, "Congrats! Your game is saved.");
        StdDraw.text(25 * 16, 15 * 16, time);
        StdDraw.show();
    }
    public void gameOver(long seed, String time) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(25 * 16, 28 * 16, "YOU DIED!(ೆ௰ೆ๑)");
        Font font2 = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font2);
        StdDraw.text(25 * 16, 25 * 16, "Try seed " + seed + " again!");
        StdDraw.text(25 * 16, 20 * 16, time);
        StdDraw.show();
    }
    public void win(long seed, String time) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.CYAN);
        StdDraw.text(25 * 16, 28 * 16, "YOU WIN!ヾ(´▽｀*)ﾉ☆");
        Font font2 = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font2);
        StdDraw.text(25 * 16, 25 * 16, "Seed defeated: " + seed);
        StdDraw.text(25 * 16, 20 * 16, time);
        StdDraw.show();
    }
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */

    public static Time watch = new Time();
    public void playWithKeyboard() {
        gameInterface();
        drawMenu();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String potentialNorL = Character.toString(StdDraw.nextKeyTyped());
                if (potentialNorL.equals("n") || potentialNorL.equals("N")) {
                    afterNmenu();
                    StdDraw.pause(100);
                    Random seedRandom = nSeedRead();
                    StdDraw.pause(100);
                    //Empty world
                    World worldObj = new World();
                    //Saving seed from our static seed
                    worldObj.seed = seed;
                    //Creates TETile[][] with the given randomizer
                    worldObj.world = worldObj.finalWorldgenerator(seedRandom);
                    ter.renderFrame(worldObj.world);
                    //Pause and then wait for WASD inputs
                    StdDraw.pause(100);
                    watch.start();
                    while (true) {
//                        mouseRead(worldObj, watch.TimeRecorder());
                        liveKeyReader(worldObj);
                        worldObj.fatWASDstring = this.fatWASDstring;
                    }
                }

                if (potentialNorL.equals("l") || potentialNorL.equals("L")) {
                    CurrentGame loadGame = new CurrentGame();
                    loadGame.loadingGame();
                }
            }
        }
    }
    public void mouseRead(World w, String time) {
        while (true) {
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            ter.renderFrame(w.world);
            if (x <= 49 && y <= 49) {
                TETile mouseTile = w.world[x][y];
                Font font = new Font("Monaco", Font.BOLD, 10);
                StdDraw.setFont(font);
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(5, HEIGHT + 0.5, time);
                StdDraw.text(47, HEIGHT + 0.5, "Flower count: " + w.getFlower);
                StdDraw.text(40, HEIGHT + 0.5, "Zombies remaining: " + w.zombies + " |");
                if (mouseTile.equals(Tileset.FLOOR)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Grass Floor: We love to frolick upon nature!");
                }
                if (mouseTile.equals(Tileset.WALL)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Wall: Built from logs.");
                }
                if (mouseTile.equals(Tileset.NOTHING)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Stone: Simple as that.");
                }
                if (mouseTile.equals(Tileset.ENEMY)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Zombie: Raaaahhhh.");
                }
                if (mouseTile.equals(Tileset.FLOWER)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Flower: Cute lil' red petals!");
                }
                if (mouseTile.equals(Tileset.PR) || mouseTile.equals(Tileset.PL)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Player: His name is Steven.");
                }
                if (mouseTile.equals(Tileset.DIAMOND)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Diamond : A valuable entity!");
                }
                if (mouseTile.equals(null)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Player: His name is Steven.");
                }
                StdDraw.show();
            }
            break;
        }
    }
    public void playWithKeyboardAfterLoad(World worldObj) {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                liveKeyReader(worldObj);
                worldObj.fatWASDstring = this.fatWASDstring;
            }
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    public TETile[][] playWithInputString(String input) {
        if (input.charAt(0) == 'l') {
            byte[] bytes = new byte[0];
            try {
                bytes = Files.readAllBytes(Paths.get("saved_seed.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            long oldSeedLong = seedKeeper(new String(bytes, StandardCharsets.UTF_8));
            String[] oldSeedArray = stringReader(new String(bytes, StandardCharsets.UTF_8));
            String oldSeed = oldSeedLong + String.join("", oldSeedArray);
            input = oldSeed + input.substring(1, input.length());
        }

        return internalPlayWithInputString(input).world;
    }

    public World internalPlayWithInputString(String input) {
        ter.initialize(50, 50);
        seedRandomizer = this.seedMaker(input);
        String[] stringKeys = stringReader(input);
        World worldObj = new World();
        worldObj.world = worldObj.finalWorldgenerator(seedRandomizer);
        worldObj.seed = seedKeeper(input);
        KeyReader kr = new KeyReader();
        kr.reader(stringKeys, worldObj);
        System.out.println(worldObj.playerX + "&" + worldObj.playerY);
        ter.renderFrame(worldObj.world);
        return worldObj;
    }
}
