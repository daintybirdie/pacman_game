package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;

import java.util.List;

public interface GhostBehaviour {
    void chasePostition(List<Ghost> ghosts);
}
