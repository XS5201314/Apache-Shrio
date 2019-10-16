package cn.com.scitc.service;

import cn.com.scitc.common.RequestHolder;
import cn.com.scitc.dao.SysDeptMapper;
import cn.com.scitc.dao.SysUserMapper;
import cn.com.scitc.exception.ParamException;
import cn.com.scitc.model.SysDept;
import cn.com.scitc.param.DeptParam;
import cn.com.scitc.util.BeanValidator;
import cn.com.scitc.util.IpUtil;
import cn.com.scitc.util.LevelUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
*@author xiaoxie
*@date create 2019/9/7   update 2019/9/12
*@return 部门service
*/
@Service
@Slf4j
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;

    public void save(DeptParam param) {
       try {
            BeanValidator.check(param);
            if (checkExist(param.getParentId(),param.getName(),param.getId())){
                throw new ParamException("同一层级下存在相同名称的部门");
            }
           SysDept dept = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                   .seq(param.getSeq()).remark(param.getRemark()).build();
            dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));

            dept.setOperator(RequestHolder.getCurrentUser().getUsername());
            dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            dept.setOperateTime(new Date());
            sysDeptMapper.insertSelective(dept);
            sysLogService.saveDeptLog(null,dept);

       }catch (Exception e){
           log.info("BeanValidator error:",e);
       }

    }

    public void update(DeptParam param) throws Exception{

        try {
            BeanValidator.check(param);
            if(checkExist(param.getParentId(), param.getName(), param.getId())) {
                throw new ParamException("同一层级下存在相同名称的部门");
            }

        }catch (Exception e){
            log.info("error ",e);
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before, after);
        sysLogService.saveDeptLog(before,after);

    }


    @Transactional
    public void updateWithChild(SysDept before,SysDept after){
       //

//        新部门
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

//判断是否有重复
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
         return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }


    //    level
    private String getLevel(Integer deptId) {
      SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
         if (dept == null) {
        return null;
      }
     return dept.getLevel();
    }

    public void delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        if(sysUserMapper.countByDeptId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }
}
