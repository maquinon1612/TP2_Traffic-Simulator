package simulator.model;

public abstract class NewRoadEvent extends Event{
	
	protected String _id;
	protected String source;
	protected String dest;
	protected int lenght;
	protected int Co2limit;
	protected int max;
	protected Weather wt;

	public NewRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather)
			{
			super(time);
			_id = id;
			source = srcJun;
			dest = destJunc;
			this.lenght = length;
			this.Co2limit = co2Limit;
			max = maxSpeed;
			wt = weather;
			}

	@Override
	abstract void execute(RoadMap map);

}
