package cg.trab2;

import org.jtrace.Scene;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.primitives.ColorRGB;

public class ProgressiveResolutionTracer extends CustomResolutionTracer {

	private int startRes;
	private int finalRes;

	private int raysTraced = 0;
	private int millisBetweenImages = 0;
	
	private boolean optimizeRaysTraced = false;
	private boolean antiAliasing = false;

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
			
			if (millisBetweenImages > 0) {
				try {
					Thread.sleep(millisBetweenImages);
				} catch (Exception e) { }
			}
			;
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
					int realR = (int) (r * vResolutionRatio);
					int realC = (int) (c * hResolutionRatio);

					ColorRGB color = traceRay(scene, planeHres, planeVres,
							camera, realR, realC);
					
					paint(hResolutionRatio, vResolutionRatio, realR, realC, color);

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

	public boolean isAntiAliasing() {
		return antiAliasing;
	}

	public void setAntiAliasing(boolean antiAliasing) {
		this.antiAliasing = antiAliasing;
	}

	public int getMillisBetweenImages() {
		return millisBetweenImages;
	}

	public void setMillisBetweenImages(int millisBetweenImages) {
		this.millisBetweenImages = millisBetweenImages;
	}

}
