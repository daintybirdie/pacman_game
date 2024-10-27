package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.level.Level;

/**
 * Represents the Pellet in Pac-Man game
 */
public class Pellet extends StaticEntityImpl implements Collectable {

    private final int points = 10;
    protected int currentPoints;
    boolean isCollectable;
    private Image image;

    public Pellet(BoundingBox boundingBox, Layer layer, Image image) {
        super(boundingBox, layer, image);
        this.image = image;
        this.isCollectable = true;
    }

    @Override
    public void collect(Level level) {
        if (level.getCollectables().contains(this)) {
            level.getCollectables().remove(this);
            this.currentPoints = this.getPoints();
            level.notifyObserversWithScoreChange(currentPoints);
            this.isCollectable = false;
            setLayer(Layer.INVISIBLE);
        }
    }

    @Override
    public Image getImage() {
       return image;
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
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public int getPoints() {
        return this.points;
    }
}
