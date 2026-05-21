package domain;

import java.io.Serializable;

public abstract class ColliderEnemy implements Serializable {

	protected Enemy enemy;
	protected CollisionChecker cCheker;
	protected CollisionContext context;
	
	
	
}
