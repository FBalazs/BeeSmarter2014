package hu.hundevelopers.beesmarter.glass;

import android.graphics.Canvas;
import android.graphics.Paint;
import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassCircleTarget extends Glass
{
	public boolean hit;
	
	public GlassCircleTarget()
	{
		this(0, 0, 0, 0);
	}
	
	public GlassCircleTarget(int id, int x, int y, int deg)
	{
		super(id, x, y, deg);
		this.r = 64;
		this.g = 64;
		this.b = 64;
		this.hit = false;
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
		this.vertices = new Vertex[36];
		for(int i = 0; i < 36; i++)
		{
			float x = this.x+MathHelper.cos(i*10)*Game.instance.tileres/4;
			float y = this.y+MathHelper.sin(i*10)*Game.instance.tileres/4;
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
		this.hit = false;
	}
	
	@Override
	public void handleLaserCollision(int side, Line laser, Vertex V, boolean color)
	{
		this.hit = true;
	}
	
	@Override
	public void render(Canvas canvas)
	{
		Paint paint = new Paint();
		if(this.hit)
			paint.setARGB(255, 255, 64, 64);
		else
			paint.setARGB(255, 100, 100, 100);
		paint.setStrokeWidth(5F);
		paint.setAntiAlias(true);
		
		canvas.drawCircle(this.x*Game.instance.size/Game.instance.resolution, this.y*Game.instance.size/Game.instance.resolution, Game.instance.tilesize/4, paint);
		if(this.hit)
		{
			paint.setARGB(255, 200, 200, 200);
			canvas.drawCircle(this.x*Game.instance.size/Game.instance.resolution, this.y*Game.instance.size/Game.instance.resolution, Game.instance.tilesize/4-4, paint);
		}
		else
		{
			paint.setARGB(255, 150, 150, 150);
			canvas.drawCircle(this.x*Game.instance.size/Game.instance.resolution, this.y*Game.instance.size/Game.instance.resolution, Game.instance.tilesize/4-2, paint);
		}
	}
}
