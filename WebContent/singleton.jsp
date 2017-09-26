<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:directive.page import="com.future.javaweb.PersonInfo" />

<%
     String action = request.getParameter("action");
     String account = request.getParameter("account");

     if ("login".equals(action) && account.trim().length() > 0) {
          PersonInfo personInfo = new PersonInfo();
          
          personInfo.setAccount(account.trim().toLowerCase());
          personInfo.setIp(request.getRemoteAddr());
          personInfo.setLoginDate(new Date());
          session.setAttribute("personInfo", personInfo);//将登陆信息保存到session中

          response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
          return;
     } else if ("logout".equals(action)) {
          session.removeAttribute("personInfo");
          response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
          return;
     }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:choose>
		
		<c:when test="${personInfo != null }">
             	 欢迎您，${personInfo.account }。<br>
           		您登陆的IP为：${personInfo.ip }，<br>
              	登陆的时间为：<fmt:formatDate value="${personInfo.loginDate }"
				pattern="yyyy-mm-dd HH:mm" />。
              <a href="${pageContext.request.requestURI}?action=logout ">退出</a>
			<script>
                   setTimeout("location=location; ", 5000);
            </script>
		</c:when>
		
		<c:otherwise>
          ${msg }
          <c:remove var="msg" scope="session" />
			<form action="${pageContext.request.requestURI }?action=login" method="post">
				账号： <input name="account" /> <input type="submit" value="登录">
			</form>
		</c:otherwise>
	</c:choose>

</body>
</html>
