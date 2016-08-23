<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%String version = "20150701";%>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=8,IE=10" />
<link rel="icon" href="${basePath}/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${basePath}/favicon.ico" type="image/x-icon" />
<script type="text/javascript">
    var zy = zy || {};
    zy.contextPath = '${basePath}';
    zy.version = '<%=version%>';
    zy.pixel_0 = '${contextPath}/styles/ext/images/pixel_0.gif';//0像素的背景，一般用于占位

</script>
<script src="${contextPath}/plugins/jquery/jquery-1.11.1.js" type="text/javascript"></script>
<script src="${contextPath}/plugins/jquery/jquery.cookie.js" type="text/javascript"></script>
<script src="${contextPath}/plugins/scrollspy/bootstrap.js" type="text/javascript"></script>

<%-- 引入jquery扩展 --%>
<script src="${contextPath}/plugins/ext/extJquery.js?version=<%=version%>" type="text/javascript" charset="utf-8"></script>

<%-- 引入EasyUI --%>
<link id="easyuiTheme" rel="stylesheet" href="${contextPath}/plugins/easyui/themes/bootstrap/easyui.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/plugins/easyui/themes/icon.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/plugins/easyui/themes/color.css" type="text/css">


<link rel="stylesheet" href="${contextPath}/styles/ext/zyExtIcon.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/page.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/button.css" type="text/css">
<link id="skinTheme" rel="stylesheet" href="${contextPath}/styles/css/skin/bootstrap/skin.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/ftl.css" type="text/css">
<script type="text/javascript" src="${contextPath}/plugins/easyui/jquery.easyui.min.js" charset="utf-8"></script>
<%--<script type="text/javascript" src="${contextPath}/plugins/easyui/src/jquery.datebox.js" charset="utf-8"></script>--%>
<script type="text/javascript" src="${contextPath}/plugins/easyui/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>

<%-- 引入EasyUI --%>
<script type="text/javascript">
    /**
     * 记忆皮肤
     * 从父级页面加载皮肤
     * 如果父级页面不存在返回空
     * 加载皮肤
     */
    var skin = getPageSkin();
    loadPageSkin(skin);


    //取上级皮肤
    function getPageSkin() {
        if(window.parent==null)
            return '';
        //如果有更变主框架配色的到头
        if(window.parent.getMainSkin!=null){
            skin=window.parent.getMainSkin();
        }
        return skin;
    }


    //加载皮肤
    function loadPageSkin(skin){
        if(skin==null||skin=='')
            skin='yanshi';
        $("#easyuiTheme").attr("href",zy.contextPath +'/plugins/easyui/themes/'+skin+'/easyui.css');
        $("#skinTheme").attr("href",zy.contextPath +'/styles/css/skin/'+skin+'/skin.css');


        // 换掉内部的样式
        var iframes = document.getElementsByTagName('iframe');
        for(var i=0;i<iframes.length;i++){
            var content=iframes[i];
            if (content != null && content.contentWindow!=null&& content.contentWindow.loadPageSkin != null)
                content.contentWindow.loadPageSkin(skin);
        }
    }
    var insubmit = false;
    var deptname='${deptname}';
</script>

<%-- 引入easyui扩展 --%>
<script src="${contextPath}/plugins/ext/extEasyUI.js?version=<%=version%>" type="text/javascript" charset="utf-8"></script>
<%-- 引入plupload --%>
<script type="text/javascript" src="${contextPath}/plugins/jquery-upload/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${contextPath}/plugins/jquery-upload/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${contextPath}/plugins/jquery-upload/jquery.ajaxupload.3.6.js"></script>

<%-- 引入javascript扩展 --%>
<script src="${contextPath}/plugins/ext/extJavascript.js?version=<%=version%>" type="text/javascript" charset="utf-8"></script>
<script src="${contextPath}/js/index/html5.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextPath}/js/index/common.js" type="text/javascript" charset="utf-8"></script>

<style type="text/css">
    .datagrid-header-row,.datagrid-row {
        height: 40px;
    }
</style>