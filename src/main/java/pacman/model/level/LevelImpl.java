package pacman.model.level;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.engine.observer.GameState;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.FrightenedState;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PowerPellet;
import pacman.model.level.observer.LevelStateObserver;
import pacman.model.maze.Maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            }
        } else {
            // Check for ghost mode change based on duration
            if (tickCount == modeLengths.get(currentGhostMode)) {
                this.currentGhostMode = GhostMode.getNextGhostMode(currentGhostMode);

                for (Ghost ghost : this.ghosts) {
                    if (ghost.getGhostMode() != GhostMode.FRIGHTENED) {
                        ghost.setGhostMode(currentGhostMode);
                    } else {
                        // In FRIGHTENED mode, check duration
                        ghost.incrementCount();
                        // Check if the FRIGHTENED mode duration has expired
                        System.out.println(ghost.getCount());
                        System.out.println(modeLengths.get(GhostMode.FRIGHTENED));
                        if (ghost.getCount() >= modeLengths.get(GhostMode.FRIGHTENED)) {
                            ghost.deactivateFrightenedMode();
                            ghostEaten = 0;
                            ghost.setGhostMode(GhostMode.SCATTER);
                        }
                    }
                }
                tickCount = 0;
            }

            // Handle image swapping for Pacman
            if (tickCount % Pacman.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
                this.player.switchImage();
            }

            // Update dynamic entities
            List<DynamicEntity> dynamicEntities = getDynamicEntities();
            for (DynamicEntity dynamicEntity : dynamicEntities) {
                maze.updatePossibleDirections(dynamicEntity);
                dynamicEntity.update();
            }


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


    private void handleCollisions(List<DynamicEntity> dynamicEntities) {
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

    // Method to handle the ghost reset upon collision with Pacman
    private void handleGhostCollision(Ghost ghost) {
        if (ghost.getGhostMode() == GhostMode.FRIGHTENED) {
            ghostEaten++;
            this.points = 200 * ghostEaten; // Award points for eating the ghost
            notifyObserversWithScoreChange(points);
            ghost.deactivateFrightenedMode();
            ghost.reset(); // Reset only this specific ghost
            ghost.setPaused(true); // Pause movement
            Timeline pauseTimeline = new Timeline(new KeyFrame(
                    Duration.seconds(1),
                    e -> ghost.setPaused(false) // Resume movement after 1 second
            ));
            pauseTimeline.play();
        } else {
            // If the ghost is NOT in FRIGHTENED mode, handle collisions
            handleCollisions(getDynamicEntities());
            ghosts.forEach(Renderable::reset);
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
    public void collect(Collectable collectable) {
        Pellet pellet = (Pellet) collectable;
        this.collectables.remove(collectable);
        this.points = pellet.getPoints();
        notifyObserversWithScoreChange(points);

        // Check if it's a PowerPellet to activate frightened mode
        if (collectable instanceof PowerPellet) {
            for (Ghost ghost : ghosts) {
                ghost.setGhostMode(GhostMode.FRIGHTENED);
                ghost.resetCount();
            }
        }
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
