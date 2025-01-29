1. How to run the code
Please click on the Stage (main window) in order to interact with the game

2. Packages and classes involved

Unique chasing behaviour (Strategy pattern):
- Package Name: pacman.model.entity.dynamic.ghost.chasestrategy
- Classes involved in design pattern: BlinkyChaseBehaviour, InkyChaseBehaviour, PinkyChaseBehaviour, ClydeChaseBehaviour, GhostChaseBehaviour
- Classes involved to implement functionality: Ghost, GhostImpl, LevelImpl, GhostFactory

Activating/Deactivating Frightened Mode (State pattern):
- Package name: pacman.model.entity.dynamic.ghost.statepattern
- Classes involved in design pattern: GhostState, FrightenedState, NormalState, Ghost
- Classes involved to implement functionality: GhostImpl, LevelImpl, PowerPelletDecorator


Power Pellet Behaviour (Decorator pattern)
- Package name (where new classes added): pacman.model.entity.staticentity.collectable.decorator
- Classes involved in design pattern: CollectableDecorator, PowerPelletDecorator, Pellet, Collectable, 
- Classes involved to implement functionality: StaticEntityImpl, PelletFactory 



