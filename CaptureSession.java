import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;


public class CaptureSession {
	public boolean recording;
	
	private static TargetDataLine getTargetDataLine() {
		AudioFormat format = new AudioFormat(8000, 8, 1, true, true);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if(!AudioSystem.isLineSupported(info)) {
			// TODO: Handle error
		}
		
		TargetDataLine line = null;
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return line;
	}
	
	// assumes byteIn is a 8-bit double
	private double byteToDouble(byte byteIn) {
		return byteIn / 128.0;
	}
	
	private double dft(double freq, byte[] data) {
		double real = 0, img = 0;
		
		for(int i = 0; i < data.length; ++i) {
			double fn = byteToDouble(data[i]);
	
			//if(fn > 0 && !Double.isInfinite(fn) && !Double.isNaN(fn)) {
			if(true){
				real += fn * Math.cos((double)2*Math.PI*freq*(double)i);
				img += fn * Math.sin((double)2*Math.PI*freq*(double)i);
			}
		}
		
		return Math.sqrt((real*real + img*img)/data.length);
	}
	
	public void capture(GUI gui) {
		recording = true;
		
		TargetDataLine line = getTargetDataLine();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int numBytesRead = 0;
		byte[] data = new byte[line.getBufferSize() / 5];
		
		line.start();
		while(recording) {
			numBytesRead = line.read(data, 0, data.length);
			out.write(data, 0, numBytesRead);
			byte[] sample = out.toByteArray();
			
			//double midC = dft(261.63, sample);
			double freq1 = dft(261.63, sample);
			double freq2 = dft(293.66, sample);
			double freq3 = dft(329.63, sample);
			double freq4 = dft(349.23, sample);
			double freq5 = dft(392.00, sample);
			double freq6 = dft(440.00, sample);
			double freq7 = dft(493.88, sample);
			double freq8 = dft(523.25, sample);
			double freq9 = dft(587.33, sample);
			double freq10 = dft(659.26, sample);
			double freq11 = dft(698.46, sample);
			double freq12 = dft(783.99, sample);
			
			GUI.debug.setText(Double.toString(freq1));
			
			out.reset();
			GUI.eqPanel.setBarHeight(0, freq1);
			GUI.eqPanel.setBarHeight(1, freq2);
			GUI.eqPanel.setBarHeight(2, freq3);
			GUI.eqPanel.setBarHeight(3, freq4);
			GUI.eqPanel.setBarHeight(4, freq5);
			GUI.eqPanel.setBarHeight(5, freq6);
			GUI.eqPanel.setBarHeight(6, freq7);
			GUI.eqPanel.setBarHeight(7, freq8);
			GUI.eqPanel.setBarHeight(8, freq9);
			GUI.eqPanel.setBarHeight(9, freq10);
			GUI.eqPanel.setBarHeight(10, freq11);
			GUI.eqPanel.setBarHeight(11, freq12);
			gui.repaint();
		}
		line.stop();
		line.close();
	}
}
