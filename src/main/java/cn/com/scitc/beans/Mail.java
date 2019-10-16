package cn.com.scitc.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;
/**
*@author xiaoxie
*@date create 2019/9/17
*@return
 * 邮件
*/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

//    邮件主题
    private String subject;

//    邮件信息
    private String message;

//    收件人
    private Set<String> receivers;


}
