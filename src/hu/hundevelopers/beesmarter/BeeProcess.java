package hu.hundevelopers.beesmarter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class BeeProcess extends Thread
{
	public SurfaceHolder holder;
	public Canvas canvas;
	
	public boolean isRunning;
	public int requestedUps, ups, rps, width, height;
	private int cups, crps;
	private long lastUpdateTime, lastClockTime;
	public float partialTick;
	
	public BeeProcess(SurfaceHolder holder)
	{
		this.holder = holder;
		this.requestedUps = 30;
		this.lastUpdateTime = -1;
	}
	
	public void init()
	{
		
	}
	
	public void update()
	{
		
	}
	
	public void render()
	{
		this.canvas.drawColor(Color.BLACK);
		Paint paint = new Paint();
		paint.setARGB(255, 255, 0, 0);
		
		
		
		paint.setTextSize(15);
		paint.setARGB(255, 0, 255, 0);
		this.canvas.drawText("ups: "+this.ups, 10, 25, paint);
		this.canvas.drawText("rps: "+this.rps, 10, 50, paint);
	}
	
	public void close()
	{
		
	}
	
	@Override
	public void run()
	{
		this.init();
		while(this.isRunning)
		{
			if(1000 <= System.currentTimeMillis()-this.lastClockTime)
			{
				this.ups = this.cups;
				this.rps = this.crps;
				this.cups = this.crps = 0;
				this.lastClockTime = System.currentTimeMillis();
				Log.d("INFO", "ups: "+this.ups+" rps: "+this.rps);
			}
			
			this.partialTick = (System.currentTimeMillis()-this.lastUpdateTime)*this.requestedUps/1000F;
			if(this.partialTick >= 1F)
			{
				this.cups++;
				this.update();
				this.partialTick = 0F;
				this.lastUpdateTime = System.currentTimeMillis();
			}
			this.crps++;
			this.canvas = this.holder.lockCanvas();
			if(this.canvas != null)
			{
				this.render();
				this.holder.unlockCanvasAndPost(this.canvas);
			}
		}
		this.close();
	}
}
