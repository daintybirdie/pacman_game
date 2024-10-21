package pacman.model.entity.dynamic.ghost;

public interface GhostState {
    void activate(GhostImpl ghost);
    void deactivate(GhostImpl ghost);
}

