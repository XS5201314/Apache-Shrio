package cn.com.scitc.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
*@author xiaoxie
*@date create 2019/9/6   update 2019/9/11
*@return
 *
 * json -->Object
 * Object -->json
*/
@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
//        config  排除空字段
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

//    对象转换字符串
    public static <T> String obj2String(T src){
        if (src == null){
            return null;
        }
        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        }catch (Exception e){
            log.warn("空字符串的错误",e);
            return null;
        }
    }


//    字符串反转成对象
    public static <T> T string2Obj(String src,TypeReference<T> tTypeReference){
        if (src == null || tTypeReference == null){
            return null;
        }
        try {
            return (T) (tTypeReference.getType().equals(String.class) ? src : objectMapper.readValue(src,tTypeReference));
        }catch (Exception e){
            log.warn("parse String to Object exception, String:{}, TypeReference<T>:{}, error:{}", src, tTypeReference.getType(), e);
            return null;
        }
    }

}
