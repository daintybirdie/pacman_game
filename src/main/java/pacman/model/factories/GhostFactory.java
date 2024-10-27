package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.chasebehaviour.*;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete renderable factory for Ghost objects
 */
public class GhostFactory implements RenderableFactory {
    // variables to set the corners for each ghost
    private static final int RIGHT_X_POSITION_OF_MAP = 448;
    private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;
    // loading each ghost image
    private static final Image BLINKY_IMAGE = new Image("maze/ghosts/blinky.png");
    private static final Image INKY_IMAGE = new Image("maze/ghosts/inky.png");
    private static final Image CLYDE_IMAGE = new Image("maze/ghosts/clyde.png");
    private static final Image PINKY_IMAGE = new Image("maze/ghosts/pinky.png");
    private static final Image FRIGHTENED_IMAGE = new Image("maze/ghosts/frightened.png");

    // The renderableTyper variable will be used to dynamically create thr unique ghost type
    private char renderableType;
    /*
    These variables are used to dynamically allocate the ghost, and assign its behaviour + corner
    These variables will be declared here, but value assigned in createRendeable.
     */
    private GhostImpl ghost;
    private Vector2D targetCorner;
    private GhostChaseBehaviour chaseBehaviour;
    // will hold the specific image a ghost instance the map creates a mapping between the character in map.txt and associated image loaded
    private final Image image;
    private static Map<Character, Image> IMAGES = new HashMap<>();
    // map will hold the ghost behaviour and allocated corner, for each ghost type
    private static Map<Character, Map<GhostChaseBehaviour, Vector2D>> GHOSTMAP = new HashMap<>();

    static {
        IMAGES.put('b', BLINKY_IMAGE);
        IMAGES.put('s', PINKY_IMAGE);
        IMAGES.put('i', INKY_IMAGE);
        IMAGES.put('c', CLYDE_IMAGE);
        GHOSTMAP.put('c', Map.of(new ClydeChaseBehaviour(), new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP)));
        GHOSTMAP.put('s', Map.of(new PinkyChaseBehaviour(), new Vector2D(0, TOP_Y_POSITION_OF_MAP)));
        GHOSTMAP.put('b', Map.of(new BlinkyChaseBehaviour(), new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP)));
        GHOSTMAP.put('i', Map.of(new InkyChaseBehaviour(), new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP)));
    }

    public GhostFactory(char renderableType) {
        this.renderableType = renderableType;
        this.image = IMAGES.get(renderableType);
    }

    @Override
    public Renderable createRenderable(Vector2D position) {
        try {
            position = position.add(new Vector2D(4, -4));

            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    image.getHeight(),
                    image.getWidth()
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position)
                    .build();

            // Retrieve the inner map from GHOSTMAP, on the renderableType
            Map<GhostChaseBehaviour, Vector2D> behaviourMap = GHOSTMAP.get(renderableType);

            if (behaviourMap != null && !behaviourMap.isEmpty()) {
                Map.Entry<GhostChaseBehaviour, Vector2D> entry = behaviourMap.entrySet().iterator().next();
                chaseBehaviour = entry.getKey();
                targetCorner = entry.getValue();
            } else {
                throw new IllegalArgumentException("Unknown ghost type");
            }

            ghost = new GhostImpl(
                    image,
                    boundingBox,
                    kinematicState,
                    GhostMode.SCATTER,
                    targetCorner,
                    renderableType, // the renderableType character is the ghost's name for ID purposes,
                    IMAGES.get(renderableType),
                    FRIGHTENED_IMAGE
            );

            /*
            - here we initialise the unique ghost behaviour for each ghost type.
            - logic for updating the target position based on the ghost's unique behaviour is done in LevelImpl's tick()
             */
            ghost.setGhostBehaviour(chaseBehaviour);

            return ghost;

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }
}
