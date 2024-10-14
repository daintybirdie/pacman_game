package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;

import java.util.List;

public interface GhostBehaviour {
    // trial and error for ghosts
    public static final int OFFSET = 0;
    public static final int MIN_X = 0;
    public static final int MAX_X = 448;
    public static final int MIN_Y = 16*2;
    public static final int MAX_Y = 576 - 16*3;
    void chasePostition(List<Ghost> ghosts);
}
