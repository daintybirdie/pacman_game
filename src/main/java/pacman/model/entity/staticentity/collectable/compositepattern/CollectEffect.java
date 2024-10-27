package pacman.model.entity.staticentity.collectable.compositepattern;

import pacman.model.entity.Renderable;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.Level;

import java.util.List;

public interface CollectEffect {
     void collect(Level level, Collectable collectable);
}
