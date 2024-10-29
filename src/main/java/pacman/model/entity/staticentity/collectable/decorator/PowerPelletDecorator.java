package pacman.model.entity.staticentity.collectable.decorator;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.Level;

/**
 Concrete class that decorates objects of type Collectable, to create a PowerPellet
 */
public class PowerPelletDecorator extends CollectableDecorator {
    private final int points = 50;

    public PowerPelletDecorator(Collectable collectable) {
        super(collectable);
    }

    @Override
    public void collect(Level level) {
        // call CollectableDecorator's collect method
        super.collect(level);
        for (Ghost ghost : level.getGhosts()) {
            // Set all ghosts in normal mode, accounting for when a power pellet is eaten when  in FRIGHTENED mode
            // This ensures when ghosts transition, they all transition to FRIGHTENED mode
            ghost.setCurrentState(ghost.getNormalState());
            // Transition into Frightened state, since only 2 states (either frightened or normal)
            ghost.transitionState();
            // Resetting internal counter for each ghost
            ghost.resetCount();
            level.setTickCount(0);
        }
        // Global variable for all ghosts, dictating their maximum stay duration in FRIGHTENED mode
        // This is they aren't eaten by PacMan
        level.setCurrentGhostMode(GhostMode.FRIGHTENED);
    }

    @Override
    public int getPoints() {
        return points;
    }

}

