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

import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2Dialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String HelMesage = "<html><p>Schedule an event to change the CO2 Class of a vehicle after a given number of simulation ticks from now.";
	
	protected int stat;
	
	protected JComboBox<Vehicle> firstbox;
	private JSpinner timespin;
	protected JComboBox<Integer> secondbox;
	
	protected DefaultComboBoxModel<Vehicle> firstmodel;
	protected DefaultComboBoxModel<Integer> secondmodel;

	protected String Title = "Change CO2 Class";
	protected String First = "Vehicles: ";
	protected String Second = "CO2 Class: ";
	
	public ChangeCO2Dialog(Frame parent) {
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
		
		secondbox = new JComboBox<Integer>(secondmodel);
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
				ChangeCO2Dialog.this.setVisible(false);
			}
			
		});
		
		buttons.add(cancel);
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(firstmodel.getSelectedItem() != null) {
					stat = 1;
					ChangeCO2Dialog.this.setVisible(false);
				}
			}
			
		});
		
		buttons.add(ok);

		main.setPreferredSize(new Dimension(500, 200));
		
		setMinimumSize(new Dimension(500, 200));
		setPreferredSize(new Dimension(500, 200));
	}
	
	protected void addsecondelements() {
		for(int i = 0 ; i < 11 ; i++) {
			secondmodel.addElement(i);
		}
	}

	public Vehicle getFirst() {
		return (Vehicle) firstmodel.getSelectedItem();
	}

	
	public Integer getSecond() {
		return (Integer) secondmodel.getSelectedItem();
	}

	public int getTicks() {
		return (int) timespin.getValue();
	}
	
	public int open(RoadMap map) {
		for(Vehicle r : map.getVehicles())
		firstmodel.addElement(r);
		firstbox.contentsChanged(null);
		this.setVisible(true);
		return stat;
	}
}
