<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../jQuery/jQuery-1.7.1.js"></script>
<title>用户列表页</title>
</head>
<script type="text/javascript">  
	function addOneUser() {
	   window.location.href = "/user/userList";
// 	   $.ajax({  
//            type:'get',  
//            url:'/user/addOneUser',  
//            data:{
//         	   "name" : "a",
//         	   "age" : 1
//            },  
//            dataType:'json',  
//            success:function(data){  
//                alert(data);  
//            }  
//        });
	}
</script>
<body>
<table border="1">
    <tr>
            <td>id</td>
            <td>name</td>
            <td>age</td>
            <td>password</td>
            <td>action</td>
        </tr>
    <c:forEach items="${userList}" var="user">
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.age}</td>
            <td>${user.password}</td>
            <td><a href="/user/deleteUser?id=${user.id}" >delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>