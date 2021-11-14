package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator Ts;
	private Factory<Event> factory;
	private OutputStream output;
	
	JSONArray ja;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory , OutputStream out){
		if(sim != null && eventsFactory != null) {
			Ts = sim;
			factory = eventsFactory;
			output = out;
			
		}
		else {
			throw new IllegalArgumentException("Argumentos no validos");
		}
	}
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory){
		if(sim != null && eventsFactory != null) {
			Ts = sim;
			factory = eventsFactory;
		}
		else {
			throw new IllegalArgumentException("Argumentos no validos");
		}
	}
	
	public void loadEvents(InputStream in) throws IllegalArgumentException{
		JSONObject jo = new JSONObject(new JSONTokener(in));
		ja =  jo.getJSONArray("events");
		for (int i = 0; i < ja.length(); i++)
			Ts.addEvent(factory.createInstance(ja.getJSONObject(i)));
	}
	
	public void run(int n) {
		//PrintStream p = new PrintStream(output);
		for(int i = 0; i< n ;i++) {
			Ts.advance();
			JSONObject Junior = new JSONObject(); 
			Junior.put("states", Ts.report());
			//p.print(Junior.toString(3) + '\n');
		}
	}
	public void run(int n, OutputStream out) {
		PrintStream p = new PrintStream(out);
		for(int i = 0; i< n ;i++) {
			Ts.advance();
			JSONObject Junior = new JSONObject(); 
			Junior.put("states", Ts.report());
			p.print(Junior.toString(3) + '\n');
		}
	}
	
	public void reset() {
		Ts.reset();
	}
	
	public void addObserver(TrafficSimObserver o){
		Ts.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o){
		Ts.removeObserver(o);
	}
	
	public void addEvent(Event e){
		Ts.addEvent(e);
	}
	
}
