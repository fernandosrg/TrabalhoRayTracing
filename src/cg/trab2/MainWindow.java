package cg.trab2;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.shader.Shaders;
import org.jtrace.swing.TracerPanel;

public class MainWindow extends JFrame {

  private static final int VIEWPLANE_DIMENSION = 512;

private static final double SPECULAR_FACTOR = 0.2;

  private static final long serialVersionUID = 8122517505454630633L;

  private Scene scene;
  
  public MainWindow(Scene scene) {
    setSize(700, 650);
    setTitle("JTrace");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    this.scene = scene;

    init();
  }

  private void init() {
    JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

    mainPanel.add(createTracerPanel());
    
    add(mainPanel);
  }

  private JPanel createTracerPanel() {
	Tracer tracer = new CustomResolutionTracer(512, 512);
	
	tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(), Shaders.specularShader(SPECULAR_FACTOR));
	  
    return new TracerPanel(tracer, scene, new ViewPlane(VIEWPLANE_DIMENSION, VIEWPLANE_DIMENSION), VIEWPLANE_DIMENSION, VIEWPLANE_DIMENSION);
  }
  
}