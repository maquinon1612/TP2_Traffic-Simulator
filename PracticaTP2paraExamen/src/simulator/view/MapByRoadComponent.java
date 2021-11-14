package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;
	
	private RoadMap _map;

	private Image _car;
	
	public MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
		this.setPreferredSize (new Dimension (300, 200));
	}
	
	private void initGUI() {
		_car = loadImage("car.png");
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(Color.WHITE);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
		drawJunctions(g);
	}

	private void drawRoads(Graphics g) {
		
		int i = 0;
		
		for (Road r : _map.getRoads()) {
			
			//draw road
			int x1 = 50;
			int x2 = getWidth() - 100;
			int y = (i+1)*50;

			g.setColor(Color.BLACK);
			g.drawLine(x1, y, x2, y);
			g.drawString(r.getId(), 10, y);
			
			//draw weather conditions
			g.drawImage(getWeatherImage(r.getWeather_conditions()), x2 + 10, y - 16 , 32, 32, this);
			
			//draw contamination class
			
			g.drawImage(getContImage(r.getTotal_contaminacion() ,r.getcontamination_alarm_limit()), x2 + 50, y - 16 , 32, 32, this);
			
			i++;
		}

	}

	private void drawVehicles(Graphics g) {
		
		int i = 0;
		
		for (Road r : _map.getRoads()) {
			for(Vehicle v : _map.getVehicles()) {
				if (v.getstatus() != VehicleStatus.ARRIVED && v.getRoad().equals(r)) {
					
					int x1 = 50;
					int x2 = getWidth() - 100;
					
					int y = (i+1)*50;
					int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLenght()));

					// draw an image of a car (with circle as background) and it identifier
					g.drawImage(_car, x, y - 12, 16, 16, this);
					
					// Choose a color for the vehcile's label depending on its
					// contamination class
					int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContamination_class()));
					g.setColor(new Color(0, vLabelColor, 0));
					g.drawString(v.getId(), x, y - 12);
				}
			}
			i++;
		}
	}

	private void drawJunctions(Graphics g) {
		
		int i = 0;
		
		for (Road r : _map.getRoads()) {
			
			//draw road
			int x1 = 50;
			int x2 = getWidth() - 100;
			int y = (i+1)*50;
			
			// draw the source junction 
			g.setColor(Color.BLUE);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// draw the junction's identifier at (x,y)
			g.setColor(Color.ORANGE);
			g.drawString(r.getSource().getId(), x1, y - 5);
			
			// draw the destination junction
			Color destColor = Color.RED;
			int idx = r.getDestination().getind_green();
			if (idx != -1 && r.equals(r.getDestination().getlist_road_entran().get(idx))) {
				destColor = Color.GREEN;
			}
			
			g.setColor(destColor);
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// draw the junction's identifier at (x,y)
			g.setColor(Color.ORANGE);
			g.drawString(r.getDestination().getId(), x2, y - 5);
			
			i++;
		}
	}
	
	private void updatePrefferedSize() {
		int maxW = 400;
		int maxH = 400;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		setPreferredSize(new Dimension(maxW, maxH));
		setSize(new Dimension(maxW, maxH));
	}
	
	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	private Image getWeatherImage(Weather w) {
		if(w.equals(Weather.SUNNY)) {
			return loadImage("sun.png");
		}
		else if(w.equals(Weather.WINDY)) {
			return loadImage("wind.png");
		}
		else if(w.equals(Weather.CLOUDY)) {
			return loadImage("cloud.png");
		}
		else if(w.equals(Weather.RAINY)) {
			return loadImage("rain.png");
		}
		else if(w.equals(Weather.STORM)) {
			return loadImage("storm.png");
		}
		else {
			return null;
		}
	}
	
	private Image getContImage(int total , int limit) {
		
		int c = (int) Math.floor(Math.min((double) total/(1.0 + (double) limit),1.0) / 0.19);
		String image = "cont_" + c + ".png";
		
		return loadImage(image);
	}
		
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this), err);
	}

}
