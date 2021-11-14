package simulator.model;

import java.io.IOException;

public class NewCityRoad extends NewRoadEvent{

	public NewCityRoad(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather)
			{
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) {
		try {
			Junction s = map.getJunction(source);
			Junction d = map.getJunction(dest);
		
			if(d != null && s != null) {
				CityRoad r = new CityRoad(_id, s, d, max, Co2limit, lenght, wt);
				map.addRoad(r);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return "New City Road '"+_id+"'";
	}

}

