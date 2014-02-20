package hu.hundevelopers.beesmarter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(new GameSurface(this));
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
		//getMenuInflater().inflate(R.menu.main, menu);
		Game.instance.onMenuPressed();
		return true;
	}
}