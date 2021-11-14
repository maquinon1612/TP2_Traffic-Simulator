package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import consola.JunctionRoadGreen;
import consola.Vehicle_Road;
import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {
	
	private enum ExecMode{
		GUI("gui"),CONSOLE("console");
		
		private String tag;
		
		ExecMode(String tag){
			this.tag = tag;
		}
		
		String gettag() {
			return tag;
		}
		
	}

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static ExecMode _execmode = null;
	private static String _timeLimit;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parsetimeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg().desc("Events duration").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("gui").hasArg().desc("Output mode").build());
		
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		String Mode = line.getOptionValue("m", "gui");
		for(ExecMode m : ExecMode.values()) {
			if(m.gettag().equals(Mode)) {
				_execmode = m;
				return;
			}
		}
		throw new ParseException("Invalid execution mode " + Mode + ";");
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parsetimeOption(CommandLine line) throws ParseException {
		_timeLimit = line.getOptionValue("t");
		if (_timeLimit == null) {
			_timeLimit = _timeLimitDefaultValue.toString();
		}
	}

	private static void initFactories() {

		
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add( new NewJunctionEventBuilder(lssFactory,dqsFactory));
		ebs.add( new NewCityRoadEventBuilder());
		ebs.add( new NewInterCityRoadEventBuilder());
		ebs.add( new NewVehicleEventBuilder());
		ebs.add( new SetWeatherEventBuilder());
		ebs.add( new SetContClassEventBuilder());
		_eventsFactory = new BuilderBasedFactory<>(ebs);
	}

	private static void startBatchMode() throws IOException {
		InputStream in = new FileInputStream(new File(_inFile));
		OutputStream out = _outFile == null ? System.out :new FileOutputStream(new File(_outFile));
		TrafficSimulator sim = new TrafficSimulator();
		Controller con = new Controller(sim, _eventsFactory);
		con.loadEvents(in);
		Vehicle_Road vr = new Vehicle_Road(con);
		JunctionRoadGreen jrg = new JunctionRoadGreen(con);
		con.run(Integer.parseInt(_timeLimit) , out);
		in.close();
		System.out.println(vr);
		System.out.println(jrg);
		System.out.println("end");
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_execmode.gettag().equals("gui")) {
			startGUIMode();
		}
		else {
			startBatchMode();
		}
	}

	// example command lines:
	//
	// -m gui -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void startGUIMode() throws IOException{
		TrafficSimulator simulador = new TrafficSimulator();
		Controller ctr = new Controller (simulador , _eventsFactory);
		
		if(_inFile != null) {
			InputStream in = new FileInputStream(new File(_inFile));
			ctr.loadEvents(in);
		}
		
		SwingUtilities.invokeLater(() -> new MainWindow(ctr));
	}

}
