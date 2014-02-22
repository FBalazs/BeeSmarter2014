package hu.hundevelopers.beesmarter;

import android.os.Bundle;
import android.app.Activity;
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
	
	@Override
	public void onBackPressed()
	{
		if(!Game.instance.onBackPressed())
			super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//this.getMenuInflater().inflate(R.menu.main, menu);
		Game.instance.onMenuPressed();
		return true;
	}
}