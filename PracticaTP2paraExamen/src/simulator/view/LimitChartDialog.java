package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;

public class LimitChartDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String HelMesage = "<html><p>Select speed limit and press Update to show vehicle that exceeded this speed in each click";
	
	protected int stat;
	Controller _ctrl;
	
	private JSpinner speedspin;
	
	ChartTable timechart;

	protected String Title = "Vehicles Speed History";
	protected String First = "Speed Limit: ";
	
	public LimitChartDialog(Frame parent , Controller _ctr) {
		super(parent , true);
		this._ctrl = _ctr;
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
		buttons.setPreferredSize(new Dimension(80 , 25));
		main.add(buttons);
		
		main.add(Box.createRigidArea(new Dimension(0 , 20)));
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		tablesPanel.setPreferredSize(new Dimension(100, 100));
		main.add(new JScrollPane(tablesPanel ,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		speedspin = new JSpinner(new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1));
		speedspin.setToolTipText("Speed to limit the vehicles apperance");
		speedspin.setPreferredSize(new Dimension(80 , 25));
		
		views.add(new JLabel(First));
		views.add(speedspin);
		
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stat = 0;
				LimitChartDialog.this.setVisible(false);
			}
			
		});
		
		buttons.add(close);
		
		JButton update = new JButton("update");
		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stat = 1;
				LimitChartDialog.this.setVisible(false);
			}
			
		});
		
		buttons.add(update);
		
		JPanel view = new JPanel();
	
		timechart = new ChartTable(_ctrl);
		
		view.add(new JScrollPane(new JTable(timechart)));
		view.setPreferredSize(new Dimension(100, 100));
		
		tablesPanel.add(view);

		main.setPreferredSize(new Dimension(500, 200));
		
		setMinimumSize(new Dimension(500, 500));
		setPreferredSize(new Dimension(500, 500));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public int getTicks() {
		return (int) speedspin.getValue();
	}
	
	public int open() {
		stat = 0;
		this.setVisible(true);
		timechart.updatelimit((int) speedspin.getValue());
		return stat;
	}
}
