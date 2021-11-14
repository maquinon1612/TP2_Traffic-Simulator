package simulator.factories;

import org.json.JSONObject;
import simulator.model.Event;
import simulator.model.NewCityRoad;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event>{

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data != null) {
			if(!data.isEmpty()) {
				return new NewCityRoad(data.getInt("time"), data.getString("id"), data.getString("src"), data.getString("dest"),data.getInt("length"), data.getInt("co2limit"), data.getInt("maxspeed"), Weather.valueOf(data.getString("weather").toUpperCase()));
			}
		}
		return null;
	}

}
