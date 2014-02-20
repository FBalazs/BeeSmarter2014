package hu.hundevelopers.beesmarter;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback
{
	public GameSurface(Context context)
	{
		super(context);
		getHolder().addCallback(this);
		new Game(getHolder());
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		Game.instance.holder = holder;
		Game.instance.resize(width, height);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		Game.instance.resize(this.getWidth(), this.getHeight());
		Game.instance.update();
		Game.instance.render();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		
	}
}
