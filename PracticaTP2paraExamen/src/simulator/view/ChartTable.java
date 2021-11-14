package simulator.view;

import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class ChartTable extends AbstractTableModel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int limit;
	
	private static String[] columnas = {"Ticks" , "Vehicles"};
	private HashMap <Integer , String> data;
	
	public ChartTable(Controller c) {
		limit = 0;
		data = new HashMap<Integer , String>();
		c.addObserver(this);
	}
	
	public void updatelimit(int limit) {
		this.limit = limit;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = rowIndex + 1;
			break;
		case 1:
			s = data.get(rowIndex + 1);
			break;
		default:
			assert (false); // should be unreachable
		}
		return s;
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}
	
	@Override
	public String getColumnName(int col){
		if (columnas == null) return "";
		else return columnas[col];
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
		List<Vehicle> lv = map.getVehicles();
		
		String s;
		
		s = "[";
		for(Vehicle v : lv) {
			if(v.getCurrent_speed() >= limit) {
				s = s + v.getId() + ", ";
			}
		}
		s = s + "]";
		
		if(time != 0) {
			data.put(time, s);
		}
		
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		data.clear();
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		List<Vehicle> lv = map.getVehicles();
		
		String s;
		
		s = "[";
		for(Vehicle v : lv) {
			if(v.getCurrent_speed() >= limit) {
				s = s + v.getId() + ", ";
			}
		}
		s = s + "]";
		
		if(time != 0) {
			data.put(time, s);
		}
		
		this.fireTableDataChanged();
	}
	
	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err);
	}
}