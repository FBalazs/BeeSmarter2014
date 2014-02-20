package hu.hundevelopers.beesmarter;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.GestureDetector;

public class BeeProcess implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
	public SurfaceHolder holder;
	public int width, height;
	public List<Glass> glasses;
	public List<Line> laser;
	
	public BeeProcess(SurfaceHolder holder)
	{
		this.holder = holder;
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
	
	public void update()
	{
		y = (n1*((y2-y1)*x1+(x1-x2)*y1) - (y2-y1)*(n1*p+n2*q))/(n1*(x1-x2)-n2*(y2-y1));
	}
	
	public void render()
	{
		Canvas canvas = this.holder.lockCanvas();
		if(canvas == null)
			return;
		canvas.drawColor(Color.BLACK);
		
		Paint paint = new Paint();
		paint.setARGB(255, 255, 0, 0);
		
		this.holder.unlockCanvasAndPost(canvas);
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
