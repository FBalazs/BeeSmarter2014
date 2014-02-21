package hu.hundevelopers.beesmarter;

import hu.hundevelopers.beesmarter.glass.Glass;
import hu.hundevelopers.beesmarter.glass.GlassSquareHalfMirror;
import hu.hundevelopers.beesmarter.glass.GlassSquareMirror;
import hu.hundevelopers.beesmarter.glass.GlassSquarePrism;
import hu.hundevelopers.beesmarter.glass.GlassTrianglePrism;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.Vertex;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.GestureDetector;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
	public static Game instance;
	
	
	
	public GestureDetectorCompat gestureDetector;
	public int width, height, tilesize, tilenumber = 6, size;
	public List<Glass> glasses;
	public List<Line> laser, claser;
	
	public int selectedGlass, squaredTouchRange;
	
	public Game(Context context)
	{
		super(context);
		this.getHolder().addCallback(this);
		this.gestureDetector = new GestureDetectorCompat(context, this);
		this.gestureDetector.setOnDoubleTapListener(this);
		
		instance = this;
		this.glasses = new ArrayList<Glass>();
		this.laser = new ArrayList<Line>();
		this.claser = new ArrayList<Line>();
		this.selectedGlass = -1;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		this.resize(width, height);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		this.resize(this.getWidth(), this.getHeight());
		this.update();
		this.render();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		
	}
	
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.size = Math.min(width, height);
		this.tilesize = this.size/this.tilenumber;
		this.squaredTouchRange = this.tilesize*this.tilesize/2;
		
		this.glasses.clear();
		this.glasses.add(new GlassSquareHalfMirror(width/2, tilesize/2, 0));
		this.glasses.add(new GlassSquareMirror(tilesize/4, tilesize, 0));
		this.glasses.add(new GlassSquareMirror(width, 0, 45));
		this.glasses.add(new GlassSquareHalfMirror(width-tilesize/2, tilesize*3/2, 0));
		this.glasses.add(new GlassSquareMirror(width, tilesize*3, 45));
		this.glasses.add(new GlassSquarePrism(width/2, tilesize*5/2, 0));
		this.glasses.add(new GlassTrianglePrism(width/2, tilesize*9/2, 45));
	}
	
	public void update()
	{
		this.laser.clear();
		this.claser.clear();
		
		this.claser.add(new Line(tilesize/2, tilesize/2, size, tilesize/2));
		
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
						if(1F <= Math.abs(this.claser.get(0).x1-v.x) || 1F <= Math.abs(this.claser.get(0).y1-v.y))
							if((vmin == null || (v.x-this.claser.get(0).x1)*(v.x-this.claser.get(0).x1) + (v.y-this.claser.get(0).y1)*(v.y-this.claser.get(0).y1) < (vmin.x-this.claser.get(0).x1)*(vmin.x-this.claser.get(0).x1) + (vmin.y-this.claser.get(0).y1)*(vmin.y-this.claser.get(0).y1)))
							{
								min = j;
								vmin = v;
							}
					}
				}
				if(min != -1)
				{
					Line nl = new Line(this.claser.get(0).x1, this.claser.get(0).y1, vmin.x, vmin.y);
					if(!this.laser.contains(nl))
					{
						this.laser.add(nl);
						this.glasses.get(min).handleLaserCollision(this.claser.get(0));
					}
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
		Canvas canvas = this.getHolder().lockCanvas();
		if(canvas == null)
			return;
		canvas.drawColor(Color.BLACK);
		
		Paint paint = new Paint();
		paint.setTextSize(15);
		paint.setAntiAlias(true);
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(3F);
		for(int i = 0; i < this.laser.size(); i++)
			canvas.drawLine(this.laser.get(i).x1, this.laser.get(i).y1, this.laser.get(i).x2, this.laser.get(i).y2, paint);
		for(int i = 0; i < this.glasses.size(); i++)
			this.glasses.get(i).render(canvas);
		paint.setARGB(255, 0, 255, 0);
		
		this.getHolder().unlockCanvasAndPost(canvas);
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
	public boolean onTouchEvent(MotionEvent event)
	{
		if(this.gestureDetector.onTouchEvent(event))
			return true;
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			this.selectedGlass = -1;
			for(int i = 0; i < this.glasses.size() && this.selectedGlass == -1; i++)
				if((this.glasses.get(i).x-event.getX())*(this.glasses.get(i).x-event.getX()) + (this.glasses.get(i).y-event.getY())*(this.glasses.get(i).y-event.getY()) <= this.squaredTouchRange)
					this.selectedGlass = i;
			this.render();
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			this.selectedGlass = -1;
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if(this.selectedGlass != -1)
			{
				this.glasses.get(this.selectedGlass).move((int)event.getX(), (int)event.getY());
				this.update();
				this.render();
				return true;
			}
		}
		return false;
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