package hu.hundevelopers.beesmarter.math;

public class Line
{
	public float x, y, nx, ny;
	
	public Line(float x, float y, float nx, float ny)
	{
		this.x = x;
		this.y = y;
		this.nx = nx;
		this.ny = ny;
	}
	
	public Line(float x1, float y1, float x2, float y2, Object o)
	{
		this.x = x1;
		this.y = y1;
		this.nx = (y2-y1);
		this.ny = (x1-x2);
	}
}
