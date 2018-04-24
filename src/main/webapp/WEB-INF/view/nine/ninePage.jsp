<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<style type="text/css">
	    table
		{
		    border-collapse:collapse;
		}
	
	    td 
	    { 
	        border:1px solid; 
	    } 
	  .bottomBorder {border-bottom:2px solid;}
	  .rightBorder {border-right:2px solid;}
	</style>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>9*9</title>
</head>
<body>
<script type="text/javascript" src="../jQuery/jQuery-1.7.1.js"></script>
<script type="text/javascript" src="../js/nine/ninePage.js"></script>
<!-- <script type="text/javascript" src="../js/common/canvas-nest.min.js"></script> -->
<script type="text/javascript">
	window.onload=function(){
		initBorder();
	}
	function initBorder () {
		$("table#numtable tr:nth-child(3n)").addClass("bottomBorder");
		$("table").find("td").each(function(i){//搜寻表格里的每一个区间 
			if((i+1)%3 == 0){
				$(this).addClass("rightBorder");
			}//给区间加上特定样式 
		}); 
    }
</script>


    <form action="countshu" method="post" style="padding-left: 15px">
        <table frame="box" style = "border: 2px solid" id = "numtable">
            <c:forEach var="i" begin="1" end="9" step="1">
                <tr>
                    <c:forEach var="j" begin="1" end="9" step="1">
                        <td>
                            <input type="text" id="${i}${j}" name="${i}" onkeyup='this.value=this.value.replace(/\D/gi,"")' oninput="valueChange(this)" style="width: 25px;text-align: center;border:0px;">
                        </td>
                     </c:forEach>
                </tr>
            </c:forEach>
        </table>
        <span>
            <input type="button" name="tijiao" onclick="jisuan()" value="计算" >
            <input type="button" name="chongzhi" onclick="revert()" value="重置">
            <input type="button" name="chongzhi" onclick="clean()" value="清空">
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