package com.future.javaweb;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoginSessionListener implements HttpSessionAttributeListener{

    public Log log = LogFactory.getLog(this.getClass());

    Map<String,HttpSession> map = new HashMap<String,HttpSession>();//保存session

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
    	//添加新Session属性时被调用
        String name = event.getName();//新建属性的名称
        if(name.equals("personInfo")) {//登陆
            PersonInfo personInfo = (PersonInfo) event.getValue();
            //创建新的PersonInfo
            if(map.get(personInfo.getAccount())!=null) {//若map中存在该账号
                HttpSession session = map.get(personInfo.getAccount());
                //账号所属session
                PersonInfo oldPersonInfo = (PersonInfo) session.getAttribute("personInfo");
                log.info("账号"+oldPersonInfo.getAccount()+"在"+oldPersonInfo.getIp()+"已经登陆，该登陆将被迫下线。");
                session.removeAttribute("personInfo");
                session.setAttribute("msg", "您的账号已经在其他机器上登陆，您被迫下线。");
            }
            //将session以用户名为索引，放入map中
            map.put(personInfo.getAccount(), event.getSession());
            log.info("账号"+personInfo.getAccount()+"在"+personInfo.getIp()+"登陆。");
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        //删除属性前被调用
        String name = event.getName();//被删除的属性
        if(name.equals("personInfo")) {//注销
            PersonInfo personInfo = (PersonInfo) event.getValue();
            //被移除的Person Info
            map.remove(personInfo.getAccount());
            log.info("账号"+personInfo.getAccount()+"注销。");
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        //修改属性时被调用
        String name = event.getName();
        if(name.equals("personInfo")) {
            PersonInfo oldPersonInfo = (PersonInfo) event.getValue();
            map.remove(oldPersonInfo.getAccount());//移除旧的登陆信息

            //新的登陆信息
            PersonInfo personInfo = (PersonInfo) event.getSession().getAttribute("personInfo");
            //检查新登陆的账号是否在别的机器上登陆过
            if(map.get(personInfo.getAccount())!=null) {
                //map中有记录，表明该账号在其他机器上登陆过，将以前的登陆失效
                HttpSession session = map.get(personInfo.getAccount());
                session.removeAttribute("personInfo");
                session.setAttribute("msg", "您的账号已经在其他机器上登陆，您被迫下线。");
            }
            map.put(personInfo.getAccount(), event.getSession());//没登录过，则放到map中
        }
    }

}
