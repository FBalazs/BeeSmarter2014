package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassTrianglePrism extends Glass
{
	public GlassTrianglePrism()
	{
		this(0, 0, 0, 0);
	}
	
	public GlassTrianglePrism(int id, int x, int y, int deg)
	{
		super(id, x, y, deg);
		this.alpha = 128;
		this.r = 100;
		this.g = 200;
	}
	
	@Override
	public void calculateVertices()
	{
		this.bbMinX = this.bbMaxX = this.x;
		this.bbMinY = this.bbMaxY = this.y;
		this.vertices = new Vertex[3];
		for(int i = 0; i < 3; i++)
		{
			float x = this.x+Game.instance.tileres*MathHelper.cos(this.deg+45+i*90)/(float)Math.sqrt(2);
			float y = this.y+Game.instance.tileres*MathHelper.sin(this.deg+45+i*90)/(float)Math.sqrt(2);
			if(x < this.bbMinX)
				this.bbMinX = x;
			if(x > this.bbMaxX)
				this.bbMaxX = x;
			if(y < this.bbMinY)
				this.bbMinY = y;
			if(y > this.bbMaxY)
				this.bbMaxY = y;
			this.vertices[i] = new Vertex(x, y);
		}
	}
	
	@Override
	public void handleLaserCollision(int side, Line laser, Vertex v, boolean color)
	{
		Vertex vm = new Vertex(this.vertices[(side+1)%this.vertices.length].y-this.vertices[side].y+v.x, this.vertices[side].x-this.vertices[(side+1)%this.vertices.length].x+v.y);
		// tükör oldalának felezõmerõlegese
		Line m = new Line(v, vm);
		
		// áteresztés
		Line sl = new Line(this.vertices[(side+1)%this.vertices.length], this.vertices[(side+2)%this.vertices.length]);
		Vertex v2 = MathHelper.getLineIntersection(m, sl);
		if(v2 != null && !MathHelper.isIntersectionPointOnSegment(sl, v2))
			v2 = null;
		if(v2 == null)
		{
			sl = new Line(this.vertices[(side+2)%this.vertices.length], this.vertices[(side+3)%this.vertices.length]);
			v2 = MathHelper.getLineIntersection(m, sl);
			if(v2 != null && !MathHelper.isIntersectionPointOnSegment(sl, v2))
				v2 = null;
		}
		if(v2 == null)
			return;
		if(color)
			Game.instance.laser1.add(new Line(v, v2));
		else
			Game.instance.laser2.add(new Line(v, v2));
		float dx = sl.y1-sl.y2;
		float dy = sl.x2-sl.x1;
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