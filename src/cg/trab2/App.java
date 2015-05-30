package cg.trab2;

import org.jtrace.Material;
import org.jtrace.Scene;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.lights.PointLight;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

public class App {

    private static MainWindow window = new MainWindow(createScene());

    public static void main(final String[] args) {
        window.setVisible(true);
    }

    public static Scene createScene() {
        final Point3D lookAt = new Point3D(0, 0, 0);
        final Point3D eye = new Point3D(-15, -15, 100);
        final Vector3D up = new Vector3D(0, 1, 0);

        final Point3D centerRed  = new Point3D(0, 0, -20);
        final Point3D centerBlue = new Point3D(-20, 0, -40);

        final Point3D planePoint = new Point3D(0, 20, 0);
        final Vector3D planeNormal = new Vector3D(0, -1, 0);

        final ReflectanceCoefficient kAmbient = new ReflectanceCoefficient(0.07, 0.07, 0.07);
        final ReflectanceCoefficient kDiffuse = new ReflectanceCoefficient(0.3, 0.3, 0.3);
        
        double kR1 = 0.1;
        double kR2 = 0.05;
        ReflectanceCoefficient kReflectance1 = new ReflectanceCoefficient(kR1, kR1, kR1);
        ReflectanceCoefficient kReflectance2 = new ReflectanceCoefficient(kR2, kR2, kR2);
        
        final Material redMaterial = new Material(ColorRGB.RED, kAmbient, kDiffuse, kReflectance1);
        final Material blueMaterial = new Material(ColorRGB.BLUE, kAmbient, kDiffuse, kReflectance2);
        final Material planeMaterial = new Material(ColorRGB.YELLOW, kAmbient, kDiffuse);
        
        final Sphere red = new Sphere(centerRed, 20, redMaterial);
        final Sphere blue = new Sphere(centerBlue, 20, blueMaterial);

        final Plane plane1 = new Plane(planePoint, planeNormal, planeMaterial);
        final Plane plane2 = new Plane(planePoint.multiply(-1), planeNormal.multiply(-1), planeMaterial);

        final Light light = new PointLight(0, -40, 20);

        final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);
        pinHoleCamera.setZoomFactor(10);
        return new Scene().add(blue, red, plane1, plane2).add(light).setCamera(pinHoleCamera);
    }

}
