package hu.hundevelopers.beesmarter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements SurfaceHolder.Callback
{
	Bitmap icon;
	
	public MainView(Context context)
	{
		super(context);
		getHolder().addCallback(this);
		
		icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawColor(Color.RED);
		canvas.drawBitmap(icon, 50, 50, null);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		
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
