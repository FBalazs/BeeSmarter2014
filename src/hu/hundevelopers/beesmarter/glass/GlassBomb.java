package hu.hundevelopers.beesmarter.glass;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassBomb extends Glass
{
	public boolean hit1, hit2;
	
	public GlassBomb()
	{
		this(0, 0, 0, 0);
	}
	
	public GlassBomb(int id, int x, int y, int deg)
	{
		super(id, x, y, deg);
		this.r = 64;
		this.g = 64;
		this.b = 64;
		this.hit1 = false;
		this.hit2 = false;
	}
	
	@Override
	public boolean isMoveable()
	{
		return false;
	}
	
	@Override
	public void calculateVertices()
	{
		this.bbMinX = this.bbMaxX = this.x;
		this.bbMinY = this.bbMaxY = this.y;
		this.vertices = new Vertex[8];
		for(int i = 0; i < 8; i++)
		{
			float x = this.x+MathHelper.cos(i*360/8+360/16+this.deg)*Game.instance.tileres/2F/(float)Math.cos(22.5F);
			float y = this.y+MathHelper.sin(i*360/8+360/16+this.deg)*Game.instance.tileres/2F/(float)Math.cos(22.5F);
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
	public void onUpdate()
	{
		this.hit1 = false;
		this.hit2 = false;
	}
	
	@Override
	public void handleLaserCollision(int side, Line laser, Vertex V, boolean color)
	{
		if(color)
			this.hit1 = true;
		else
			this.hit2 = true;
	}
	
	@Override
	public void render(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setARGB(255, this.hit1 ? 255 : 100, 100, this.hit2 ? 255 : 100);
		paint.setStrokeWidth(5F);
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
		paint.setARGB(255, 48, 48, 48);
		canvas.drawPath(path, paint);
	}
}
