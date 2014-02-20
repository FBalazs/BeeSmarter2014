package hu.hundevelopers.beesmarter;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements SurfaceHolder.Callback
{
	private BeeProcess thread;
	
	public MainView(Context context)
	{
		super(context);
		getHolder().addCallback(this);
		this.thread = new BeeProcess(getHolder());
	}
	
	/*@Override
	protected void onDraw(Canvas canvas)
	{
		Log.d("TAG", "MSG");
		canvas = getHolder().lockCanvas();
		Paint paint = new Paint();
		paint.setARGB(255, 255, 0, 0);
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(icon, 50, 50, paint);
		canvas.drawCircle(150, 150, 25, paint);
		getHolder().unlockCanvasAndPost(canvas);
	}*/
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		this.thread.isRunning = true;
		this.thread.start();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		this.thread.isRunning = false;
		System.exit(0);
	}
}
