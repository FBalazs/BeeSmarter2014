package hu.hundevelopers.beesmarter.math;

public class MathHelper
{
	private static float[] sinTable;
	
	static
	{
		sinTable = new float[360];
		for(int i = 0; i < 360; i++)
			sinTable[i] = (float)Math.sin(Math.toRadians(i));
	}
	
	public static float sin(int deg)
	{
		while(deg >= 360)
			deg -= 360;
		while(deg < 0)
			deg += 360;
		return sinTable[deg];
	}
	
	public static float cos(int deg)
	{
		return sin(deg-90);
	}
	
	public static Vertex getLineIntersection(Line l1, Line l2)
	{
		if(l1.nx/l1.ny == l2.nx/l2.ny)
			return null;
		float x, y;
		y = (l2.nx*(l1.nx*l1.x+l1.ny*l1.y) - l1.nx*(l2.nx*l2.x+l2.ny*l2.y))/(l2.nx*l1.ny-l2.ny*l1.nx);
		x = (l1.nx*l1.x+l1.ny*l1.y-l1.ny*y)/l1.nx;
		//y = (n1*((y2-y1)*x1+(x1-x2)*y1) - (y2-y1)*(n1*p+n2*q))/(n1*(x1-x2)-n2*(y2-y1));
		return new Vertex(x, y);
	}
}