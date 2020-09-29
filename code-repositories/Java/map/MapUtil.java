/**
 * Description: 地图工具类
 * Author: DUHAIHANG - ABCC
 * Date: 2018/10/23 10:48
 * Version: V1.0
 */
public class MapUtil {

    public static void main(String[] agrs) {
        // 北京-天津
        System.out.println(getDistHaversineRAD(39.90419989999999, 116.40739630000007, 39.3433574,117.36164759999997));
        System.out.println(getDist(39.90419989999999, 116.40739630000007, 39.3433574,117.36164759999997));
    }

    /**
     * @Author DUHAIHANG
     * @Description 通过经纬度获取距离(单位：米) 基于百度地图经纬度
     * https://blog.csdn.net/woaixinxin123/article/details/45935439
     * @Date 11:18 2018/10/23
     * @Param [lat1 纬度1, lon1 经度1, lat2 纬度2, lon2 经度2]
     * @return double
     */
    public static double getDist(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137;
        s = Math.round(s * 10000d) / 10000d;
        return s * 1000;
    }
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @Author DUHAIHANG
     * @Description Haversine公式计算距离
     * https://blog.csdn.net/u011001084/article/details/52980834
     * @Date 10:54 2018/10/23
     * @Param [lat1 纬度1, lon1 经度1, lat2 纬度2, lon2 经度2]
     * @return double
     */
    public static double getDistHaversineRAD (double lat1, double lon1, double lat2, double lon2) {
        double hsinX = Math.sin((lon1 - lon2) * 0.5);
        double hsinY = Math.sin((lat1 - lat2) * 0.5);
        double h = hsinY * hsinY + (Math.cos(lat1) * Math.cos(lat2) * hsinX * hsinY);
        return 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h)) * 6367000;
    }


}
