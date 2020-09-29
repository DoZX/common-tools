import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Description: 自定义缓存类
 * Author: DUHAIHANG - ABCC
 * Date: 2019/3/5 11:55
 * Version: V1.0
 */
public class SystemCache {

    private static final Integer dataSourceSize = 135; // 最大容量 100
    private static final Integer dataSourceLevel = 100; // 临界值
    private static final Integer dataSourceCPIndex = 74;// CP起始的下标(第75个用户)

    private static class DataSource {
        private static Integer guideLinkKey = 0;// KEY 链指针
        private static String[] linkKeys = new String[100];// KEY 链
        private static Boolean guideDataSource = Boolean.TRUE;// true:E0 空间;  false:E1 空间
        private static Map<String, Object> dataSourceE0 = new LinkedHashMap<String, Object>(dataSourceSize);// 负载因子0.75
        private static Map<String, Object> dataSourceE1 = new LinkedHashMap<String, Object>(dataSourceSize);// 负载因子0.75
    }

    /**
     * @Author DUHAIHANG
     * @Description 存入缓存
     * @Date 12:03 2019/3/5
     * @Param [ket, data]
     * @return java.lang.Boolean
     */
    public static synchronized Boolean put(String key, Object data) {
        if (StringUtils.isNotBlank(key) && null != data) {

            if (DataSource.guideLinkKey.equals(dataSourceLevel)) {
                cpDataSource();
            }

            if (DataSource.guideDataSource) {
                DataSource.dataSourceE0.put(key, data);
            } else {
                DataSource.dataSourceE1.put(key, data);
            }
            DataSource.linkKeys[DataSource.guideLinkKey++] = key;

            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @Author DUHAIHANG
     * @Description 获取缓存
     * @Date 12:51 2019/3/5
     * @Param [key]
     * @return java.lang.Object
     */
    public static Object get(String key) {
        if (StringUtils.isNotBlank(key)) {
            if (DataSource.guideDataSource) {
                return DataSource.dataSourceE0.get(key);
            } else {
                return DataSource.dataSourceE1.get(key);
            }
        }
        return null;
    }

    // Map 对象CP
    private static void cpDataSource() {
        DataSource.guideLinkKey = 0; // 重置linkKeys 指针

        if (DataSource.guideDataSource) {
            // E0 -> E1
            DataSource.dataSourceE1 = new HashMap<String, Object>(dataSourceSize);
            for (int i = dataSourceCPIndex; i < dataSourceLevel; i ++) {
                DataSource.linkKeys[DataSource.guideLinkKey++] = DataSource.linkKeys[i];
                DataSource.dataSourceE1.put(DataSource.linkKeys[i], DataSource.dataSourceE0.get(DataSource.linkKeys[i]));
            }
            DataSource.guideDataSource = Boolean.FALSE;
        } else {
            // E1 -> E0
            DataSource.dataSourceE0 = new HashMap<String, Object>(dataSourceSize);
            for (int i = dataSourceCPIndex; i < dataSourceLevel; i ++) {
                DataSource.linkKeys[DataSource.guideLinkKey++] = DataSource.linkKeys[i];
                DataSource.dataSourceE0.put(DataSource.linkKeys[i], DataSource.dataSourceE1.get(DataSource.linkKeys[i]));
            }
            DataSource.guideDataSource = Boolean.TRUE;
        }
    }

}
