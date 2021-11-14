package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class changeatm extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String HelMesage = "<html><p>Schedule an event to change the weather of a road after a given number of simulation ticks from now.";
	
	protected int stat;
	
	protected JComboBox<Road> firstbox;
	private JSpinner timespin;
	protected JComboBox<Weather> secondbox;
	
	protected DefaultComboBoxModel<Road> firstmodel;
	protected DefaultComboBoxModel<Weather> secondmodel;
	
	protected String Title = "Change weather conditions";
	protected String First = "Roads: ";
	protected String Second = "Weather: ";
	
	public changeatm(Frame parent) {
		super(parent , true);
		initGUI();
	}
	
	private void initGUI() {
		stat = 0;
		
		setTitle(Title);
		
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		setContentPane(main);
		
		JLabel help = new JLabel(HelMesage);
		help.setAlignmentX(CENTER_ALIGNMENT);
		main.add(help);
		
		main.add(Box.createRigidArea(new Dimension(0 , 20)));
		
		JPanel views = new JPanel();
		views.setAlignmentX(CENTER_ALIGNMENT);
		main.add(views);
		
		main.add(Box.createRigidArea(new Dimension(0 , 20)));
		
		JPanel buttons = new JPanel();
		buttons.setAlignmentX(CENTER_ALIGNMENT);
		main.add(buttons);
		
		firstmodel = new DefaultComboBoxModel<>();
		firstbox = new JComboBox<>(firstmodel);
		firstbox.setPreferredSize(new Dimension(100 , 25));
		
		timespin = new JSpinner(new SpinnerNumberModel(1,1,10000,1));
		timespin.setToolTipText("Time from now for scheduling event");
		timespin.setPreferredSize(new Dimension(80 , 25));
		
		secondmodel = new DefaultComboBoxModel<>();
		
		addsecondelements();
		
		secondbox = new JComboBox<Weather>(secondmodel);
		secondbox.setPreferredSize(new Dimension(80 , 25));
		
		views.add(new JLabel(First));
		views.add(firstbox);
		views.add(new JLabel(Second));
		views.add(secondbox);
		views.add(new JLabel("Ticks:"));
		views.add(timespin);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stat = 0;
				changeatm.this.setVisible(false);
			}
			
		});
		
		buttons.add(cancel);
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(firstmodel.getSelectedItem() != null) {
					stat = 1;
					changeatm.this.setVisible(false);
				}
			}
			
		});
		
		buttons.add(ok);

		main.setPreferredSize(new Dimension(500, 200));
		
		setMinimumSize(new Dimension(500, 200));
		setPreferredSize(new Dimension(500, 200));
	}
	
	protected void addsecondelements() {
		secondmodel.addElement(Weather.SUNNY);
		secondmodel.addElement(Weather.CLOUDY);
		secondmodel.addElement(Weather.RAINY);
		secondmodel.addElement(Weather.WINDY);
		secondmodel.addElement(Weather.STORM);
	}

	public Road getFirst() {
		return (Road) firstmodel.getSelectedItem();
	}

	
	public Weather getSecond() {
		return (Weather) secondmodel.getSelectedItem();
	}

	public int getTicks() {
		return (int) timespin.getValue();
	}
	
	public int open(RoadMap map) {
		for(Road r : map.getRoads())
		firstmodel.addElement(r);
		firstbox.contentsChanged(null);
		this.setVisible(true);
		return stat;
	}

}

