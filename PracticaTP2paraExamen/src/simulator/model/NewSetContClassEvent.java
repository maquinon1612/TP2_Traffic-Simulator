package simulator.model;

import java.util.List;

import excepciones.mapainvalido;
import excepciones.parametrosincorrectos;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event{
	
	List<Pair<String,Integer>> cs;

	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) throws parametrosincorrectos { 
		
		super(time);
		if(cs != null) {
			this.cs = cs;
		}
		else {
			throw new parametrosincorrectos("Incluye todos los parametros necesarios");
		}
		
	}



	@Override
	void execute(RoadMap map) throws mapainvalido {
		try {
			for(Pair<String,Integer> c : cs) {
				Vehicle v = map.getVehicle(c.getFirst());
				if(v != null) {
					v.setContaminationClass(c.getSecond());
				}
				else{
					throw new mapainvalido("Vehicle not in map");
				}
			}
		
		} catch (parametrosincorrectos e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		String s = "New Contamination Class '";
		for(Pair<String,Integer> c : cs) {
			s = s + c.getFirst() + ":" + c.getSecond() + " '" ;
		}
		return s;
	}


}
