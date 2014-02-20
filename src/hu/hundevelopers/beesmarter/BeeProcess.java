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
	private long lastUpdateTime, lastClockTime, tickStartTime;
	public float partialTick;
	
	public BeeProcess(SurfaceHolder holder)
	{
		this.holder = holder;
		this.requestedUps = 30;
		this.lastUpdateTime = -1;
	}
	
	float y, my, ay, r;
	
	public void init()
	{
		this.y = this.height*0.75F;
		this.my = 0F;
		this.ay = -this.height*9.81F/50F;
		this.r = this.height/100F;
	}
	
	public void update()
	{
		if(this.lastUpdateTime == -1)
			this.init();
		this.y += this.my;
		this.my += this.ay/this.requestedUps;
		if(this.y <= this.r && this.my < 0F)
		{
			this.my *= -0.75F;
			this.y = this.r;
		}
	}
	
	public void render()
	{
		this.canvas.drawColor(Color.BLACK);
		Paint paint = new Paint();
		paint.setARGB(255, 255, 0, 0);
		this.canvas.drawCircle(this.width/2, this.height-this.y-this.my*this.partialTick, 25, paint);
		paint.setARGB(255, 0, 255, 0);
		paint.setTextSize(15);
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
			
			this.tickStartTime = System.currentTimeMillis();
			this.partialTick = (this.tickStartTime-this.lastUpdateTime)*this.requestedUps/1000F;
			if(this.partialTick >= 1F)
			{
				this.cups++;
				this.update();
				this.partialTick = 0F;
				this.lastUpdateTime = this.tickStartTime;
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
