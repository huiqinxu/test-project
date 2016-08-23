<%@ taglib prefix="text-align" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
  <title>工作日管理</title>
  <jsp:include page="/common/common.jsp"/>
    <style>
        *{
            font-size:12px;
            /*background: #f0f0f0;*/
        }
        .main-table{
          background: #f0f0f0;
        }
        .myTable th, .myTable td{


        }

    </style>
  <script type="text/javascript">
    $(function(){
        calendar();
    });


    //实现日历
    function calendar(year,month) {
        var today = new Date();
        if(year == '' || year == null){
             year = today.getFullYear();      //本年
             month = today.getMonth() + 1;    //本月
        }
        var day = today.getDate();           //本日
        //本月第一天是星期几（距星期日离开的天数）
        var startDay = new Date(year, month - 1, 1).getDay();
        //本月有多少天(即最后一天的getDate()，但是最后一天不知道，我们可以用“上个月的0来表示本月的最后一天”)
        var nDays = new Date(year, month, 0).getDate();
        var numpre = nDays+startDay;
//        alert(mid);

        //开始画日历
        var numRow = 0;  //记录行的个数，到达7的时候创建tr
        var i;           //日期



        //查询后台
        $.post(zy.contextPath+'/reportforms/getCurrentMonthDateByCurrentDate',{year:year,month:month},function(data){
            var Data=data;
            var currentYear = today.getFullYear();      //当前年
            var currentMonth = today.getMonth() + 1;    //当前月
            $("#Container").empty();
            var html = '';
            html += '<table class="myTable"><tbody>';
            //第一行
            html += '<tr height="80px">';
            for (i = 0; i < startDay; i++) {
                html += '<td style="background:#f0f0f0;"></td>';
                numRow++;
            }

            for (var j = 1; j <= nDays; j++) {
                if(j.toString().length == 1){
                    j = "0"+j;
                }
                if(month.toString().length ==1){
                    month = "0"+month;
                }
               var currentDay = year+""+month+""+j;

                //如果是今天则显示红色
//                if (j == day) {
//                    if(currentYear == year && currentMonth == month){
//                        html += '<td style="background:red" onclick="' + "changeworkday("+currentDay+");" + '" align="center" id="'+currentDay+'">';
//                        html += currentDay;
//                        html +='</br>'
//                        for(var key in Data) {
//                            if(Data[key].id == currentDay){
//                                if(Data[key].workday ==1){
//                                    html+= '<p>工作日</p>';
//                                }
//                                if(Data[key].workday ==0){
//                                    html+= '<p>休息日</p>';
//                                }
//                            }
//                        }
//                    }else{
//                        html += '<td onclick="' + "changeworkday("+currentDay+");" + '" align="center" id="'+currentDay+'">';
//                        html += currentDay;
//                        html +='</br>'
//                        for(var key in Data) {
//                            if(Data[key].id == currentDay){
//                                if(Data[key].workday ==1){
//                                    html+= '<p>工作日</p>';
//                                }
//                                if(Data[key].workday ==0){
//                                    html+= '<p>休息日</p>';
//                                }
//                            }
//                        }
//                    }
//                }
                //else {
                    var p = 0;
                    for(var key in Data) {
                        if(Data[key].id == currentDay){
                            if(Data[key].workday ==1){
                                html += '<td style="background:#ADFF2F" onclick="' + "changeworkday("+currentDay+");" + '" align="center" id="'+currentDay+'">';
                                //html += currentDay;
                                html += '<span style="font-size:30px;">'+j+'</span>';
                                html +='</br>'
                                html+= '<p>工作日</p>';
                                p++;
                            }
                            if(Data[key].workday ==0){
                                html += '<td style="background:#FF7F24" onclick="' + "changeworkday("+currentDay+");" + '" align="center" id="'+currentDay+'">';
                                html +=  '<span style="font-size:30px;">'+j+'</span>';
                                html +='</br>'
                                html+= '<p>休息日</p>';
                                p++;
                            }
                        }
                    }
                    if(p == 0){
                        html += '<td style="background:#FF7F24" onclick="' + "changeworkday("+currentDay+");" + '" align="center" id="'+currentDay+'">';
                        html += currentDay;
                        html +='</br>'
                        html+= '<p>休息日</p>';
                    }
               // }
                html += '</td>';
                numRow++;
                if (numRow == 7) {  //如果已经到一行（一周）了，重新创建tr
                    numRow = 0;
                    html += '</tr><tr height="80px">';
                }
            }
            if(numpre<35){
                for (var k = numpre+1; k <=35; k++) {
                    html += '<td style="background:#f0f0f0;"></td>';
                }
            }
            else if(numpre>35){
                for (var k = numpre+1; k <=42; k++) {
                    html += '<td style="background:#f0f0f0;"></td>';
                }

            }
            else{
                html += '';
            }




            html += '</tbody></table>';
            $("#Container").append(html);
//            var showDate = year  + "年" + month + "月" + day+ "日";
            var showDate = year  + "年" + month + "月" ;
            $("#showDate").text(showDate);
            $("#year").val(year);
            $("#month").val(month);
        },'json');
    }

    /**
     * 获取上个月
     */
    function getPreMonth() {
        var year = $("#year").val() ;
        var month = $("#month").val() ;
        var days = new Date(year, month, 0);
        days = days.getDate(); //获取当前日期中月的天数
        var year2 = year;
        var month2 = parseInt(month) - 1;
        if (month2 == 0) {
            year2 = parseInt(year2) - 1;
            month2 = 12;
        }
        if (month2 < 10) {
            month2 = '0' + month2;
        }
        calendar(year2,month2);
    }

    /**
     * 获取下个月
     */
    function getNextMonth() {
        var year = $("#year").val() ;
        var month = $("#month").val() ;
        var days = new Date(year, month, 0);
        days = days.getDate(); //获取当前日期中的月的天数
        var year2 = year;
        var month2 = parseInt(month) + 1;
        if (month2 == 13) {
            year2 = parseInt(year2) + 1;
            month2 = 1;
        }
        if (month2 < 10) {
            month2 = '0' + month2;
        }
        calendar(year2,month2);
    }


    //更改工作日
    function changeworkday(currentDay) {
        $.post(zy.contextPath+'/reportforms/changeworkday',{currentDay:currentDay},function(result){
            if(result.success == false){
                $.messager.alert('系统消息',result.msg,'error');
                return;
            }
            var text = $("#"+currentDay +" p").text();
            if(text == '工作日'){
                $("#"+currentDay).css("background","#FF7F24");
                $("#"+currentDay +" p").text('休息日');
            }else{
                $("#"+currentDay).css("background","#ADFF2F");
                $("#"+currentDay +" p").text('工作日');
            }

        },'json');
    }


  </script>

