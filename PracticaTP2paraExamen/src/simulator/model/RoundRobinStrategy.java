package simulator.model;
import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy{

	int timeSlot;
	
	public RoundRobinStrategy (int timeSlot){
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		if(roads == null) {
			return -1;
		}
		if(currGreen == -1) {
			return 0;
		}
		if((currTime <= timeSlot && currTime-lastSwitchingTime-1 < timeSlot) || currTime-lastSwitchingTime < timeSlot) {
			return currGreen;
		}
		else {
			return (currGreen + 1 > roads.size() - 1) ? 0 : currGreen + 1;
		}
	}

}
