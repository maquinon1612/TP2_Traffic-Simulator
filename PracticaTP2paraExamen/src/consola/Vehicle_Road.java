package consola;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class Vehicle_Road implements TrafficSimObserver {

	private Map<String , Road> mapa;
	
	public Vehicle_Road(Controller ctr) {
		mapa = new HashMap<String , Road>();
		ctr.addObserver(this);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		for(Vehicle v: map.getVehicles()) {
			if(v.getstatus() == VehicleStatus.TRAVELING) {
				String id = v.getId();
				mapa.put(id, v.getRoad());
			}
		}
		
	}
	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		mapa = new HashMap<>();
		
	}
	@Override
	public void onError(String err) {
		mapa.clear();
	}
	
	public String toString() {
		String ret = "";
		
		for(Map.Entry<String , Road> entrada : mapa.entrySet()) {
			
			ret  =ret + "Vehicle= " + entrada.getKey() + ", Road = " + entrada.getValue().getId() + "\n" ;
		
		}
		
		return ret;
	}
	
}
