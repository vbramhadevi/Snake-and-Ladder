import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.Map;

public class BoardRenderer extends JPanel {
	private final IBoardModel boardModel;

	public BoardRenderer(IBoardModel boardModel) {
		this.boardModel = boardModel;
		setOpaque(false);
		setBounds(0, 0, 600, 600);
	}

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);

	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    g2d.setColor(Color.RED);
	    g2d.setStroke(new BasicStroke(3));
	    for (Map.Entry<Integer, Integer> s : boardModel.getSnakes().entrySet()) {
	        drawSnake(g2d, getCellCenter(s.getKey()), getCellCenter(s.getValue()));
	    }

	    g2d.setColor(Color.GREEN.darker());
	    g2d.setStroke(new BasicStroke(3));
	    for (Map.Entry<Integer, Integer> l : boardModel.getLadders().entrySet()) {
	        drawLadder(g2d, getCellCenter(l.getKey()), getCellCenter(l.getValue()));
	    }
	}

	private Point getCellCenter(int cellNum) {
		int row = (100 - cellNum) / 10;
		int col = (100 - cellNum) % 10;
		if (row % 2 == 1)
			col = 9 - col;

		int x = col * 60 + 30;
		int y = row * 60 + 30;
		return new Point(x, y);
	}

//	private void drawArrow(Graphics g, Point from, Point to) {
//		g.drawLine(from.x, from.y, to.x, to.y);
//		int dx = to.x - from.x, dy = to.y - from.y;
//		double D = Math.sqrt(dx * dx + dy * dy);
//		double xm = D - 10, xn = xm, ym = 5, yn = -5, x;
//		double sin = dy / D, cos = dx / D;
//
//		x = xm * cos - ym * sin + from.x;
//		ym = xm * sin + ym * cos + from.y;
//		xm = x;
//
//		x = xn * cos - yn * sin + from.x;
//		yn = xn * sin + yn * cos + from.y;
//		xn = x;
//
//		int[] xpoints = { to.x, (int) xm, (int) xn };
//		int[] ypoints = { to.y, (int) ym, (int) yn };
//		g.fillPolygon(xpoints, ypoints, 3);
//	}
	
	
	private void drawSnake(Graphics2D g2d, Point from, Point to) {
	    int segments = 10;
	    double dx = (to.x - from.x) / (double) segments;
	    double dy = (to.y - from.y) / (double) segments;
	    int amplitude = 10; // Controls the wave height

	    GeneralPath path = new GeneralPath();
	    path.moveTo(from.x, from.y);

	    for (int i = 1; i <= segments; i++) {
	        double t = i / (double) segments;
	        int x = (int) (from.x + dx * i);
	        int y = (int) (from.y + dy * i + amplitude * Math.sin(t * Math.PI * 4));
	        path.lineTo(x, y);
	    }

	    g2d.draw(path);
	}

	private void drawLadder(Graphics2D g2d, Point from, Point to) {
	    // two parallel lines
		// Distance between parallel lines
	    int offset = 5; 
	    double dx = to.x - from.x;
	    double dy = to.y - from.y;
	    double length = Math.sqrt(dx * dx + dy * dy);
	    double cos = dx / length;
	    double sin = dy / length;

	    // perpendicular offsets for parallel lines
	    double perpX = -sin * offset;
	    double perpY = cos * offset;

	    // parallel lines
	    g2d.drawLine(from.x + (int) perpX, from.y + (int) perpY, to.x + (int) perpX, to.y + (int) perpY);
	    g2d.drawLine(from.x - (int) perpX, from.y - (int) perpY, to.x - (int) perpX, to.y - (int) perpY);

	    // runs
	    int numRuns = 5;
	    for (int i = 1; i < numRuns; i++) {
	        double t = i / (double) numRuns;
	        int x1 = (int) (from.x + t * dx + perpX);
	        int y1 = (int) (from.y + t * dy + perpY);
	        int x2 = (int) (from.x + t * dx - perpX);
	        int y2 = (int) (from.y + t * dy - perpY);
	        g2d.drawLine(x1, y1, x2, y2);
	    }
	}
}