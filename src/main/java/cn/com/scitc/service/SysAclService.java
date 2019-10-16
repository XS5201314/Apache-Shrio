package cn.com.scitc.service;

import cn.com.scitc.beans.PageQuery;
import cn.com.scitc.beans.PageResult;
import cn.com.scitc.common.RequestHolder;
import cn.com.scitc.dao.SysAclMapper;
import cn.com.scitc.exception.ParamException;
import cn.com.scitc.model.SysAcl;
import cn.com.scitc.param.AclParam;
import cn.com.scitc.util.BeanValidator;
import cn.com.scitc.util.IpUtil;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
*@author xiaoxie
*@date create 2019/9/17
*@return
 * 权限模块点
*/
@Service
@Slf4j
public class SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

    public void save(AclParam param){
        try {
            BeanValidator.check(param);
            if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
                throw new ParamException("当前权限模块下面存在相同名称的权限点");
            }
            SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                    .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
            acl.setCode(generateCode());
            acl.setOperator(RequestHolder.getCurrentUser().getUsername());
            acl.setOperateTime(new Date());
            acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            sysAclMapper.insertSelective(acl);
            sysLogService.saveAclLog(null,acl);

        }catch (Exception e){
            log.warn("SysAclService error :" +e);
        }
    }


    public void update(AclParam param){
        try {
            BeanValidator.check(param);
            if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
                throw new ParamException("当前权限模块下面存在相同名称的权限点");
            }
            SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
            Preconditions.checkNotNull(before, "待更新的权限点不存在");

            SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                    .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
            after.setOperator(RequestHolder.getCurrentUser().getUsername());
            after.setOperateTime(new Date());
            after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

            sysAclMapper.updateByPrimaryKeySelective(after);
            sysLogService.saveAclLog(before,after);
        }catch (Exception e){
            log.warn("SysAclService error :" +e);
        }
    }




    public boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

//    拼接
    public String generateCode(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }

//    分页
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId , PageQuery page){
        try {
            BeanValidator.check(page);
            int count = sysAclMapper.countByAclModuleId(aclModuleId);
            if (count > 0) {
                List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, page);
                return PageResult.<SysAcl>builder().data(aclList).total(count).build();
            }
        }catch (Exception e){
            log.warn("PageResult :"+e);
        }
        return PageResult.<SysAcl>builder().build();
    }

}
