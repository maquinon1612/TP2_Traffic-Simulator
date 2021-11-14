package simulator.model;

import java.io.IOException;

import excepciones.parametrosincorrectos;

public class NewJunctionEvent extends Event{
	
	private String _id;
	private DequeingStrategy deque;
	private int x;
	private int y;
	private LightSwitchingStrategy lightswitch;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy
			lsStrategy, DequeingStrategy dqStrategy, int xCoor, int yCoor){
		super(time);
		_id = id;
		deque = dqStrategy;
		x = xCoor;
		y = yCoor;
		lightswitch = lsStrategy;
	}

	@Override
	void execute(RoadMap map) {
		try {
			Junction j = new Junction(_id, lightswitch, deque, x, y);
			map.addJunction(j);
		} catch (IOException | parametrosincorrectos e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String toString() {
		return "New Junction '"+_id+"'";
	}

}
