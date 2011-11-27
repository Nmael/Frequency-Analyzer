import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.lang.IllegalArgumentException;

public class EQPanel extends JPanel {
	private static final long serialVersionUID = -7357454042329239461L;
	
	private int numBars;
	private int barSepSpace;
	private int barWidth;
	private int[] barHeights;
	private int panelWidth = 800;
	private int panelHeight = 600;
	
	public EQPanel(int numBars, int barSepSpace) {
		this.numBars = numBars;
		this.barSepSpace = barSepSpace;
		this.barWidth = (panelWidth - barSepSpace*(numBars - 1)) / numBars;
		this.barHeights = new int[numBars];
	}
	
	/***
	 * Changes the height of an equalizer bar.
	 * @param barNum Which bar to change (0-indexed).
	 * @param height New height, as percentage of available space.
	 */
	public void setBarHeight(int barNum, double height) {
		if(barNum >= numBars) throw new IllegalArgumentException("Bar number greater than number of available bars.");
		
		barHeights[barNum] = (int) (height * panelHeight);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(panelWidth, panelHeight);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int offset = 0;
		for(int i = 0; i < numBars; ++i) {
			float thisHeight = (float)barHeights[i]/panelHeight;
			int r = (int)(255 * thisHeight);
			r = Math.min(255, r);
			g.setColor(new Color(r, 0, 0));
			g.fillRect(offset, panelHeight - barHeights[i], barWidth, barHeights[i]);
			offset += barWidth + barSepSpace;
		}
	}
}
