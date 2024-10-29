package pacman.model.level;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.engine.observer.GameState;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.statepattern.FrightenedState;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.observer.LevelStateObserver;
import pacman.model.maze.Maze;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Concrete implement of Pac-Man level
 */
public class LevelImpl implements Level {

    private static final int START_LEVEL_TIME = 100;
    private final Maze maze;
    private final List<LevelStateObserver> observers;
    private List<Renderable> renderables;
    private Controllable player;
    private List<Ghost> ghosts;
    private int tickCount;
    private Map<GhostMode, Integer> modeLengths;
    private int numLives;
    private int points;
    private GameState gameState;
    private List<Renderable> collectables;
    private GhostMode currentGhostMode;
    private Map<GhostMode, Double> ghostSpeeds;

    // track how many ghosts eaten in a single frightened state:
    private int ghostEaten = 0;

    public LevelImpl(JSONObject levelConfiguration,
                     Maze maze) {
        this.renderables = new ArrayList<>();
        this.maze = maze;
        this.tickCount = 0;
        this.observers = new ArrayList<>();
        this.modeLengths = new HashMap<>();
        this.gameState = GameState.READY;
        this.currentGhostMode = GhostMode.SCATTER;
        this.points = 0;

        initLevel(new LevelConfigurationReader(levelConfiguration));
    }

    private void initLevel(LevelConfigurationReader levelConfigurationReader) {
        // Fetch all renderables for the level
        this.renderables = maze.getRenderables();

        // Set up player
        if (!(maze.getControllable() instanceof Controllable)) {
            throw new ConfigurationParseException("Player entity is not controllable");
        }
        this.player = (Controllable) maze.getControllable();
        this.player.setSpeed(levelConfigurationReader.getPlayerSpeed());
        setNumLives(maze.getNumLives());

        // Set up ghosts
        this.ghosts = maze.getGhosts().stream()
                .map(element -> (Ghost) element)
                .collect(Collectors.toList());
        ghostSpeeds = levelConfigurationReader.getGhostSpeeds();

        for (Ghost ghost : this.ghosts) {
            player.registerObserver(ghost);
            ghost.setSpeeds(ghostSpeeds);
            ghost.setGhostMode(this.currentGhostMode);
        }
        this.modeLengths = levelConfigurationReader.getGhostModeLengths();
        // Set up collectables
        this.collectables = new ArrayList<>(maze.getPellets());
    }

    @Override
    public List<Renderable> getRenderables() {
        return this.renderables;
    }

    private List<DynamicEntity> getDynamicEntities() {
        return renderables.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return renderables.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }


    @Override
    public void tick() {
        if (this.gameState != GameState.IN_PROGRESS) {
            if (tickCount >= START_LEVEL_TIME) {
                setGameState(GameState.IN_PROGRESS);
                tickCount = 0;
                // Reset ghost states when starting a new level
                for (Ghost ghost : ghosts) {
                    ghost.setGhostMode(GhostMode.SCATTER);
                }
            }
        } else {

            // Check for mode transition based on tick count
            checkForModeTransition();

            // Handle image swapping for Pacman
            if (tickCount % Pacman.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
                this.player.switchImage();
            }

            // Update dynamic entities
            List<DynamicEntity> dynamicEntities = getDynamicEntities();
            updateDynamicEntities(dynamicEntities);

            // Handle collisions with ghosts
            for (DynamicEntity dynamicEntity : dynamicEntities) {
                if (dynamicEntity instanceof Pacman) {
                    for (DynamicEntity other : dynamicEntities) {
                        if (other instanceof Ghost && dynamicEntity.collidesWith(other)) {
                            handleGhostCollision((Ghost) other);
                        }
                    }
                }
            }

            // Handle collisions with static entities
            handleCollisionsWithStaticEntities(dynamicEntities);
        }

        tickCount++;
    }

    private void checkForModeTransition() {
        if (tickCount >= modeLengths.get(currentGhostMode)) {
            if (currentGhostMode != GhostMode.FRIGHTENED) {
                // Transition to the next ghost mode
                this.currentGhostMode = GhostMode.getNextGhostMode(currentGhostMode);
                for (Ghost ghost : this.ghosts) {
                    ghost.setGhostMode(this.currentGhostMode);
                }
            } else {
                // In FRIGHTENED mode, check duration
                for (Ghost ghost : this.ghosts) {
                    if (ghost.getCurrentState() instanceof FrightenedState) {
                        ghost.transitionState();
                    }
                    ghost.setGhostMode(GhostMode.SCATTER);
                    ghostEaten = 0; // Reset the count of ghosts eaten
                }
                currentGhostMode = GhostMode.SCATTER; // Ensure mode is set to SCATTER
            }

            tickCount = 0; // Reset tick count after processing
        }
    }


    // This is the high-level method that handle collision
    // Delegates to appropriate collision handling mechanism for whether the ghost is FRIGHTENED or not
    private void handleGhostCollision(Ghost ghost) {
        if (ghost.getGhostMode() == GhostMode.FRIGHTENED) {
            // PacMan can eat ghosts
            handleCollisionsFrightened(ghost);
        } else {
            // If the ghost is NOT in FRIGHTENED mode, resume normal collision handling
            handleCollisionsNonFrightened(getDynamicEntities());
            ghosts.forEach(Renderable::reset);
        }
    }



