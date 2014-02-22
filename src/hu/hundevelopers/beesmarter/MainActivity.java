package hu.hundevelopers.beesmarter;

import hu.hundevelopers.beesmarter.glass.Glass;
import hu.hundevelopers.beesmarter.glass.GlassSquareHalfMirror;
import hu.hundevelopers.beesmarter.glass.GlassSquareMirror;
import hu.hundevelopers.beesmarter.glass.GlassSquarePrism;
import hu.hundevelopers.beesmarter.glass.GlassTrianglePrism;
import hu.hundevelopers.beesmarter.math.Line;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity
{
	public static MainActivity instance;
	
	
	
	public MainActivity()
	{
		instance = this;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.activity_main);
	}
	
	/*@Override
	public void onResume()
	{
		super.onResume();
		Log.i("activity", "resuming...");
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(this.getFilesDir().getPath()+"/save.dat2"));
			
			Game.instance.startLasers.clear();
			int n = Integer.parseInt(br.readLine());
			for(int i = 0; i < n; i++)
			{
				String[] split = br.readLine().split(" ");
				Game.instance.startLasers.add(new Line(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
			}
			
			Game.instance.glasses.clear();
			n = Integer.parseInt(br.readLine());
			for(int i = 0; i < n; i++)
			{
				String[] split = br.readLine().split(" ");
				try
				{
					Game.instance.glasses.add((Glass)this.getClassLoader().loadClass(split[3]).newInstance());
					Game.instance.glasses.get(i).x = Integer.parseInt(split[0]);
					Game.instance.glasses.get(i).y = Integer.parseInt(split[1]);
					Game.instance.glasses.get(i).rotate(Integer.parseInt(split[2]));
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
		Game.instance.onLoad();
	}*/
	
	/*@Override
	public void onPause()
	{
		super.onPause();
		Log.i("activity", "pausing...");
		this.getFilesDir().mkdirs();
		try
		{
			PrintWriter pw = new PrintWriter(new File(this.getFilesDir().getPath()+"/save.dat2"));
			
			pw.println(Game.instance.startLasers.size());
			for(int i = 0; i < Game.instance.startLasers.size(); i++)
				pw.println(Game.instance.startLasers.get(i).x1+" "+Game.instance.startLasers.get(i).y1+" "+Game.instance.startLasers.get(i).x2+" "+Game.instance.startLasers.get(i).y2);
			
			pw.println(Game.instance.glasses.size());
			for(int i = 0; i < Game.instance.glasses.size(); i++)
				pw.println(Game.instance.glasses.get(i).x+" "+Game.instance.glasses.get(i).y+" "+Game.instance.glasses.get(i).deg+" "+Game.instance.glasses.get(i).getClass().getName());
			
			pw.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}*/
	
	@Override
	public void onBackPressed()
	{
		if(!Game.instance.onBackPressed())
			super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//getMenuInflater().inflate(R.menu.main, menu);
		Game.instance.onMenuPressed();
		return true;
	}
}