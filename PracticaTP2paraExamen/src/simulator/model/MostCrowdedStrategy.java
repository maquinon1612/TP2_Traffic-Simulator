package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{

	int timeSlot;
	
	public MostCrowdedStrategy (int timeSlot){
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		if(roads == null) {
			return -1;
		}
		if(currGreen == -1) {
			int tamaño = -1 , pos = -1;
			for(int i = 0 ; i < qs.size() ; i++) {
				if(qs.get(i).size() > tamaño) {
					tamaño = qs.get(i).size();
					pos = i; 
				}
			}
			return pos;
		}
		if(currTime-lastSwitchingTime <timeSlot) {
			return currGreen;
		}
		else {
			int tamaño = -1 , pos = currGreen + 1 , aux = -1;
			for(int i = 0 ; i < qs.size() ; i++) {
				pos = (pos + 1> roads.size()) ? pos + 1 : 0;
				if(qs.get(pos).size() > tamaño) {
					tamaño = qs.get(pos).size();
					aux = pos; 
				}
			}
			return aux;
			
		}
	}

}
