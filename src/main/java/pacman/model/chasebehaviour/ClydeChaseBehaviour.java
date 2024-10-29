package pacman.model.chasebehaviour;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

import java.util.List;

public class ClydeChaseBehaviour implements GhostChaseBehaviour {
    private static final double TARGET_DISTANCE = 8;
    private static final double TILE_SIZE = MazeCreator.RESIZING_FACTOR;
    private static final double BOTTOM_Y_POSITION_OF_MAP =  16 * 34;
    private Ghost clyde;
    @Override
    public void chasePosition(List<Ghost> ghosts) {
        // get Clyde ghost
        for (Ghost ghost : ghosts) {
            if (ghost.getName() == 'c') {
                clyde = ghost;
                break;
            }
        }
        // Obtaining Clyde's target position- PacMan since chase position will only be used in CHASE mode
        Vector2D pacmanPosition = clyde.getTargetLocation();
        double xP = pacmanPosition.getX();
        double yP = pacmanPosition.getY();
        // Obtaining Clyde's position
        Vector2D ghostPosition = clyde.getPosition();
        double xG = ghostPosition.getX();
        double yG = ghostPosition.getY();
        // Calculating Euclidean distance between Clyde and PacMan
        double distance = Math.sqrt(Math.pow(xP - xG, 2) + Math.pow(yP - yG, 2));
        // If their distance greater than 8 tiles away, then target location is set to PacMan
        if (distance > TARGET_DISTANCE*TILE_SIZE) {
            clyde.setTargetLocation(pacmanPosition);
        }
        // Else, The target position is bottom left corner of the map
        else {
            clyde.setTargetLocation(new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP));
        }
    }
}
