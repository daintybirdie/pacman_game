package pacman.model.chasebehaviour;

import pacman.model.entity.dynamic.ghost.Ghost;

import java.util.List;

public interface GhostChaseBehaviour {
    void chasePosition(List<Ghost> ghosts);
}
