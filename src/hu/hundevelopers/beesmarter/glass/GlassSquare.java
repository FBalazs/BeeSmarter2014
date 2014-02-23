package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public abstract class GlassSquare extends Glass
{
	public GlassSquare()
	{
		this(0, 0, 0, 0);
	}
	
	public GlassSquare(int id, int x, int y, int deg)
	{
		super(id, x, y, deg);
	}

	@Override
	public void calculateVertices()
	{
		this.bbMinX = this.bbMaxX = this.x;
		this.bbMinY = this.bbMaxY = this.y;
		this.vertices = new Vertex[4];
		int d = 45;
		for(int i = 0; i < 4; i++)
		{
			float x = this.x+MathHelper.cos(d+this.deg)*Game.instance.tileres/(float)Math.sqrt(2);
			float y = this.y+MathHelper.sin(d+this.deg)*Game.instance.tileres/(float)Math.sqrt(2);
			if(x < this.bbMinX)
				this.bbMinX = x;
			if(x > this.bbMaxX)
				this.bbMaxX = x;
			if(y < this.bbMinY)
				this.bbMinY = y;
			if(y > this.bbMaxY)
				this.bbMaxY = y;
			this.vertices[i] = new Vertex(x, y);
			d += 90;
		}
	}
}
