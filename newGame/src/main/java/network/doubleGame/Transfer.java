package network.doubleGame;

import thing.Thing;

public class Transfer {

    public static String inTransfer(Thing[][] things, int live0, int live1){
        char[] thingChar = new char[things.length * things[0].length];
        int k = 0;
        for (int i = 0; i < things.length; i++){
            for (int j = 0; j < things[0].length; j++){
                thingChar[k++] = things[i][j].getSign();
            }
        }
        return "DATA:"+String.valueOf(thingChar)+ live0 +live1;
    }

    public static char[][] outTransferThings(String data){
        char[][] things = new char[25][25];
        int k = 0;
        for (int i = 0; i < 25; i ++){
            for (int j = 0; j < 25; j++){
                things[i][j] = data.charAt(k++);
            }
        }
        return things;
    }

    /**
     * 解析出生命值
     * @param s
     * @return 数组下标对应角色生命值
     */
    public static int[] outTransferLive(String s){
        return new int[]{Integer.parseInt(s.substring(0, 1)),
                Integer.parseInt(s.substring(1, 2))};
    }
}
