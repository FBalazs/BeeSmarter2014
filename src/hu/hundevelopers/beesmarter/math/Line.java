package hu.hundevelopers.beesmarter.math;

public class Line
{
	public float x1, y1, x2, y2;
	
	public Line(Vertex v1, Vertex v2)
	{
		this(v1.x, v1.y, v2.x, v2.y);
	}
	
	public Line(float x1, float y1, float x2, float y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Line))
			return false;
		Line l = (Line)o;
		return (Math.abs(this.x1-l.x1) < 1F
				&& Math.abs(this.y1-l.y1) < 1F
				&& Math.abs(this.x2-l.x2) < 1F
				&& Math.abs(this.y2-l.y2) < 1F);
	}
}
