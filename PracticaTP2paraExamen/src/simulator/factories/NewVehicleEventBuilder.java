package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data != null) {
			if(!data.isEmpty()) {
				JSONArray ja =  data.getJSONArray("itinerary");
				List<String> l = new LinkedList<String>();
				for (int i = 0; i < ja.length(); i++) l.add(ja.getString(i));
				return new NewVehicleEvent(data.getInt("time"), data.getString("id"), data.getInt("maxspeed"), data.getInt("class") , l);
			}
		}
		return null;
	}
}

