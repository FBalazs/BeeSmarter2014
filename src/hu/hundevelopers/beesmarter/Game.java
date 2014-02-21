package hu.hundevelopers.beesmarter;

import hu.hundevelopers.beesmarter.glass.Glass;
import hu.hundevelopers.beesmarter.glass.GlassSquareMirror;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.Vertex;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.GestureDetector;

public class Game implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
	public static Game instance;
	
	
	
	public SurfaceHolder holder;
	public int width, height, tilesize = 64;
	public List<Glass> glasses;
	public List<Line> laser, claser;
	
	public Game(SurfaceHolder holder)
	{
		instance = this;
		this.holder = holder;
		this.glasses = new ArrayList<Glass>();
		this.laser = new ArrayList<Line>();
		this.claser = new ArrayList<Line>();
	}
	
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
		//this.glasses.add(new GlassSquareMirror(tilesize/2, height/2, 0));
		this.glasses.add(new GlassSquareMirror(width-tilesize/2, height/2, 0));
	}
	
	public void update()
	{
		this.laser.clear();
		this.claser.clear();
		
		this.claser.add(new Line(width/10, height/10, width-tilesize/2, height/2-tilesize/2));
		
		/*Vertex v = this.glasses.get(0).getLaserInterSectionPoint(this.claser.get(0));
		if(v == null)
		{
			this.laser.add(this.claser.get(0));
			this.claser.remove(0);
		}
		else
		{
			this.glasses.get(0).handleLaserCollision(this.claser.get(0));
			this.laser.add(new Line(this.claser.get(0).x1, this.claser.get(0).y1, v.x, v.y));
			this.claser.remove(0);
		}
		if(this.claser.size() > 0)
			this.laser.add(this.claser.get(0));*/
		
		while(this.claser.size() > 0)
		{
			int n = this.claser.size();
			for(int i = 0; i < n; i++)
			{
				int min = -1;
				Vertex vmin = null;
				for(int j = 0; j < this.glasses.size(); j++)
				{
					Vertex v = this.glasses.get(j).getLaserInterSectionPoint(this.claser.get(0));
					if(v != null)
					{
						if((int)v.x != (int)this.claser.get(0).x1 || (int)v.y != (int)this.claser.get(0).y1)
							if((vmin == null || (v.x-this.claser.get(0).x1)*(v.x-this.claser.get(0).x1) + (v.y-this.claser.get(0).y1)*(v.y-this.claser.get(0).y1) < (vmin.x-this.claser.get(0).x1)*(vmin.x-this.claser.get(0).x1) + (vmin.y-this.claser.get(0).y1)*(vmin.y-this.claser.get(0).y1)))
							{
								min = j;
								vmin = v;
							}
					}
				}
				if(min != -1)
				{
					this.laser.add(new Line(this.claser.get(0).x1, this.claser.get(0).y1, vmin.x, vmin.y));
					this.glasses.get(min).handleLaserCollision(this.claser.get(0));
					this.claser.remove(0);
				}
				else
				{
					this.laser.add(this.claser.get(0));
					this.claser.remove(0);
				}
			}
		}
	}
	
	public void render()
	{
		Canvas canvas = this.holder.lockCanvas();
		if(canvas == null)
			return;
		canvas.drawColor(Color.BLACK);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(2F);
		for(int i = 0; i < this.laser.size(); i++)
			canvas.drawLine(this.laser.get(i).x1, this.laser.get(i).y1, this.laser.get(i).x2, this.laser.get(i).y2, paint);
		for(int i = 0; i < this.glasses.size(); i++)
			this.glasses.get(i).render(canvas);
		
		/*Glass g = new GlassSquareMirror(width/2, height/2, 45);
		Line l = new Line(width/10, height/2, width, height/2);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(2F);
		Vertex v = g.getLaserInterSectionPoint(l);
		if(v != null)
		{
			l.x2 = v.x;
			l.y2 = v.y;
		}
		canvas.drawLine(l.x1, l.y1, l.x2, l.y2, paint);
		g.render(canvas);*/
		
		this.holder.unlockCanvasAndPost(canvas);
	}
	
	
	
	/**
	 * Function called by activity when the back button has been pressed.
	 * @return True if the press is processed, false if it isn't.
	 */
	public boolean onBackPressed()
	{
		
		return false;
	}
	
	public void onMenuPressed()
	{
		
	}

	@Override
	public boolean onDoubleTap(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}
}
