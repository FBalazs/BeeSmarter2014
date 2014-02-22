package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquareHalfMirror extends Glass
{
	public GlassSquareHalfMirror()
	{
		this(0, 0, 0);
	}
	
	public GlassSquareHalfMirror(int x, int y, int deg)
	{
		super(x, y, deg);
		this.alpha = 64;
	}

	@Override
	public void calculateVertices()
	{
		this.vertices = new Vertex[4];
		int d = 45;
		for(int i = 0; i < 4; i++)
		{
			this.vertices[i] = new Vertex(this.x+MathHelper.cos(d+this.deg)*Game.instance.tileres/(float)Math.sqrt(2), this.y+MathHelper.sin(d+this.deg)*Game.instance.tileres/(float)Math.sqrt(2));
			d += 90;
		}
	}
	
	@Override
	public void handleLaserCollision(int side, Line laser, Vertex v)
	{
		Vertex vm = new Vertex(this.vertices[(side+1)%this.vertices.length].y-this.vertices[side].y+v.x, this.vertices[side].x-this.vertices[(side+1)%this.vertices.length].x+v.y);
		// t�k�r oldal�nak felez�mer�legese
		Line m = new Line(v, vm);
		// mer�leges a kezd�pontb�l a felez�mer�legesre
		Line m2 = new Line(new Vertex(laser.x1, laser.y1), new Vertex(laser.x1-this.vertices[side].x+this.vertices[(side+1)%this.vertices.length].x, laser.y1-this.vertices[side].y+this.vertices[(side+1)%this.vertices.length].y));
		// metsz�spont
		Vertex k = MathHelper.getLineIntersection(m, m2);
		// a t�kr�z�tt pont
		Vertex t = new Vertex(laser.x1+2*(k.x-laser.x1), laser.y1+2*(k.y-laser.y1));
		float dx = t.x-v.x;
		float dy = t.y-v.y;
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
		Game.instance.claser.add(new Line(v, new Vertex(v.x+dx, v.y+dy)));
		
		// �tereszt�s
		Vertex v2 = MathHelper.getLineIntersection(m, new Line(this.vertices[(side+2)%this.vertices.length], this.vertices[(side+3)%this.vertices.length]));
		Game.instance.laser.add(new Line(v, v2));
		dx = v2.x-v.x;
		dy = v2.y-v.y;
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
		Game.instance.claser.add(new Line(v2, new Vertex(v2.x+dx, v2.y+dy)));
	}
}