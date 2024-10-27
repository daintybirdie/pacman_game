package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.level.Level;

public abstract class PelletDecorator extends StaticEntityImpl implements Collectable {
    protected Collectable collectable;
    protected int displayPoints;

    public PelletDecorator(Collectable collectable) {
        super(collectable.getBoundingBox(), collectable.getLayer(), collectable.getImage());
        this.collectable = collectable;
    }

    @Override
    public Image getImage() {
        return collectable.getImage();
    }

    @Override
    public int getPoints() {
        return collectable.getPoints();
    }

    @Override
    public void reset() {
        collectable.reset();
    }

    @Override
    public boolean isCollectable() {
        System.out.println(collectable.isCollectable());
        return true;
    }

    @Override
    public double getWidth() {
        return collectable.getWidth();
    }

    @Override
    public double getHeight() {
        return collectable.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return collectable.getPosition();
    }

    @Override
    public boolean canPassThrough() {
        return true;
    };

    @Override
    public void collect(Level level) {
        collectable.collect(level);
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return collectable.getBoundingBox();
    }

}
