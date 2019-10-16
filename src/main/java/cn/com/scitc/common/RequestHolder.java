package cn.com.scitc.common;

import cn.com.scitc.model.SysUser;

import javax.servlet.http.HttpServletRequest;
/**
*@author xiaoxie
*@date create 2019/9/17
*@return
 * 处理高并发
*/
public class RequestHolder {

//    线程处理
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<SysUser>();

//    登录
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

//    设置登陆用户
    public static void add(SysUser sysUser){
        userHolder.set(sysUser);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

//    处理当前用户
    public static SysUser getCurrentUser() {
        return userHolder.get();
    }

//    处理当前登录
    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

//    消亡
    public static void remove(){
        userHolder.remove();
        requestHolder.remove();
    }
}
