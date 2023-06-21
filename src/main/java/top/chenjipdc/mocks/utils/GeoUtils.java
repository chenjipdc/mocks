package top.chenjipdc.mocks.utils;

import top.chenjipdc.mocks.other.Validate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeoUtils {

    private static final double MIN_LON = -180.0;
    private static final double MAX_LON = 180.0;

    private static final double MIN_LAT = -90.0;
    private static final double MAX_LAT = 90.0;

    private static final int DEFAULT_SCALE = 6;

    public static GeoPoint random(int scale) {
        return point(MIN_LON,
                MAX_LON,
                MIN_LAT,
                MAX_LAT,
                scale);
    }

    public static GeoPoint random() {
        return point(MIN_LON,
                MAX_LON,
                MIN_LAT,
                MAX_LAT,
                DEFAULT_SCALE);
    }

    public static GeoPoint pointLon(double minLon, double maxLon) {
        return point(minLon,
                maxLon,
                MIN_LAT,
                MAX_LAT,
                DEFAULT_SCALE);
    }

    public static GeoPoint pointLat(double minLat, double maxLat) {
        return point(MIN_LON,
                MAX_LON,
                minLat,
                maxLat,
                DEFAULT_SCALE);
    }

    public static GeoPoint pointLon(double minLon, double maxLon, int scale) {
        return point(minLon,
                maxLon,
                MIN_LAT,
                MAX_LAT,
                scale);
    }

    public static GeoPoint pointLat(double minLat, double maxLat, int scale) {
        return point(MIN_LON,
                MAX_LON,
                minLat,
                maxLat,
                scale);
    }

    public static GeoPoint point(double minLon, double maxLon, double minLat, double maxLat, int scale) {
        Validate.isTrue(minLon <= maxLon,
                "longitude range error：" + minLon + "," + maxLon);
        Validate.isTrue(minLat <= maxLat,
                "latitude range error：" + minLat + "," + maxLat);

        Validate.isTrue(minLon >= MIN_LON,
                "longitude minimum must getter than" + MIN_LON);
        Validate.isTrue(maxLon <= MAX_LON,
                "longitude maximum must letter than" + MAX_LON);
        Validate.isTrue(minLat >= MIN_LAT,
                "latitude minimum must getter than" + MIN_LAT);
        Validate.isTrue(maxLat <= MAX_LAT,
                "latitude maximum must letter than" + MAX_LAT);

        return new GeoPoint(BigDecimal.valueOf(Math.random() * (maxLon - minLon) + minLon).setScale(scale,
                RoundingMode.DOWN).doubleValue(),
                BigDecimal.valueOf(Math.random() * (maxLat - minLat) + minLat).setScale(scale,
                        RoundingMode.DOWN).doubleValue());
    }


    public static class GeoPoint {
        private double lon;

        private double lat;

        public GeoPoint(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }


        @Override
        public String toString() {
            return lon + "," + lat;
        }
    }
}
