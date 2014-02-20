package hu.hundevelopers.beesmarter;

import hu.hundevelopers.beesmarter.glass.Glass;
import hu.hundevelopers.beesmarter.glass.GlassSquareMirror;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.Vertex;

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
	public List<Line> laser;
	
	public Game(SurfaceHolder holder)
	{
		instance = this;
		this.holder = holder;
	}
	
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void update()
	{
		
	}
	
	public void render()
	{
		Canvas canvas = this.holder.lockCanvas();
		if(canvas == null)
			return;
		canvas.drawColor(Color.BLACK);
		
		Glass g = new GlassSquareMirror(width/2, height/2, 22);
		Line l = new Line(width/10, height/10, 1F, -1F);
		
		Paint paint = new Paint();
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(2F);
		Vertex v = g.getLaserInterSectionPoint(l);
		if(v == null)
			v = new Vertex(l.x, l.y);
		canvas.drawLine(l.x, l.y, v.x, v.y, paint);
		Log.d("DEBUG", ""+l.x+" "+l.y+" "+v.x+" "+v.y);
		g.render(canvas);
		
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
