//package pacman.model.factories;
//
//import pacman.model.entity.dynamic.ghost.GhostImpl;
//import pacman.model.entity.dynamic.physics.Direction;
//import pacman.model.entity.dynamic.physics.Vector2D;
//
//public class InkyChaseBehaviour implements GhostBehaviour {
//    private static final double DISTANCE = 2; // Tiles ahead of Pacman
//
//    @Override
//    public void chasePostition(GhostImpl ghost) {
//        Vector2D pacmanPosition = ghost.getTargetLocation();
//        double x = pacmanPosition.getX();
//        double y = pacmanPosition.getY();
//        Vector2D pacmanPlus2;
//        Direction pacmanDirection = ghost.getDirection();
//        if (pacmanDirection == Direction.RIGHT) {
//            pacmanPlus2 = new Vector2D(DISTANCE + x, y );
//        }
//        else if (pacmanDirection == Direction.LEFT) {
//            pacmanPlus2 = new Vector2D(-DISTANCE + x, y );
//        }
//        else if (pacmanDirection == Direction.UP) {
//            pacmanPlus2 = new Vector2D( x, y + DISTANCE);
//        }
//        else {
//            pacmanPlus2 = new Vector2D( x, y - DISTANCE);
//        }
//
//    }
//}
