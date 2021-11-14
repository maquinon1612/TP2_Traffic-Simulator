package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeingStrategy;
import simulator.model.MoveAllStrategy;

public class MoveAllStrategyBuilder extends Builder<DequeingStrategy>{

	public MoveAllStrategyBuilder() {
		super("most_all_dqs");
	}

	@Override
	protected DequeingStrategy createTheInstance(JSONObject data) {
		if(data != null) {
			return new MoveAllStrategy();
		}
		return null;
	}

}
