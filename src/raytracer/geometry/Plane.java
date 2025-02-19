package raytracer.geometry;

import raytracer.material.Material;
import raytracer.math.Constants;
import raytracer.math.Normal3;
import raytracer.math.Point3;
import raytracer.math.Ray;
import raytracer.texture.Color;
import raytracer.texture.TexCoord2;
import raytracer.texture.TextureUtils;

/**
 * This class represents a plane.
 *
 * @author Marie Hennings
 */
public class Plane extends Geometry {
    /**
     * A point of the plane.
     */
    public final Point3 a;
    /**
     * The normal of the plane.
     */
    public final Normal3 n;
    /**
     * The scalar for the texture of the plane.
     */
    public final int textureScalar;

    /**
     * This constructor creates a plane on 0 spanning with the normal in y-direction with a default texture scalar.
     *
     * @param material The material.
     */
    public Plane(final Material material) {
        this(new Point3(0, 0, 0), new Normal3(0, 1, 0), material, 1);
    }

    /**
     * This constructor creates a plane on 0 spanning with the normal in y-direction.
     *
     * @param material      The material.
     * @param textureScalar The scalar of the texture.
     */
    public Plane(final Material material, final int textureScalar) {
        this(new Point3(0, 0, 0), new Normal3(0, 1, 0), material, textureScalar);
    }


    /**
     * This constructor creates a plane of a point and the normal with a default texture scalar.
     *
     * @param a        A Point.
     * @param n        The normal. The magnitude of the normal must not be zero.
     * @param material The material.
     */
    public Plane(final Point3 a, final Normal3 n, final Material material) {
        this(a, n, material, 1);
    }

    /**
     * This constructor creates a plane of a point and the normal.
     *
     * @param a             A Point.
     * @param n             The normal. The magnitude of the normal must not be zero.
     * @param material      The material.
     * @param textureScalar The scalar of the texture.
     */
    public Plane(final Point3 a, final Normal3 n, final Material material, final int textureScalar) {
        super(material);
        if (a == null || n == null) throw new IllegalArgumentException("Parameters must not be null.");
        if (n.x == 0 && n.y == 0 && n.z == 0) {
            throw new IllegalArgumentException("Magnitude of the normal of the plane must not be zero.");
        }
        this.a = a;
        this.n = n;
        this.textureScalar = textureScalar;
    }

    /**
     * This method creates and returns the hit of this plane and a ray.
     *
     * @param r The ray.
     * @return The hit of the ray and the geometry.
     */
    public Hit hit(final Ray r) {
        if (r == null) throw new IllegalArgumentException("Ray must not be null.");
        double t = ((a.sub(r.o)).dot(n)) / ((r.d).dot(n));
        if (t > Constants.EPSILON) {
            Normal3 normal = n.mul(1 / Math.sqrt(n.x * n.x + n.y * n.y + n.z * n.z));


            return new Hit(t, r, this.material, normal, TextureUtils.getPlaneTexCoord(r, t, n, textureScalar));
        }
        return null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Plane plane = (Plane) o;

        if (!a.equals(plane.a)) return false;
        return n.equals(plane.n);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + a.hashCode();
        result = 31 * result + n.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "a=" + a +
                ", n=" + n +
                '}';
    }
}
