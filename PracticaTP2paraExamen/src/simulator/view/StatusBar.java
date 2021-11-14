package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel currenttime;
	private JLabel mesage;
	
	/*
	public StatusBar() {
		intiGUI();
	}
	*/
	
	public StatusBar(Controller _ctrl) {
		_ctrl.addObserver(this);
		intiGUI();
	}

	private void intiGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		currenttime = new JLabel("0");
		currenttime.setPreferredSize(new Dimension(100, 20));
		mesage = new JLabel("Welcome");
		
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10,20));
		
		this.add(new JLabel("Time: "));
		this.add(currenttime);
		this.add(s);
		this.add(mesage);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		String s = "" + time;
		currenttime.setText(s);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		mesage.setText(e.toString());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		currenttime.setName("0");
		mesage.setName("Welcome");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this), err);
	}
}
