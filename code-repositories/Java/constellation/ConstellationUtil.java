import org.apache.commons.lang.math.NumberUtils;

import java.util.Date;

/**
 * Description: 计算星座
 * Author: DUHAIHANG - ABCC
 * Date: 2018/12/5 14:46
 * Version: V1.0
 */
public class ConstellationUtil {
    private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
    private final static String[] constellationArr = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};

    /**
     * @return java.lang.String
     * @Author DUHAIHANG
     * @Description 生日计算星座
     * @Date 14:56 2018/12/5
     * @Param [date]
     */
    public static String getConstellation(Date date) {
        if (null != date) {
            try {
                String dateStr = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_FORMAT_YYYY_MM_DD);
                String[] dataStrArry = dateStr.split("-");
                if (dataStrArry.length == 3 && NumberUtils.isNumber(dataStrArry[0]) && NumberUtils.isNumber(dataStrArry[1]) && NumberUtils.isNumber(dataStrArry[2])) {
                    int day = Integer.parseInt(dataStrArry[2]);
                    int month = Integer.parseInt(dataStrArry[1]);
                    return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
