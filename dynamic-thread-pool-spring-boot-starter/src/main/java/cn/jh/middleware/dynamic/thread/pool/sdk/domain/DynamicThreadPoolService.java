package cn.jh.middleware.dynamic.thread.pool.sdk.domain;

import cn.jh.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jh
 * @description 动态线程池服务
 * @create 2024/6/7
 */
public class DynamicThreadPoolService implements IDynamicThreadPoolService {

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolService.class);
    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;
    private final String applicationName;

    public DynamicThreadPoolService(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.threadPoolExecutorMap = threadPoolExecutorMap;
        this.applicationName = applicationName;
    }

    @Override
    public List<ThreadPoolConfigEntity> queryThreadPoolList() {
        Set<String> threadPoolBeanNames = threadPoolExecutorMap.keySet();
        List<ThreadPoolConfigEntity> threadPoolVOS = new ArrayList<>(threadPoolBeanNames.size());
        for (String threadPoolBeanName : threadPoolBeanNames) {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolBeanName);
            ThreadPoolConfigEntity threadPoolVO = new ThreadPoolConfigEntity(applicationName, threadPoolBeanName);
            threadPoolVO.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
            threadPoolVO.setPoolSize(threadPoolExecutor.getPoolSize());
            threadPoolVO.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
            threadPoolVO.setActiveCount(threadPoolExecutor.getActiveCount());
            threadPoolVO.setQueueSize(threadPoolExecutor.getQueue().size());
            threadPoolVO.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
            threadPoolVO.setRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());
            threadPoolVOS.add(threadPoolVO);
        }
        return threadPoolVOS;
    }

    @Override
    public ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (null == threadPoolExecutor) {
            return new ThreadPoolConfigEntity(applicationName, threadPoolName);
        }
        ThreadPoolConfigEntity threadPoolVO = new ThreadPoolConfigEntity(applicationName, threadPoolName);
        threadPoolVO.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
        threadPoolVO.setPoolSize(threadPoolExecutor.getPoolSize());
        threadPoolVO.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
        threadPoolVO.setActiveCount(threadPoolExecutor.getActiveCount());
        threadPoolVO.setQueueSize(threadPoolExecutor.getQueue().size());
        threadPoolVO.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
        threadPoolVO.setRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());
        if (logger.isDebugEnabled()) {
            logger.info("动态线程池配置 应用名：{} 线程名:{} 池化配置：{}",
                    applicationName, threadPoolName, JSON.toJSONString(threadPoolVO));
        }
        return threadPoolVO;
    }

    @Override
    public void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity) {
        if (null == threadPoolConfigEntity || !applicationName.equals(threadPoolConfigEntity.getAppName())) return;
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (null == threadPoolExecutor) {
            return;
        }
        //设置核心最大线程数
        threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
    }
}
