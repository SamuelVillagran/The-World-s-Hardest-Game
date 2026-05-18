package domain;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.List;


public class Figure {

	private List<Point> points;
	private Polygon polygon;
	
	/**
	 * Constructor of Polygon
	 * @param vertices vertices are the vertices that make form to this polygon
	 */
	public Figure(List<Point> vertices) {
		points = vertices;
		polygon = new Polygon();
		for (Point p : vertices) {
			polygon.addPoint((int) p.getX(), (int) p.getY());
		}
	}
	
	/**
	 * This method verify if the point given is at this figure that forms with the points
	 * @param point point that going to verify if this is inside of this figure
	 * @return true if point is inside of the figure, false if is out of this figure
	 */
	public boolean contains(int x, int y) {
		return polygon.contains(x, y);
	}
	
	public int getXCenter() {
		Rectangle rec = polygon.getBounds();
		return  rec.x + rec.width / 2;
	}
	
	public int getYCenter() {
		Rectangle rec = polygon.getBounds();
		return  rec.y + rec.height / 2;
	}
}
