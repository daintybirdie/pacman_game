package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.level.Level;

/**
    Abstract class that decorates objects of type Collectable
 */
public abstract class CollectableDecorator extends StaticEntityImpl implements Collectable {
    protected Collectable collectable;
    protected int displayPoints;
    protected boolean isCollectable;

    public CollectableDecorator(Collectable collectable) {
        super(collectable.getBoundingBox(), collectable.getLayer(), collectable.getImage());
        this.collectable = collectable;
        isCollectable = true;
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
        this.isCollectable = true;
        setLayer(Layer.BACKGROUND);
    }

    @Override
    public boolean isCollectable() {
        return isCollectable;
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
        // Check if the collectable is present in the level's collectables before removing
        if (level.getCollectables().contains(this)) {
            level.getCollectables().remove(this);
            // score observer notification
            this.displayPoints = this.getPoints();
            level.notifyObserversWithScoreChange(displayPoints);
            // making the pellet not collectable and invisible after removing from the collectables list
            this.isCollectable = false;
            this.setLayer(Layer.INVISIBLE);
        }
    }


    @Override
    public BoundingBox getBoundingBox() {
        return collectable.getBoundingBox();
    }

}
