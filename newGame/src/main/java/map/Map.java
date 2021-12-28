package map;

import java.io.Serializable;

public class Map implements Serializable {

    int[][] map;

    public Map(int dimension){
        MazeGenerator mazeGenerator = new MazeGenerator(dimension);
        mazeGenerator.generateMaze();
        map = mazeGenerator.getMaze();
    }

    public int[][] getMap(){
        return map;
    }
}
