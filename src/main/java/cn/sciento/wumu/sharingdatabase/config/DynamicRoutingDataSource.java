package cn.sciento.wumu.sharingdatabase.config;

import cn.sciento.wumu.sharingdatabase.config.DynamicDataSourceContextHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 *
 * @author Taven
 *
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private  Map<Object, Object> targetDataSources = new HashMap<>();

    /**
     * 设置当前数据源
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("Current DataSource is [{}]", DynamicDataSourceContextHolder.getDataSourceKey());
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        this.targetDataSources = targetDataSources;
    }

    /**
     * 是否存在当前key的 DataSource
     *
     * @param key
     * @return 存在返回 true, 不存在返回 false
     */
    public  boolean isExistDataSource(String key) {
        return targetDataSources.containsKey(key);
    }

    /**
     * 动态增加数据源
     *
     * @param map 数据源属性
     * @return
     */
    public synchronized boolean addDataSource(Map<String, String> map) {
        try {
            Connection connection = null;
            // 排除连接不上的错误
            try {
                Class.forName(map.get(DruidDataSourceFactory.PROP_DRIVERCLASSNAME));
                connection = DriverManager.getConnection(
                        map.get(DruidDataSourceFactory.PROP_URL),
                        map.get(DruidDataSourceFactory.PROP_USERNAME),
                        map.get(DruidDataSourceFactory.PROP_PASSWORD));
                System.out.println(connection.isClosed());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
            String database = map.get("key");//获取要添加的数据库名
            if (database == null) {
                return false;
            }
            if (isExistDataSource(database)) {
                return true;
            }
            DruidDataSource druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(map);
            druidDataSource.init();
            Map<Object, Object> targetMap = new HashMap<>();
            targetMap.put(database, druidDataSource);
            // 当前 targetDataSources 与 父类 targetDataSources 为同一对象 所以不需要set
			this.setTargetDataSources(targetMap);

			// 这个一定要添加，不然不能运行
            this.afterPropertiesSet();
            logger.info("dataSource [{}] has been added", database);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

}