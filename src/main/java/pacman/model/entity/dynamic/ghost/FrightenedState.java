package pacman.model.entity.dynamic.ghost;

public class FrightenedState implements GhostState {
    @Override
    public void activate(Ghost ghost) {
        ghost.setGhostMode(GhostMode.FRIGHTENED);
        ghost.setCurrentImage(ghost.getFrightenedImage());
    }

    @Override
    public void deactivate(Ghost ghost) {
        ghost.setGhostMode(GhostMode.SCATTER);
        ghost.setCurrentImage(ghost.getNormalImage());
    }

    @Override
    public GhostState transitionToNextState(Ghost ghost) {
        if ((ghost.getCurrentState() instanceof FrightenedState)) {
            this.deactivate(ghost);
            ghost.setCurrentState(ghost.getNormalState());
            ghost.getCurrentState().activate(ghost);
        }
        return ghost.getNormalState();
    }
}
