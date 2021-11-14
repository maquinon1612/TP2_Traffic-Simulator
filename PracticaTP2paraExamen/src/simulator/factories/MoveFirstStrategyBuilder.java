package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeingStrategy;
import simulator.model.MoveFirstStrategy;

public class MoveFirstStrategyBuilder extends Builder<DequeingStrategy>{

	public MoveFirstStrategyBuilder() {
		super("move_first_dqs");
	}

	@Override
	protected DequeingStrategy createTheInstance(JSONObject data) {
		if(data != null) {
			return new MoveFirstStrategy();
		}
		return null;
	}

}