package hu.hundevelopers.beesmarter.glass;

import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassTrianglePrism extends Glass
{
	public GlassTrianglePrism(int x, int y, int deg)
	{
		super(x, y, deg);
		this.alpha = 128;
		this.r = 100;
		this.g = 200;
	}
	
	@Override
	public void calculateVertices()
	{
		this.vertices = new Vertex[3];
		this.vertices[0] = new Vertex(this.x+Game.instance.tilesize*MathHelper.cos(this.deg+45)/(float)Math.sqrt(2), this.y+Game.instance.tilesize*MathHelper.sin(this.deg+45)/(float)Math.sqrt(2));
		this.vertices[1] = new Vertex(this.x+Game.instance.tilesize*MathHelper.cos(this.deg+135)/(float)Math.sqrt(2), this.y+Game.instance.tilesize*MathHelper.sin(this.deg+135)/(float)Math.sqrt(2));
		this.vertices[2] = new Vertex(this.x+Game.instance.tilesize*MathHelper.cos(this.deg+225)/(float)Math.sqrt(2), this.y+Game.instance.tilesize*MathHelper.sin(this.deg+225)/(float)Math.sqrt(2));
	}
	
	@Override
	public void handleLaserCollision(int side, Line laser, Vertex v)
	{
		Vertex vm = new Vertex(-(this.vertices[side].y-v.y)+v.x, (this.vertices[side].x-v.x)+v.y);
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
		Game.instance.laser.add(new Line(v, v2));
		float dx = sl.y1-sl.y2;
		float dy = sl.x2-sl.x1;
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