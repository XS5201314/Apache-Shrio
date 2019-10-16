package cn.com.scitc.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
*@author xiaoxie
*@date crate 2019/9/17
*@return
 * 分页展示
*/

@Data
@ToString
@Builder
public class PageResult<T> {

//    与前端对应
    private List<T> data = Lists.newArrayList();

    private int total = 0;//总条数
}
