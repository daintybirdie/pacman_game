package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

import java.util.List;

public class InkyChaseBehaviour implements GhostBehaviour {
    private static final double DISTANCE = 2; // Tiles ahead of Pacman
    private static final double TILE_SIZE = MazeCreator.RESIZING_FACTOR; // Tiles ahead of Pacman
    private GhostImpl blinky;
    private GhostImpl inky;
    @Override
    public void chasePosition(List<Ghost> ghosts) {
        ghosts.forEach(ghost -> {
            if (ghost.getName() == 'b') {
                blinky = (GhostImpl) ghost;
            }
            else if (ghost.getName() == 'i') {
                inky = (GhostImpl)  ghost;
            }
        });

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
        Vector2D blinkyPosition = blinky.getPosition();
        Vector2D difference = new Vector2D(blinkyPosition.getX()-pacmanPlus2.getX(), blinkyPosition.getY()-pacmanPlus2.getY());
        Vector2D doubledDifference = new Vector2D(difference.getX()*2, difference.getY()*2);
        Vector2D inkyPosition = new Vector2D(doubledDifference.getX() + blinkyPosition.getX(),
                doubledDifference.getY() + blinkyPosition.getY());
        inky.setTargetLocation(inkyPosition);
    }
}