</head>
<body id="cc" class="easyui-layout"  style="font-size:30px;">
    <input type="hidden" id="year">
    <input type="hidden" id="month">
<div data-options="region:'center',border:false" id="myBody">
  <div class="main-table">
      <div class="toolbar datagrid-row-over" style="padding:10px 0px;">
        <div style="float:left;padding-left:5px;">
          <div class="content-head-title">
            <span class="label">当前位置：工作日选择</span>
          </div>
        </div>
      </div>
      <div>
          <table id="Calendar">
              <tr>
                  <td bgcolor="#78b3ed">
                      <table class="myTable">
                          <tbody>
                          <tr>
                              <td align="center" onclick="getPreMonth()">&lt;上一月</td>
                              <td align="center">
                                  <span id="showDate"></span>
                              </td>
                              <td align="center" onclick="getNextMonth()">&gt;下一月</td>
                          </tr>
                          </tbody>
                      </table>
                  </td>
              </tr>
              <tr>
                  <td>
                      <table class="myTable">
                          <tbody>
                              <tr>
                                  <th align="center">星期日</th>
                                  <th align="center">星期一</th>
                                  <th align="center">星期二</th>
                                  <th align="center">星期三</th>
                                  <th align="center">星期四</th>
                                  <th align="center">星期五</th>
                                  <th align="center">星期六</th>
                              </tr>
                          <tbody>
                      </table>
                  </td>
              </tr>
              <tr>
                  <td id="Container"></td>
              </tr>
          </table>
      </div>
  </div>
</div>
</body>
</html>
