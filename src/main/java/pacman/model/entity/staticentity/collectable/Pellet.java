package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.entity.staticentity.collectable.compositepattern.CollectEffect;
import pacman.model.level.Level;

/**
 * Represents the Pellet in Pac-Man game
 */
public class Pellet extends StaticEntityImpl implements Collectable {

    private final int points;
    private boolean isCollectable;
    private CollectEffect collectEffect;

    public Pellet(BoundingBox boundingBox, Layer layer, Image image, int points, CollectEffect collectEffect) {
        super(boundingBox, layer, image);
        this.points = points;
        this.isCollectable = true;
        this.collectEffect = collectEffect;

    }

    @Override
    public void collect(Level level, Collectable collectable) {
        collectEffect.collect(level, collectable);
        this.isCollectable = false;
        setLayer(Layer.INVISIBLE);
    }

    @Override
    public void reset() {
        this.isCollectable = true;
        setLayer(Layer.BACKGROUND);
    }

    @Override
    public boolean isCollectable() {
        return this.isCollectable;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public int getPoints() {
        return this.points;
    }
}
