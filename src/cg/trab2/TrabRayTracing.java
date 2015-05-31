package cg.trab2;

import org.jtrace.Material;
import org.jtrace.Scene;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.interceptor.ShadowInterceptor;
import org.jtrace.lights.Light;
import org.jtrace.lights.PointLight;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class TrabRayTracing {
	
	private static final double SPECULAR_FACTOR = 0.2;

    private static MainWindow window;
    
    private static Sphere obj1;
    private static Sphere obj2;

    public static void main(final String[] args) {
    	Scene scene = createScene();
    	ProgressiveResolutionTracer tracer = createTracer();
    	
    	window = new MainWindow(scene, tracer, obj1, obj2);
        window.setVisible(true);
    }

    public static Scene createScene() {
        Point3D lookAt = new Point3D(0, 0, 0);
        Point3D eye = new Point3D(0, 50, 100);
        Vector3D up = new Vector3D(0, 1, 0);

        Point3D centerRed  = new Point3D(0, 0, -20);
        Point3D centerBlue = new Point3D(-20, 0, -40);

        ReflectanceCoefficient kAmbient = new ReflectanceCoefficient(0.07, 0.07, 0.07);
        ReflectanceCoefficient kDiffuse = new ReflectanceCoefficient(0.5, 0.5, 0.5);
        
        double kR1 = 0.1;
        double kR2 = 0.05;
        ReflectanceCoefficient kReflectance1 = new ReflectanceCoefficient(kR1, kR1, kR1);
        ReflectanceCoefficient kReflectance2 = new ReflectanceCoefficient(kR2, kR2, kR2);
        
        Material redMaterial = new Material(ColorRGB.RED, kAmbient, kDiffuse, kReflectance1);
        Material blueMaterial = new Material(ColorRGB.BLUE, kAmbient, kDiffuse, kReflectance2);
        Material planeMaterial = new Material(ColorRGB.GREEN, kAmbient, kDiffuse);
        
        obj1 = new Sphere(centerRed, 20, redMaterial);
        obj2 = new Sphere(centerBlue, 20, blueMaterial);

        Plane plane = new Plane(new Point3D(0, -30, 0), new Vector3D(0, 1, 0), planeMaterial);

        Light light = new PointLight(-50, 40, 120);

        Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);
        pinHoleCamera.setZoomFactor(10);
        return new Scene().add(obj2, obj1, plane).add(light).setCamera(pinHoleCamera);
    }
    
    public static ProgressiveResolutionTracer createTracer() {
    	ProgressiveResolutionTracer tracer = new ProgressiveResolutionTracer(2,
				512);
		tracer.setOptimizeRaysTraced(false);
		tracer.setAntiAliasing(false);
		tracer.setMillisBetweenImages(200);
		
		tracer.addInterceptors(new ShadowInterceptor());

		tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(),
				Shaders.specularShader(SPECULAR_FACTOR));
		
		return tracer;
    }

}
