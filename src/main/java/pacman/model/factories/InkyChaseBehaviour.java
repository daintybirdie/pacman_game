package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Vector2D;

public class InkyChaseBehaviour implements GhostBehaviour{
    @Override
    public void move(GhostImpl ghost) {
        Vector2D targetLocation = ghost.getPlayerPosition();
        ghost.setPosition(targetLocation);
    }
}
