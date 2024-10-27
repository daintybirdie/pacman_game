package pacman.model.entity.staticentity.collectable.compositepattern;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.level.Level;

public class PowerPelletEffect implements CollectEffect{
    int points;

    @Override
    public void collect(Level level, Collectable collectable) {
        level.getCollectables().remove(collectable);
        Pellet pellet = (Pellet) collectable;
        this.points = pellet.getPoints();
        level.notifyObserversWithScoreChange(points);
        for (Ghost ghost : level.getGhosts()) {
            ghost.setGhostMode(GhostMode.FRIGHTENED);
            ghost.setCurrentState(ghost.getNormalState());
            ghost.transitionState();
            ghost.resetCount();
            level.setTickCount(0);
        }
        level.setCurrentGhostMode(GhostMode.FRIGHTENED);
    }
}
