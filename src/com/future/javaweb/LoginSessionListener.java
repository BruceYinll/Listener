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

    Map<String,HttpSession> map = new HashMap<String,HttpSession>();//����session

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
    	//�����Session����ʱ������
        String name = event.getName();//�½����Ե�����
        if(name.equals("personInfo")) {//��½
            PersonInfo personInfo = (PersonInfo) event.getValue();
            //�����µ�PersonInfo
            if(map.get(personInfo.getAccount())!=null) {//��map�д��ڸ��˺�
                HttpSession session = map.get(personInfo.getAccount());
                //�˺�����session
                PersonInfo oldPersonInfo = (PersonInfo) session.getAttribute("personInfo");
                log.info("�˺�"+oldPersonInfo.getAccount()+"��"+oldPersonInfo.getIp()+"�Ѿ���½���õ�½���������ߡ�");
                session.removeAttribute("personInfo");
                session.setAttribute("msg", "�����˺��Ѿ������������ϵ�½�����������ߡ�");
            }
            //��session���û���Ϊ����������map��
            map.put(personInfo.getAccount(), event.getSession());
            log.info("�˺�"+personInfo.getAccount()+"��"+personInfo.getIp()+"��½��");
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        //ɾ������ǰ������
        String name = event.getName();//��ɾ��������
        if(name.equals("personInfo")) {//ע��
            PersonInfo personInfo = (PersonInfo) event.getValue();
            //���Ƴ���Person Info
            map.remove(personInfo.getAccount());
            log.info("�˺�"+personInfo.getAccount()+"ע����");
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        //�޸�����ʱ������
        String name = event.getName();
        if(name.equals("personInfo")) {
            PersonInfo oldPersonInfo = (PersonInfo) event.getValue();
            map.remove(oldPersonInfo.getAccount());//�Ƴ��ɵĵ�½��Ϣ

            //�µĵ�½��Ϣ
            PersonInfo personInfo = (PersonInfo) event.getSession().getAttribute("personInfo");
            //����µ�½���˺��Ƿ��ڱ�Ļ����ϵ�½��
            if(map.get(personInfo.getAccount())!=null) {
                //map���м�¼���������˺������������ϵ�½��������ǰ�ĵ�½ʧЧ
                HttpSession session = map.get(personInfo.getAccount());
                session.removeAttribute("personInfo");
                session.setAttribute("msg", "�����˺��Ѿ������������ϵ�½�����������ߡ�");
            }
            map.put(personInfo.getAccount(), event.getSession());//û��¼������ŵ�map��
        }
    }

}
