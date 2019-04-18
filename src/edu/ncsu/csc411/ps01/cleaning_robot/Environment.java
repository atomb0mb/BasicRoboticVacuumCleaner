package edu.ncsu.csc411.ps01.cleaning_robot;

/**
 * The world in which this simulation exists. As a base world, this produces a
 * 10x10 room of DIRTY tiles. In addition, 20% of the room is covered with
 * "walls" (tiles marked as IMPASSABLE).
 *
 * This object will allow the agent to explore the world and is how the agent
 * will retrieve information about neighboring tiles. DO NOT MODIFY.
 *
 * @author Adam Gaweda
 */
public class Environment {
    private final Tile[][] tiles;
    private final int      row, cols;

    public Environment () {
        this( 10, 10 );
    }

    public Environment ( final int rows, final int columns ) {
        // Add 2 to create exterior walls
        this.row = rows;
        this.cols = columns;
        tiles = new Tile[row][cols];
        for ( int i = 0; i < row; i++ ) {
            for ( int j = 0; j < cols; j++ ) {
                tiles[i][j] = new Tile( TileStatus.DIRTY );
            }
        }
        final int numWalls = (int) ( Math.random() * ( row * columns * .2 ) );
        for ( int i = 0; i < numWalls; i++ ) {
            final int x = (int) ( Math.random() * row );
            final int y = (int) ( Math.random() * cols );
            tiles[x][y] = new Tile( TileStatus.IMPASSABLE );
        }

        // for ( int i = 1; i < 9; i++ ) {
        // for ( int j = 0; j < 3; j++ ) {
        // tiles[i][j] = new Tile( TileStatus.IMPASSABLE );
        // }
        // }
        // tiles[1][3] = new Tile( TileStatus.CLEAN );
        // tiles[1][4] = new Tile( TileStatus.CLEAN );
        //
        // tiles[3][3] = new Tile( TileStatus.CLEAN );
        // tiles[3][4] = new Tile( TileStatus.CLEAN );
        //
        // tiles[6][3] = new Tile( TileStatus.CLEAN );
        // tiles[6][4] = new Tile( TileStatus.CLEAN );
        //
        // tiles[8][3] = new Tile( TileStatus.CLEAN );
        // tiles[8][4] = new Tile( TileStatus.CLEAN );

    }

    /* Traditional Getters */
    public Tile[][] getTiles () {
        return tiles;
    }

    public int getRows () {
        return this.row;
    }

    public int getCols () {
        return this.cols;
    }

    /*
     * Returns a 3x3 array of tile statuses, centered around the agent (which is
     * located at index [1][1]).
     */
    public Tile[][] getNeighborTiles ( final Robot robot ) {
        final Tile[][] neighbors = new Tile[3][3];
        final int roboX = robot.getPosX();
        final int roboY = robot.getPosY();
        int currentX = 0;
        int currentY = 0;

        for ( int i = -1; i <= 1; i++ ) {
            for ( int j = -1; j <= 1; j++ ) {
                final int X = roboX + i;
                final int Y = roboY + j;
                if ( X < 0 || X > row - 1 || Y < 0 || Y > cols - 1 ) {
                    neighbors[currentX][currentY] = new Tile( TileStatus.IMPASSABLE );
                }
                else {
                    neighbors[currentX][currentY] = tiles[X][Y];
                }
                currentY++;
                currentY = currentY % 3;
            }
            currentX++;
            currentX = currentX % 3;
        }

        return neighbors;
    }

    /* Cleans the tile at coordinate [x][y] */
    public void cleanTile ( final int x, final int y ) {
        tiles[x][y].cleanTile();
    }

    /* Counts number of cleaned tiles */
    public int getNumCleanedTiles () {
        int count = 0;
        for ( int i = 0; i < row; i++ ) {
            for ( int j = 0; j < cols; j++ ) {
                if ( this.tiles[i][j].getStatus() == TileStatus.CLEAN ) {
                    count++;
                }
            }
        }
        return count;
    }

    /* Counts number of tiles that are not walls */
    public int getNumTiles () {
        int count = 0;
        for ( int i = 0; i < row; i++ ) {
            for ( int j = 0; j < cols; j++ ) {
                if ( this.tiles[i][j].getStatus() != TileStatus.IMPASSABLE ) {
                    count++;
                }
            }
        }
        return count;
    }

    /* Returns a ratio of the number of cleaned tiles */
    public double getPerformanceMeasure () {
        return ( 1.0 * getNumCleanedTiles() ) / getNumTiles();
    }

    /*
     * Determines if a particular [x][y] coordinate is within the boundaries of
     * the environment. This is a rudimentary "collision detection" to ensure
     * the agent does not walk outside the world (or through walls).
     */
    public boolean validPos ( final int x, final int y ) {
        return x >= 0 && x < row && y >= 0 && y < cols && tiles[x][y].getStatus() != TileStatus.IMPASSABLE;
    }
}
