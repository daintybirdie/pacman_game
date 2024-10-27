package pacman.model.entity.dynamic.ghost;

public class FrightenedState implements GhostState {
    Ghost ghost;

    public FrightenedState(Ghost ghost) {
        this.ghost = ghost;
    }
    @Override
    public void activate() {
        ghost.setGhostMode(GhostMode.FRIGHTENED);
        ghost.setCurrentImage(ghost.getFrightenedImage());
    }

    @Override
    public void deactivate() {
        ghost.setGhostMode(GhostMode.SCATTER);
        ghost.setCurrentImage(ghost.getNormalImage());
    }

    @Override
    public GhostState transitionToNextState() {
        if ((ghost.getCurrentState() instanceof FrightenedState)) {
            this.deactivate();
            ghost.setCurrentState(ghost.getNormalState());
            ghost.getCurrentState().activate();
        }
        return ghost.getNormalState();
    }
}
