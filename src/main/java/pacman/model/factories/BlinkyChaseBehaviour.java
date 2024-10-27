package pacman.model.factories;

import pacman.model.entity.dynamic.ghost.Ghost;

import java.util.List;


public class BlinkyChaseBehaviour implements GhostChaseBehaviour {

    @Override
    public void chasePosition(List<Ghost> ghosts) {
        //no specialised behaviour for blinky.
    }

}
