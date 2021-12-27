package map;

public class Map {

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
