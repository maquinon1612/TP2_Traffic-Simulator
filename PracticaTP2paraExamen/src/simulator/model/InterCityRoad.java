package simulator.model;

import excepciones.parametrosincorrectos;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void reduceTotalContamination() {
		int x = 0;
		if(weather_conditions == Weather.SUNNY) {
			x = 2;
		}
		else if(weather_conditions == Weather.CLOUDY) {
			x = 3;
		}
		else if(weather_conditions == Weather.RAINY) {
			x = 10;
		}
		else if(weather_conditions == Weather.WINDY) {
			x = 15;
		}
		else if(weather_conditions == Weather.STORM) {
			x = 20;
		}
		
		total_contaminacion -= (int)((x/100.0)*total_contaminacion);
	}

	@Override
	public void updateSpeedLimit() {
		if(total_contaminacion > contamination_alarm_limit) {
			current_speed_limit = (int)(maximum_speed*0.5);
		}
		else {
			current_speed_limit = maximum_speed;
		}
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) throws parametrosincorrectos {
		if(weather_conditions == Weather.STORM) {
			return (int)(current_speed_limit*0.8);
		}
		else {
			return current_speed_limit;
		}
	}

}
