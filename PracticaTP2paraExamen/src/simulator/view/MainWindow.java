package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(_ctrl , this), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		// tables
		JPanel eventsView =
				createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		
		JPanel VehiclesView =
				createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		VehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(VehiclesView);
		
		JPanel RoadsView =
				createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		RoadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(RoadsView);
		
		JPanel JunctionsView =
				createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		JunctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(JunctionsView);
		
		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));	
		mapsPanel.add(mapView);
		
		JPanel maproad = createViewPanel(new MapByRoadComponent(_ctrl), "Map by Road");
		maproad.setPreferredSize(new Dimension(500, 400));	
		mapsPanel.add(maproad);
		
		
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				int n = JOptionPane.showOptionDialog((Frame) SwingUtilities.getWindowAncestor(mainPanel), 
						"You sure you wanna quit?", "Quit",  JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, null , null);
				
				if(n == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		// TODO add a framed border to p with title
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), title));
		
		p.add(new JScrollPane(c));
		return p;
	}
}

