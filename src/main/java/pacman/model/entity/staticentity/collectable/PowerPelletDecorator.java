package pacman.model.entity.staticentity.collectable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.level.Level;

public class PowerPelletDecorator extends PelletDecorator {
    private Pellet pellet;
    private final int points = 50;
    public PowerPelletDecorator(Pellet pellet) {
        super(pellet);
        this.pellet = pellet;
    }

    @Override
    public void collect(Level level) {
        if (level.getCollectables().contains(this)) {
            level.getCollectables().remove(pellet);
            this.displayPoints = points;
            level.notifyObserversWithScoreChange(displayPoints);
            for (Ghost ghost : level.getGhosts()) {
                ghost.setGhostMode(GhostMode.FRIGHTENED);
                ghost.setCurrentState(ghost.getNormalState());
                ghost.transitionState();
                ghost.resetCount();
                level.setTickCount(0);
            }
            level.setCurrentGhostMode(GhostMode.FRIGHTENED);
        }
        else {
            System.out.println(System.identityHashCode(this) + " was already removed.");
        }
    }
}

