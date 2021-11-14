package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import excepciones.mapainvalido;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{

	private RoadMap roadmap;
	
	private List<Event> le;
	
	private int time;
	
	private List<TrafficSimObserver> lo;
	
	public TrafficSimulator () {
		roadmap = new RoadMap();
		le = new SortedArrayList<Event>();
		lo = new ArrayList<>();
		time = 0;
	}
	
	public void addEvent(Event e) {
		le.add(e);
		createonEventAdded(e);
	}

	public void advance() {
		try {
			
			time++;
			createonAdvanceStart();
			
			while(le.size() != 0 && le.get(0).getTime() == time) {
				le.get(0).execute(roadmap);
				le.remove(0);
			}
		
			for(Junction j : roadmap.getJunctions()) {
				j.advance(time);
			}
		
			for(Road r : roadmap.getRoads()) {
				r.advance(1);
			}
			
			createonAdvanceEnd();
			
		} catch (mapainvalido e) {
			createonError(e.getMessage());
			System.out.println(e.getMessage());//dice de volver a lanzarla pero no la lanzo
		}
	}
	

	public void reset(){
		roadmap.reset();
		le.clear();
		time = 0;
		createonReset(); 
	}

	public JSONObject report() {
		
		JSONObject JONSON = new JSONObject();
		
		JONSON.put("time", time);
		JONSON.put("state", roadmap.report());
		
		return JONSON;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		if(!lo.contains(o)) {
			lo.add(o);
			o.onRegister(roadmap, le, time);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		lo.remove(o);
	}
	
	private void createonAdvanceEnd() {
		for(TrafficSimObserver o : lo) {
			o.onAdvanceEnd(roadmap, le, time);
		}
		
	}

	private void createonError(String message) {
		for(TrafficSimObserver o : lo) {
			o.onError(message);
		}
	}

	private void createonEventAdded(Event e) {
		for(TrafficSimObserver o : lo) {
			o.onEventAdded(roadmap, le, e , time);
		}
	}
	
	private void createonAdvanceStart() {
		for(TrafficSimObserver o : lo) {
			o.onAdvanceStart(roadmap, le , time);
		}
	}
	

	private void createonReset() {
		for(TrafficSimObserver o : lo) {
			o.onReset(roadmap, le , time);
		}
	}
	
}