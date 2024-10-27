package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

import java.util.List;

public class PinkyChaseBehaviour implements GhostChaseBehaviour {
    private static final double TARGET_DISTANCE = 4; // Tiles ahead of Pacman
    private static final double TILE_SIZE = MazeCreator.RESIZING_FACTOR;
    private GhostImpl pinky;
    @Override
    public void chasePosition(List<Ghost> ghosts) {
        for (Ghost ghost :ghosts)
            if (ghost.getName() == 's') {
                pinky = (GhostImpl) ghost;
                break;
            }

        Vector2D pacmanPosition = pinky.getTargetLocation();
        Direction pacmanDirection = pinky.getDirection();

        Vector2D targetPosition;
        switch (pacmanDirection) {
            case UP:
                targetPosition = pacmanPosition.add(new Vector2D(0, -TARGET_DISTANCE * TILE_SIZE));
                break;
            case DOWN:
                targetPosition = pacmanPosition.add(new Vector2D(0, TARGET_DISTANCE * TILE_SIZE));
                break;
            case LEFT:
                targetPosition = pacmanPosition.add(new Vector2D(-TARGET_DISTANCE * TILE_SIZE, 0));
                break;
            case RIGHT:
                targetPosition = pacmanPosition.add(new Vector2D(TARGET_DISTANCE * TILE_SIZE, 0));
                break;
            default:
                targetPosition = pacmanPosition;
                break;
        }

        pinky.setTargetLocation(targetPosition);
    }
}
