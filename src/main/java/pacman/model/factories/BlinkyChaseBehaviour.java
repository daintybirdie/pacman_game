package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;

import java.util.List;


public class BlinkyChaseBehaviour implements GhostBehaviour {

    @Override
    public void chasePosition(List<Ghost> ghosts) {
        //no specialised behaviour for blinky.
    }

}
