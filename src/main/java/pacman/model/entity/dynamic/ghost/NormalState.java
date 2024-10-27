package pacman.model.entity.dynamic.ghost;

public class NormalState implements GhostState {
    @Override
    public void activate(Ghost ghost) {
        ghost.setCurrentImage(ghost.getNormalImage()); // Set to normal image
        //scatter mode already assigned when deactivating from frightened state
    }

    @Override
    public void deactivate(Ghost ghost) {
        // No action needed when leaving normal state, because the activate method in frightened state takes care of this.
    }

    @Override
    public GhostState transitionToNextState(Ghost ghost) {
        if ((ghost.getCurrentState() instanceof NormalState)) {
            this.deactivate(ghost);
            ghost.setCurrentState(ghost.getFrightenedState());
            ghost.getCurrentState().activate(ghost);
        }
        return ghost.getFrightenedState();
    }
}
