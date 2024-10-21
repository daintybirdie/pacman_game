package pacman.model.entity.dynamic.ghost;

public class FrightenedState implements GhostState {
    @Override
    public void activate(GhostImpl ghost) {
        ghost.setCurrentImage(ghost.getFrightenedImage()); // Set to frightened image
    }

    @Override
    public void deactivate(GhostImpl ghost) {
        // No action needed when leaving frightened state
    }
}
