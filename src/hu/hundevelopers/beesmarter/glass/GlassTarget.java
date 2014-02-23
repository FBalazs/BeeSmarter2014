package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassTarget extends Glass
{
	public GlassTarget()
	{
		this(0, 0, 0, 0);
	}
	
	public GlassTarget(int id, int x, int y, int deg)
	{
		super(id, x, y, deg);
		this.r = 64;
		this.g = 64;
		this.b = 64;
	}
	
	@Override
	public boolean isMoveable()
	{
		return false;
	}
	
	@Override
	public void calculateVertices()
	{
		
	}

	@Override
	public void handleLaserCollision(int side, Line laser, Vertex V)
	{
		
	}
}
