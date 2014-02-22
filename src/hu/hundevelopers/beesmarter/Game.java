package hu.hundevelopers.beesmarter;

import hu.hundevelopers.beesmarter.glass.Glass;
import hu.hundevelopers.beesmarter.glass.GlassSquareHalfMirror;
import hu.hundevelopers.beesmarter.glass.GlassSquareMirror;
import hu.hundevelopers.beesmarter.glass.GlassSquarePrism;
import hu.hundevelopers.beesmarter.glass.GlassTrianglePrism;
import hu.hundevelopers.beesmarter.math.Line;
import hu.hundevelopers.beesmarter.math.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.GestureDetector;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
	public static Game instance;
	
	
	
	public GestureDetectorCompat gestureDetector;
	public int width, height, tilesize, tilenumber = 6, size;
	public List<Glass> glasses;
	public List<Line> laser, claser;
	public List<Line> startLasers;
	
	public int selectedGlass, selectionRange;
	public boolean selectionMode;
	public Bitmap bitmapArrows, bitmapArrows2;
	
	public Game(Context context, AttributeSet attributeSet)
	{
		super(context);
		this.getHolder().addCallback(this);
		this.gestureDetector = new GestureDetectorCompat(context, this);
		this.gestureDetector.setOnDoubleTapListener(this);
		
		this.bitmapArrows = BitmapFactory.decodeResource(this.getResources(), R.drawable.arrows);
		this.bitmapArrows2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.arrows2);
		
		instance = this;
		this.glasses = new ArrayList<Glass>();
		this.laser = new ArrayList<Line>();
		this.claser = new ArrayList<Line>();
		this.startLasers = new ArrayList<Line>();
		this.selectedGlass = -1;
		this.selectionMode = false;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		this.resize(width, height);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		Log.i("surface", "creating...");
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(MainActivity.instance.getFilesDir().getPath()+"/save.dat2"));
			
			this.startLasers.clear();
			int n = Integer.parseInt(br.readLine());
			for(int i = 0; i < n; i++)
			{
				String[] split = br.readLine().split(" ");
				this.startLasers.add(new Line(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
			}
			
			this.glasses.clear();
			n = Integer.parseInt(br.readLine());
			for(int i = 0; i < n; i++)
			{
				String[] split = br.readLine().split(" ");
				try
				{
					this.glasses.add((Glass)MainActivity.instance.getClassLoader().loadClass(split[3]).newInstance());
					this.glasses.get(i).x = Integer.parseInt(split[0]);
					this.glasses.get(i).y = Integer.parseInt(split[1]);
					this.glasses.get(i).rotate(Integer.parseInt(split[2]));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			br.close();
		}
		catch(IOException e)
		{
			
		}
		
		this.resize(this.getWidth(), this.getHeight());
		
		if(this.startLasers.size() == 0)
		{
			//Log.i("game", "adding glasses...");
			this.startLasers.clear();
			this.startLasers.add(new Line(this.size/2, this.size/2, this.size, this.size));
			this.glasses.clear();
			this.glasses.add(new GlassSquareMirror(this.tilesize/2, this.size+this.tilesize/2, 0));
			this.glasses.add(new GlassSquareMirror(this.tilesize*3/2, this.size+this.tilesize/2, 45));
			this.glasses.add(new GlassSquareHalfMirror(this.tilesize*5/2, this.size+this.tilesize/2, 0));
			this.glasses.add(new GlassSquarePrism(this.tilesize*7/2, this.size+this.tilesize/2, 45));
			this.glasses.add(new GlassTrianglePrism(this.tilesize*9/2, this.size+this.tilesize/2, 225));
			this.glasses.add(new GlassTrianglePrism(this.tilesize*11/2,this.size+this.tilesize/2, 315));
		}
		
		this.update();
		this.render();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.i("surface", "destroying...");
		MainActivity.instance.getFilesDir().mkdirs();
		try
		{
			PrintWriter pw = new PrintWriter(new File(MainActivity.instance.getFilesDir().getPath()+"/save.dat2"));
			
			pw.println(this.startLasers.size());
			for(int i = 0; i < this.startLasers.size(); i++)
				pw.println(this.startLasers.get(i).x1+" "+this.startLasers.get(i).y1+" "+this.startLasers.get(i).x2+" "+this.startLasers.get(i).y2);
			
			pw.println(this.glasses.size());
			for(int i = 0; i < this.glasses.size(); i++)
				pw.println(this.glasses.get(i).x+" "+this.glasses.get(i).y+" "+this.glasses.get(i).deg+" "+this.glasses.get(i).getClass().getName());
			
			pw.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.size = Math.min(width, height);
		this.tilesize = this.size/this.tilenumber;
		this.selectionRange = Math.round(this.tilesize*(float)Math.sqrt(2));
	}
	
	public void update()
	{
		this.laser.clear();
		this.claser.clear();
		
		for(int i = 0; i < this.startLasers.size(); i++)
			this.claser.add(this.startLasers.get(i));
		for(int i = 0; i < this.glasses.size(); i++)
			this.glasses.get(i).collide = false;
		while(this.claser.size() > 0)
		{
			int n = this.claser.size();
			for(int i = 0; i < n; i++)
			{
				int min = -1;
				Vertex vmin = null;
				for(int j = 0; j < this.glasses.size(); j++)
				{
					Vertex v = this.glasses.get(j).getLaserInterSectionPoint(this.claser.get(0));
					if(v != null)
					{
						if(1F <= Math.abs(this.claser.get(0).x1-v.x) || 1F <= Math.abs(this.claser.get(0).y1-v.y))
							if((vmin == null || (v.x-this.claser.get(0).x1)*(v.x-this.claser.get(0).x1) + (v.y-this.claser.get(0).y1)*(v.y-this.claser.get(0).y1) < (vmin.x-this.claser.get(0).x1)*(vmin.x-this.claser.get(0).x1) + (vmin.y-this.claser.get(0).y1)*(vmin.y-this.claser.get(0).y1)))
							{
								min = j;
								vmin = v;
							}
					}
				}
				if(min != -1)
				{
					Line nl = new Line(this.claser.get(0).x1, this.claser.get(0).y1, vmin.x, vmin.y);
					if(!this.laser.contains(nl))
					{
						this.laser.add(nl);
						this.glasses.get(min).collide = true;
						this.glasses.get(min).handleLaserCollision(this.claser.get(0));
					}
					this.claser.remove(0);
				}
				else
				{
					this.laser.add(this.claser.get(0));
					this.claser.remove(0);
				}
			}
		}
	}
	
	public void render()
	{
		Canvas canvas = this.getHolder().lockCanvas();
		if(canvas == null)
			return;
		canvas.drawColor(Color.BLACK);
		
		Paint paint = new Paint();
		paint.setTextSize(20);
		paint.setAntiAlias(true);
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(1F);
		for(int i = 0; i < this.laser.size(); i++)
			canvas.drawLine(this.laser.get(i).x1, this.laser.get(i).y1, this.laser.get(i).x2, this.laser.get(i).y2, paint);
		paint.setARGB(255, 0, 255, 0);
		for(int i = 0; i < this.glasses.size(); i++)
		{
			if(this.glasses.get(i).collide)
				canvas.drawText("C", this.glasses.get(i).x-10, this.glasses.get(i).y+10, paint);
			this.glasses.get(i).render(canvas);
		}
		
		if(this.selectedGlass != -1)
		{
			paint.setARGB(128, 255, 255, 255);
			Rect dst = new Rect(this.glasses.get(this.selectedGlass).x-this.selectionRange,
								this.glasses.get(this.selectedGlass).y-this.selectionRange,
								this.glasses.get(this.selectedGlass).x+this.selectionRange,
								this.glasses.get(this.selectedGlass).y+this.selectionRange);
			if(this.selectionMode)
				canvas.drawBitmap(bitmapArrows2, new Rect(0, 0, bitmapArrows2.getWidth(), bitmapArrows2.getHeight()), dst, paint);
			else
				canvas.drawBitmap(bitmapArrows, new Rect(0, 0, bitmapArrows.getWidth(), bitmapArrows.getHeight()), dst, paint);
		}
		
		paint.setARGB(255, 128, 128, 128);
		Rect dst = new Rect(0, this.size, this.tilesize, this.size+this.tilesize);
		canvas.drawRect(dst, paint);
		paint.setARGB(128, 255, 255, 255);
		if(this.selectionMode)
			canvas.drawBitmap(bitmapArrows, new Rect(0, 0, bitmapArrows.getWidth(), bitmapArrows.getHeight()), dst, paint);
		else
			canvas.drawBitmap(bitmapArrows2, new Rect(0, 0, bitmapArrows2.getWidth(), bitmapArrows2.getHeight()), dst, paint);
		
		this.getHolder().unlockCanvasAndPost(canvas);
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
	public boolean onTouchEvent(MotionEvent event)
	{
		if(this.gestureDetector.onTouchEvent(event))
			return true;
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			this.selectedGlass = -1;
			if(new Rect(0, this.size, this.tilesize, this.size+this.tilesize).contains((int)event.getX(), (int)event.getY()))
			{
				this.selectionMode = !this.selectionMode;
				this.render();
				return true;
			}
			for(int i = 0; i < this.glasses.size() && this.selectedGlass == -1; i++)
				if(this.glasses.get(i).isMoveable() && (this.glasses.get(i).x-event.getX())*(this.glasses.get(i).x-event.getX()) + (this.glasses.get(i).y-event.getY())*(this.glasses.get(i).y-event.getY()) <= this.selectionRange*this.selectionRange)
					this.selectedGlass = i;
			this.render();
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			//this.selectedGlass = -1;
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if(this.selectedGlass != -1)
			{
				if(!this.selectionMode)
					this.glasses.get(this.selectedGlass).move((int)event.getX(), (int)event.getY());
				else
				{
					int d = -Math.round((float)Math.toDegrees(Math.atan((event.getY()-this.glasses.get(this.selectedGlass).y)/(event.getX()-this.glasses.get(this.selectedGlass).x))));
					this.glasses.get(this.selectedGlass).rotate(d+(event.getX()-this.glasses.get(this.selectedGlass).x < 0 ? 180 : 0));
				}
				this.update();
				this.render();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e)
	{
		/*if(this.selectedGlass != -1 && (this.glasses.get(this.selectedGlass).x-e.getX())*(this.glasses.get(this.selectedGlass).x-e.getX()) + (this.glasses.get(this.selectedGlass).y-e.getY())*(this.glasses.get(this.selectedGlass).y-e.getY()) <= this.selectionRange*this.selectionRange)
		{
			this.selectionMode = !this.selectionMode;
			this.render();
			return true;
		}*/
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