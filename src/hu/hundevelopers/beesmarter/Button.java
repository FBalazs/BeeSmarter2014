package hu.hundevelopers.beesmarter;

public class Button
{
	public int x, y, width, height;
	
	public Button(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean isPointInBounds(float x, float y)
	{
		return this.x <= x && x <= this.x+this.width && this.y <= y && y <= this.y+this.height;
	}
}