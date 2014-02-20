package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquareMirror extends Glass
{
	public GlassSquareMirror(int x, int y, int deg)
	{
		super(x, y, deg);
		this.alpha = 128;
	}

	@Override
	public void calculateVertices()
	{
		this.vertices = new Vertex[4];
		int d = 45;
		for(int i = 0; i < 4; i++)
		{
			this.vertices[i] = new Vertex(this.x+MathHelper.cos(d+this.deg)*Game.instance.tilesize, this.y+MathHelper.sin(d+this.deg)*Game.instance.tilesize);
			d += 90;
		}
	}

	@Override
	public void handleLaserCollision(Line laser)
	{
		
	}
}