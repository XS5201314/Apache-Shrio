package cn.com.scitc.service;

import cn.com.scitc.beans.PageQuery;
import cn.com.scitc.beans.PageResult;
import cn.com.scitc.common.RequestHolder;
import cn.com.scitc.dao.SysUserMapper;
import cn.com.scitc.exception.ParamException;
import cn.com.scitc.model.SysUser;
import cn.com.scitc.param.UserParam;
import cn.com.scitc.util.BeanValidator;
import cn.com.scitc.util.IpUtil;
import cn.com.scitc.util.MD5Util;
import cn.com.scitc.util.PasswordUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
*@author xiaoxie
*@date create 2019/9/16
*@return
 * 用户服务
*/
@Service
@Slf4j
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;

    public void save(UserParam param) {

        try {
            BeanValidator.check(param);
            if(checkTelephoneExist(param.getTelephone(), param.getId())) {
                throw new ParamException("电话已被占用");
            }
            if(checkEmailExist(param.getMail(), param.getId())) {
                throw new ParamException("邮箱已被占用");
            }
        }catch (Exception e){
            log.error("system update save error"+e);
        }
        String password = PasswordUtil.randomPassword();
        //TODO:
        password = "12345678";
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser user = SysUser.builder().username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .password(encryptedPassword).deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
//        取出操作人的IP
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        // TODO: sendEmail

        sysUserMapper.insertSelective(user);
        sysLogService.saveUserLog(null,user);
    }

    public void update(UserParam param) {
        try {
            BeanValidator.check(param);
            if(checkTelephoneExist(param.getTelephone(), param.getId())) {
                throw new ParamException("电话已被占用");
            }
            if(checkEmailExist(param.getMail(), param.getId())) {
                throw new ParamException("邮箱已被占用");
            }
        }catch (Exception e){
            log.error("system update user error"+e);
        }

        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername()).telephone(param.getTelephone()).mail(param.getMail())
                .deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveUserLog(before,after);

    }

    public boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }


//    分页查询
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page) {
        try {
            BeanValidator.check(page);
            int count = sysUserMapper.countByDeptId(deptId);
            if (count > 0) {
                List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
                return PageResult.<SysUser>builder().total(count).data(list).build();
            }
        }catch (Exception e){
            log.warn("page error "+e);
        }
        return PageResult.<SysUser>builder().build();
    }


    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }
}
