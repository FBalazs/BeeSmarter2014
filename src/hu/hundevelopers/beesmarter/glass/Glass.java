package hu.hundevelopers.beesmarter.glass;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public abstract class Glass
{
	public int id, x, y, deg, alpha, r, g, b;
	public Vertex[] vertices;
	
	public Glass()
	{
		this(0, 0, 0, 0);
	}
	
	public Glass(int id, int x, int y, int deg)
	{
		this.id = id;
		this.alpha = 255;
		this.r = 255;
		this.g = 255;
		this.b = 255;
		this.x = x;
		this.y = y;
		this.deg = deg;
		this.calculateVertices();
	}
	
	public boolean isMoveable()
	{
		return true;
	}
	
	public abstract void calculateVertices();
	
	public Vertex getLaserInterSectionPoint(Line laser)
	{
		Vertex min = MathHelper.getLineIntersection(laser, new Line(this.vertices[0], this.vertices[1]));
		if(min != null && (!MathHelper.isIntersectionPointOnSegment(new Line(this.vertices[0], this.vertices[1]), min) || !MathHelper.isIntersectionPointOnSegment(laser, min)))	
			min = null;
		for(int i = 1; i < this.vertices.length; i++)
		{
			Vertex v = MathHelper.getLineIntersection(laser, new Line(this.vertices[i], this.vertices[(i+1)%this.vertices.length]));
			if(v != null && (!MathHelper.isIntersectionPointOnSegment(laser, v) || !MathHelper.isIntersectionPointOnSegment(new Line(this.vertices[i], this.vertices[(i+1)%this.vertices.length]), v)))
				v = null;
			if(v != null && (min == null || (min.x-laser.x1)*(min.x-laser.x1) + (min.y-laser.y1)*(min.y-laser.y1) > (v.x-laser.x1)*(v.x-laser.x1) + (v.y-laser.y1)*(v.y-laser.y1)))
				min = v;
		}
		return min;
	}
	
	public void handleLaserCollision(Line laser)
	{
		int min = 0;
		float dmin = MathHelper.getLineAndVertexSquaredDistance(new Line(this.vertices[0], this.vertices[1]), new Vertex(laser.x1, laser.y1));
		Vertex vmin = MathHelper.getLineIntersection(laser, new Line(this.vertices[0], this.vertices[1]));
		if(vmin != null && (!MathHelper.isIntersectionPointOnSegment(new Line(this.vertices[0], this.vertices[1]), vmin) || !MathHelper.isIntersectionPointOnSegment(laser, vmin)))	
			vmin = null;
		for(int i = 1; i < this.vertices.length; i++)
		{
			Line s = new Line(this.vertices[i], this.vertices[(i+1)%this.vertices.length]);
			float d = MathHelper.getLineAndVertexSquaredDistance(s, new Vertex(laser.x1, laser.y1));
			Vertex v = MathHelper.getLineIntersection(laser, s);
			if(v != null && (!MathHelper.isIntersectionPointOnSegment(laser, v) || !MathHelper.isIntersectionPointOnSegment(s, v)))
				v = null;
			if(v != null && (vmin == null || /*(vmin.x-laser.x1)*(vmin.x-laser.x1) + (vmin.y-laser.y1)*(vmin.y-laser.y1) > (v.x-laser.x1)*(v.x-laser.x1) + (v.y-laser.y1)*(v.y-laser.y1)*/ d < dmin))
			{
				vmin = v;
				min = i;
				dmin = d;
			}
		}
		if(vmin != null)
			this.handleLaserCollision(min, laser, vmin);
	}
	
	public abstract void handleLaserCollision(int side, Line laser, Vertex V);
	
	public void rotate(int deg)
	{
		int pdeg = this.deg;
		this.deg = deg;
		this.calculateVertices();
		this.checkOutside();
		if(this.isColliding())
		{
			this.deg = pdeg;
			this.calculateVertices();
		}
	}
	
	public void move(int x, int y)
	{
		int px = this.x;
		int py = this.y;
		this.x = x;
		this.y = y;
		this.calculateVertices();
		this.checkOutside();
		if(this.isColliding())
		{
			this.x = px;
			this.y = py;
			this.calculateVertices();
		}
	}
	
	public void checkOutside()
	{
		float minx, maxx, miny, maxy;
		minx = maxx = miny = maxy = 0F;
		for(int i = 0; i < this.vertices.length; i++)
		{
			if(this.vertices[i].x < minx)
				minx = this.vertices[i].x;
			if(this.vertices[i].x-Game.instance.resolution > maxx)
				maxx = this.vertices[i].x-Game.instance.resolution;
			if(this.vertices[i].y < miny)
				miny = this.vertices[i].y;
			if(this.vertices[i].y-Game.instance.resolution > maxy)
				maxy = this.vertices[i].y-Game.instance.resolution;
		}
		this.x -= minx;
		this.x -= maxx;
		this.y -= miny;
		this.y -= maxy;
		this.calculateVertices();
	}
	
	public boolean isColliding()
	{
		for(int i = 0; i < Game.instance.glasses.size(); i++)
			if(this.id != Game.instance.glasses.get(i).id)
			{
				for(int j = 0; j < this.vertices.length; j++)
				{
					Line s1 = new Line(this.vertices[j], this.vertices[(j+1)%this.vertices.length]);
					for(int k = 0; k < Game.instance.glasses.get(i).vertices.length; k++)
					{
						Line s2 = new Line(Game.instance.glasses.get(i).vertices[k], Game.instance.glasses.get(i).vertices[(k+1)%Game.instance.glasses.get(i).vertices.length]);
						Vertex v = MathHelper.getLineIntersection(s1, s2);
						if(v != null && MathHelper.isIntersectionPointOnSegment(s1, v) && MathHelper.isIntersectionPointOnSegment(s2, v))
							return true;
					}
				}
			}
		return false;
	}
	
	public boolean isVertexInBounds(float x, float y)
	{
		Line l = new Line(x, y, this.x, this.y);
		for(int i = 0; i < this.vertices.length; i++)
		{
			Line s = new Line(this.vertices[i], this.vertices[(i+1)%this.vertices.length]);
			Vertex c = MathHelper.getLineIntersection(l, s);
			if(c != null && MathHelper.isIntersectionPointOnSegment(l, c) && MathHelper.isIntersectionPointOnSegment(s, c))
				return false;
		}
		return true;
	}
	
	public void render(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setARGB(255, 100, 150, 255);
		paint.setStrokeWidth(2F);
		paint.setAntiAlias(true);
		
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(this.vertices[this.vertices.length-1].x*Game.instance.size/Game.instance.resolution, this.vertices[this.vertices.length-1].y*Game.instance.size/Game.instance.resolution);
		for(int i = 0; i < this.vertices.length; i++)
		{
			path.lineTo(this.vertices[i].x*Game.instance.size/Game.instance.resolution, this.vertices[i].y*Game.instance.size/Game.instance.resolution);
			canvas.drawLine(this.vertices[i].x*Game.instance.size/Game.instance.resolution, this.vertices[i].y*Game.instance.size/Game.instance.resolution, this.vertices[(i+1)%this.vertices.length].x*Game.instance.size/Game.instance.resolution, this.vertices[(i+1)%this.vertices.length].y*Game.instance.size/Game.instance.resolution, paint);
		}
		path.close();
		
		paint.setARGB(this.alpha, this.r, this.g, this.b);
		canvas.drawPath(path, paint);
	}
}