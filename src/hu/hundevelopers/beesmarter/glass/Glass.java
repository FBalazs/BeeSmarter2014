package hu.hundevelopers.beesmarter.glass;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public abstract class Glass
{
	public int x, y, deg, alpha;
	public Vertex[] vertices;
	
	public Glass(int x, int y, int deg)
	{
		this.alpha = 255;
		this.x = x;
		this.y = y;
		this.deg = deg;
		this.calculateVertices();
	}
	
	public abstract void calculateVertices();
	
	public Vertex getLaserInterSectionPoint(Line laser)
	{
		Vertex min = MathHelper.getLineIntersection(new Line(this.vertices[0], this.vertices[1]), laser);
		if(Math.max(this.vertices[0].x, this.vertices[1].x) <= min.x
			|| min.x <= Math.min(this.vertices[0].x, this.vertices[1].x)
			|| Math.max(this.vertices[0].y, this.vertices[1].y) <= min.y
			|| min.y <= Math.min(this.vertices[0].y, this.vertices[1].y))
			min = null;
		for(int i = 1; i < this.vertices.length; i++)
		{
			Vertex v = MathHelper.getLineIntersection(new Line(this.vertices[i], this.vertices[(i+1)%this.vertices.length]), laser);
			if(v != null)
			{
				if(Math.min(this.vertices[i].x, this.vertices[(i+1)%this.vertices.length].x) <= v.x
					&& v.x <= Math.max(this.vertices[i].x, this.vertices[(i+1)%this.vertices.length].x)
					&& Math.min(this.vertices[i].y, this.vertices[(i+1)%this.vertices.length].y) <= v.y
					&& v.y <= Math.max(this.vertices[i].y, this.vertices[(i+1)%this.vertices.length].y)
					&& (v.x-laser.x)/laser.nx > 0F && (v.y-laser.y/laser.ny) > 0F && (min == null || (v.x-laser.x)*(v.x-laser.x)+(v.y-laser.y)*(v.y-laser.y) < (min.x-laser.x)*(min.x-laser.x)+(min.y-laser.y)*(min.y-laser.y)))
					min = v;
			}
		}
		return min;
	}
	
	public abstract void handleLaserCollision(Line laser);
	
	public void rotate(int amount)
	{
		this.deg += amount;
		this.calculateVertices();
	}
	
	public void move(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.calculateVertices();
	}
	
	public void render(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setARGB(255, 100, 150, 255);
		paint.setStrokeWidth(2F);
		paint.setAntiAlias(true);
		
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(this.vertices[this.vertices.length-1].x, this.vertices[this.vertices.length-1].y);
		for(int i = 0; i < this.vertices.length; i++)
		{
			path.lineTo(this.vertices[i].x, this.vertices[i].y);
			canvas.drawLine(this.vertices[i].x, this.vertices[i].y, this.vertices[(i+1)%this.vertices.length].x, this.vertices[(i+1)%this.vertices.length].y, paint);
		}
		path.close();
		
		paint.setARGB(this.alpha, 255, 255, 255);
		canvas.drawPath(path, paint);
	}
}