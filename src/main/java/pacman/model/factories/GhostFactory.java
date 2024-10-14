package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete renderable factory for Ghost objects
 */
public class GhostFactory implements RenderableFactory {

    private static final int RIGHT_X_POSITION_OF_MAP = 448;
    private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;
    private static final Image BLINKY_IMAGE = new Image("maze/ghosts/blinky.png");
    private static final Image INKY_IMAGE = new Image("maze/ghosts/inky.png");
    private static final Image CLYDE_IMAGE = new Image("maze/ghosts/clyde.png");
    private static final Image PINKY_IMAGE = new Image("maze/ghosts/pinky.png");
    private static final Image GHOST_IMAGE = BLINKY_IMAGE;
    private char renderableType;
    List<Vector2D> targetCorners = Arrays.asList(
            new Vector2D(0, TOP_Y_POSITION_OF_MAP),
            new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP),
            new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP),
            new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP)
    );
    private final Image image;

    static Map<Character, Image> IMAGES = new HashMap<>();

    static {
        IMAGES.put('b', BLINKY_IMAGE);
        IMAGES.put('s', PINKY_IMAGE);
        IMAGES.put('i', INKY_IMAGE);
        IMAGES.put('c', CLYDE_IMAGE);
    }

    public GhostFactory(char renderableType) {
        this.renderableType = renderableType;
        this.image = IMAGES.get(renderableType);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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

            GhostImpl ghost;
            Vector2D targetCorner = null;
            GhostBehaviour chaseBehaviour = null;
            char name;

            switch (renderableType) {
                case 'c':
                    chaseBehaviour = new ClydeChaseBehaviour();
                    targetCorner = new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP);
                    name = renderableType;
                    break;
                case 'b':
                    chaseBehaviour = new BlinkyChaseBehaviour();
                    targetCorner = new Vector2D(RIGHT_X_POSITION_OF_MAP, TOP_Y_POSITION_OF_MAP);
                    name = renderableType;
                    break;
                case 'i':
                    chaseBehaviour = new InkyChaseBehaviour();
                    targetCorner = new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP);
                    name = renderableType;
                    break;
                case 's':
                    chaseBehaviour = new PinkyChaseBehaviour();
                    targetCorner = new Vector2D(0, TOP_Y_POSITION_OF_MAP);
                    name = renderableType;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown ghost type");
            }

            ghost = new GhostImpl(
                    image,
                    boundingBox,
                    kinematicState,
                    GhostMode.SCATTER,
                    targetCorner,
                    name
            );

            ghost.setGhostBehaviour(chaseBehaviour);

            return ghost;

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }


}
