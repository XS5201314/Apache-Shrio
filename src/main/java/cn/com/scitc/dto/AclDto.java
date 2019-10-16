package cn.com.scitc.dto;


import cn.com.scitc.model.SysAcl;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
/**
*@author xiaoxie
*@date  create 2019/9/17
*@return
 * 权限操作
*/
@Data
@ToString
public class AclDto extends SysAcl {

    // 是否要默认选中
    private boolean checked = false;

    // 是否有权限操作
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }
}
