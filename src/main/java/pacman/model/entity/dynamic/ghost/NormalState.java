package pacman.model.entity.dynamic.ghost;

public class NormalState implements GhostState {
    @Override
    public void activate(GhostImpl ghost) {
        ghost.setCurrentImage(ghost.getNormalImage()); // Set to normal image
    }

    @Override
    public void deactivate(GhostImpl ghost) {
        // No action needed when leaving normal state
    }
}
