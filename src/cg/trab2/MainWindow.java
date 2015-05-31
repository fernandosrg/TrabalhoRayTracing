package cg.trab2;

import java.awt.Checkbox;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jtrace.Scene;
import org.jtrace.ViewPlane;
import org.jtrace.swing.TracerPanel;

public class MainWindow extends JFrame {

	private static final String LABEL_MILLIS = "Time between images:";

	private static final String LABEL_OPTIMIZE = "Optimize";

	private static final String LABEL_ANTI_ALIASING = "Anti-aliasing";

	private static final int VIEWPLANE_DIMENSION = 512;

	private static final long serialVersionUID = 8122517505454630633L;

	private Scene scene;
	private ProgressiveResolutionTracer tracer;
	
	private TracerPanel tracerPanel;
	private Checkbox chkAntiAliasing;
	private Checkbox chkOptimize;
	private TextField txtMillisBetweenImages;
	
	public MainWindow(Scene scene, ProgressiveResolutionTracer tracer) {
		setSize(550, 650);
		setTitle("Trabalho Ray Tracing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.scene = scene;
		this.tracer = tracer;

		init();
	}

	private void init() {
		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		tracerPanel = createTracerPanel();
		mainPanel.add(tracerPanel);
		
		chkAntiAliasing = new Checkbox(LABEL_ANTI_ALIASING);
		chkAntiAliasing.setState(tracer.isAntiAliasing());
		chkAntiAliasing.addItemListener(new AntiAliasingEventListener());
		mainPanel.add(chkAntiAliasing);
		
		chkOptimize = new Checkbox(LABEL_OPTIMIZE);
		chkOptimize.setState(tracer.isOptimizeRaysTraced());
		chkOptimize.addItemListener(new OptimizeEventListener());
		mainPanel.add(chkOptimize);
		
		Label lblMillisBetweenImages = new Label(LABEL_MILLIS);
		mainPanel.add(lblMillisBetweenImages);
		
		txtMillisBetweenImages = new TextField(6);
		txtMillisBetweenImages.setText(Integer.toString(tracer.getMillisBetweenImages()));
		txtMillisBetweenImages.addFocusListener(new MillisEventListener());
		mainPanel.add(txtMillisBetweenImages);

		add(mainPanel);
	}

	private TracerPanel createTracerPanel() {
		return new TracerPanel(tracer, scene, new ViewPlane(
				VIEWPLANE_DIMENSION, VIEWPLANE_DIMENSION), VIEWPLANE_DIMENSION,
				VIEWPLANE_DIMENSION);
	}
	
	private class AntiAliasingEventListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			tracer.setAntiAliasing(chkAntiAliasing.getState());

			if (tracer.isAntiAliasing()) {
				tracer.setOptimizeRaysTraced(false);
				chkOptimize.setState(false);
				chkOptimize.setEnabled(false);
			} else {
				chkOptimize.setEnabled(true);
			}
		}
	}
	
	private class OptimizeEventListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			tracer.setOptimizeRaysTraced(chkOptimize.getState());
		}
	}
	
	private class MillisEventListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent arg0) { }

		@Override
		public void focusLost(FocusEvent arg0) {
			int millis;
			
			try {
				millis = Integer.parseInt(txtMillisBetweenImages.getText());
			} catch (NumberFormatException e) {
				millis = 0;
				txtMillisBetweenImages.setText(Integer.toString(millis));
			}
			
			tracer.setMillisBetweenImages(millis);
		}
	}

}