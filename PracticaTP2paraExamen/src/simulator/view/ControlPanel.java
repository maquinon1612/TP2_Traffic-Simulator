package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller ctr;
	private RoadMap map;
	private int time;
	private boolean stp;
	
	private JToolBar toolBar;
	private JButton load;
	private JButton reset;
	private JButton run;
	private JButton stop;
	private JButton close;
	private JButton contchange;
	private JButton atmchange;
	private JButton chart;
	private JSpinner ticksSpin;
	
	private File resetfile;
	
	private JFileChooser fc;
	
	LimitChartDialog chartdialogo;
	
	private MainWindow parent;
	
	
	public ControlPanel(Controller c, MainWindow parent) {
		ctr = c;
		stp = true;
		time = 0;
		this.parent = parent;
		initGUI();
		ctr.addObserver(this);
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		toolBar = new JToolBar();
		add(toolBar , BorderLayout.PAGE_END);
		
		JMenuBar menuBar = new JMenuBar();
		add(menuBar , BorderLayout.PAGE_START);
		
		
		JMenu Menu = new JMenu("Menu");
		menuBar.add(Menu);
		
		chartdialogo = new LimitChartDialog(parent , ctr);
		
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("resources/examples"));
		load = new JButton();
		load.setToolTipText("Load a file");
		load.addActionListener((e) -> loadFile());
		load.setIcon(loadImage("resources/icons/open.png"));
		toolBar.add(load);
		
		JMenuItem load = new JMenuItem("Load");
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,ActionEvent.SHIFT_MASK));
		load.addActionListener((e) -> loadFile());
		Menu.add(load);
		
		
		reset = new JButton();
		reset.setToolTipText("Resets the Simulation");
		reset.addActionListener((e) -> reset());
		reset.setIcon(loadImage("resources/icons/reset.png"));
		toolBar.add(reset);
		
		JMenuItem reset = new JMenuItem("Reset");
		reset.addActionListener((e) -> reset());
		Menu.add(reset);
		
		
		
		
		toolBar.addSeparator();

		contchange = new JButton();
		contchange.setToolTipText("Changes contamination class for a vehicle");
		contchange.addActionListener((e) -> co2ChangeClass());
		contchange.setIcon(loadImage("resources/icons/co2class.png"));
		toolBar.add(contchange);
		
		JMenuItem setcontclass = new JMenuItem("SetContClass");
		setcontclass.addActionListener((e) -> co2ChangeClass());
		Menu.add(setcontclass);
		
		
		
		
		atmchange = new JButton();
		atmchange.setToolTipText("Changes weather conditions in a road");
		atmchange.addActionListener((e) -> atmchange());
		atmchange.setIcon(loadImage("resources/icons/weather.png"));
		toolBar.add(atmchange);
		
		JMenuItem setweather = new JMenuItem("SetWeather");
		setweather.addActionListener((e) -> atmchange());
		Menu.add(setweather);
		
		
		chart = new JButton();
		chart.setToolTipText("shows a chart of the vehicles which speed is higher than a certain number");
		chart.addActionListener((e) -> limitchart());
		chart.setIcon(loadImage("resources/icons/pie-chart.png"));
		toolBar.add(chart);
		
		
		
		run = new JButton();
		run.setToolTipText("runs the simulator");
		run.addActionListener((e) -> run());
		run.setIcon(loadImage("resources/icons/run.png"));
		toolBar.add(run);
		
		JMenuItem run = new JMenuItem("Run");
		run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,ActionEvent.SHIFT_MASK));
		run.addActionListener((e) -> run());
		Menu.add(run);
		
		
		
		stop = new JButton();
		stop.setToolTipText("stops the simulator");
		stop.addActionListener((e) -> stop());
		stop.setIcon(loadImage("resources/icons/stop.png"));
		toolBar.add(stop);
		
		JMenuItem stop = new JMenuItem("Stop");
		stop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,ActionEvent.SHIFT_MASK));
		stop.addActionListener((e) -> stop());
		Menu.add(stop);
		
		
		
		
		toolBar.add(new JLabel("Ticks:"));
		ticksSpin = new JSpinner(new SpinnerNumberModel(10,1,10000,1));
		ticksSpin.setToolTipText("Simulation ticks run : 1 - 10000");
		ticksSpin.setMaximumSize(new Dimension(80,40));
		ticksSpin.setMinimumSize(new Dimension(80,40));
		ticksSpin.setPreferredSize(new Dimension(80,40));
		toolBar.add(ticksSpin);
		
		toolBar.add(Box.createGlue());
		toolBar.addSeparator();
		
		close = new JButton();
		close.setToolTipText("stops the simulator");
		close.addActionListener((e) -> close());
		close.setIcon(loadImage("resources/icons/exit.png"));
		toolBar.add(close);
		
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener((e) -> close());
		Menu.add(close);
	}

	private void limitchart() {
		int status = chartdialogo.open();
		if(status == 1) {
			run();
			limitchart();
		}
	}

	private void reset() {
		ctr.reset();
		try {
			ctr.loadEvents(new FileInputStream(resetfile));
		} catch (IllegalArgumentException | FileNotFoundException e) {
			showError("Somethig went pretty badly reseting: " + e.getLocalizedMessage());
		}
	}

	private void atmchange() {
		changeatm dialogo = new changeatm(parent);
		int status = dialogo.open(map);
		if(status == 1) {
			List<Pair<String,Weather>> ws = new ArrayList<>();
			ws.add(new Pair<String,Weather>(dialogo.getFirst().getId() , dialogo.getSecond()));
			try {
				ctr.addEvent(new SetWeatherEvent(time + dialogo.getTicks() , ws));
			}
			catch(Exception e) {
				showError("Salio mal: " + e.getLocalizedMessage());
			}
		}
	}

	private void co2ChangeClass() {
		ChangeCO2Dialog dialogo = new ChangeCO2Dialog(parent);
		int status = dialogo.open(map);
		if(status == 1) {
			List<Pair<String,Integer>> cs = new ArrayList<>();
			cs.add(new Pair<String,Integer>(dialogo.getFirst().getId() , dialogo.getSecond()));
			try {
				ctr.addEvent(new NewSetContClassEvent(time + dialogo.getTicks() , cs));
			}
			catch(Exception e) {
				showError("Salio mal: " + e.getLocalizedMessage());
			}
		}
	}

	private void close() {
		int n = JOptionPane.showOptionDialog((Frame) SwingUtilities.getWindowAncestor(this), 
				"You sure you wanna quit?", "Quit",  JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, null , null);
		
		if(n == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	private void run() {
		stp = false;
		enableToolBar(stp);
		run_sim((Integer) ticksSpin.getValue());
	}
	
	private void showError(String error) {
		SwingUtilities.invokeLater(() -> {JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this) , error);
										  enableToolBar(true);
										 });
	}

	protected ImageIcon loadImage(String p) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(p));
	}

	private Object loadFile() {
		int valor = fc.showOpenDialog(this.getParent());
		
		if(valor == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			resetfile = f;
			try {
				ctr.reset();
				ctr.loadEvents(new FileInputStream(f));
			}
			catch(Exception e){
				showError("Somethig went pretty badly loading the file: " + e.getLocalizedMessage());
			}
		}
		return null;
	}
	
	private void stop() {
		stp = true;
	}
	
	private void run_sim(int n) {
		if(n > 0 && !stp) {
			try {
				ctr.run(1);
			}
			catch(Exception e){
				showError("Something went wrong: " + e.getLocalizedMessage());
				stp = true;
				return;
			}
			SwingUtilities.invokeLater(() -> run_sim(n - 1));
		}
		else {
			enableToolBar(true);
			stp = true;
		}
	}

	private void enableToolBar(boolean b) {
		load.setEnabled(b);
		run.setEnabled(b);
		reset.setEnabled(b);
		stop.setEnabled(true);
		close.setEnabled(b);
		contchange.setEnabled(b);
		atmchange.setEnabled(b);
		chart.setEnabled(b);
		ticksSpin.setEnabled(b);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.map = map;
		this.time = time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.map = map;
		this.time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.map = map;
		this.time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.map = map;
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.map = map;
		this.time = time;
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this), err);
	}
}
