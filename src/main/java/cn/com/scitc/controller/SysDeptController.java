package cn.com.scitc.controller;

import cn.com.scitc.common.JsonData;
import cn.com.scitc.dto.DeptLevelDto;
import cn.com.scitc.param.DeptParam;
import cn.com.scitc.service.SysDeptService;
import cn.com.scitc.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
*@author xiaoxie
*@date create 2019/9/7  update 2019/9/12  2019/9/18
*@return
*/
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.page")
    public ModelAndView page() {
        return new ModelAndView("dept");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam param) {
        sysDeptService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam param) throws Exception{
        sysDeptService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int id) {
        sysDeptService.delete(id);
        return JsonData.success();
    }

}
