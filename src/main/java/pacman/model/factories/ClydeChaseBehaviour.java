package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

public class ClydeChaseBehaviour implements GhostBehaviour {
    private static final double TARGET_DISTANCE = 8;
    private static final double TILE_SIZE = MazeCreator.RESIZING_FACTOR;
    private static final double BOTTOM_Y_POSITION_OF_MAP =  16 * 34;

    @Override
    public void chasePostition(GhostImpl ghost) {
        Vector2D pacmanPosition = ghost.getTargetLocation();
        double xP = pacmanPosition.getX();
        double yP = pacmanPosition.getY();
        Vector2D ghostPosition = ghost.getPosition();
        double xG = ghostPosition.getX();
        double yG = ghostPosition.getY();
        double distance = Math.sqrt(Math.pow(xP - xG, 2) + Math.pow(yP - yG, 2));
        if (distance > TARGET_DISTANCE*TILE_SIZE) {
            ghost.setTargetLocation(pacmanPosition);
        }
        else {
            ghost.setTargetLocation(new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP));
        }
    }
}
