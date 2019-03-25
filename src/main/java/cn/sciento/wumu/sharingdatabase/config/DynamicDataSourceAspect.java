package cn.sciento.wumu.sharingdatabase.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DynamicDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    /**
     * 定义切点,
     * 	service包 以及其子包下 第一个参数为String 的方法作为切点
     */
    @Pointcut("execution( * cn.sciento.wumu.sharingdatabase.*.*(String, ..))")
    public void serviceAspect() {}

    /**
     * 选择数据源
     *
     * @param point
     */
    @Before("serviceAspect()")
    public void switchDataSource(JoinPoint point) {
        Object[] params = point.getArgs();
        String param = (String) params[0];

        if(param == null) {
            return;
        }

        if( !param.equals(DynamicDataSourceContextHolder.getDataSourceKey()) ) {
            DynamicDataSourceContextHolder.setDataSourceKey(param);
        }

        logger.info("Switch DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }

    /**
     * 重置数据源
     *
     * @param point
     */
    @After("serviceAspect()")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        logger.info("Restore DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }

}
