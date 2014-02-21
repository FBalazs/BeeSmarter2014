package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquarePrism extends Glass
{
	public GlassSquarePrism(int x, int y, int deg)
	{
		super(x, y, deg);
		this.alpha = 128;
		this.r = 100;
		this.g = 200;
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
		Vertex vm = new Vertex(-(this.vertices[side].y-v.y)+v.x, (this.vertices[side].x-v.x)+v.y);
		// tükör oldalának felezõmerõlegese
		Line m = new Line(v, vm);
		
		// áteresztés
		Vertex v2 = MathHelper.getLineIntersection(m, new Line(this.vertices[(side+2)%this.vertices.length], this.vertices[(side+3)%this.vertices.length]));
		Game.instance.laser.add(new Line(v, v2));
		float dx = v2.x-v.x;
		float dy = v2.y-v.y;
		if(dx == 0)
			dy = Math.abs(dy)/dy*Game.instance.height;
		else if(dy == 0)
			dx = Math.abs(dx)/dx*Game.instance.width;
		else
		{
			float s = Math.max(Game.instance.width/Math.abs(dx), Game.instance.height/Math.abs(dy));
			dx *= s;
			dy *= s;
		}
		Game.instance.claser.add(new Line(v2, new Vertex(v2.x+dx, v2.y+dy)));
	}
}