package pacman.model.entity.dynamic.ghost;

public interface GhostState {
    //There are 2 states, frightened mode (GhostMode.FRIGHTENED) AND normal mode (GhostMode.SCATTER + GhostMode.Chase)
    void activate();
    void deactivate();
    GhostState transitionToNextState();

}

