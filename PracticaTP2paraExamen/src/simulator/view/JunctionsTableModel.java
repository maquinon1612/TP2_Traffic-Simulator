package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected List<Junction> lt;
	private static String columnas[] = {"Id" , "Green" , "Queues" };
	
	public JunctionsTableModel(Controller c) {
		lt = new ArrayList<Junction>();
		c.addObserver(this);;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		Junction j  = lt.get(rowIndex);
		switch (columnIndex) {
		case 0:
			s = j.getId();
			break;
		case 1:
			List<Road> lr = j.getlist_road_entran();
			if(j.getind_green() == -1) {
				s = "NONE";
			}
			else {
				s = lr.get(j.getind_green());
			}
			break;
		case 2:
			s = "";
			for(int i = 0 ; i < j.getlist_road_entran().size() ; i++) {
				s = s + j.getlist_road_entran().get(i).getId() + ":[";
				for(int h = 0 ; h < j.getList_colas().get(i).size() ; h++) {
					s = s + j.getList_colas().get(i).get(h).getId() + ",";
				}
				s = s + "]";
			}
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
		lt = map.getJunctions();
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		lt = map.getJunctions();
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		lt = map.getJunctions();
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		lt = map.getJunctions();
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		lt = map.getJunctions();
		this.fireTableDataChanged();
	}
	
	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err);
	}
}