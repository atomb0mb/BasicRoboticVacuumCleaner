package edu.ncsu.csc411.ps01.cleaning_robot;

import java.util.Random;

/**
 * Represents a simple-reflex agent cleaning a particular room. At all times the
 * robot has a direction in the environment. The robot only has one sensor - the
 * status of all its neighboring tiles, including the one it is currently on
 * (located at [1][1]).
 *
 * Your task is to modify the getAction method below to produce better coverage.
 */

public class Robot {
    private final Environment env;
    private int               posX;
    private int               posY;

    /**
     * Initializes a Robot on a specific tile in the environment. The robot
     * facing to the right.
     */
    public Robot ( final Environment env ) {
        this.env = env;
        this.posX = 0;
        this.posY = 0;
    }

    public int getPosX () {
        return posX;
    }

    public int getPosY () {
        return posY;
    }

    public void incPosX () {
        posX++;
    }

    public void decPosX () {
        posX--;
    }

    public void incPosY () {
        posY++;
    }

    public void decPosY () {
        posY--;
    }

    /**
     * Simulate the passage of a single time-step. At each time-step, the Robot
     * decides whether to clean the current tile or move tiles.
     */
    public Action getAction () {
        // Gets a 3x3 array of tile statuses, centered around
        // the robot.
        final Tile[][] tiles = env.getNeighborTiles( this );
        // System.out.println(
        // "Current status: " + tiles[1][1].getStatus() + " Coordinate: (" +
        // this.posX + ", " + this.posY + ")" );
        // In first run if time-step = 1, the tile happens to be an obstacle
        // then move right as the first direction.
        if ( tiles[1][1].getStatus() == TileStatus.IMPASSABLE ) {
            return Action.MOVE_RIGHT;
        }

        // tiles[1][1] is the tile the robot is currently
        // standing on.
        if ( tiles[1][1].getStatus() == TileStatus.DIRTY ) {
            return Action.CLEAN;
        }
        // Modify the code below to get better coverage.
        else if ( tiles[1][1].getStatus() == TileStatus.CLEAN ) {

            if ( tiles[1 + 1][1].getStatus() != TileStatus.IMPASSABLE
                    && tiles[1 + 1][1].getStatus() == TileStatus.DIRTY ) {
                return Action.MOVE_RIGHT;
            }
            else if ( tiles[1][1 + 1].getStatus() != TileStatus.IMPASSABLE
                    && tiles[1][1 + 1].getStatus() == TileStatus.DIRTY ) {
                return Action.MOVE_DOWN;
            }
            else if ( tiles[1 - 1][1].getStatus() != TileStatus.IMPASSABLE
                    && tiles[1 - 1][1].getStatus() == TileStatus.DIRTY ) {
                return Action.MOVE_LEFT;
            }
            else if ( tiles[1][1 - 1].getStatus() != TileStatus.IMPASSABLE
                    && tiles[1][1 - 1].getStatus() == TileStatus.DIRTY ) {
                return Action.MOVE_UP;
            }
            else {
                boolean flag = false;
                while ( !flag ) {
                    final Random rn = new Random();
                    final int range = 4 - 1 + 1;
                    final int randomNum = rn.nextInt( range ) + 1;
                    if ( randomNum == 1 && tiles[1 + 1][1].getStatus() != TileStatus.IMPASSABLE ) {
                        return Action.MOVE_RIGHT;
                    }
                    else if ( randomNum == 2 && tiles[1][1 + 1].getStatus() != TileStatus.IMPASSABLE ) {
                        return Action.MOVE_DOWN;
                    }
                    else if ( randomNum == 3 && tiles[1 - 1][1].getStatus() != TileStatus.IMPASSABLE ) {
                        return Action.MOVE_LEFT;
                    }
                    else if ( randomNum == 4 && tiles[1][1 - 1].getStatus() != TileStatus.IMPASSABLE ) {
                        return Action.MOVE_UP;
                    }
                    else {
                        flag = false;
                    }
                }
            }
        }

        return Action.DO_NOTHING;
    }
}
