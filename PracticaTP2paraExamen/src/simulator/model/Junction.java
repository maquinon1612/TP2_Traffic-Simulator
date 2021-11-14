package simulator.model;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import excepciones.carreterainvalida;
import excepciones.parametrosincorrectos;


public class Junction extends SimulatedObject{
	
	List<Road> list_road_entran;
	Map<Junction,Road> map_road_sal;
	List<List<Vehicle>> list_colas;
	Map<Road,List<Vehicle>> map_que;
	
	int ind_green;
	int lastSwitchingTime;
	int x;
	int y;
	
	LightSwitchingStrategy strat_change_light;
	DequeingStrategy strat_ext_queue;
	
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeingStrategy dqStrategy, int xCoor, int yCoor) throws parametrosincorrectos {
			super(id);
			if(lsStrategy != null && dqStrategy != null && xCoor >= 0 && yCoor >= 0) {
				strat_change_light = lsStrategy;
				strat_ext_queue = dqStrategy;
				list_road_entran = new LinkedList<Road>();
				map_road_sal = new HashMap<Junction,Road>();
				list_colas = new LinkedList<List<Vehicle>>();
				map_que  = new HashMap<Road,List<Vehicle>>();
				ind_green = -1;
				x = xCoor;
				y = yCoor;
			}
			else {
				throw new parametrosincorrectos("Datos no validos"); 
			}
	}
	
	public void addIncommingRoad(Road r) throws carreterainvalida{
		if(r.getDestination() != this) {
			throw new carreterainvalida("carretera añadida no va a esta interseccion");
		}
		else {
			list_road_entran.add(r);
			List<Vehicle> Lv = new LinkedList<Vehicle> ();
			list_colas.add(Lv);
			map_que.put(r, Lv);
		}
	}
	
	public void addOutGoingRoad(Road r) throws carreterainvalida {
		if(r.getSource() != this) {
			throw new carreterainvalida("carretera añadida no sale de esta interseccion");
		}
		else {
			if(map_road_sal.containsKey(r.getDestination())) {}
			else
			map_road_sal.put(r.getDestination(), r);
		}
	}
	
	public void enter(Vehicle v) throws carreterainvalida {
		
		int pos = -1;
		
		for(int i = 0 ; i < list_road_entran.size() ; i++) {
			if(list_road_entran.get(i).equals(v.getRoad())) {
				pos = i;
			}
		}
		
		if(pos == -1) {
			throw new carreterainvalida("El coche viene de una carretera no valida");
		}
		
		list_colas.get(pos).add(v);
	}

	public Road roadTo(Junction j) {
		return map_road_sal.get(j);
	}
	
	////////////////////////////////////////
	public void advance(int time) {
		try {
			if(list_road_entran.size() != 0) {
				if(ind_green != -1) {
					List<Vehicle> Lv = strat_ext_queue.dequeue(map_que.get(list_road_entran.get(ind_green)));
					if(Lv != null) {
						for(Vehicle v: Lv) {
							v.moveToNextRoad();
							list_colas.get(ind_green).remove(v);
						}
					}
				}
				int indice = strat_change_light.chooseNextGreen(list_road_entran, list_colas, ind_green, lastSwitchingTime, time);
				if(indice != ind_green) {
					lastSwitchingTime = time;
				}
				ind_green = indice;
			}
		} catch (parametrosincorrectos e) {
			System.out.println(e.getMessage());
		}
	}
    ////////////////////////////////////////
	

	public JSONObject report() {
		
		String info = "";
		JSONArray jonsados = new JSONArray();
		JSONArray jonsater = new JSONArray();
		JSONObject jo2 = new JSONObject();
		
		jo2.put("id", _id);
		
		if (list_road_entran.size() != 0) {
			if(ind_green == -1) {
				info = "none";
			}
			else{
				info = list_road_entran.get(ind_green).getId();
			}
			
			for(int i = 0 ; i < list_road_entran.size() ; i++) {
				JSONObject JONSONJR = new JSONObject();
				jonsados = new JSONArray();
				JONSONJR.put("road", list_road_entran.get(i).getId());
				for(int j = 0 ; j < list_colas.get(i).size() ; j++) {
					if(list_colas.get(i).size() != 0){
						jonsados.put(list_colas.get(i).get(j).getId());
					}
				}
				JONSONJR.put("vehicles", jonsados);
				jonsater.put(JONSONJR);
			}
			
		}
		else {
			info = "none";
		}
		
	
		jo2.put("green", info);
		jo2.put("queues", jonsater);
		
		
		
		return jo2;
	}

	public List<List<Vehicle>> getList_colas() {
		return list_colas;
	}

	public int getind_green() {
		return ind_green;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public List<Road> getlist_road_entran() {
		return list_road_entran;
	}
	
	
	
}
