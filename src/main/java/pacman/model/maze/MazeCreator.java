package pacman.model.maze;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.factories.RenderableFactoryRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Responsible for creating renderables and storing it in the Maze
 */
public class MazeCreator {

    public static final int RESIZING_FACTOR = 16;
    private final String fileName;
    private final RenderableFactoryRegistry renderableFactoryRegistry;

    public MazeCreator(String fileName,
                       RenderableFactoryRegistry renderableFactoryRegistry) {
        this.fileName = fileName;
        this.renderableFactoryRegistry = renderableFactoryRegistry;
    }

    public Maze createMaze() {
        File f = new File(this.fileName);
        Maze maze = new Maze();

        try {
            Scanner scanner = new Scanner(f);

            int y = 0;

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                char[] row = line.toCharArray();
                // moved variable declaration here for ease of understanding
                Vector2D position;
                char renderableType;
                Renderable renderable;

                for (int x = 0; x < row.length; x++) {
                    position = new Vector2D(x * RESIZING_FACTOR, y * RESIZING_FACTOR);

                    renderableType = row[x];

                    if (renderableType == 'z') {
                        position = new Vector2D(position.getX() -8, position.getY() - 8);
                    }
                    // had to offset ghosts - collision between pacman + ghosts not detected in some corridors
                    if (renderableType == 'b'||renderableType == 's'||renderableType == 'i'||renderableType == 'c') {
                        position = new Vector2D(position.getX() -4, position.getY() - 4);
                    }

                    renderable = renderableFactoryRegistry.createRenderable(
                            renderableType, position
                    );

                    maze.addRenderable(renderable, renderableType, x, y);
                }

                y += 1;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No maze file was found.");
            exit(0);
        }

        return maze;
    }
}
