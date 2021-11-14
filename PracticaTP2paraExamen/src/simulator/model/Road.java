package simulator.model;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import excepciones.parametrosincorrectos;
import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject{
	
	private Junction source;
	private Junction destination;
	
	private int lenght;
	protected int maximum_speed;
	protected int current_speed_limit;
	protected int contamination_alarm_limit;
	protected int total_contaminacion;

	protected Weather weather_conditions;
	
	private  List<Vehicle> vehicles;
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		source = srcJunc;
		destination = destJunc;
		maximum_speed = maxSpeed;
		current_speed_limit = maxSpeed;
		contamination_alarm_limit = contLimit;
		this.lenght = length;
		weather_conditions = weather;
		vehicles = new SortedArrayList<Vehicle>();
	}
	
	public int getLenght() {
		/*if(this.lenght<0) {
			throw new carreterainvalida("Carretera con distancia negativa es invalida");
		}*/
		return lenght;
	}


	@Override
	void advance(int time) {
		try {
			reduceTotalContamination();
			updateSpeedLimit();
			for(Vehicle v : vehicles) {
				v.setSpeed(calculateVehicleSpeed(v));
				v.advance(time);
			}
			vehicles.sort(new Comparator<Vehicle>() {
						@Override
						public int compare(Vehicle v1, Vehicle v2) {
							return v1.compareTo(v2);
						}
						});
		} catch (IOException | parametrosincorrectos e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public JSONObject report() {
		
		JSONArray jonsa = new JSONArray();
		JSONObject JONSON = new JSONObject();
		JSONObject jo2 = new JSONObject();
		
		jo2.put("id", _id);
		jo2.put("speedlimit", current_speed_limit);
		jo2.put("weather", weather_conditions);
		jo2.put("co2", total_contaminacion);
		for(int i = 0 ; i < vehicles.size() ;i++) {
			jonsa.put(vehicles.get(i));
		}
		jo2.put("vehicles", jonsa);
		
		JONSON.put("JSON:", jo2);
		
		return JONSON;
	}
	
	public void enter(Vehicle v) throws parametrosincorrectos {
		if(v.getCurrent_speed() == 0 && v.getLocation() == 0) {
			vehicles.add(v);
			v.setroad(this);
		}
		else {
			throw new parametrosincorrectos("Datos no validos");
		}
	}
	
	public void exit(Vehicle v){
		vehicles.remove(v);
	}
	
	public void setWeather(Weather w) throws parametrosincorrectos {
		
		if(w != null) {
			weather_conditions = w;
		}
		else {
			throw new parametrosincorrectos("Datos no validos");
		}
		
	}
	
	public void addContamination(int c) throws parametrosincorrectos {
		
		if(c >= 0) {
			total_contaminacion += c;
		}
		else {
			throw new parametrosincorrectos("Datos no validos");
		}
		
	}
	
	public abstract void reduceTotalContamination() throws IOException;
	
	public abstract void updateSpeedLimit();
	
	public abstract int calculateVehicleSpeed(Vehicle v) throws parametrosincorrectos;

	public Junction getDestination() {
		return destination;
	}

	public Junction getSource() {
		return source;
	}
	
	public int getMaximum_speed() {
		return maximum_speed;
	}

	public int getCurrent_speed_limit() {
		return current_speed_limit;
	}

	public int getTotal_contaminacion() {
		return total_contaminacion;
	}

	public Weather getWeather_conditions() {
		return weather_conditions;
	}
	
	public int getcontamination_alarm_limit() {
		return contamination_alarm_limit;
	}
}
