import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = -288708828031647612L;
	protected static JLabel debug;
	protected static JButton startStopButton;
	protected static EQPanel eqPanel;
	protected static CaptureSession currCapSes = null;

	public GUI() {
		debug = new JLabel("Debug");
		startStopButton = new JButton("Start/Stop");
		startStopButton.setActionCommand("startstop");
		startStopButton.addActionListener(this);
		
		eqPanel = new EQPanel(12, 1);

		this.setLayout(new BorderLayout());
		this.add(debug, BorderLayout.NORTH);
		this.add(eqPanel, BorderLayout.CENTER);
		this.add(startStopButton, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "startstop") {
			if(currCapSes == null || !currCapSes.recording) {
				final GUI gui = this;
				currCapSes = new CaptureSession();
				Thread captureThread = new Thread() {
					public void run() {
						currCapSes.capture(gui);
					}
				};
				
				captureThread.start();
			} else if(currCapSes != null) {
				currCapSes.recording = false;
			}
		}
	}
	
	private static void initGUI() {
		JFrame mainWin = new JFrame("Sound Sandbox");
		
		mainWin.setContentPane(new GUI());
		mainWin.pack();
		mainWin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainWin.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initGUI();
			}
	        });
	}
}
