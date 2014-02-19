package hu.hundevelopers.beesmarter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class BeeProcess extends Thread
{
	private SurfaceHolder holder;
	public boolean isRunning;
	
	public BeeProcess(SurfaceHolder holder)
	{
		this.holder = holder;
	}
	
	public void update()
	{
		
	}
	
	public void render(Canvas canvas)
	{
		canvas.drawColor(Color.BLACK);
		Paint paint = new Paint();
		paint.setARGB(255, 255, 0, 0);
		canvas.drawCircle(50, 50, 25, paint);
	}
	
	@Override
	public void run()
	{
		while(this.isRunning)
		{
			Canvas canvas = this.holder.lockCanvas();
			this.render(canvas);
			this.holder.unlockCanvasAndPost(canvas);
		}
	}
}
