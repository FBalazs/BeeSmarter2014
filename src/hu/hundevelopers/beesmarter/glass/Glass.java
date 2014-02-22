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
	public int x, y, deg, alpha, r, g, b;
	public Vertex[] vertices;
	
	public Glass()
	{
		this(0, 0, 0);
	}
	
	public Glass(int x, int y, int deg)
	{
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
		Vertex vmin = MathHelper.getLineIntersection(laser, new Line(this.vertices[0], this.vertices[1]));
		if(vmin != null && (!MathHelper.isIntersectionPointOnSegment(new Line(this.vertices[0], this.vertices[1]), vmin) || !MathHelper.isIntersectionPointOnSegment(laser, vmin)))	
			vmin = null;
		for(int i = 1; i < this.vertices.length; i++)
		{
			Vertex v = MathHelper.getLineIntersection(laser, new Line(this.vertices[i], this.vertices[(i+1)%this.vertices.length]));
			if(v != null && (!MathHelper.isIntersectionPointOnSegment(laser, v) || !MathHelper.isIntersectionPointOnSegment(new Line(this.vertices[i], this.vertices[(i+1)%this.vertices.length]), v)))
				v = null;
			if(v != null && (vmin == null || (vmin.x-laser.x1)*(vmin.x-laser.x1) + (vmin.y-laser.y1)*(vmin.y-laser.y1) > (v.x-laser.x1)*(v.x-laser.x1) + (v.y-laser.y1)*(v.y-laser.y1)))
			{
				vmin = v;
				min = i;
			}
		}
		if(vmin != null)
			this.handleLaserCollision(min, laser, vmin);
	}
	
	public abstract void handleLaserCollision(int side, Line laser, Vertex V);
	
	public void rotate(int amount)
	{
		this.deg = amount;
		this.calculateVertices();
		this.checkOutside();
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
	
	public void move(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.calculateVertices();
		this.checkOutside();
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