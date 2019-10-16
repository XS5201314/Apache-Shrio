package cn.com.scitc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
*@author xiaoxie
*@date create 2019/9/17
 *
*@return
 * 登录成功之后
*/

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

   @RequestMapping("index.page")
    public ModelAndView index(){
       log.warn("index.page fail");
       return new ModelAndView("admin");
   }

}