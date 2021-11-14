package simulator.model;
import java.util.List;

import excepciones.mapainvalido;
import excepciones.parametrosincorrectos;
import excepciones.weatherincorrecto;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	List<Pair<String,Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws weatherincorrecto {
		super(time);
		if(ws != null) {
			this.ws = ws;
		}
		else {
			throw new weatherincorrecto("El weather especificado no está dentro de los validos");
		}
	}


	@Override
	void execute(RoadMap map) throws mapainvalido{
		try {
			for(Pair<String,Weather> w : ws) {
				if(map.getRoad(w.getFirst()) != null) {
					map.getRoad(w.getFirst()).setWeather(w.getSecond());
				}
				else {
					throw new mapainvalido("vehículo no existe en el mapa de carreteras");
				}
			}
		} catch (parametrosincorrectos e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		String s = "Set Weather Event '";
		for(Pair<String,Weather> w : ws) {
			s = s + w.getFirst() + ":" + w.getSecond().toString() + " '" ;
		}
		return s;
	}

}
