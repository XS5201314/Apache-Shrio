package cn.com.scitc.param;

import lombok.Data;
import lombok.ToString;
/**
*@author xiaoxie
*@date create 2019/9/19
*@return
 *
 * 日志操作搜索
*/
@Data
@ToString
public class SearchLogParam {

//    类型
    private Integer type;

//    更新之前
    private String beforeSeg;

//    更新之后
    private String afterSeg;

//    操作者
    private String operator;

    private String fromTime;//yyyy-MM-dd HH:mm:ss

//    时间段
    private String toTime;
}

