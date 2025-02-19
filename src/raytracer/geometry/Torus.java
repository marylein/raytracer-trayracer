package raytracer.geometry;

import raytracer.material.Material;
import raytracer.math.*;
import raytracer.texture.TexCoord2;

/**
 * This class represents a Torus around the z-axis on the 0-Point.
 *
 * @author Oliver Kniejski
 */
public class Torus extends Geometry {
    /**
     * The radius - distance from middle of the hole to the middle of the tube.
     */
    final public double radius;
    /**
     * The diameter - the radius of the tube.
     */
    final public double diameter;

    /**
     * This constructor creates a new Torus.
     *
     * @param radius   The radius.
     * @param diameter The diameter.
     * @param material The material.
     */
    public Torus(final double radius, final double diameter, final Material material) {
        super(material);
        this.radius = radius;
        this.diameter = diameter;
    }

    @Override
    public Hit hit(final Ray r) {
        if (r == null) throw new IllegalArgumentException("Ray must not be null.");
        double a = 4 * Math.pow(radius, 2) * (Math.pow(r.d.x, 2) + Math.pow(r.d.y, 2));
        double b = 8 * Math.pow(radius, 2) * (r.o.x * r.d.x + r.o.y * r.d.y);
        double c = 4 * Math.pow(radius, 2) * (Math.pow(r.o.x, 2) + Math.pow(r.o.y, 2));
        double d = Math.pow(r.d.magnitude, 2);
        double e = 2 * (r.o.x * r.d.x + r.o.y * r.d.y + r.o.z * r.d.z);
        double f = Math.pow(r.o.x, 2) + Math.pow(r.o.y, 2) + Math.pow(r.o.z, 2) + Math.pow(radius, 2) - Math.pow(diameter, 2);

        double[] roots = Solvers.solveQuartic(Math.pow(d, 2), 2 * d * e, 2 * d * f + Math.pow(e, 2) - a, 2 * e * f - b, Math.pow(f, 2) - c);

        if (roots != null) {
            double t = roots[0];
            return new Hit(t, r, this.material, normalAt(r, roots[0]), calcTexCoord(r, t));
        }
        return null;
    }

    /**
     * This method calculates the coordinates of the texture.
     *
     * @param ray the ray
     * @param t   the t
     * @return The TexCoord2
     */
    public TexCoord2 calcTexCoord(final Ray ray, final double t) {
        Point3 hitpoint = ray.at(t);

        double v;
        double phi = Math.asin(hitpoint.z / diameter);
        if (radius > Math.sqrt(hitpoint.x * hitpoint.x + hitpoint.y * hitpoint.y)) {
            //inner tube
            if (phi > 0) {
                v = 1 - (phi / (2 * Math.PI));
            } else {
                v = -(phi / (2 * Math.PI));
            }
        } else {
            //outer tube
            v = (Math.PI + phi) / (2 * Math.PI);
        }

        double u = Math.atan2(hitpoint.x, hitpoint.y) / (2 * Math.PI);

        return new TexCoord2(u, v);
    }

    /**
     * This method takes a ray and a double t and calculates the hitPoint and returns the normal of that point
     *
     * @param r the ray
     * @param t the double
     * @return the normal of the hitPoint.
     */
    public Normal3 normalAt(final Ray r, final double t) {
        final Point3 p = r.at(t);
        final Vector3 m = new Vector3(p.x, p.y, 0).normalized().mul(radius);
        final Vector3 hitVector = m.sub(new Normal3(p.x, p.y, p.z));
        return hitVector.invert().asNormal();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Torus torus = (Torus) o;

        return Double.compare(torus.radius, radius) == 0 && Double.compare(torus.diameter, diameter) == 0;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(radius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(diameter);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Torus{" +
                "radius=" + radius +
                ", diameter=" + diameter +
                "} " + super.toString();
    }
}
