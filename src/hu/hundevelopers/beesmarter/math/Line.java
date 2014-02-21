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
}
