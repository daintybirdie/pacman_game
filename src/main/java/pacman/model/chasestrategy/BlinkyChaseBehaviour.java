package pacman.model.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;

import java.util.List;


public class BlinkyChaseBehaviour implements GhostChaseBehaviour {
    private Ghost blinky;

    @Override
    public void chasePosition(List<Ghost> ghosts) {
        for (Ghost ghost :ghosts) {
            // Get Blinky Ghost
            if (ghost.getName() == 'b') {
                blinky = ghost;
                break;
            }
        }
        // Blinky's target location is default
        blinky.setTargetLocation(blinky.getTargetLocation());
    }

}
