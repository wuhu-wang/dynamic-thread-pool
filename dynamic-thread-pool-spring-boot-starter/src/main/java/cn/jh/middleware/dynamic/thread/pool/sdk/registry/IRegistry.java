package cn.jh.middleware.dynamic.thread.pool.sdk.registry;

import cn.jh.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @author jh
 * @description 注册中心接口
 * @create 2024/6/16
 */
public interface IRegistry {
    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntityList);
    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
