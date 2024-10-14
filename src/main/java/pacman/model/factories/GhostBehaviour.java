package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.GhostImpl;

public interface GhostBehaviour {
    void move(GhostImpl ghost);
}
