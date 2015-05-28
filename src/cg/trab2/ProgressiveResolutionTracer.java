package cg.trab2;

import org.jtrace.Jay;
import org.jtrace.Scene;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.primitives.ColorRGB;

public class ProgressiveResolutionTracer extends CustomResolutionTracer {

	public int startRes;
	public int finalRes;
	
	public ProgressiveResolutionTracer(int startRes, int finalRes) {
		super(startRes, startRes);
		
		this.startRes = startRes;
		this.finalRes = finalRes;
	}
	
	@Override
	public void render(Scene scene, ViewPlane viewPlane) {
		fireStart(viewPlane);
		
		while (hres <= finalRes) {
			doRendering(scene, viewPlane);
			hres *= 2;
			vres *= 2;
		}
		
		fireFinish();
		setResolution(startRes, startRes);
	}

}
