package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PowerPellet;
import pacman.model.entity.staticentity.collectable.compositepattern.CollectEffect;
import pacman.model.entity.staticentity.collectable.compositepattern.PelletEffect;
import pacman.model.entity.staticentity.collectable.compositepattern.PowerPelletEffect;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete renderable factory for Pellet objects
 */
public class PelletFactory implements RenderableFactory {
    private static final Image PELLET_IMAGE = new Image("maze/pellet.png");
    private static final int NUM_POINTS_7 = 10;
    private static final int NUM_POINTS_Z = 50;
    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;
    private char renderableType;
    private static Map<Character, Integer> PELLETMAP = new HashMap<>();

    //height and width


    public PelletFactory(Character renderableType) {
        this.renderableType = renderableType;
    }

    static {
        // this defines the scaling factor for the pellets
        PELLETMAP.put('7',1);
        PELLETMAP.put('z',2);
    }


    @Override
    public Renderable createRenderable(Vector2D position) {
        int scaling = PELLETMAP.get(renderableType); // Get the scaling factor for the pellet

        BoundingBox boundingBox = new BoundingBoxImpl(
                position,
                PELLET_IMAGE.getHeight() * scaling,
                PELLET_IMAGE.getWidth() * scaling
        );

        // Check if the renderableType is 'z' for PowerPellet
        if (renderableType == 'z') {
            // Return a PowerPellet if renderableType is 'z'
            PowerPellet powerPellet = new PowerPellet(boundingBox, layer, PELLET_IMAGE, NUM_POINTS_Z);
            CollectEffect pelletEffect = new PowerPelletEffect(powerPellet);
            powerPellet.setCollectEffect(pelletEffect);
            return powerPellet;
        } else if (renderableType == '7') {
            // Return a regular Pellet if renderableType is '7'
            Pellet pellet = new Pellet(boundingBox, layer, PELLET_IMAGE, NUM_POINTS_7);
            CollectEffect pelletEffect = new PelletEffect(pellet);
            pellet.setCollectEffect(pelletEffect);

            return pellet;
        } else {
            throw new ConfigurationParseException("Unknown pellet type");
        }
    }
}
