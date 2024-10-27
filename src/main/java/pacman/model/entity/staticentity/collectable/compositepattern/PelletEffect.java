package pacman.model.entity.staticentity.collectable.compositepattern;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.level.Level;

public class PelletEffect implements CollectEffect{
    int points;
    Pellet pellet;
    public PelletEffect(Pellet pellet) {
        this.pellet = pellet;
    }
    @Override
    public void collect(Level level) {
        level.getCollectables().remove(pellet);
        this.points = pellet.getPoints();
        level.notifyObserversWithScoreChange(points);
    }
}
