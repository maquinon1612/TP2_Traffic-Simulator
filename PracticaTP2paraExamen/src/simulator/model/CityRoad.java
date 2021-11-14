package simulator.model;

import java.io.IOException;

import excepciones.parametrosincorrectos;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void reduceTotalContamination() throws IOException {
		int x;
		if(weather_conditions == Weather.STORM || weather_conditions == Weather.WINDY) {
			x = 10;
		}
		else {
			x = 2;
		}
		
		
		if(total_contaminacion != 0) {
			total_contaminacion -= x;
		}
		
		
		if(total_contaminacion < 0) {
			total_contaminacion = 0;
			throw new IOException();
		}
	}

	@Override
	public void updateSpeedLimit() {
		current_speed_limit = maximum_speed;
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) throws parametrosincorrectos {
		return ((int)(((11.0-v.getContamination_class())/11.0)*current_speed_limit) + 1);
	}

}
