package cn.com.scitc.util;

import org.apache.commons.lang3.StringUtils;

/**
*@author xiaoxie
*@date create 2019/9/7  update 2019/9/12
*@return 分级管理部门
*/
public class LevelUtil {
//分层
    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

//    计算规则
    // 0
    // 0.1
    // 0.1.2
    // 0.1.3
    // 0.4
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
