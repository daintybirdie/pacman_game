package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.Maze;
import pacman.model.maze.MazeCreator;

public class PinkyChaseBehaviour implements GhostBehaviour {
    private static final double TARGET_DISTANCE = 4; // Tiles ahead of Pacman
    private static final double TILE_SIZE = MazeCreator.RESIZING_FACTOR;

    @Override
    public void chasePostition(GhostImpl ghost) {
        Vector2D pacmanPosition = ghost.getTargetLocation();
        Direction pacmanDirection = ghost.getDirection();

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

        ghost.setTargetLocation(targetPosition);
    }
}
