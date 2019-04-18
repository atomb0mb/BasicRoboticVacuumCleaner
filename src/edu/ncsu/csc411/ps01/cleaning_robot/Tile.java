package edu.ncsu.csc411.ps01.cleaning_robot;

/**
 * A simple object representing the Tiles in the
 * environment. Their only purpose is to pass their
 * status or change into a CLEAN tile.
 * DO NOT MODIFY.
 * @author Adam Gaweda
 */
public class Tile {
	private TileStatus status;
	
	public Tile(TileStatus status) {
		this.status = status;
	}
	
	public TileStatus getStatus() { return status; }
	public void cleanTile() { status = TileStatus.CLEAN; }
}
