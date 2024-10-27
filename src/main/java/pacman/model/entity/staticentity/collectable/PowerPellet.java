package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.collectable.compositepattern.CollectEffect;

public class PowerPellet extends Pellet{

    public PowerPellet(BoundingBox boundingBox, Layer layer, Image image, int points, CollectEffect collectEffect) {
        super(boundingBox, layer, image, points,collectEffect);
    }
}
