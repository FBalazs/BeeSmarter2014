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
		float x, y;
		x = ((l1.x1*l1.y2-l1.y1*l1.x2)*(l2.x1-l2.x2)-(l1.x1-l1.x2)*(l2.x1*l2.y2-l2.y1*l2.x2))/((l1.x1-l1.x2)*(l2.y1-l2.y2)-(l1.y1-l1.y2)*(l2.x1-l2.x2));
		y = ((l1.x1*l1.y2-l1.y1*l1.x2)*(l2.y1-l2.y2)-(l1.y1-l1.y2)*(l2.x1*l2.y2-l2.y1*l2.x2))/((l1.x1-l1.x2)*(l2.y1-l2.y2)-(l1.y1-l1.y2)*(l2.x1-l2.x2));
		if(Float.isNaN(x) || Float.isNaN(y))
			return null;
		/*if((int)x < (int)Math.min(l1.x1, l1.x2)
			|| (int)x > (int)Math.max(l1.x1, l1.x2)
			|| (int)x < (int)Math.min(l2.x1, l2.x2)
			|| (int)x > (int)Math.max(l2.x1, l2.x2)
			|| (int)y < (int)Math.min(l1.y1, l1.y2)
			|| (int)y > (int)Math.max(l1.y1, l1.y2)
			|| (int)y < (int)Math.min(l2.y1, l2.y2)
			|| (int)y > (int)Math.max(l2.y1, l2.y2))
			return null;*/
		return new Vertex(x, y);
	}
}