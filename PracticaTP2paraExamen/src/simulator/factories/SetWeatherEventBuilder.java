package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import excepciones.weatherincorrecto;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		try {
			if(data != null) {
				if(!data.isEmpty()) {
					JSONArray ja =  data.getJSONArray("info");
					List<Pair<String,Weather>> l = new LinkedList<Pair<String,Weather>>();
					Pair<String,Weather> p;
					for (int i = 0; i < ja.length(); i++) {
						p = new Pair<String,Weather>(ja.getJSONObject(i).getString("road"), 
						Weather.valueOf(ja.getJSONObject(i).getString("weather").toUpperCase()));
						l.add(p);
					}
					return new SetWeatherEvent(data.getInt("time"), l);
				}
			}
			return null;
		} catch (weatherincorrecto e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}
