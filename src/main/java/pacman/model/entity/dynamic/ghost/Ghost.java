package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;
import pacman.model.factories.GhostBehaviour;

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
    GhostBehaviour getGhostBehaviour();

    /**
     *
     * @param ghostBehaviour sets the ghost behaviour during intialisation (for CHASE MODE)- updated in tick()
     */
    void setGhostBehaviour(GhostBehaviour ghostBehaviour);

    /**
     *
     * @return character- the name of the ghost is the character allocated on map.txt
     */
    char getName();

    GhostState getCurrentState();
    GhostMode getGhostMode();
    void resetCount();
    void incrementCount();
    int getCount();

    void setPaused(boolean b);
    boolean isPaused();
    void transitionState();
    void activateState();
    void deactivateState();
    void setCurrentImage(Image image);
    Image getFrightenedImage();
    Image getNormalImage();
    void setCurrentState(GhostState state);
    GhostState getNormalState();
    GhostState getFrightenedState();

}
