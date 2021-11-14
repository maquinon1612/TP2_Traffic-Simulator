package consola;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionRoadGreen implements TrafficSimObserver {

	private Map<Junction , Road> mapa;
	
	public JunctionRoadGreen(Controller ctr) {
		mapa = new HashMap<Junction , Road>();
		ctr.addObserver(this);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
		for(Junction j: map.getJunctions()) {
			
			int ind = j.getind_green();
			
			if(ind != -1) {
				Road r = j.getlist_road_entran().get(ind);
				mapa.put(j , r);
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
		
		for(Map.Entry<Junction , Road> entrada : mapa.entrySet()) {
			
			ret  =ret + "Juntion = " + entrada.getKey().getId() + ", Road = " + entrada.getValue().getId() + "\n" ;
		
		}
		
		return ret;
	}
	
}
