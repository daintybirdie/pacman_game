package pacman.model.entity.staticentity.collectable.compositepattern;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.level.Level;

public class PowerPelletEffect implements CollectEffect{
    int points;

    Pellet pellet;
    public PowerPelletEffect(Pellet pellet) {
        this.pellet = pellet;
    }
    @Override
    public void collect(Level level) {
        level.getCollectables().remove(pellet);
        this.points = pellet.getPoints();
        level.notifyObserversWithScoreChange(points);
        for (Ghost ghost : level.getGhosts()) {
            System.out.println("NOW CHANGING");
            ghost.setGhostMode(GhostMode.FRIGHTENED);
            ghost.setCurrentState(ghost.getNormalState());
            ghost.transitionState();
            ghost.resetCount();
            level.setTickCount(0);
        }
        level.setCurrentGhostMode(GhostMode.FRIGHTENED);
    }
}
