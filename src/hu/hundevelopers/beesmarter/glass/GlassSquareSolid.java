package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquareSolid extends Glass
{
	public GlassSquareSolid(int x, int y, int deg)
	{
		super(x, y, deg);
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
		this.vertices = new Vertex[4];
		int d = 45;
		for(int i = 0; i < 4; i++)
		{
			this.vertices[i] = new Vertex(this.x+MathHelper.cos(d+this.deg)*Game.instance.tilesize/(float)Math.sqrt(2), this.y+MathHelper.sin(d+this.deg)*Game.instance.tilesize/(float)Math.sqrt(2));
			d += 90;
		}
	}
	
	@Override
	public void handleLaserCollision(int side, Line laser, Vertex v)
	{
		
	}
}