    private void handleCollisionsFrightened(Ghost ghost) {
            ghostEaten++;
            this.points = 200 * ghostEaten; // Award points for eating the ghost
            notifyObserversWithScoreChange(points);
            ghost.getCurrentState().deactivate();
            ghost.reset(); // Reset only this specific ghost
            ghost.setPaused(true); // Pause movement
            // Create a Timeline to resume movement after 1 second
            Timeline pauseTimeline = new Timeline(new KeyFrame(
                    Duration.seconds(1),
                    e -> {
                        ghost.setPaused(false); // Resume movement after 1 second
                        ghost.setGhostMode(GhostMode.SCATTER); // Ensure it goes back to SCATTER mode after pause
                    }
            ));
            pauseTimeline.play();
    }

    private void handleCollisionsNonFrightened(List<DynamicEntity> dynamicEntities) {
        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity dynamicEntityA = dynamicEntities.get(i);

            // handle collisions between dynamic entities
            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                if (dynamicEntityA.collidesWith(dynamicEntityB) ||
                        dynamicEntityB.collidesWith(dynamicEntityA)) {
                    dynamicEntityA.collideWith(this, dynamicEntityB);
                    dynamicEntityB.collideWith(this, dynamicEntityA);
                }
            }
        }
    }

    // Handling collisions with static entities
    private void handleCollisionsWithStaticEntities(List<DynamicEntity> dynamicEntities) {
        for (DynamicEntity dynamicEntity : dynamicEntities) {
            // Check for collisions with static entities
            if (dynamicEntity instanceof Pacman) {
                for (StaticEntity staticEntity : getStaticEntities()) {
                    if (dynamicEntity.collidesWith(staticEntity)) {
                        dynamicEntity.collideWith(this, staticEntity);
                        PhysicsEngine.resolveCollision(dynamicEntity, staticEntity);
                    }
                }
            }
        }
    }

    // Updating Dynamic entities positions
    private void updateDynamicEntities(List<DynamicEntity> dynamicEntities) {
        for (DynamicEntity dynamicEntity : dynamicEntities) {

            //for each ghost, we obtain the current ghost behaviour if it is in CHASE mode.
            //Then we set the chase position according to the unique ghost behaviour of each ghost.

            ghosts.forEach(ghost -> {
                if (this.currentGhostMode == GhostMode.CHASE) {
                    ghost.getGhostBehaviour().chasePosition(ghosts);
                }
            });
            maze.updatePossibleDirections(dynamicEntity);
            dynamicEntity.update();
        }
    }



    @Override
    public boolean isPlayer(Renderable renderable) {
        return renderable == this.player;
    }

    @Override
    public boolean isCollectable(Renderable renderable) {
        return maze.getPellets().contains(renderable) && ((Collectable) renderable).isCollectable();
    }

    @Override
    public List<Renderable> getCollectables() {
        return collectables;
    }

    @Override
    public void collect(Collectable collectable) {
        collectable.collect(this);
    }

    @Override
    public List<Ghost> getGhosts() {
        return ghosts;
    }

    @Override
    public void setTickCount(int num) {
        this.tickCount = num;
    }

    @Override
    public void setCurrentGhostMode(GhostMode mode) {
        currentGhostMode = mode;
    }

    @Override
    public void handleLoseLife() {
        if (gameState == GameState.IN_PROGRESS) {
            for (DynamicEntity dynamicEntity : getDynamicEntities()) {
                dynamicEntity.reset();
            }
            setNumLives(numLives - 1);
            setGameState(GameState.READY);
            tickCount = 0;
        }
    }

    @Override
    public void moveLeft() {
        player.left();
    }

    @Override
    public void moveRight() {
        player.right();
    }

    @Override
    public void moveUp() {
        player.up();
    }

    @Override
    public void moveDown() {
        player.down();
    }

    @Override
    public boolean isLevelFinished() {
        return collectables.isEmpty();
    }

    @Override
    public void registerObserver(LevelStateObserver observer) {
        this.observers.add(observer);
        observer.updateNumLives(this.numLives);
        observer.updateGameState(this.gameState);
    }

    @Override
    public void removeObserver(LevelStateObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObserversWithNumLives() {
        for (LevelStateObserver observer : observers) {
            observer.updateNumLives(this.numLives);
        }
    }

    private void setGameState(GameState gameState) {
        this.gameState = gameState;
        notifyObserversWithGameState();
    }

    @Override
    public void notifyObserversWithGameState() {
        for (LevelStateObserver observer : observers) {
            observer.updateGameState(gameState);
        }
    }

    /**
     * Notifies observer of change in player's score
     */
    public void notifyObserversWithScoreChange(int scoreChange) {
        for (LevelStateObserver observer : observers) {
            observer.updateScore(scoreChange);
        }
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public int getNumLives() {
        return this.numLives;
    }

    private void setNumLives(int numLives) {
        this.numLives = numLives;
        notifyObserversWithNumLives();
    }

    @Override
    public void handleGameEnd() {
        this.renderables.removeAll(getDynamicEntities());
    }
}
