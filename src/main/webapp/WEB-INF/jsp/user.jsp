<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../jQuery/jQuery-1.7.1.js"></script>
<title>用户页面</title>
</head>
<script type="text/javascript">  
	function addOneUser() {
	   window.location.href = "/user/userList?name=a&age=1";
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
        用户名称： ${name} </br>
        添加新用户 ： </br>
   <form id="addUser" action="/user/addUser" method="post">     
       username: <input id="name" name="name" /><br/>     
       password:  <input id="password" name="password" /><br/>    
       <input type="submit" value="添加新用户"/>     
   </form> 
   ================================================</br>
   <input type="button" value="用户列表" onclick = "addOneUser()">
</body>
</html>