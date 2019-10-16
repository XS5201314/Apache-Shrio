package cn.com.scitc.beans;
/**
*@author xiaoxie
*@date  create 2019/9/19
*@return
 * 日志类型
*/
public interface LogType {
//    跟新后部门
    int TYPE_DEPT = 1;
//    用户
    int TYPE_USER = 2;
//    模块
    int TYPE_ACL_MODULE = 3;
// 权限
    int TYPE_ACL = 4;

    //角色
    int TYPE_ROLE = 5;

//角色权限
    int TYPE_ROLE_ACL = 6;

//    角色用户
    int TYPE_ROLE_USER = 7;
}
