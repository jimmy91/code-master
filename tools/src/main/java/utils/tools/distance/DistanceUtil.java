package utils.tools.distance;

import java.util.Objects;

/**
 * @author Jimmy
 */
public class DistanceUtil {

    /**
     * 地球半径,单位 km
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 根据经纬度，计算两点间的距离
     *
     * @param point1 第一个点的经度
     * @param point2 第二个点的坐标
     * @return 返回距离 单位千米
     */
    public static String calcDistance(Point point1, Point point2) {
        String value = "";
        if (point1.getLongitude() == null || point1.getLatitude() == null || point2.getLongitude() == null || point2.getLatitude() == null) {
            return value;
        }
        // 纬度
        double lat1 = Math.toRadians(point1.getLatitude());
        double lat2 = Math.toRadians(point2.getLatitude());
        // 经度
        double lng1 = Math.toRadians(point1.getLongitude());
        double lng2 = Math.toRadians(point2.getLongitude());
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s = s * EARTH_RADIUS;

        return formatDistance(s);
    }

    public static String formatDistance(Double s){
        if(Objects.isNull(s)){
            return null;
        }
        String value = String.format("%.2fkm", s);
        if ("0.00km".equals(value)) {
            value = "0.01km";
        }
        return value;
    }



}