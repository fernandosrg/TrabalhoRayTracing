package cg.trab2;

import org.jtrace.Scene;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;

public class ProgressiveResolutionTracer extends CustomResolutionTracer {

	private int startRes;
	private int finalRes;
	
	private int raysTraced = 0;
	private boolean optimizeRaysTraced = false;
	
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
		
		System.out.println("Rays traced: " + raysTraced);
		raysTraced = 0;
	}
	
	@Override
	protected void doRendering(Scene scene, ViewPlane viewPlane) {
		final int planeHres = viewPlane.getHres();
		final int planeVres = viewPlane.getVres();
		final Camera camera = scene.getCamera();

		initInterceptors(scene);

		double hResolutionRatio = planeHres / hres;
		double vResolutionRatio = planeVres / vres;

		for (int r = 0; r < vres; r++) {
			for (int c = 0; c < hres; c++) {
				if (!optimizeRaysTraced || !alreadyTraced(c, r)) {
					traceRay(scene, planeHres, planeVres, camera, hResolutionRatio,
							vResolutionRatio, r, c);
					
					raysTraced++;
				}
			}
		}
	}
	
	private boolean alreadyTraced(int c, int r) {
		return (hres != 2 && c % 2 == 0 && r % 2 == 0);
	}

	public boolean isOptimizeRaysTraced() {
		return optimizeRaysTraced;
	}

	public void setOptimizeRaysTraced(boolean optimizeRaysTraced) {
		this.optimizeRaysTraced = optimizeRaysTraced;
	}

}
