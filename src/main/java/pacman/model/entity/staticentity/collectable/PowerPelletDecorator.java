package pacman.model.entity.staticentity.collectable;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.level.Level;

public class PowerPelletDecorator extends PelletDecorator {
    private final int points = 50;

    public PowerPelletDecorator(Collectable collectable) {
        super(collectable);
    }

    @Override
    public void collect(Level level) {
        super.collect(level);
        for (Ghost ghost : level.getGhosts()) {
            ghost.setGhostMode(GhostMode.FRIGHTENED);
            ghost.setCurrentState(ghost.getNormalState());
            ghost.transitionState();
            ghost.resetCount();
            level.setTickCount(0);
        }
        level.setCurrentGhostMode(GhostMode.FRIGHTENED);
    }

    @Override
    public int getPoints() {
        return points;
    }

}

