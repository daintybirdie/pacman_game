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
    protected boolean isCollectable;

    public PelletDecorator(Collectable collectable) {
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
        collectable.reset();
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
        // Check if the collectable is present in the level's collectables
        if (level.getCollectables().contains(this)) {
            level.getCollectables().remove(this);
            this.displayPoints = this.getPoints();
            level.notifyObserversWithScoreChange(displayPoints);
            this.isCollectable = false;
            setLayer(Layer.INVISIBLE);
            System.out.println("Successfully removed the collectable from the level.");
        } else {
            System.out.println("Collectable not found in the level.");
        }
    }


    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return collectable.getBoundingBox();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check for reference equality
        if (!(obj instanceof PelletDecorator)) return false; // Check type
        PelletDecorator other = (PelletDecorator) obj;

        // Check equality based on the wrapped collectable
        return this.collectable.equals(other.collectable); // Use the equals of the wrapped collectable
    }

    @Override
    public int hashCode() {
        return collectable.hashCode(); // Use the hash code of the wrapped collectable
    }

}
