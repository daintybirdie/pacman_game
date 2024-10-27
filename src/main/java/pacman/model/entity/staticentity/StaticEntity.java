package pacman.model.entity.staticentity;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Vector2D;

/**
 * Represents a Static Entity in Pac-Man game
 */
public interface StaticEntity extends Renderable {
    /**
     * Returns true if Dynamic Entities can pass through entity
     *
     * @return true, if Dynamic Entities can pass through entity
     */
    default boolean canPassThrough() {
        return true;
    };

    @Override
    default double getWidth() {
        return getBoundingBox().getWidth();
    }

    @Override
    default double getHeight() {
        return getBoundingBox().getHeight();
    }


    @Override
    default Vector2D getPosition() {
        return new Vector2D(getBoundingBox().getLeftX(), getBoundingBox().getTopY());
    }

    @Override
    default void reset() {
    }

}