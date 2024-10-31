package pacman.model.entity.dynamic.ghost.statepattern;

import pacman.model.entity.dynamic.ghost.Ghost;

public class NonFrightenedState implements GhostState {
    Ghost ghost;

    public NonFrightenedState(Ghost ghost) {
        this.ghost = ghost;
    }
    @Override
    public void activate() {
        ghost.setCurrentImage(ghost.getNormalImage()); // Set to normal image
        //scatter mode already assigned when deactivating from frightened state
    }

    @Override
    public void deactivate() {
        // No action needed when leaving normal state, because the activate method in frightened state takes care of this.
        // This is because there are only two possible states: FrightenedState or NonFrightenedState
    }

    @Override
    public GhostState transitionToNextState() {
        // Only transition to FrightenedState instance, if current state is NonFrightenedState
        if ((ghost.getCurrentState() instanceof NonFrightenedState)) {
            this.deactivate();
            ghost.setCurrentState(ghost.getFrightenedState());
            ghost.getCurrentState().activate();
        }
        return ghost.getFrightenedState();
    }
}
