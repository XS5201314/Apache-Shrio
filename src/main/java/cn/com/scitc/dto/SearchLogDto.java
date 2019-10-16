package cn.com.scitc.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
/**
*@author xiaoxie
*@date create 2019/9/17
*@return
 * 进入数据库
*/
@Data
@ToString
public class SearchLogDto {


    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;

    private Date fromTime;//yyyy-MM-dd HH:mm:ss

    private Date toTime; //yyyy-MM-dd HH:mm:ss
}
