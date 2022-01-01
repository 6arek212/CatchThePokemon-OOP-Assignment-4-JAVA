package assignemt4.models;

import assignemt4.api.GeoLocation;

import java.util.Objects;

public class GeoLocationImpl implements GeoLocation {
    private double x;
    private double y;
    private double z;

    public GeoLocationImpl(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GeoLocationImpl(String g) {
        String[] geos = g.split(",");
        this.x = Double.parseDouble(geos[0]);
        this.y = Double.parseDouble(geos[1]);
        this.z = Double.parseDouble(geos[2]);
    }

    public GeoLocationImpl(GeoLocation g) {
        if (!(g instanceof GeoLocationImpl))
            throw new ClassCastException();
        this.x = ((GeoLocationImpl) g).x;
        this.y = ((GeoLocationImpl) g).y;
        this.z = ((GeoLocationImpl) g).z;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {
        double d = Math.pow((this.x - g.x()), 2) + Math.pow((this.y - g.y()), 2) + Math.pow((this.z - g.z()), 2);
        return Math.sqrt(d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocationImpl that = (GeoLocationImpl) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "GeoLocationImpl{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
