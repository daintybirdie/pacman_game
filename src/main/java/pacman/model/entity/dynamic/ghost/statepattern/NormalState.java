package pacman.model.entity.dynamic.ghost.statepattern;

import pacman.model.entity.dynamic.ghost.Ghost;

public class NormalState implements GhostState {
    Ghost ghost;

    public NormalState(Ghost ghost) {
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
        // This is because there are only two possible states: FrightenedState or NormalState
    }

    @Override
    public GhostState transitionToNextState() {
        // Only transition to FrightenedState instance, if current state is NormalState
        if ((ghost.getCurrentState() instanceof NormalState)) {
            this.deactivate();
            ghost.setCurrentState(ghost.getFrightenedState());
            ghost.getCurrentState().activate();
        }
        return ghost.getFrightenedState();
    }
}
