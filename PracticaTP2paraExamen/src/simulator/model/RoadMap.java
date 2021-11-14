package simulator.model;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import excepciones.carreterainvalida;
import excepciones.parametrosincorrectos;

public class RoadMap {
	private List<Junction> junclist;
	private List<Road> roadlist;
	private List<Vehicle> vehilist;
	private Map<String,Junction> juncmap;
	private Map<String,Road> roadmap;
	private Map<String,Vehicle> vehimap;
	
	RoadMap(){
		junclist = new LinkedList<Junction>();
		roadlist = new LinkedList<Road>();
		vehilist = new LinkedList<Vehicle>();
		juncmap = new HashMap<String, Junction>();
		roadmap = new HashMap<String, Road>();
		vehimap = new HashMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) throws IOException {
		if(!juncmap.containsKey(j.getId())) {
			junclist.add(j);
			juncmap.put(j.getId(), j);
		}
		else {
			throw new IOException("Junction already added");
		}
	}
	
	void addRoad(Road r) throws IOException {
		if(!roadmap.containsKey(r.getId())) { 
			try {
				if(juncmap.get(r.getDestination().getId()) != null) {
					if(juncmap.get(r.getSource().getId()) != null) {
						juncmap.get(r.getDestination().getId()).addIncommingRoad(r);
						juncmap.get(r.getSource().getId()).addOutGoingRoad(r);
						roadlist.add(r);
						roadmap.put(r.getId(), r);
					}
				}
			}
			catch (carreterainvalida e) {
				System.out.println(e.getMessage());
			}
		}
		else {
			throw new IOException("Road already added");
		}
	}
	
	void addVehicle(Vehicle v) throws parametrosincorrectos {
		boolean enable = true;
		if(!vehimap.containsKey(v.getId())) { 
			for(int i = 0 ; i < v.getItinerary().size() - 1 ; i++) { //juntions en itinerary unidos por carreteras
				if(v.getItinerary().get(i).roadTo(v.getItinerary().get(i + 1)) == null) { //que la interseccion tenga carretera entre ella y su siguiente
					enable = false;
				}
			}
			if(enable) {
				vehilist.add(v);
				vehimap.put(v.getId(), v);
			}
		}
		else {
			throw new parametrosincorrectos("Incluye todos los parametros necesarios");
		}
	}
	
	public Junction getJunction(String id) {
		return juncmap.get(id);
	}
	
	public Road getRoad(String id) {
		return roadmap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return vehimap.get(id);
	}
	
	public List<Junction> getJunctions(){
        List<Junction> list = Collections.unmodifiableList(junclist);
		return list;
	}
	
	public List<Road> getRoads(){
        List<Road> list = Collections.unmodifiableList(roadlist);
		return list;
	}
	
	public List<Vehicle> getVehicles(){
        List<Vehicle> list = Collections.unmodifiableList(vehilist);
		return list;
	}
	
	void reset() {
		junclist.clear();
		roadlist.clear();
		vehilist.clear();
		juncmap.clear();
		roadmap.clear();
		vehimap.clear();
	}
	
	public JSONObject report() {
		
		JSONArray jonsados= new JSONArray() , 
				  jonsatres = new JSONArray() , 
				  jonsauno = new JSONArray();
		JSONObject J = new JSONObject();
		
		
		for(Junction j : junclist) {
			jonsatres.put(j.report());
		}
		
		for(Road r : roadlist) {
			jonsados.put(r.report());
		}
		
		for(Vehicle v : vehilist) {
			jonsauno.put(v.report());
		}
		
		J.put("junctions", jonsatres);
		J.put("road", jonsados);
		J.put("vehicles", jonsauno);
		
		return J;
	}
}
