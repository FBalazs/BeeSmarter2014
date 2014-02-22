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
		return new Vertex(x, y);
	}
	
	public static boolean isIntersectionPointOnSegment(Line l, Vertex v)
	{
		return (Math.round(v.x) >= Math.round(Math.min(l.x1, l.x2))-1
				&& Math.round(v.x) <= Math.round(Math.max(l.x1, l.x2))+1
				&& Math.round(v.y) >= Math.round(Math.min(l.y1, l.y2))-1
				&& Math.round(v.y) <= Math.round(Math.max(l.y1, l.y2))+1);
	}
	
	public static float getLineAndVertexSquaredDistance(Line l, Vertex v)
	{
		Line m = new Line(v.x, v.y, v.x+(l.y1-l.y2), v.y+(l.x2-l.x1));
		Vertex c = getLineIntersection(l, m);
		if(isIntersectionPointOnSegment(l, c))
			return (v.x-c.x)*(v.x-c.x) + (v.y-c.y)*(v.y-c.y);
		else
			return Math.min((v.x-l.x1)*(v.x-l.x1) + (v.y-l.y1)*(v.y-l.y1), (v.x-l.x2)*(v.x-l.x2) + (v.y-l.y2)*(v.y-l.y2));
	}
}