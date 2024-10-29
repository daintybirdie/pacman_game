package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

import java.util.List;

public class InkyChaseBehaviour implements GhostChaseBehaviour {
    private static final double DISTANCE = 2; // Tiles ahead of Pacman
    private static final double TILE_SIZE = MazeCreator.RESIZING_FACTOR; // Tiles ahead of Pacman
    private Ghost blinky;
    private Ghost inky;
    @Override
    public void chasePosition(List<Ghost> ghosts) {
        ghosts.forEach(ghost -> {
            // Get Blinky ghost
            if (ghost.getName() == 'b') {
                blinky = ghost;
            }
            // Get Inky ghost
            else if (ghost.getName() == 'i') {
                inky = ghost;
            }
        });

        // Pacman position + 2 grids
        Vector2D pacmanPlus2 = getVectorPlus2();
        Vector2D blinkyPosition = blinky.getPosition();

        // Performing vector subtraction of Blinky's and pacmanPlus2's vector coordinates
        Vector2D difference = new Vector2D(blinkyPosition.getX()-pacmanPlus2.getX(), blinkyPosition.getY()-pacmanPlus2.getY());
        // Doubling the vector difference
        Vector2D doubledDifference = new Vector2D(difference.getX()*2, difference.getY()*2);

        // Setting Inky's position: vector addition of his doubleDifference and Blinky's position
        Vector2D inkyPosition = new Vector2D(doubledDifference.getX() + blinkyPosition.getX(),
                doubledDifference.getY() + blinkyPosition.getY());
        inky.setTargetLocation(inkyPosition);
    }

    // Method to obtain PacMan's position + 2 grids in the direction he is heading
    private Vector2D getVectorPlus2() {
        Vector2D pacmanPosition = blinky.getTargetLocation();
        double x = pacmanPosition.getX();
        double y = pacmanPosition.getY();
        Vector2D pacmanPlus2;
        Direction pacmanDirection = blinky.getDirection();
        if (pacmanDirection == Direction.RIGHT) {
            pacmanPlus2 = new Vector2D(DISTANCE*TILE_SIZE + x, y );
        }
        else if (pacmanDirection == Direction.LEFT) {
            pacmanPlus2 = new Vector2D(-DISTANCE*TILE_SIZE + x, y );
        }
        else if (pacmanDirection == Direction.UP) {
            pacmanPlus2 = new Vector2D( x, y + DISTANCE*TILE_SIZE);
        }
        else {
            pacmanPlus2 = new Vector2D( x, y - DISTANCE*TILE_SIZE);
        }
        return pacmanPlus2;
    }
}
