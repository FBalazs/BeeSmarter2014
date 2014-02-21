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
	
	public Line(Vertex v1, Vertex v2)
	{
		this.x = v1.x;
		this.y = v1.y;
		this.nx = (v2.y-v1.y);
		this.ny = (v1.x-v2.x);
	}
}
