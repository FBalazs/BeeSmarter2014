package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquarePrism extends Glass
{
	public GlassSquarePrism()
	{
		this(0, 0, 0);
	}
	
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
		Vertex vm = new Vertex(this.vertices[(side+1)%this.vertices.length].y-this.vertices[side].y+v.x, this.vertices[side].x-this.vertices[(side+1)%this.vertices.length].x+v.y);
		// tükör oldalának felezõmerõlegese
		Line m = new Line(v, vm);
		
		// áteresztés
		Vertex v2 = MathHelper.getLineIntersection(m, new Line(this.vertices[(side+2)%this.vertices.length], this.vertices[(side+3)%this.vertices.length]));
		Game.instance.laser.add(new Line(v, v2));
		float dx = v2.x-v.x;
		float dy = v2.y-v.y;
		if(dx == 0)
			dy = Math.abs(dy)/dy*Game.instance.size;
		else if(dy == 0)
			dx = Math.abs(dx)/dx*Game.instance.size;
		else
		{
			float s = Math.max(Game.instance.size/Math.abs(dx), Game.instance.size/Math.abs(dy));
			dx *= s;
			dy *= s;
		}
		Game.instance.claser.add(new Line(v2, new Vertex(v2.x+dx, v2.y+dy)));
	}
}