package domain.services;

import java.util.ArrayList;
import java.util.List;

import domain.Actor;

public class ActorService {
	
	private static List<Actor> dbActors = new ArrayList<Actor>();
	private static int currentActorId = 0;
	
	public List<Actor> getAll() {
		return dbActors;
	}
	
	public Actor get(int id) {
		for (Actor a : dbActors) {
			if (a.getId() == id)
				return a;
		}
		return null;
	}
	
	public void add(Actor actor) {
		actor.setId(++currentActorId);
		dbActors.add(actor);
	}
	
	public boolean update(Actor actor) {
		Actor a = get(actor.getId());
		if (a != null) {
			a.setFullname(actor.getFullname());
			return true;
		}
		return false;
	}
	
	public void delete(Actor actor) {
		dbActors.remove(actor);
	}
}
