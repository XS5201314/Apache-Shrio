package cn.com.scitc.service;

import cn.com.scitc.common.RequestHolder;
import cn.com.scitc.dao.SysRoleAclMapper;
import cn.com.scitc.dao.SysRoleMapper;
import cn.com.scitc.dao.SysRoleUserMapper;
import cn.com.scitc.dao.SysUserMapper;
import cn.com.scitc.exception.ParamException;
import cn.com.scitc.model.SysRole;
import cn.com.scitc.model.SysUser;
import cn.com.scitc.param.RoleParam;
import cn.com.scitc.util.BeanValidator;
import cn.com.scitc.util.IpUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
*@author xiaoxie
*@date create 2019/9/17
*@return
 * 角色模块
*/
@Service
@Slf4j
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;

    //    save
    public void save(RoleParam param) {
        try {
            BeanValidator.check(param);
            if (checkExist(param.getName(), param.getId())) {
                throw new ParamException("角色名称已经存在");
            }
            SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                    .remark(param.getRemark()).build();
            role.setOperator(RequestHolder.getCurrentUser().getUsername());
            role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            role.setOperateTime(new Date());
            sysRoleMapper.insertSelective(role);
            sysLogService.saveRoleLog(null,role);
        } catch (Exception e) {
            log.warn("SysRoleService error" + e);
        }
    }

    //    update
    public void update(RoleParam param) {
        try {
            BeanValidator.check(param);
            if (checkExist(param.getName(), param.getId())) {
                throw new ParamException("角色名称已经存在");
            }
            SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
            Preconditions.checkNotNull(before, "待更新的角色不存在");

            SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus()).type(param.getType())
                    .remark(param.getRemark()).build();
            after.setOperator(RequestHolder.getCurrentUser().getUsername());
            after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            after.setOperateTime(new Date());
            sysRoleMapper.updateByPrimaryKeySelective(after);
            sysLogService.saveRoleLog(before,after);
        } catch (Exception e) {
            log.warn("SysRoleService error" + e);
        }
    }

    //    全部
    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

    //    取出用户
    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

//    取出角色
    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }



    //角色 -- 权限
    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
//        取出用户遍历 jdk1.8支持
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }
}
