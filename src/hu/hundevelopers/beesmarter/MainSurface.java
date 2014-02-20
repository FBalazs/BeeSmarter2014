package hu.hundevelopers.beesmarter;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainSurface extends SurfaceView implements SurfaceHolder.Callback
{
	public BeeProcess thread;
	
	public MainSurface(Context context)
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
		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		
	}
}
