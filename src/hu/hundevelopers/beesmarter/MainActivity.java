package hu.hundevelopers.beesmarter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity
{
	public MainSurface surface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.surface = new MainSurface(this);
		setContentView(this.surface);
	}
	
	@Override
	public void onBackPressed()
	{
		if(!this.surface.thread.onBackPressed())
			super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//getMenuInflater().inflate(R.menu.main, menu);
		this.surface.thread.onMenuPressed();
		return true;
	}
}