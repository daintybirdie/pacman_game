package pacman.model.entity.dynamic.ghost.statepattern;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;

public class FrightenedState implements GhostState {
    Ghost ghost;

    public FrightenedState(Ghost ghost) {
        this.ghost = ghost;
    }
    @Override
    public void activate() {
        // setting mode to FRIGHTENED mode, will be applied to each individual ghost upon PacMan consuming a Power Pellet
        ghost.setGhostMode(GhostMode.FRIGHTENED);
        // Setting image to the new image
        ghost.setCurrentImage(ghost.getFrightenedImage());
    }

    @Override
    public void deactivate() {
        // Setting mode to SCATTER when FRIGHTENED mode finished
        ghost.setGhostMode(GhostMode.SCATTER);
        // Set image back to the image of the ghosts, pre power pellet consumption
        ghost.setCurrentImage(ghost.getNormalImage());
    }

    @Override
    public GhostState transitionToNextState() {
        // Only transition to NormalState instance, if current state is FrightenedState
        if ((ghost.getCurrentState() instanceof FrightenedState)) {
            this.deactivate();
            ghost.setCurrentState(ghost.getNormalState());
            ghost.getCurrentState().activate();
        }
        return ghost.getNormalState();
    }
}
