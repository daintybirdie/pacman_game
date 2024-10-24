package pacman.model.entity.dynamic.ghost;

public class FrightenedState implements GhostState {
    @Override
    public void activate(GhostImpl ghost) {
        ghost.setGhostMode(GhostMode.FRIGHTENED);
        ghost.setCurrentImage(ghost.getFrightenedImage());
    }

    @Override
    public void deactivate(GhostImpl ghost) {
        ghost.setGhostMode(GhostMode.SCATTER);
        ghost.setCurrentImage(ghost.getNormalImage());
    }
}
