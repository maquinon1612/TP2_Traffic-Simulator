package simulator.model;

import java.io.IOException;

public class NewInterCityRoad extends NewRoadEvent{

	public NewInterCityRoad(int time, String id, String srcJun, String
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
				InterCityRoad r = new InterCityRoad(_id, s, d, max, Co2limit, lenght, wt);
				map.addRoad(r);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return "New Inter City Road '"+_id+"'";
	}

}

