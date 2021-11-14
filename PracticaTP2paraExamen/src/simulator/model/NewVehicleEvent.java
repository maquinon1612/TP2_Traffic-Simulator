package simulator.model;

import java.util.LinkedList;

import excepciones.mapainvalido;
import excepciones.parametrosincorrectos;
import java.util.List;

public class NewVehicleEvent extends Event{
	
	private String _id;
	private int contCls;
	private int maxspd;
	List<String> Ity;

	public NewVehicleEvent(int time, String id, int maxSpeed, int
			contClass, List<String> itinerary) {
			super(time);
			
			_id = id;
			maxspd = maxSpeed;
			contCls = contClass;
			Ity = itinerary;
	}

	@Override
	void execute(RoadMap map) throws mapainvalido  {
		
		try {
			List<Junction> lj = new LinkedList<Junction>();
		
			for(String s : Ity) {
				Junction j = map.getJunction(s);
				if(j != null) {
					lj.add(j);
				}
				else {
					throw new mapainvalido("Incluye todos los parametros necesarios");
				}
			}
		
			Vehicle v = new Vehicle(_id , maxspd , contCls, lj);
			map.addVehicle(v);
			v.moveToNextRoad();
		} catch (parametrosincorrectos e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return "New Vehicle '"+_id+"'";
	}

}
