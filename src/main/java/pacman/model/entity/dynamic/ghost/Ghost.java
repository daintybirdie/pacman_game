package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.statepattern.GhostState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;
import pacman.model.entity.dynamic.ghost.chasestrategy.GhostChaseBehaviour;

import java.util.Map;

/**
 * Represents Ghost entity in Pac-Man Game
 */
public interface Ghost extends DynamicEntity, PlayerPositionObserver {

    /***
     * Sets the speeds of the Ghost for each GhostMode
     * @param speeds speeds of the Ghost for each GhostMode
     */
    void setSpeeds(Map<GhostMode, Double> speeds);

    /**
     * Sets the mode of the Ghost used to calculate target position
     *
     * @param ghostMode mode of the Ghost
     */
    void setGhostMode(GhostMode ghostMode);

    /**
     * @param vector2D sets target location based on Vector2D position given
     */
    void setTargetLocation(Vector2D vector2D);

    /**
     * @return Ghostbehaviour, which allows for dynamically setting target position based on unique ghost behaviour
     */
    GhostChaseBehaviour getGhostBehaviour();

    /**
     *
     * @param ghostBehaviour sets the ghost behaviour during intialisation (for CHASE MODE)- updated in tick()
     */
    void setGhostBehaviour(GhostChaseBehaviour ghostBehaviour);

    /**
     *
     * @return character the name of the ghost is the character allocated on map.txt
     */
    char getName();

    // Get current state of Ghost
    GhostState getCurrentState();

    // Get current ghost mode
    GhostMode getGhostMode();

    // Update whether mode is paused or not
    void setPaused(boolean b);

    // Get whether paused boolean
    boolean isPaused();

    // Transition between states for FrightenedState and NormalState
    void transitionState();

    // Setting the ghosts current image
    void setCurrentImage(Image image);

    // Get Frightened image
    Image getFrightenedImage();

    // Get ghosts unique image non-frightened state
    Image getNormalImage();

    // Set ghost's current state
    void setCurrentState(GhostState state);

    // Get the ghost's NormalState instance
    GhostState getNormalState();

    // Get the ghost's FrightenedState instance
    GhostState getFrightenedState();

    // Get the ghost's target location
    Vector2D getTargetLocation();

}
