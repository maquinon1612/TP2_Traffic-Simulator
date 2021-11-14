package simulator.model;

import java.util.List;

public class MoveAllStrategy implements DequeingStrategy{
	
	public MoveAllStrategy(){}

	public List<Vehicle> dequeue(List<Vehicle> q) {
		if(q.size() == 0) {
			return null;
		}
		else {
			return q.subList(0, q.size());
		}
	}

}
