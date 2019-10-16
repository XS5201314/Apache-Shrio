package cn.com.scitc.controller;

import cn.com.scitc.common.ApplicationContextHelper;
import cn.com.scitc.common.JsonData;
import cn.com.scitc.dao.SysAclModuleMapper;
import cn.com.scitc.exception.PermissonException;
import cn.com.scitc.model.SysAclModule;
import cn.com.scitc.param.TestVo;
import cn.com.scitc.util.BeanValidator;
import cn.com.scitc.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello(){
        log.info("hello ");
    throw new RuntimeException("xxx");
        //        return JsonData.success("success");
    }

//    @RequestMapping("/validate.json")
//    @ResponseBody
//    public JsonData validate(TestVo vo){
//        log.info("validate ");
//       try {
//           Map<String ,String > map = BeanValidator.validateObject(vo);
//           if (MapUtils.isNotEmpty(map)){
//              for (Map.Entry<String,String> entry : map.entrySet()){
//                log.info("{}->{}",entry.getKey(),entry.getValue());
//              }
//           }
//       }catch (Exception e){
//
//       }
//       return JsonData.success("test validate");
//    }


    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo) throws Exception{
        log.info("validate ");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.obj2String(module));

        BeanValidator.check(vo);
        return JsonData.success("test validate");
    }
}
