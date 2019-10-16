package cn.com.scitc.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
*@author xiaoxie
*@date  create 2019/9/17
*@return
 * 解析字符串
*/
public class StringUtil {

//  1,2.。。。会被移除
    public static List<Integer> splitToListInt(String str) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
//        jdk1.8
        return strList.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
    }
}
