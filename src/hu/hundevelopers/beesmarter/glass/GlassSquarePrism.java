package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquarePrism extends GlassSquare
{
	public GlassSquarePrism()
	{
		this(0, 0, 0, 0);
	}
	
	public GlassSquarePrism(int id, int x, int y, int deg)
	{
		super(id, x, y, deg);
		this.alpha = 128;
		this.r = 100;
		this.g = 200;
	}
	
	@Override
	public void handleLaserCollision(int side, Line laser, Vertex v, boolean color)
	{
		Vertex vm = new Vertex(this.vertices[(side+1)%this.vertices.length].y-this.vertices[side].y+v.x, this.vertices[side].x-this.vertices[(side+1)%this.vertices.length].x+v.y);
		// tükör oldalának felezõmerõlegese
		Line m = new Line(v, vm);
		
		// áteresztés
		Vertex v2 = MathHelper.getLineIntersection(m, new Line(this.vertices[(side+2)%this.vertices.length], this.vertices[(side+3)%this.vertices.length]));
		if(color)
			Game.instance.laser1.add(new Line(v, v2));
		else
			Game.instance.laser2.add(new Line(v, v2));
		float dx = v2.x-v.x;
		float dy = v2.y-v.y;
		if(dx == 0)
			dy = Math.abs(dy)/dy*Game.instance.resolution;
		else if(dy == 0)
			dx = Math.abs(dx)/dx*Game.instance.resolution;
		else
		{
			float s = Math.max(Game.instance.resolution/Math.abs(dx), Game.instance.resolution/Math.abs(dy));
			dx *= s;
			dy *= s;
		}
		if(color)
			Game.instance.claser1.add(new Line(v2, new Vertex(v2.x+dx, v2.y+dy)));
		else
			Game.instance.claser2.add(new Line(v2, new Vertex(v2.x+dx, v2.y+dy)));
	}
}