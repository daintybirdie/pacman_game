package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.statepattern.FrightenedState;
import pacman.model.entity.dynamic.ghost.statepattern.GhostState;
import pacman.model.entity.dynamic.ghost.statepattern.NormalState;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.factories.GhostChaseBehaviour;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {
    private static final int minimumDirectionCount = 8;
    private final Layer layer = Layer.FOREGROUND;
    private Image image;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Vector2D playerPosition;
    private Direction currentDirection;
    private Set<Direction> possibleDirections;
    private Map<GhostMode, Double> speeds;
    private int currentDirectionCount = 0;
    private GhostChaseBehaviour ghostBehaviour;
    private char name;
    // IMAGES
    private Image normalImage;
    private Image frightenedImage;

    // State management
    private GhostState currentState; // New field for state management
    private final GhostState normalState = new NormalState(this); // Normal state
    private final GhostState frightenedState = new FrightenedState(this); // Frightened state

    private static final double SOME_DISTANCE = 50;

    //frightened tick
    private int tickCountFrightened = 0;
    private boolean isPaused = false;

    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, char name,
                     Image normalImage, Image frightenedImage) {
        this.image = image;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.targetLocation = getTargetLocation();
        this.currentDirection = null;
        this.name = name;
        this.normalImage = normalImage;
        this.frightenedImage = frightenedImage;
        this.currentState = normalState; // Start in normal state
    }

    public void incrementCount() {
        tickCountFrightened++;
    }

    public void resetCount() {
        tickCountFrightened = 0;
    }

    public int getCount() {
        return tickCountFrightened;
    }
    public void setCurrentImage(Image image) {
        this.image = image; // This will update the image shown for the ghost
    }

    public Image getNormalImage() {
        return normalImage; // Return normal ghost image
    }

    public Image getFrightenedImage() {
        return frightenedImage; // Return frightened ghost image
    }

    public void transitionState() {
        currentState = currentState.transitionToNextState();
    }

    public void activateState() {
        currentState.activate();
    }

    public void deactivateState() {
        currentState.activate();
    }

    public void setCurrentState(GhostState state) {
        this.currentState = state;
    }

    public GhostState getNormalState() {
        return normalState;
    }

    public GhostState getFrightenedState() {
        return frightenedState;
    }


    @Override
    public GhostState getCurrentState() {
        return  currentState;
    }


    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.speeds = speeds;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void update() {
        if (!isPaused()) {
            this.updateDirection();
            this.kinematicState.update();
            this.boundingBox.setTopLeft(this.kinematicState.getPosition());
        }
    }

    @Override
    public GhostChaseBehaviour getGhostBehaviour() {
        return ghostBehaviour;
    }

    @Override
    public void setGhostBehaviour(GhostChaseBehaviour ghostBehaviour) {
        this.ghostBehaviour = ghostBehaviour;
    }

    private void updateDirection() {
        // Ghosts update their target location when they reach an intersection
        if (Maze.isAtIntersection(this.possibleDirections)) {
            this.targetLocation = getTargetLocation();
        }

        Direction newDirection = selectDirection(possibleDirections);

        // Ghosts have to continue in a direction for a minimum time before changing direction
        if (this.currentDirection != newDirection) {
            this.currentDirectionCount = 0;
        }
        this.currentDirection = newDirection;

        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }
    }


    // Added this message to ensure we can change the target's position based on GhostMode
    @Override
    public void setTargetLocation(Vector2D vector2D) {
        if (this.ghostMode == GhostMode.CHASE) {
            this.playerPosition = vector2D;
        }
    }

    public Vector2D getTargetLocation() {
        return switch (this.ghostMode) {
            case CHASE -> this.playerPosition; // Move towards the player in CHASE mode
            case SCATTER -> this.targetCorner; // Move towards the corner in SCATTER mode
            case FRIGHTENED -> getFrightenedTargetLocation(); // Move away from the player in FRIGHTENED mode
        };
    }

    /**
     * Calculates a target location away from the player position when in FRIGHTENED mode.
     *
     * @return A Vector2D that represents a location away from the player's position.
     */
    private Vector2D getFrightenedTargetLocation() {
        // Calculate a direction vector away from the player position
        Vector2D currentPosition = this.kinematicState.getPosition(); // Get current position of the ghost
        Vector2D directionAwayFromPlayer = this.playerPosition.subtract(currentPosition).normalize(); // Normalize to get a unit vector
        Vector2D awayFromPlayerLocation = currentPosition.add(directionAwayFromPlayer.scale(SOME_DISTANCE)); // Scale by a distance
        return awayFromPlayerLocation; // Return the calculated target location
    }



    public char getName() {
        return name;
    }


    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        // ghosts have to continue in a direction for a minimum time before changing direction
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            currentDirectionCount++;
            return currentDirection;
        }

        Map<Direction, Double> distances = new HashMap<>();

        for (Direction direction : possibleDirections) {
            // ghosts never choose to reverse travel
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(this.kinematicState.getPotentialPosition(direction), this.targetLocation));
            }
        }

        // only go the opposite way if trapped
        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        // select the direction that will reach the target location fastest
        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        this.ghostMode = ghostMode;
        this.kinematicState.setSpeed(speeds.get(ghostMode));
        this.currentDirectionCount = minimumDirectionCount;
    }


    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            level.handleLoseLife();
        }
    }

    @Override
    public void update(Vector2D playerPosition) {
        this.playerPosition = playerPosition;
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        // ensure ghost state is normal
        if (currentState instanceof FrightenedState) {
            transitionState();
        }
        // return ghost to starting position
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .build();
        this.boundingBox.setTopLeft(startingPosition);
        this.ghostMode = GhostMode.SCATTER;
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public GhostMode getGhostMode() {
        return this.ghostMode;
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }
}
