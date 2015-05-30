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
				} catch (Exception e) {
				}
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
				if (antiAliasing || !optimizeRaysTraced || !alreadyTraced(c, r)) {
					int realR = (int) (r * vResolutionRatio);
					int realC = (int) (c * hResolutionRatio);

					ColorRGB color;

					if (antiAliasing) {
						color = traceRayWithAntiAliasing(scene,
								planeHres, planeVres, camera, hResolutionRatio,
								vResolutionRatio, realR, realC);
					} else {
						color = traceRay(scene, planeHres, planeVres, camera,
								realR, realC);
					}

					paint(hResolutionRatio, vResolutionRatio, realR, realC,
							color);

				}
			}
		}
	}

	protected ColorRGB traceRayWithAntiAliasing(Scene scene,
			final int planeHres, final int planeVres, final Camera camera,
			double hResolutionRatio, double vResolutionRatio, int realR,
			int realC) {
		double rOffset = vResolutionRatio/2;
		double cOffset = hResolutionRatio/2;

		ColorRGB c1 = traceRay(scene, planeHres, planeVres,
				camera, realR+rOffset, realC+cOffset);
		
		ColorRGB c2 = traceRay(scene, planeHres, planeVres,
				camera, realR+rOffset, realC-cOffset);
		
		ColorRGB c3 = traceRay(scene, planeHres, planeVres,
				camera, realR-rOffset, realC+cOffset);
		
		ColorRGB c4 = traceRay(scene, planeHres, planeVres,
				camera, realR-rOffset, realC-cOffset);
		
		ColorRGB finalColor = new ColorRGB(
				(c1.getRed() + c2.getRed() + c3.getRed() + c4.getRed())/4,
				(c1.getGreen() + c2.getGreen() + c3.getGreen() + c4.getGreen())/4,
				(c1.getBlue() + c2.getBlue() + c3.getBlue() + c4.getBlue())/4
				);
		return finalColor;
	}
	
	@Override
	protected ColorRGB traceRay(Scene scene, int planeHres, int planeVres,
			Camera camera, double realR, double realC) {
		raysTraced++;
		return super.traceRay(scene, planeHres, planeVres, camera, realR, realC);
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
