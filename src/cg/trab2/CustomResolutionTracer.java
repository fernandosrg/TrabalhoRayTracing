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
				int realR = (int) (r * vResolutionRatio);
				int realC = (int) (c * hResolutionRatio);

				ColorRGB color = traceRay(scene, planeHres, planeVres, camera,
						realR, realC);

				paint(hResolutionRatio, vResolutionRatio, realR, realC, color);
			}
		}
	}

	protected void paint(double hResolutionRatio, double vResolutionRatio,
			int realR, int realC, ColorRGB color) {

		for (int x = realC; x < realC + hResolutionRatio; x++)
			for (int y = realR; y < realR + vResolutionRatio; y++)
				fireAfterTrace(color, x, y);
	}

	protected ColorRGB traceRay(Scene scene, int planeHres, int planeVres,
			Camera camera, double realR, double realC) {

		final Jay jay = camera.createJay((int) (realR * 2), (int) (realC * 2),
				planeVres * 2, planeHres * 2);

		final ColorRGB color = trace(scene, jay);

		return color;
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
