import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DrawPanel extends JPanel {
	private final Map<Integer, Integer> snakes;
	private final Map<Integer, Integer> ladders;

	public DrawPanel(Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
		this.snakes = snakes;
		this.ladders = ladders;
		setOpaque(false);
		setBounds(0, 0, 600, 600);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.RED);
		for (Map.Entry<Integer, Integer> s : snakes.entrySet()) {
			drawArrow(g, getCellCenter(s.getKey()), getCellCenter(s.getValue()));
		}

		g.setColor(Color.GREEN.darker());
		for (Map.Entry<Integer, Integer> l : ladders.entrySet()) {
			drawArrow(g, getCellCenter(l.getKey()), getCellCenter(l.getValue()));
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

	private void drawArrow(Graphics g, Point from, Point to) {
		g.drawLine(from.x, from.y, to.x, to.y);
		int dx = to.x - from.x, dy = to.y - from.y;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - 10, xn = xm, ym = 5, yn = -5, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + from.x;
		ym = xm * sin + ym * cos + from.y;
		xm = x;

		x = xn * cos - yn * sin + from.x;
		yn = xn * sin + yn * cos + from.y;
		xn = x;

		int[] xpoints = { to.x, (int) xm, (int) xn };
		int[] ypoints = { to.y, (int) ym, (int) yn };
		g.fillPolygon(xpoints, ypoints, 3);
	}
}
