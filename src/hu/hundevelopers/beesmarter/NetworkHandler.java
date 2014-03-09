package hu.hundevelopers.beesmarter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.util.Log;

public class NetworkHandler
{
	public static NetworkHandler instance;
	public static int PORT = 4444;
	
	
	
	public DatagramSocket socket;
	public InetAddress myAddress, partnerAddress;
	public int myPort, partnerPort;
	public boolean isClient, isWaiting, isReceiving;
	public Thread receive;
	
	public NetworkHandler(boolean client, InetAddress address)
	{
		instance = this;
		this.isClient = client;
		this.isWaiting = false;
		this.partnerAddress = address;
		/*try
		{
			this.myAddress = InetAddress.getLocalHost();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}*/
		
		try
		{
			this.socket = new DatagramSocket(PORT);
		}
		catch(SocketException e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendData()
	{
		Log.i("szõttes", "kûûûldés");
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					String data = Game.instance.getSaveData();
					socket.send(new DatagramPacket(data.getBytes(), data.length(), partnerAddress, PORT));
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				Log.i("szõttes", "kúdve");
			}
		}.start();
		isWaiting = true;
	}
	
	public void receiveData()
	{
		Log.i("szõttes", "várás indíd");
		this.isReceiving = true;
		this.receive = new Thread()
		{
			@Override
			public void run()
			{
				while(isReceiving)
				{
					byte[] data = new byte[5400];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					boolean received = true;
					try
					{
						socket.receive(packet);
					}
					catch(Exception e)
					{
						received = false;
					}
					if(received)
					{
						Log.i("szõttes", "fógadva");
						partnerAddress = packet.getAddress();
						Game.instance.loadSaveData(new String(packet.getData()));
						isWaiting = false;
						Game.instance.update();
						Game.instance.render();
					}
				}
			}
		};
		this.receive.start();
	}
	
	public void destroy()
	{
		this.socket.close();
		this.isReceiving = false;
	}
}
