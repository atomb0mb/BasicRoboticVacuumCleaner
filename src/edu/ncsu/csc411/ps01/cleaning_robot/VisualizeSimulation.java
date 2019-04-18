package edu.ncsu.csc411.ps01.cleaning_robot;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * A Visual Guide toward testing whether your robot agent is operating
 * correctly. This visualization will run for 100 time steps, afterwards it will
 * output the number of tiles cleaned, and a percentage of the room cleaned. DO
 * NOT MODIFY.
 *
 * @author Adam Gaweda
 */
public class VisualizeSimulation extends JFrame {
    private static final long      serialVersionUID = 1L;
    private final EnvironmentPanel envPanel;
    private Environment            env;
    private final ArrayList<Robot> robots;
    private final int              numRobots;

    /*
     * Builds the environment; while not necessary for this problem set, this
     * could be modified to allow for different types of environments, for
     * example loading from a file, or creating multiple agents that can
     * communicate/interact with each other.
     */
    public VisualizeSimulation () {
        final Environment env = new Environment();
        numRobots = 1;
        robots = new ArrayList<Robot>();
        for ( int i = 0; i < numRobots; i++ ) {
            final Robot robot = new Robot( env );
            robots.add( robot );
        }
        envPanel = new EnvironmentPanel( env, robots );
        add( envPanel );
    }

    public static void main ( final String[] args ) {
        final JFrame frame = new VisualizeSimulation();

        frame.setTitle( "CSC 411 - Problem Set 01" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.pack();
        // frame.setSize( 1000, 700 );
        frame.setVisible( true );
    }
}

@SuppressWarnings ( "serial" )
class EnvironmentPanel extends JPanel {
    private final Timer            timer;
    private final Environment      env;
    private final ArrayList<Robot> robots;
    public static final int        TILESIZE = 85;

    public EnvironmentPanel ( final Environment env, final ArrayList<Robot> robots ) {
        setPreferredSize( new Dimension( env.getRows() * TILESIZE, env.getCols() * TILESIZE ) );
        this.env = env;
        this.robots = robots;

        // 100 millisecond time steps
        final int timeSteps = 100;
        this.timer = new Timer( timeSteps, new ActionListener() {
            int timeStepCount = 0;

            @Override
            public void actionPerformed ( final ActionEvent e ) {
                updateEnvironment();
                repaint();
                timeStepCount++;
                if ( env.getNumCleanedTiles() == env.getNumTiles() ) {
                    timer.stop();
                    printPerformanceMeasure();
                    System.out.print( "Number of Steps: " + timeStepCount );
                }
                if ( timeStepCount == 200 ) {
                    timer.stop();
                    printPerformanceMeasure();
                    System.out.print( "Number of Steps: " + timeStepCount );
                }
            }

            // Prints the stats of the agent after 100 time steps
            public void printPerformanceMeasure () {
                final int cleanTiles = env.getNumCleanedTiles();
                final int totalTiles = env.getNumTiles();
                System.out.println( "Simulation Complete" );
                System.out.println( "Total Tiles:\t" + totalTiles );
                System.out.println( "Clean Tiles:\t" + cleanTiles );
                System.out.printf( "%% Cleaned:\t%.2f%%\n", ( 100.0 * cleanTiles / totalTiles ) );
            }

            // Gets the new state of the world after robot actions
            public void updateEnvironment () {
                for ( final Robot robot : robots ) {
                    final Action action = robot.getAction();
                    final int x = robot.getPosX();
                    final int y = robot.getPosY();
                    switch ( action ) {
                        case CLEAN:
                            env.cleanTile( x, y );
                            break;
                        case MOVE_DOWN:
                            if ( env.validPos( x, y + 1 ) ) {
                                robot.incPosY();
                            }
                            break;
                        case MOVE_LEFT:
                            if ( env.validPos( x - 1, y ) ) {
                                robot.decPosX();
                            }
                            break;
                        case MOVE_RIGHT:
                            if ( env.validPos( x + 1, y ) ) {
                                robot.incPosX();
                            }
                            break;
                        case MOVE_UP:
                            if ( env.validPos( x, y - 1 ) ) {
                                robot.decPosY();
                            }
                            break;
                        case DO_NOTHING: // pass to default
                        default:
                            break;
                    }
                }
            }
        } );
        this.timer.start();
    }

    /*
     * The paintComponent method draws all of the objects onto the panel. This
     * is updated at each time step when we call repaint().
     */
    @Override
    protected void paintComponent ( final Graphics g ) {
        super.paintComponent( g );
        // Paint Environment Tiles
        final Tile[][] tiles = env.getTiles();
        for ( int i = 0; i < tiles.length; i++ ) {
            for ( int j = 0; j < tiles[i].length; j++ ) {
                if ( tiles[i][j].getStatus() == TileStatus.CLEAN ) {
                    g.setColor( Properties.SILVER );
                }
                else if ( tiles[i][j].getStatus() == TileStatus.DIRTY ) {
                    g.setColor( Properties.BROWN );
                }
                else if ( tiles[i][j].getStatus() == TileStatus.IMPASSABLE ) {
                    g.setColor( Properties.BLACK );
                }
                g.fillRect( i * TILESIZE, j * TILESIZE, TILESIZE, TILESIZE );

                g.setColor( Properties.BLACK );
                g.drawRect( i * TILESIZE, j * TILESIZE, TILESIZE, TILESIZE );
            }
        }
        // Paint Robot
        g.setColor( Properties.GREEN );
        for ( final Robot robot : robots ) {
            g.fillOval( robot.getPosX() * TILESIZE + TILESIZE / 4, robot.getPosY() * TILESIZE + TILESIZE / 4,
                    TILESIZE / 2, TILESIZE / 2 );
        }
    }
}
