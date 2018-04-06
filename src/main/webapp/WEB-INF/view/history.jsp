<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
    <title>用户页面</title>
</head>
<body>
<script type="text/javascript" src="../jQuery/jQuery-1.7.1.js"></script>
<script type="text/javascript">
    function ceshi () {
        var rows = "";
        var longnumber = "";
        var haveNumber = null;
        for (var i = 1 ; i < 4 ; i++) {
            rows =  document.getElementsByName(i);
            for (var j = 0; j < rows.length ; j++) {
                var number = rows[j].value;
                if (number == "") {
                    number = 0;
                } else {
                    haveNumber = document.getElementById( i + "," + (j + 1));
                    haveNumber.disabled = "disabled";
                    haveNumber.style.backgroundColor  = "#3fad";
                }
                if (j == rows.length - 1) {
                    longnumber += number;
                } else {
                    longnumber += number + ",";
                }
            }

            longnumber += ";";
        }

        $.ajax({
            type:'post',
            url:'countshu',
            // data:$("#myform").serialize(),
            data: {
                "longnumber" : longnumber
            },
            cache:false,
            dataType:'json',
            success:function(data){
                alert(data);
            }
        });
    }
    function revert () {
        for (var i = 1 ; i < 4 ; i++) {
            var rows =  document.getElementsByName(i);
            for (var j = 0; j < rows.length ; j++) {
                var haveNumber = rows[j];
                haveNumber.value = "";
                haveNumber.disabled = "";
                haveNumber.style.backgroundColor  = "#fff";
            }
        }
    }
</script>


    <form action="countshu" method="post" style="padding-left: 15px">
        <table border="1px">
            <c:forEach var="i" begin="1" end="3" step="1">
                <tr>
                    <c:forEach var="j" begin="1" end="3" step="1">
                        <td>
                            <input type="text" id="${i},${j}" name="${i}" style="width: 25px;text-align: center">
                        </td>
                     </c:forEach>
                </tr>
            </c:forEach>
        </table>
        <span>
            <input type="button" name="tijiao" onclick="ceshi()" value="提交" >
            <input type="button" name="chongzhi" onclick="revert()" value="重置">
        </span>
    </form>
    </br>
    <span><a href="history">return</a></span></br>
    </br>

    <%-- <span style="float: left;">perfect</span></br>
        <#list 0..(perfectList?size)-1 as index >
        <#if index != 0 && index%6 == 0>
            </br></br></br></br>
        </#if>
        <span style="float: left;padding-left: 2px;" name="history${index}">
            <table border="1px">
                <#list 0..(perfectList[index]?size)-1 as row>
                    <tr style="width: 25px;">
                        <#list 0..(perfectList[index][row]?size)-1 as line>
                            <td style="width: 25px;text-align: center" >
                                <span>${perfectList[index][row][line]}</span>
                            </td>
                        </#list>
                    </tr>
                </#list>
            </table>
        </span>
    </#list>
    </br></br></br></br></br>

    <span style="float: left;">history</span></br>
    <#list 0..(list?size)-1 as index >
        <#if index != 0 && index%6 == 0>
            </br></br></br></br>
        </#if>
        <span style="float: left;padding-left: 2px;" name="history${index}">
            <table border="1px">
                <#list 0..(list[index]?size)-1 as row>
                    <tr style="width: 25px;">
                        <#list 0..(list[index][row]?size)-1 as line>
                            <td style="width: 25px;text-align: center" >
                                <span>${list[index][row][line]}</span>
                            </td>
                        </#list>
                    </tr>
                </#list>
            </table>
        </span>
    </#list> --%>
</body>
</html>