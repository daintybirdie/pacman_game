package pacman.model.entity.staticentity.collectable.compositepattern;

import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.level.Level;

public class PelletEffect implements CollectEffect{
    int points;
    @Override
    public void collect(Level level, Collectable collectable) {
        level.getCollectables().remove(collectable);
        Pellet pellet = (Pellet) collectable;
        this.points = pellet.getPoints();
        level.notifyObserversWithScoreChange(points);
    }
}
