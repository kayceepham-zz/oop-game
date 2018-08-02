package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

public class KeyReader extends World {

    //reading the keys for AWSD
    public boolean reader(String[] stringKeys, World worldObj) {
        int i = 0;
        boolean b = true;
        while (i < stringKeys.length && b) {
            String input = stringKeys[i];
            if (input != null) {
                if (input.equals("W") || input.equals("w")) {
                    b = worldObj.playerUp();
                }
                if (input.equals("A") || input.equals("a")) {
                    b = worldObj.playerLeft();
                }
                if (input.equals("S") || input.equals("s")) {
                    b = worldObj.playerDown();
                }
                if (input.equals("D") || input.equals("d")) {
                    b = worldObj.playerRight();
                }
                //Deserialize
                if (input.equals("L") || input.equals("l")) {
                    CurrentGame loadGame = new CurrentGame();
                    loadGame.loadingGame();
                }
                //Saving or serializing
                if (input.equals("q") || (input.equals("Q"))) {
                    CurrentGame gameSaved = new CurrentGame(worldObj);
                    gameSaved.savingGame();
                    StdDraw.clear();
                    gameSaved(watch.TimeRecorder());
                    StdDraw.show();
                    StdDraw.pause(1000000000);
                    break;
                }
            }
            i++;
        }
        return b;
    }
}

