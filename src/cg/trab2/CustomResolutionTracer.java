package cg.trab2;

import org.jtrace.Jay;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.primitives.ColorRGB;

public class CustomResolutionTracer extends Tracer {

	protected int hres;
	protected int vres;

	public CustomResolutionTracer(int hres, int vres) {
		super();
		setResolution(hres, vres);
	}

	@Override
	public void render(Scene scene, ViewPlane viewPlane) {
		fireStart(viewPlane);

		doRendering(scene, viewPlane);

		fireFinish();
	}

	protected void doRendering(Scene scene, ViewPlane viewPlane) {
		final int planeHres = viewPlane.getHres();
		final int planeVres = viewPlane.getVres();
		final Camera camera = scene.getCamera();

		initInterceptors(scene);

		double hResolutionRatio = planeHres / hres;
		double vResolutionRatio = planeVres / vres;

		for (int r = 0; r < vres; r++) {
			for (int c = 0; c < hres; c++) {
				traceRay(scene, planeHres, planeVres, camera, hResolutionRatio,
						vResolutionRatio, r, c);
			}
		}
	}

	protected void traceRay(Scene scene, final int planeHres,
			final int planeVres, final Camera camera, double hResolutionRatio,
			double vResolutionRatio, int r, int c) {
		int realR = (int) (r*vResolutionRatio);
		int realC = (int) (c*hResolutionRatio);

		final Jay jay = camera.createJay(realR, realC, planeVres, planeHres);

		final ColorRGB color = trace(scene, jay);
		
		for (int x = realC; x < realC + hResolutionRatio ; x++)
			for (int y = realR; y < realR + vResolutionRatio ; y++)
				fireAfterTrace(color, x, y);
	}

	public void setResolution(int hres, int vres) {
		this.hres = hres;
		this.vres = vres;
	}

	public int getVres() {
		return vres;
	}

	public void setVres(int vres) {
		this.vres = vres;
	}

	public int getHres() {
		return hres;
	}

	public void setHres(int hres) {
		this.hres = hres;
	}
}
