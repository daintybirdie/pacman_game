package pacman.model.entity.dynamic.ghost;

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
    }

    @Override
    public GhostState transitionToNextState() {
        if ((ghost.getCurrentState() instanceof NormalState)) {
            this.deactivate();
            ghost.setCurrentState(ghost.getFrightenedState());
            ghost.getCurrentState().activate();
        }
        return ghost.getFrightenedState();
    }
}
