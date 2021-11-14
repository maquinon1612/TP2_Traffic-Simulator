package simulator.factories;

import org.json.JSONObject;
import simulator.model.Event;
import simulator.model.NewInterCityRoad;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event>{

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data != null) {
			if(!data.isEmpty()) {
				return new NewInterCityRoad(data.getInt("time"), data.getString("id"), data.getString("src"), data.getString("dest"),data.getInt("length"), data.getInt("co2limit"), data.getInt("maxspeed"), Weather.valueOf(data.getString("weather").toUpperCase()));
			}
		}
		return null;
	}

}