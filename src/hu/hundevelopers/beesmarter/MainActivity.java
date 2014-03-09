package hu.hundevelopers.beesmarter;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity
{
	public static MainActivity instance;
	
	
	
	public boolean editor;
	
	public MainActivity()
	{
		instance = this;
	}
	
	public void initClient()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Server IP");
		builder.setMessage("Please type the host IP in");
		final EditText input = new EditText(this);
		builder.setView(input);
		builder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				final String s = input.getText().toString();
				new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							new NetworkHandler(true, InetAddress.getByName(s.equals("") ? "localhost" : s));
							NetworkHandler.instance.receiveData();
						}
						catch(UnknownHostException e)
						{
							e.printStackTrace();
						}
					}
				}.start();
				setContentView(R.layout.activity_main);
			}
		});
		builder.create().show();
	}
	
	public void initServer()
	{
		new NetworkHandler(false, null);
		setContentView(R.layout.activity_main);
		NetworkHandler.instance.receiveData();
	}
	
	public void initEditor()
	{
		this.editor = true;
		setContentView(R.layout.activity_editor);
	}
	
	public void initMenu()
	{
		this.setContentView(R.layout.activity_load);
		Button btnClient = (Button)findViewById(R.id.button1);
		btnClient.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initClient();
			}
		});
		Button btnServer = (Button)findViewById(R.id.button2);
		btnServer.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initServer();
			}
		});
		Button btnEditor = (Button)findViewById(R.id.button3);
		btnEditor.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				initEditor();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initMenu();
	}
	
	@Override
	public void onBackPressed()
	{
		if(Game.instance == null)
			super.onBackPressed();
		else
		{
			if(NetworkHandler.instance != null)
				NetworkHandler.instance.destroy();
			Game.instance = null;
			initMenu();
		}
	}
}