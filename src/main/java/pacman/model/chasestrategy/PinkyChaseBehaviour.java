package pacman.model.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

import java.util.List;

public class PinkyChaseBehaviour implements GhostChaseBehaviour {
    private static final double TARGET_DISTANCE = 4; // Tiles ahead of Pacman
    private static final double TILE_SIZE = MazeCreator.RESIZING_FACTOR;
    private Ghost pinky;
    @Override
    public void chasePosition(List<Ghost> ghosts) {
        for (Ghost ghost :ghosts)
            // Get Pinky Ghost
            if (ghost.getName() == 's') {
                pinky = ghost;
                break;
            }

        Vector2D pacmanPosition = pinky.getTargetLocation();
        Direction pacmanDirection = pinky.getDirection();

        // Pinky's Chase position is 4 grid spaces in front of PacMan's position
        Vector2D targetPosition = switch (pacmanDirection) {
            case UP -> pacmanPosition.add(new Vector2D(0, -TARGET_DISTANCE * TILE_SIZE));
            case DOWN -> pacmanPosition.add(new Vector2D(0, TARGET_DISTANCE * TILE_SIZE));
            case LEFT -> pacmanPosition.add(new Vector2D(-TARGET_DISTANCE * TILE_SIZE, 0));
            case RIGHT -> pacmanPosition.add(new Vector2D(TARGET_DISTANCE * TILE_SIZE, 0));
        };

        pinky.setTargetLocation(targetPosition);
    }
}
