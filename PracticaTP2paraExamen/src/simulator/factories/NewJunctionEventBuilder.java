package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
	
	Factory<LightSwitchingStrategy> bl;
	Factory<DequeingStrategy> bd;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeingStrategy> dqsFactory) {
		super("new_junction");
		bl = lssFactory;
		bd = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data != null) {
			if(!data.isEmpty()) {
				return new NewJunctionEvent(data.getInt("time"), data.getString("id"), bl.createInstance(data.getJSONObject("ls_strategy")), bd.createInstance(data.getJSONObject("dq_strategy")), data.getJSONArray("coor").getInt(0) , data.getJSONArray("coor").getInt(1));
			}
		}
		return null;
	}

}
