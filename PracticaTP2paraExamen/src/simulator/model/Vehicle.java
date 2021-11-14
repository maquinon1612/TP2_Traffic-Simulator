package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import excepciones.carreterainvalida;
import excepciones.parametrosincorrectos;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle>{
	
	public Road getRoad() {
		return road;
	}

	private List<Junction> itinerary;
	
	private int maximum_speed;
	
	private int current_speed;
	
	private VehicleStatus status;
	
	private Road road;

	private int location;
	
	private int contamination_class;
	
	private int total_contamination;
	
	private int total_travelled_distance;
	
	int currint;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws parametrosincorrectos {
		super(id);
		if(maxSpeed > 0 && contClass >= 0 && contClass <= 10 && itinerary.size() >= 2) {
				currint = 0;
				location = 0;
				status = VehicleStatus.PENDING;
				total_travelled_distance = 0;
				maximum_speed = maxSpeed;
				contamination_class = contClass;
				this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		}
		else {
			throw new parametrosincorrectos("Datos no validos");
		}
	}
	
	public int getContamination_class() {
		return contamination_class;
	}

	public int getCurrent_speed() {
		return current_speed;
	}
	
	public int getLocation() {
		return location;
	}
///////////////////////////////////////////////
	@Override
	void advance(int time) {
		try {
			if(status == VehicleStatus.TRAVELING) {
				int i = 0;
				while(i < time) {
					total_travelled_distance +=  Math.min(current_speed,road.getLenght() - location);
					location += Math.min(current_speed,road.getLenght() - location);
					int c = contamination_class*Math.min(current_speed,road.getLenght());
					total_contamination += c;
					road.addContamination(c);
					if(location >= road.getLenght()) {
						itinerary.get(itinerary.indexOf(road.getDestination())).enter(this);
						current_speed = 0;
						status = VehicleStatus.WAITING;
					}
					i++;
				}
			}
		} catch (carreterainvalida | parametrosincorrectos e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public JSONObject report() {
		
		JSONObject jo2 = new JSONObject();
		
		jo2.put("id", _id);
		jo2.put("speed", current_speed);
		jo2.put("distance", total_travelled_distance);
		jo2.put("co2", total_contamination);
		jo2.put("class", contamination_class);
		jo2.put("status", status);
		if(status != VehicleStatus.ARRIVED) {
			jo2.put("road", road);
			jo2.put("location", location);
		}
		
		return jo2;
	}
	
	public void moveToNextRoad() throws parametrosincorrectos {
		if(status == VehicleStatus.PENDING) {
			itinerary.get(0).roadTo(itinerary.get(1)).enter(this);
			status = VehicleStatus.TRAVELING;
			currint++;
		}
		else if(status == VehicleStatus.WAITING){
			road.exit(this);
			location = 0;
			current_speed = 0;
			if(currint == itinerary.size() - 1) {
				status = VehicleStatus.ARRIVED;
				road = null;
			}
			else {
				itinerary.get(currint).roadTo(itinerary.get(currint + 1)).enter(this);
				status = VehicleStatus.TRAVELING;
				currint++;
			}
		}
		else {
			throw new parametrosincorrectos("El vehiculo no se puede mover de carretera");
		}
	}
	
	public void setroad(Road r) {
		road = r;
	}
	
////////////////////////////////////////////
	public void setSpeed(int s) throws parametrosincorrectos {
		if(s > 0) {
			current_speed = Math.min(s,maximum_speed);
		}
		else {
			throw new parametrosincorrectos("No puede ser negativa la velocidad");
		}
	}
	
	void setContaminationClass(int c) throws parametrosincorrectos {
		if( c >= 0 && c <= 10) {
			contamination_class = c;
		}
		else {
			throw new parametrosincorrectos("Tiene que ser un valor entre 0 y 10");
		}
	}
	
	public List<Junction> getItinerary(){
        List<Junction> list = Collections.unmodifiableList(itinerary);
		return list;
	}

	@Override
	public int compareTo(Vehicle o) {
		int resultado=0;

        if (this.location < o.location){
        	resultado = -1;
        }

        else if (this.location > o.location) {
        	resultado = 1;
        }

        else {
        	resultado = 0;
        }

        return resultado;
	}

	public VehicleStatus getstatus() {
		return status;
	}
	
	public int getmaximum_speed(){
		return maximum_speed;
	}
	
	public int getdistance() {
		return total_travelled_distance;
	}
	
	public int gettotalCO2() {
		return total_contamination;
	}
}
