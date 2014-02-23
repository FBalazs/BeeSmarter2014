package hu.hundevelopers.beesmarter.glass;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import hu.hundevelopers.beesmarter.Game;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.MathHelper;
import hu.hundevelopers.beesmarter.math.Vertex;

public class GlassSquareEmitter extends GlassSquare
{
	public GlassSquareEmitter()
	{
		this(0, 0, 0, 0);
	}
	
	public GlassSquareEmitter(int id, int x, int y, int deg)
	{
		super(id, x, y, deg);
		this.r = 64;
		this.g = 64;
		this.b = 64;
	}
	
	@Override
	public boolean isMoveable()
	{
		return false;
	}
	
	@Override
	public void onUpdate()
	{
		Game.instance.claser.add(new Line(this.x, this.y, this.x+Game.instance.resolution*2*MathHelper.cos(this.deg), this.y+Game.instance.resolution*2*MathHelper.sin(this.deg)));
	}
	
	@Override
	public Vertex getLaserInterSectionPoint(Line laser)
	{
		if(Math.round(laser.x1) == this.x && Math.round(laser.y1) == this.y)
			return null;
		return super.getLaserInterSectionPoint(laser);
	}

	@Override
	public void handleLaserCollision(int side, Line laser, Vertex V)
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
	}
}
