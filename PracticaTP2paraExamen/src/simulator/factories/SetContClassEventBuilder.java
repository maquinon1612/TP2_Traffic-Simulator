package simulator.factories;

import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import excepciones.parametrosincorrectos;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		try {
			if(data != null) {
				if(!data.isEmpty()) {
					JSONArray ja =  data.getJSONArray("info");
					List<Pair<String,Integer>> l = new LinkedList<Pair<String,Integer>>();
					Pair<String,Integer> p;
					for (int i = 0; i < ja.length(); i++) {
						p = new Pair<String,Integer>(ja.getJSONObject(i).getString("vehicle"), 
							ja.getJSONObject(i).getInt("class"));
						l.add(p);
					}
						return new NewSetContClassEvent(data.getInt("time"), l);
				}
			}
			return null;
		} catch (parametrosincorrectos e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}