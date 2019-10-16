package cn.com.scitc.util;

import cn.com.scitc.exception.ParamException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
*@author xiaoxie
*@date create 2019/9/6  update 2019/9/11
*@return  校验工具
*/
public class BeanValidator {

//    全局处理
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

//    校验方法
    public static <T>Map<String,String> validate(T t,Class... groups){
        Validator validator = validatorFactory.getValidator();
        Set vlidateResult = validator.validate(t,groups);

//        有值
        if (vlidateResult.isEmpty()){
            return Collections.emptyMap();
        }else {
//        Google新写法
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = vlidateResult.iterator();
            while (iterator.hasNext()){
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(),violation.getMessage());
            }
            return errors;
        }
    }

//    list
    public static Map<String,String> validateList(Collection<?> collection){
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()){
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object,new Class[0]);
        }while (errors.isEmpty());
        return errors;
    }

    public static Map<String ,String> validateObject(Object first,Object... objects){
        if (objects != null && objects.length > 0 ){
           return validateList(Lists.asList(first,objects));
        }else {
           return validate(first,new Class[0]);
        }
    }

//    仅判断是否异常
    public static void check(Object param) throws Exception{
        Map<String,String > map = BeanValidator.validateObject(param);
        if (MapUtils.isNotEmpty(map)){
             throw new ParamException(map.toString());
        }
    }

}
