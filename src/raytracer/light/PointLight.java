package raytracer.light;

import raytracer.geometry.Hit;
import raytracer.geometry.World;
import raytracer.math.Constants;
import raytracer.math.Point3;
import raytracer.math.Ray;
import raytracer.math.Vector3;
import raytracer.texture.Color;

/**
 * This class represents a PointLight.
 *
 * @author Steven Sobkowski
 */
public class PointLight extends Light {
    /**
     * The position of the Light
     */
    public final Point3 position;

    /**
     * This constructor creates a PointLight with the given parameters.
     *
     * @param color       the given color
     * @param position    the given position
     * @param castsShadow determines if the given light casts a shadow
     */
    public PointLight(final Color color, final Point3 position, final boolean castsShadow) {
        super(color, castsShadow);
        if (color == null || position == null) throw new IllegalArgumentException("Parameters must not be null!");
        this.position = position;
    }

    @Override
    public boolean illuminates(final Point3 point, final World world) {
        if (point == null || world == null) throw new IllegalArgumentException("Parameters must not be null.");
        if (castsShadow) {
            double tl = position.sub(point).magnitude / directionFrom(point).magnitude;
            Ray shadowray = new Ray(point, directionFrom(point));
            Hit hitpoint = world.hit(shadowray);

            if (hitpoint == null) return true;
            else if (Constants.EPSILON < hitpoint.t && hitpoint.t < tl) return false;
            else return true;
        } else return true;
    }

    @Override
    public Vector3 directionFrom(final Point3 point) {
        if (point == null) throw new IllegalArgumentException("Point must not be null.");
        return position.sub(point);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointLight that = (PointLight) o;

        return position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    @Override
    public String toString() {
        return "PointLight{" +
                "position=" + position +
                '}';
    }
}
