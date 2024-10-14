package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.Vector2D;

public class PinkyChaseBehaviour implements  GhostBehaviour{
    @Override
    public void move(GhostImpl ghost) {
        Vector2D targetLocation = ghost.getPlayerPosition();
        ghost.setPosition(targetLocation.add(new Vector2D(targetLocation.getX(), targetLocation.getY() + 4)));
    }
}
