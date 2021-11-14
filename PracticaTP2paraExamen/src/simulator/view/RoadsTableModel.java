package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected List<Road> lt;
	private static String[] columnas = {"Id" , "Length" , "Weather" , "Max. Speed" , "Speed Limit" , "Total CO2" , "CO2 Limit"};
	
	public RoadsTableModel(Controller c) {
		lt = new ArrayList<Road>();
		c.addObserver(this);
	}
	
	@Override
	public int getRowCount() {
		return lt.size();
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		Road r  = lt.get(rowIndex);
		switch (columnIndex) {
		case 0:
			s = r.getId();
			break;
		case 1:
			s = r.getLenght();
			break;
		case 2:
			s = r.getWeather_conditions().toString();
			break;
		case 3:
			s = r.getMaximum_speed();
			break;
		case 4:
			s = r.getCurrent_speed_limit();
			break;
		case 5:
			s = r.getTotal_contaminacion();
			break;
		case 6:
			s = r.getcontamination_alarm_limit();
			break;
		default:
			assert (false); // should be unreachable
		}
		return s;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		lt = map.getRoads();
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		lt = map.getRoads();
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		lt = map.getRoads();
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		lt = map.getRoads();
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		lt = map.getRoads();
		this.fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err);
	}
}
