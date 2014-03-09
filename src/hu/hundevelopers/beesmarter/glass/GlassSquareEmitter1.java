package hu.hundevelopers.beesmarter.glass;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquareEmitter1 extends Glass
{
	public GlassSquareEmitter1()
	{
		this(0, 0, 0, 0);
	}
	
	public GlassSquareEmitter1(int id, int x, int y, int deg)
	{
		super(id, x, y, deg);
		this.r = 64;
		this.g = 64;
		this.b = 64;
	}
	
	@Override
	public void calculateVertices()
	{
		this.bbMinX = this.bbMaxX = this.x;
		this.bbMinY = this.bbMaxY = this.y;
		this.vertices = new Vertex[4];
		int d = 45;
		for(int i = 0; i < 4; i++)
		{
			float x = this.x+MathHelper.cos(d+this.deg)*Game.instance.tileres/1.5F/(float)Math.sqrt(2);
			float y = this.y+MathHelper.sin(d+this.deg)*Game.instance.tileres/1.5F/(float)Math.sqrt(2);
			if(x < this.bbMinX)
				this.bbMinX = x;
			if(x > this.bbMaxX)
				this.bbMaxX = x;
			if(y < this.bbMinY)
				this.bbMinY = y;
			if(y > this.bbMaxY)
				this.bbMaxY = y;
			this.vertices[i] = new Vertex(x, y);
			d += 90;
		}
	}
	
	@Override
	public boolean isMoveable()
	{
		return false;
	}
	
	@Override
	public void onUpdate()
	{
		Game.instance.claser1.add(new Line(this.x, this.y, this.x+Game.instance.resolution*2*MathHelper.cos(this.deg), this.y+Game.instance.resolution*2*MathHelper.sin(this.deg)));
	}
	
	@Override
	public Vertex getLaserInterSectionPoint(Line laser)
	{
		if(Math.round(laser.x1) == this.x && Math.round(laser.y1) == this.y)
			return null;
		return super.getLaserInterSectionPoint(laser);
	}

	@Override
	public void handleLaserCollision(int side, Line laser, Vertex V, boolean color)
	{
		
	}
	
	@Override
	public void render(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setARGB(255, 255, 0, 0);
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
		paint.setARGB(255, 255, 0, 0);
		canvas.drawLine(this.x*Game.instance.size/Game.instance.resolution, this.y*Game.instance.size/Game.instance.resolution, this.vertices[3].x/2*Game.instance.size/Game.instance.resolution+this.vertices[0].x/2*Game.instance.size/Game.instance.resolution, this.vertices[3].y/2*Game.instance.size/Game.instance.resolution+this.vertices[0].y/2*Game.instance.size/Game.instance.resolution, paint);
	}
}
