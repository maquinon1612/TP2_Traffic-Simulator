package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected List<Event> lt;
	private static String[] columnas = {"Time" , "Desc."};
	
	public EventsTableModel(Controller c) {
		lt = new ArrayList<Event>();
		c.addObserver(this);;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		Event e  = lt.get(rowIndex);
		switch (columnIndex) {
		case 0:
			s = e.getTime();
			break;
		case 1:
			s = e.toString();
			break;
		default:
			assert (false); // should be unreachable
		}
		return s;
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
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		lt = events;
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		lt = events;
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		lt = events;
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		lt = events;
		this.fireTableDataChanged();
	}
	
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		lt = events;
		this.fireTableDataChanged();
	}
	
	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err);
	}
}

