package simulator.model;

import java.util.List;

public class MoveFirstStrategy implements DequeingStrategy{
	
	public MoveFirstStrategy(){}

	public List<Vehicle> dequeue(List<Vehicle> q) {
		if(q.size() == 0) {
			return null;
		}
		else {
			return q.subList(0, 1);
		}
	}

}
