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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback
{
	public static Game instance;
	
	
	
	public int width, height, tileres, tilenumber = 6, resolution = 600, tilesize, palettePos = 0, paletteGrab;
	public List<Glass> glasses, palette;
	public List<Line> laser, claser;
	public List<Line> startLasers;
	public int size = 600;
	
	public int selectedGlass, selectionRange, grabX, grabY, grabDeg;
	public boolean selectionMode, rotation45 = true, preciseSelection = false;
	public Bitmap bitmapMove, bitmapRotate, bitmapIconDelete, bitmapIconMove, bitmapIconRotate, bitmapIconRotate45;
	public Rect btnChange1, btnChange2, btnChange3, btnDelete, paletteRect;
	
	public Game(Context context, AttributeSet attributeSet)
	{
		super(context);
		this.getHolder().addCallback(this);
		
		this.bitmapMove = BitmapFactory.decodeResource(this.getResources(), R.drawable.move);
		this.bitmapRotate = BitmapFactory.decodeResource(this.getResources(), R.drawable.rotate);
		this.bitmapIconDelete = BitmapFactory.decodeResource(this.getResources(), R.drawable.delete);
		this.bitmapIconMove = BitmapFactory.decodeResource(this.getResources(), R.drawable.move_icon);
		this.bitmapIconRotate = BitmapFactory.decodeResource(this.getResources(), R.drawable.rotate_icon);
		this.bitmapIconRotate45 = BitmapFactory.decodeResource(this.getResources(), R.drawable.rotate2_icon);
		
		instance = this;
		this.glasses = new ArrayList<Glass>();
		this.laser = new ArrayList<Line>();
		this.claser = new ArrayList<Line>();
		this.startLasers = new ArrayList<Line>();
		this.palette = new ArrayList<Glass>();
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
		this.resize(this.getWidth(), this.getHeight());
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(MainActivity.instance.getFilesDir().getPath()+"/save.dat"));
			
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
					this.glasses.get(i).id = i;
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
		catch(Exception e)
		{
			
		}
		
		if(this.startLasers.size() == 0)
		{
			this.startLasers.clear();
			this.startLasers.add(new Line(this.resolution/2, this.resolution/2, this.resolution, this.resolution));
			this.glasses.clear();
			this.glasses.add(new GlassSquareMirror(0, this.tileres/2, this.resolution+this.tileres/2, 0));
			this.glasses.add(new GlassSquareMirror(1, this.tileres*3/2, this.resolution+this.tileres/2, 45));
			this.glasses.add(new GlassSquareHalfMirror(2, this.tileres*5/2, this.resolution+this.tileres/2, 0));
			this.glasses.add(new GlassSquarePrism(3, this.tileres*7/2, this.resolution+this.tileres/2, 45));
			this.glasses.add(new GlassTrianglePrism(4, this.tileres*9/2, this.resolution+this.tileres/2, 225));
			this.glasses.add(new GlassTrianglePrism(5, this.tileres*11/2,this.resolution+this.tileres/2, 315));
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
			PrintWriter pw = new PrintWriter(new File(MainActivity.instance.getFilesDir().getPath()+"/save.dat"));
			
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
		this.tileres = this.resolution/this.tilenumber;
		this.selectionRange = Math.round(this.tileres*(float)Math.sqrt(2));
		if(this.width < this.height)
		{
			this.btnChange1 = new Rect(0, this.height-this.tilesize, this.tilesize, this.height);
			this.btnChange2 = new Rect(this.tilesize, this.height-this.tilesize, 2*this.tilesize, this.height);
			this.btnChange3 = new Rect(2*this.tilesize, this.height-this.tilesize, 3*this.tilesize, this.height);
			this.btnDelete = new Rect(this.width-this.tilesize, this.height-this.tilesize, this.width, this.height);
		}
		else
		{
			this.btnChange1 = new Rect(this.width-this.tilesize, 0, this.width, this.tilesize);
			this.btnChange2 = new Rect(this.width-this.tilesize, this.tilesize, this.width, 2*this.tilesize);
			this.btnChange3 = new Rect(this.width-this.tilesize, 2*this.tilesize, this.width, 3*this.tilesize);
			this.btnDelete = new Rect(this.width-this.tilesize, this.height-this.tilesize, this.width, this.height);
		}
	}
	
	public void update()
	{
		this.laser.clear();
		this.claser.clear();
		
		for(int i = 0; i < this.startLasers.size(); i++)
			this.claser.add(this.startLasers.get(i));
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
			canvas.drawLine(this.laser.get(i).x1*this.size/this.resolution, this.laser.get(i).y1*this.size/this.resolution, this.laser.get(i).x2*this.size/this.resolution, this.laser.get(i).y2*this.size/this.resolution, paint);
		paint.setARGB(255, 0, 255, 0);
		for(int i = 0; i < this.glasses.size(); i++)
			this.glasses.get(i).render(canvas);
		
		paint.setARGB(255, 64, 64, 64);
		if(this.width < this.height)
			canvas.drawRect(new Rect(0, this.size, this.width, this.height), paint);
		else
			canvas.drawRect(new Rect(this.size, 0, this.width, this.height), paint);
		
		if(this.selectedGlass != -1)
		{
			paint.setARGB(128, 255, 255, 255);
			Rect dst = new Rect(this.glasses.get(this.selectedGlass).x*this.size/this.resolution-this.selectionRange*this.size/this.resolution,
								this.glasses.get(this.selectedGlass).y*this.size/this.resolution-this.selectionRange*this.size/this.resolution,
								this.glasses.get(this.selectedGlass).x*this.size/this.resolution+this.selectionRange*this.size/this.resolution,
								this.glasses.get(this.selectedGlass).y*this.size/this.resolution+this.selectionRange*this.size/this.resolution);
			if(this.selectionMode)
				canvas.drawBitmap(bitmapRotate, new Rect(0, 0, bitmapRotate.getWidth(), bitmapRotate.getHeight()), dst, paint);
			else
				canvas.drawBitmap(bitmapMove, new Rect(0, 0, bitmapMove.getWidth(), bitmapMove.getHeight()), dst, paint);
		}
		
		paint.setARGB(128, 255, 255, 255);
		canvas.drawBitmap(bitmapIconMove, new Rect(0, 0, bitmapIconMove.getWidth(), bitmapIconMove.getHeight()), this.btnChange1, paint);
		canvas.drawBitmap(bitmapIconRotate, new Rect(0, 0, bitmapIconRotate.getWidth(), bitmapIconRotate.getHeight()), this.btnChange2, paint);
		canvas.drawBitmap(bitmapIconRotate45, new Rect(0, 0, bitmapIconRotate45.getWidth(), bitmapIconRotate45.getHeight()), this.btnChange3, paint);
		canvas.drawBitmap(bitmapIconDelete, new Rect(0, 0, bitmapIconDelete.getWidth(), bitmapIconDelete.getHeight()), this.btnDelete, paint);
		
		paint.setARGB(128, 255, 255, 255);
		paint.setTextSize(5);
		canvas.drawText("The icons license can be found at: http://creativecommons.org/licenses/by-nc/3.0/nl/deed.en_GB", 10, 10, paint);
		
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
		float x = event.getX()*this.resolution/this.size;
		float y = event.getY()*this.resolution/this.size;
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(this.selectedGlass != -1)
			{
				if(this.btnDelete.contains((int)event.getX(), (int)event.getY()))
				{
					this.glasses.remove(this.selectedGlass);
					this.selectedGlass = -1;
					this.update();
					this.render();
					return true;
				}
			}
			
			this.selectedGlass = -1;
			
			if(this.btnChange1.contains((int)event.getX(), (int)event.getY()))
			{
				this.selectionMode = false;
				this.render();
				return true;
			}
			if(this.btnChange2.contains((int)event.getX(), (int)event.getY()))
			{
				this.selectionMode = true;
				this.rotation45 = false;
				this.render();
				return true;
			}
			if(this.btnChange3.contains((int)event.getX(), (int)event.getY()))
			{
				this.selectionMode = true;
				this.rotation45 = true;
				this.render();
				return true;
			}
			
			if(this.preciseSelection)
			{
				for(int i = 0; i < this.glasses.size() && this.selectedGlass == -1; i++)
					if(this.glasses.get(i).isMoveable() && this.glasses.get(i).isVertexInBounds(x, y))
						this.selectedGlass = i;
			}
			else
			{
				for(int i = 0; i < this.glasses.size() && this.selectedGlass == -1; i++)
					if(this.glasses.get(i).isMoveable() && (this.glasses.get(i).x-x)*(this.glasses.get(i).x-x) + (this.glasses.get(i).y-y)*(this.glasses.get(i).y-y) <= this.selectionRange*this.selectionRange)
						this.selectedGlass = i;
			}
			if(this.selectedGlass != -1)
			{
				this.grabX = (int)(x-this.glasses.get(this.selectedGlass).x);
				this.grabY = (int)(y-this.glasses.get(this.selectedGlass).y);
				this.grabDeg = -this.glasses.get(this.selectedGlass).deg-Math.round((float)Math.toDegrees(Math.atan((y-this.glasses.get(this.selectedGlass).y)/(x-this.glasses.get(this.selectedGlass).x))))+(x-this.glasses.get(this.selectedGlass).x < 0 ? 180 : 0);
			}
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
				{
					this.glasses.get(this.selectedGlass).move((int)x-this.grabX, (int)y-this.grabY);
				}
				else
				{
					int d = -Math.round((float)Math.toDegrees(Math.atan((y-this.glasses.get(this.selectedGlass).y)/(x-this.glasses.get(this.selectedGlass).x))))+(x-this.glasses.get(this.selectedGlass).x < 0 ? 180 : 0);
					if(this.rotation45)
						this.glasses.get(this.selectedGlass).rotate((d-this.grabDeg)/45*45);
					else
						this.glasses.get(this.selectedGlass).rotate(d-this.grabDeg);
				}
				this.update();
				this.render();
				return true;
			}
		}
		return false;
	}
}