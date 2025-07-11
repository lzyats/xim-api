package com.platform.common.task;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SysTask {

    /**
     * 清理缓存
     */
    public void cache() {
        if (SystemUtil.getOsInfo().isLinux()){
            RuntimeUtil.exec("echo 1 > /proc/sys/vm/drop_caches");
            RuntimeUtil.exec("echo 2 > /proc/sys/vm/drop_caches");
            RuntimeUtil.exec("echo 3 > /proc/sys/vm/drop_caches");
        }
    }

}
