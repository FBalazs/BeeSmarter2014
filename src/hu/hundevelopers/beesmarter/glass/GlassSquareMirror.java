package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquareMirror extends Glass
{
	public GlassSquareMirror()
	{
		this(0, 0, 0);
	}
	
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
		// merõleges a kezdõpontból a felezõmerõlegesre
		Line m2 = new Line(new Vertex(laser.x1, laser.y1), new Vertex(laser.x1-this.vertices[side].x+this.vertices[(side+1)%this.vertices.length].x, laser.y1-this.vertices[side].y+this.vertices[(side+1)%this.vertices.length].y));
		// metszéspont
		Vertex k = MathHelper.getLineIntersection(m, m2);
		// a tükrözött pont
		Vertex t = new Vertex(laser.x1+2*(k.x-laser.x1), laser.y1+2*(k.y-laser.y1));
		float dx = (t.x-v.x);
		float dy = (t.y-v.y);
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
		Game.instance.claser.add(new Line(v, new Vertex(v.x+dx, v.y+dy)));
	}
}