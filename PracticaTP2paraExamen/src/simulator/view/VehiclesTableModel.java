package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected List<Vehicle> lt;
	private static String[] columnas = {"Id" , "Location" , "Iterinary" , "CO2 Class" , "Max. Speed" , "Speed" , "Total CO2" , "Distance"};
	
	public VehiclesTableModel(Controller c) {
		lt = new ArrayList<Vehicle>();
		c.addObserver(this);;
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
		Vehicle v  = lt.get(rowIndex);
		switch (columnIndex) {
		case 0:
			s = v.getId();
			break;
		case 1:
			if(v.getstatus() == VehicleStatus.TRAVELING) {
				s = v.getRoad().getId() + ":" + v.getLocation();
			}
			else {
				s = v.getstatus().toString();
			}
			break;
		case 2:
			s = "[";
			for(Junction j : v.getItinerary()) {
				s = s + j.getId() + ",";
			}
			s = s + "]" ; 
			break;
		case 3:
			s = v.getContamination_class();
			break;
		case 4:
			s = v.getmaximum_speed();
			break;
		case 5:
			s = v.getCurrent_speed();
			break;
		case 6:
			s = v.gettotalCO2();
			break;
		case 7:
			s = v.getdistance();
			break;
		default:
			assert (false); // should be unreachable
		}
		return s;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		lt = map.getVehicles();
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		lt = map.getVehicles();
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		lt = map.getVehicles();
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		lt = map.getVehicles();
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		lt = map.getVehicles();
		this.fireTableDataChanged();
	}
	
	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err);
	}

}
