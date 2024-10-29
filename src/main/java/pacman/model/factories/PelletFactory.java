package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PowerPelletDecorator;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete renderable factory for Pellet objects
 */
public class PelletFactory implements RenderableFactory {
    private static final Image PELLET_IMAGE = new Image("maze/pellet.png");
    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;
    private char renderableType;
    private static Map<Character, Integer> PELLETMAP = new HashMap<>();


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
            // Create a Pellet instance, and wrap it using the PowerPelletDecorator to enable Power Pellet behaviour
            Collectable pellet = new Pellet(boundingBox, layer, PELLET_IMAGE);
            Collectable powerPellet = new PowerPelletDecorator(pellet);
            return powerPellet;
        }
        // renderableType is '7' for regular Pellet
        else if (renderableType == '7') {
            return new Pellet(boundingBox, layer, PELLET_IMAGE);
        } else {
            throw new ConfigurationParseException("Unknown pellet type");
        }
    }
}
