package cn.com.scitc.service;

import cn.com.scitc.common.RequestHolder;
import cn.com.scitc.dao.SysAclMapper;
import cn.com.scitc.dao.SysAclModuleMapper;
import cn.com.scitc.exception.ParamException;
import cn.com.scitc.model.SysAclModule;
import cn.com.scitc.param.AclModuleParam;
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
*@date create 2019/9/17
*@return
 * 权限模块的开发
*/
@Service
@Slf4j
public class SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

//    save
    public void save(AclModuleParam param){
        try {
            BeanValidator.check(param);
            if(checkExist(param.getParentId(), param.getName(), param.getId())) {
                throw new ParamException("同一层级下存在相同名称的权限模块");
            }
            SysAclModule aclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                    .status(param.getStatus()).remark(param.getRemark()).build();
            aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
            aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
            aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            aclModule.setOperateTime(new Date());
            sysAclModuleMapper.insertSelective(aclModule);
            sysLogService.saveAclModuleLog(null,aclModule);


        }catch (Exception e){
            log.warn("SysAclModuleService error:"+e);
        }
    }


//    update
    public void update(AclModuleParam param) {
        try {
            BeanValidator.check(param);
            if(checkExist(param.getParentId(), param.getName(), param.getId())) {
                throw new ParamException("同一层级下存在相同名称的权限模块");
            }

            SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
            Preconditions.checkNotNull(before, "待更新的权限模块不存在");

            SysAclModule after = SysAclModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                    .status(param.getStatus()).remark(param.getRemark()).build();
            after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
            after.setOperator(RequestHolder.getCurrentUser().getUsername());
            after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            after.setOperateTime(new Date());
            updateWithChild(before, after);
            sysLogService.saveAclModuleLog(before,after);
        }catch (Exception e){
            log.warn("sysaclmodule update" +e);
        }

    }

//    updateWithChild
    @Transactional
    public void updateWithChild(SysAclModule before, SysAclModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                for (SysAclModule aclModule : aclModuleList) {
                    String level = aclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }

//    checkExist
    private boolean checkExist(Integer parentId, String aclModuleName, Integer deptId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, deptId) > 0;
    }

//    getLevel
    private String getLevel(Integer aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }


//    delete
    public void delete(int aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule, "待删除的权限模块不存在，无法删除");
        if(sysAclModuleMapper.countByParentId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有子模块，无法删除");
        }
        if (sysAclMapper.countByAclModuleId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有用户，无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }

}
