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
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		this.thread.holder = holder;
		this.thread.width = width;
		this.thread.height = height;
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
	}
}
