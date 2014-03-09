package hu.hundevelopers.beesmarter;

import hu.hundevelopers.beesmarter.glass.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
//import java.net.InetAddress;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.EditText;

public class Editor extends Game
{
	public Editor(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
	}
	
	public void generateMap()
	{
		this.glasses.clear();
		Random rand = new Random(System.currentTimeMillis());
		int x1 = tileres/2+rand.nextInt(resolution/3);
		int y1 = tileres/2+rand.nextInt(resolution/3);
		this.glasses.add(new GlassSquareEmitter1(0, x1, y1, 180));
		this.glasses.add(new GlassSquareEmitter2(1, resolution-x1, resolution-y1, 0));
		this.glasses.add(new GlassTarget(2, resolution/2, resolution/2, 0));
		this.glasses.add(new GlassBomb(3, tileres/2, resolution/2, 0));
		this.glasses.add(new GlassSquareSolid(4, resolution-tileres/2, resolution/2, 0));
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		this.resize(width, height);
	}
	
	public static String glassPackage = "hu.hundevelopers.beesmarter.glass.";
	
	public void saveData()
	{
		MainActivity.instance.getFilesDir().mkdirs();
		try
		{
			PrintWriter pw = new PrintWriter(new File(MainActivity.instance.getFilesDir().getPath()+"/"+saveFile+".map"));
			pw.print(this.getSaveData());
			pw.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadData()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.instance);
		builder.setTitle("Load");
		builder.setMessage("Please type the map's name!");
		final EditText input = new EditText(MainActivity.instance);
		builder.setView(input);
		builder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				saveFile = input.getText().toString();
				try
				{
					BufferedReader br = new BufferedReader(new FileReader(MainActivity.instance.getFilesDir().getPath()+"/"+saveFile+".map"));
					
					StringBuilder str = new StringBuilder();
					String cline = br.readLine();
					while(cline != null)
					{
						str.append(cline+"\n");
						cline = br.readLine();
					}
					br.close();
					loadSaveData(new String(str));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				update();
				render();
			}
		});
		builder.create().show();
	}
	
	public String getSaveData()
	{
		StringBuilder str = new StringBuilder();
		str.append(this.glasses.size()+"\n");
		for(int i = 0; i < this.glasses.size(); i++)
			str.append(this.glasses.get(i).x+" "+this.glasses.get(i).y+" "+this.glasses.get(i).deg+" "+this.glasses.get(i).getClass().getName().substring(glassPackage.length())+"\n");
		return new String(str);
	}
	
	public void loadSaveData(String str)
	{
		this.glasses.clear();
		String[] split = str.split("\n");
		int n = Integer.parseInt(split[0]);
		for(int i = 0; i < n; i++)
		{
			String[] split2 = split[i+1].split(" ");
			try
			{
				this.glasses.add((Glass)MainActivity.instance.getClassLoader().loadClass(glassPackage+split2[3]).newInstance());
				this.glasses.get(i).id = i;
				this.glasses.get(i).x = Integer.parseInt(split2[0]);
				this.glasses.get(i).y = Integer.parseInt(split2[1]);
				this.glasses.get(i).rotate(Integer.parseInt(split2[2]));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		Log.i("surface", "creating...");
		this.resize(this.getWidth(), this.getHeight());
		this.loadData();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.i("surface", "destroying...");
		this.saveData();
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
			this.btnNext = new Rect(this.width-3*this.tilesize, this.height-this.tilesize, this.width-2*this.tilesize, this.height);
			this.btnInfo = new Rect(this.width-2*this.tilesize, this.height-this.tilesize, this.width-this.tilesize, this.height);
			this.btnDelete = new Rect(this.width-this.tilesize, this.height-this.tilesize, this.width, this.height);
			this.paletteRect = new Rect(2, this.size+2, this.width-2, this.size+this.tilesize*3/2+4);
		}
		else
		{
			this.btnChange1 = new Rect(this.width-this.tilesize, 0, this.width, this.tilesize);
			this.btnChange2 = new Rect(this.width-this.tilesize, this.tilesize, this.width, 2*this.tilesize);
			this.btnChange3 = new Rect(this.width-this.tilesize, 2*this.tilesize, this.width, 3*this.tilesize);
			this.btnNext = new Rect(this.width-this.tilesize, this.height-3*this.tilesize, this.width, this.height-2*this.tilesize);
			this.btnInfo = new Rect(this.width-this.tilesize, this.height-2*this.tilesize, this.width, this.height-this.tilesize);
			this.btnDelete = new Rect(this.width-this.tilesize, this.height-this.tilesize, this.width, this.height);
			this.paletteRect = new Rect(this.size+2, 2, this.size+this.tilesize*3/2+4, this.height-2);
		}
	}
	
	public void update()
	{
		
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
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(1F);
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
		
		paint.setARGB(128, 0, 0, 0);
		canvas.drawRect(this.paletteRect, paint);
		if(this.width < this.height)
		{
			int left = this.paletteRect.left*Editor.instance.resolution/Editor.instance.size;
			int width = this.paletteRect.width()*Editor.instance.resolution/Editor.instance.size;
			int center = this.paletteRect.centerY()*Editor.instance.resolution/Editor.instance.size;
			if(this.paletteSelection != -1)
			{
				paint.setARGB(128, 255, 255, 255);
				canvas.drawRect(new Rect(this.paletteRect.left+this.paletteRect.width()*this.paletteSelection/4, this.paletteRect.top, this.paletteRect.left+this.paletteRect.width()*(this.paletteSelection+1)/4, this.paletteRect.bottom), paint);
			}
			new GlassSquareEmitter1(-1, left+width*1/8, center, 0).render(canvas);
			new GlassSquareEmitter2(-1, left+width*3/8, center, 0).render(canvas);
			new GlassTarget(-1, left+width*5/8, center, 0).render(canvas);
			new GlassBomb(-1, left+width*7/8, center, 0).render(canvas);
		}
		else
		{
			int top = this.paletteRect.top*Editor.instance.resolution/Editor.instance.size;
			int height = this.paletteRect.height()*Editor.instance.resolution/Editor.instance.size;
			int center = this.paletteRect.centerX()*Editor.instance.resolution/Editor.instance.size;
			if(this.paletteSelection != -1)
			{
				paint.setARGB(128, 255, 255, 255);
				canvas.drawRect(new Rect(this.paletteRect.left, this.paletteRect.top+this.paletteRect.height()*this.paletteSelection/4, this.paletteRect.right, this.paletteRect.top+this.paletteRect.height()*(this.paletteSelection+1)/4), paint);
			}
			new GlassSquareEmitter1(-1, center, top+height*1/8, 0).render(canvas);
			new GlassSquareEmitter2(-1, center, top+height*3/8, 0).render(canvas);
			new GlassTarget(-1, center, top+height*5/8, 0).render(canvas);
			new GlassBomb(-1, center, top+height*7/8, 0).render(canvas);
		}
		
		paint.setARGB(128, 255, 255, 255);
		canvas.drawBitmap(bitmapIconMove, new Rect(0, 0, bitmapIconMove.getWidth(), bitmapIconMove.getHeight()), this.btnChange1, paint);
		canvas.drawBitmap(bitmapIconRotate, new Rect(0, 0, bitmapIconRotate.getWidth(), bitmapIconRotate.getHeight()), this.btnChange2, paint);
		canvas.drawBitmap(bitmapIconRotate45, new Rect(0, 0, bitmapIconRotate45.getWidth(), bitmapIconRotate45.getHeight()), this.btnChange3, paint);
		canvas.drawBitmap(bitmapIconDelete, new Rect(0, 0, bitmapIconDelete.getWidth(), bitmapIconDelete.getHeight()), this.btnDelete, paint);
		canvas.drawBitmap(bitmapIconInfo, new Rect(0, 0, bitmapIconInfo.getWidth(), bitmapIconInfo.getHeight()), this.btnInfo, paint);
		
		this.getHolder().unlockCanvasAndPost(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(this.gestureDetector.onTouchEvent(event))
			return true;
		
		float x = event.getX()*this.resolution/this.size;
		float y = event.getY()*this.resolution/this.size;
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(this.paletteRect.contains((int)event.getX(), (int)event.getY())) // elem kiválasztása
			{
				if(this.width < this.height)
					this.paletteSelection = (int)event.getX()*4/this.width;
				else
					this.paletteSelection = (int)event.getY()*4/this.height;
				this.render();
				return true;
			}
			else if((int)event.getX() < this.size && (int)event.getY() < size && this.paletteSelection != -1) // megpróbálja elhelyezni a kiválasztott elemet
			{
				Glass g = null;
				int id = (this.glasses.size() == 0 ? 0 : this.glasses.get(this.glasses.size()-1).id+1);
				switch(this.paletteSelection)
				{
					case 0:
						g = new GlassSquareEmitter1(id, (int)x, (int)y, 0);
					break;
					case 1:
						g = new GlassSquareEmitter2(id, (int)x, (int)y, 0);
					break;
					case 2:
						g = new GlassTarget(id, (int)x, (int)y, 0);
					break;
					case 3:
						g = new GlassBomb(id, (int)x, (int)y, 0);
					break;
				}
				if(g != null)
				{
					g.checkOutside();
					if(g.getCollidingGlassIndex() == -1)
					{
						this.paletteSelection = -1;
						this.selectedGlass = this.glasses.size();
						Editor.instance.glasses.add(g);
						this.update();
						this.render();
						return true;
					}
				}
			}
			this.paletteSelection = -1;
			
			if(this.selectedGlass != -1)
			{
				if(this.btnDelete.contains((int)event.getX(), (int)event.getY())) // törlõ gomb megnyomva
				{
					this.glasses.remove(this.selectedGlass);
					this.selectedGlass = -1;
					this.update();
					this.render();
					return true;
				}
			}
			
			if(this.btnNext.contains((int)event.getX(), (int)event.getY()))
			{
				this.saveData();
				this.selectedGlass = -1;
				this.render();
				return true;
			}
			
			this.selectedGlass = -1;
			
			if(this.preciseSelection)
			{
				for(int i = 0; i < this.glasses.size() && this.selectedGlass == -1; i++)
					if(this.glasses.get(i).isVertexInBounds(x, y))
						this.selectedGlass = i;
			}
			else
			{
				for(int i = 0; i < this.glasses.size() && this.selectedGlass == -1; i++)
					if((this.glasses.get(i).x-x)*(this.glasses.get(i).x-x) + (this.glasses.get(i).y-y)*(this.glasses.get(i).y-y) <= this.selectionRange*this.selectionRange)
						this.selectedGlass = i;
			}
			
			if(this.selectedGlass != -1)
			{
				this.grabX = (int)(x-this.glasses.get(this.selectedGlass).x);
				this.grabY = (int)(y-this.glasses.get(this.selectedGlass).y);
				this.grabDeg = -this.glasses.get(this.selectedGlass).deg-Math.round((float)Math.toDegrees(Math.atan((y-this.glasses.get(this.selectedGlass).y)/(x-this.glasses.get(this.selectedGlass).x))))+(x-this.glasses.get(this.selectedGlass).x < 0 ? 180 : 0);
			}
			
			if(this.btnInfo.contains((int)event.getX(), (int)event.getY())) // infó gomb megnyomva
			{
				this.selectedGlass = -1;
				// Linkify the message
			    final SpannableString s = new SpannableString(
			    		"This app was made for the BeeSmarter2014 competition by the HunDevelopers team.\n"
						+"\nThe icons are from https://www.iconfinder.com/search/?q=iconset:iconic-1 and are under the licence of http://creativecommons.org/licenses/by-nc/3.0/nl/deed.en_GB" +
						"\nThe icons have been modified!");
			    Linkify.addLinks(s, Linkify.ALL);

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.instance);
				builder.setTitle("About the game");
				builder.setMessage(s);
				builder.setIcon(R.drawable.logo2);
				builder.setNeutralButton("Return", null);
				builder.create().show();
				return true;
			}
			
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
			
			this.render();
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			//this.selectedGlass = -1;
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE) // mozgatás
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

	@Override
	public boolean onDoubleTap(MotionEvent event){return false;}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event){return false;}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event){return false;}

	@Override
	public boolean onDown(MotionEvent event){return false;}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float f1, float f2){return false;}

	@Override
	public void onLongPress(MotionEvent event)
	{
		if(this.btnDelete.contains((int)event.getX(), (int)event.getY()))
		{
			for(int i = 0; i < this.glasses.size(); i++)
				this.glasses.remove(i--);
			this.selectedGlass = -1;
			this.update();
			this.render();
		}
	}

	@Override
	public boolean onScroll(MotionEvent event1, MotionEvent event2, float f1, float f2){return false;}

	@Override
	public void onShowPress(MotionEvent event){}

	@Override
	public boolean onSingleTapUp(MotionEvent event){return false;}
}