
//用户协议隐私多语言切换
function tourl(lanname,hname){

	//修改链接页面地址
	var lanurl = lanname+"/"+hname+".html"
	parent.$("#FrameNav").attr("src", lanurl);
	
	//修改页面标题
	var $mainFrame=$('#FrameNav');
	setTimeout(function () { 
        console.log($mainFrame.contents().attr("title"));	
        lanName = $mainFrame.contents().attr("title");
        $(document).attr("title",lanName);
    }, 1000);
}

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}