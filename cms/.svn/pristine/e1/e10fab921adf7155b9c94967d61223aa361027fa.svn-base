//初始化
$(document).ready(function() {
	var windowWidth = $(window).width();
	var windowHeight = $(window).height();
	var width = windowWidth;
	var height = windowHeight-15;

	/*var margin_left = Math.ceil((width-1520)/2);
	margin_left=margin_left<=0?10:margin_left;
		
	width = width>1320?width:1320;
	width = 1320;*/
	
	width = width-21;
//	width = width>800?width:800;
	var margin_left = 10;
	
	$("#mainDiv").css({"width":width,"height":height,"margin-left":margin_left});
	
	$.parser.parse();
	
	loginDecide();
});

function loginDecide(){
	$.ajax({
		url : "../LoginWS/decideLogin.do",
		type : "POST",
		data : {},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#mainShadeDiv").remove();
				currentUserFunctions = data.content.securityFunctionList;
				currentUserButtons = data.content.buttonList;
				currentUserOpers = data.content.userOperList;
				currentUserUsername = data.content.securityLogin.username;
				currentUserNickname = data.content.securityLogin.nickName;
				currentUserPossessor = data.content.securityLogin.possessor;
				initSkin();
				initHelp();
				initTabs();
				initCache();
				//setInterval(getLogWarn,1000*60*5);
				//showNotice("big");
				//decideAlterPassword();
				initRemark();
				$("#currentUser").text(currentUserNickname);
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
				window.location.href = "../manager.html";
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function initCache(){
	var myCache = xyzGetUserOpers("myCache");
	if(xyzIsNull(myCache)){
		var u_agent = navigator.userAgent;
		var url = "";
		var score = 0;
		if(u_agent.indexOf('Firefox')>-1){
			url = "../image/mainFrame/CACHEFF.png";
			score++;
		}
		if(u_agent.indexOf('Chrome')>-1){
			url = "../image/mainFrame/CACHEGG.png";
			score++;
		}
		if(u_agent.indexOf('MSIE')>-1){
			url = "../image/mainFrame/CACHEIE.png";
			score++;
		}
		if(u_agent.indexOf('Safari')>-1){
			score++;
		}
		if(score>1){
			url = "../image/mainFrame/CACHE360.png";
		}
		xyzdialog({
			dialog : 'dialogFormDiv_initCache',
			title : "清理缓存提示",
			fit:false,
			width:500,
			height:650,
			content:'<H1 style="color:red;">请同时按住ctrl+shift+delete并参照下图清理浏览器缓存</H1><div><img src="'+url+'"></img></div>',
		    buttons:[{
				text:'已清理',
				iconCls:'icon-ok',
				handler:function(){
					$("#dialogFormDiv_initCache").dialog("destroy");
					window.location.href = window.location.href;
				}
			},{
				text:'不要你管',
				handler:function(){
					$("#dialogFormDiv_initCache").dialog("destroy");
				}
			}]
		});
		$(window).keydown(function(e){
			if(e.ctrlKey && e.shiftKey && e.which==46){
				$.ajax({
					url : "../UserOperWS/addUserOper.do",
					type : "POST",
					data : {
						keyCode : "myCache",
						content : "yes"
					},
					async : false,
					dataType : "json",
					success : function(data) {
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
					}
				});
			}
		});
		
	}
}

function initTabs(){
	//初始化选项卡的工具栏
	{
		$("#centerContentTabs").tabs({
			toolPosition : 'right',
			tools:[{
				id : 'xyzasdasdsddsadsadasdg',
		        iconCls:'icon-no',
		        handler:function(){
		        	var tab = $('#centerContentTabs').tabs('getSelected');
		        	if(!tab){
		        		return;
		        	}
		        	var currentTitle = tab.panel("options").title;
		        	
		            var tabs = $("#centerContentTabs").tabs('tabs');
		            var tabsTitles = new Array(tabs.length);
		            var titleC = currentTitle;
		            for(var i=0;i<tabs.length;i++){
		            	var titleT = tabs[i].panel("options").title;
		            	tabsTitles[i] = titleT;
		            }
		            for(var i=0;i<tabsTitles.length;i++){
		            	var titleT = tabsTitles[i];
		            	if(titleT!=titleC){
		            		$("#centerContentTabs").tabs('close',titleT);
		            	}
		            }
		        }
		    },{
		    	id : 'xyzasdjgashdga3eydyweada',
		        iconCls:'icon-reload',
		        handler:function(){ 
		        	var tab = $('#centerContentTabs').tabs("getSelected");
		        	if(!tab){
		        		return;
		        	}
		        	var id = tab.panel("body").find("iframe").attr("id");
		        	var url = tab.panel("body").find("iframe").attr("src");
		        	$("#"+id).attr("src",url);
		        }
		    }],
			onSelect:function(title,index){
				for(var i=0;i<currentUserFunctions.length;i++){
					var f = currentUserFunctions[i];
					if(f.nameCn==title){
						$("#helpGreatDiv").accordion("select",f.groupCn);
						$("#helpGreatDiv_"+f.groupCn).accordion("select",f.nameCn);
					}
				}
			},
			onClose : function(){
				var tabsLength = $('#centerContentTabs').tabs("tabs").length;
				if(tabsLength==0){
					$("#promptDiv").show();
				}
			}
		});
		
		$("#xyzasdasdsddsadsadasdg").tooltip({
			position: 'bottom',    
			content: '关闭所有未选中的选项卡'
		});
		
		$("#xyzasdjgashdga3eydyweada").tooltip({
			position: 'bottom',
			content: '刷新当前选项卡的工作面板'
		});
	}
}

function initSkin(){
	$('#skinMenuForMenubutton').menu({
		onClick : function(item){
			var  skinTemp = item.id.substr(5,1000);
			$.ajax({
				url : "../UserOperWS/addUserOper.do",
				type : "POST",
				data : {
					keyCode : "mySkin",
					content : skinTemp
				},
				async : false,
				dataType : "json",
				success : function(data) {
					window.location.href=window.location.href;
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		},
		onShow : function(){
			var currentSkinTemp = $("#currentSkin").val();
			$("div[id^='skin_']").each(function(i){
				var itemEl = $("#"+this.id)[0];
				var item = $('#skinMenuForMenubutton').menu('getItem', itemEl);
				if(this.id==("skin_"+currentSkinTemp)){
					$('#skinMenuForMenubutton').menu('setIcon', {
						target: item.target,
						iconCls: 'icon-ok'
					});
				}else{
					$('#skinMenuForMenubutton').menu('setIcon', {
						target: item.target,
						iconCls:''
					});
				}
			});
		}
	});
	
	$('#skinMenubutton').menubutton({
		menu:'#skinMenuForMenubutton',
		iconCls:'icon-large-picture'
	});
	
	$('#otherMenubutton').menubutton({
		menu:'#otherMenuForMenubutton',
		iconCls:'icon-large-clipart'
	});

	
	var mySkin = xyzGetUserOpers("mySkin");
	if(!xyzIsNull(mySkin)){
		$("#currentSkin").val(mySkin);
		
		var mySkinColorTemp={
			"black":"#0052A3",
			"bootstrap":"#0081C2",
			"default":"#5992d9",
			"gray":"#0092DC",
			"material":"#00BBEE",
			"metro":"#5992d9",
			"metro-blue":"#6CAEF5",
			"metro-gray":"#84909C",
			"metro-green":"#C8D47B",
			"metro-orange":"#F7CC8F",
			"metro-red":"#F09090",
			"ui-cupertino":"#3BAAE3",
			"ui-dark-hive":"#0972A5",
			"ui-pepper-grinder":"#B83400",
			"ui-sunny":"#817865"
		};
		if(!xyzIsNull(mySkinColorTemp[mySkin])){
			$("#southDivForFrame").css({"background-color":mySkinColorTemp[mySkin]});
			$("#northDivForFrame").css({"background-color":mySkinColorTemp[mySkin]});
		}
		setSystemSkin();
	}
}

function safeQuit(){
	$.ajax({
		url : "../LoginWS/safeQuit.do",
		type : "POST",
		data : {},
		async : false,
		dataType : "json",
		success : function(data) {
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	deleteLoginCookie();
	window.location.href = "../manager.html";
}

function initHelp(){
	for(var i=0;i<currentUserFunctions.length;i++){
		var f = currentUserFunctions[i];
		if(f.isApi==1){
			continue;
		}
		var groupCn = f.groupCn;
		if($("#helpGreatDiv").accordion("getPanel",f.groupCn)){
			;
		}else{
			$("#helpGreatDiv").accordion("add",{
				title:groupCn,
				selected: true,
				collapsible:true,
				content:'<div id="helpGreatDiv_'+groupCn+'" style="padding: 5px;"></div>'
			});
			$('#helpGreatDiv_'+groupCn).accordion({
				fit:true,
				animate:false,
				selected:100
			});
		}
		
		var nameCn = f.nameCn;
		if($('#helpGreatDiv_'+f.groupCn).accordion("getPanel",nameCn)){
			;
		}else{
			$('#helpGreatDiv_'+f.groupCn).accordion("add",{
				title:nameCn,
				selected: false,
				onExpand:function(){
					var title = $(this).panel("options").title;
					var numberCode = "";
					var url = "";
					for(var i=0;i<currentUserFunctions.length;i++){
						if(currentUserFunctions[i].nameCn==title){
							numberCode = currentUserFunctions[i].numberCode;
							url = ".."+currentUserFunctions[i].url;
							break;
						}
					}
					$("#promptDiv").hide();
					if($("#helpGreatDiv").accordion("getSelected")){
						titleBig = $("#helpGreatDiv").accordion("getSelected").panel("options").title;
					}
					var iframeid = "contentIframe_"+numberCode;
					if($('#centerContentTabs').tabs("exists",title)){
						$('#centerContentTabs').tabs("select",title);
						return;
					}
					$('#centerContentTabs').tabs('add',{
					    title:title,
					    content:"<iframe id='"+iframeid+"' name='centerContentIframe' scrolling='auto' frameborder='0' style='overflow-x:hidden; overflow-y:auto;'></iframe>",
					    closable:true
					});
					var tempWidth = $("#centerContentTabs").css("width");
					var tempHeight = $("#centerContentTabs").css("height");
					var tempWidth2 = parseInt(tempWidth.split("px")[0]);
					var tempHeight2 = parseInt(tempHeight.split("px")[0]);
					$("#"+iframeid).css("width",(tempWidth2)+"px");
					$("#"+iframeid).css("height",(tempHeight2-40)+"px");
					$("#"+iframeid).attr("src",url);
				}
			});
		}
	}
	//选中一级目录的时候让二级目录自动归位
	$("#helpGreatDiv").accordion({
		onSelect : function(title,index){
			var p = $('#helpGreatDiv_'+title).accordion('getSelected');
			if (p){
				var index = $('#helpGreatDiv_'+title).accordion('getPanelIndex', p);
				$('#helpGreatDiv_'+title).accordion("unselect",index);
			}
		}
	});
}

function initRemark(){
	$("#remarkGreatDiv").accordion("add",{
		title:"通用备注",
		selected: false,
		collapsible:true,
		content:'<span id="remarkTop"></span>'
	});
	if (xyzControlButton("buttonCode_s20161209095402") || xyzControlButton("buttonCode_s20161209095403")) {
		var orderCoreRemarkTopDiv = '<input type="hidden" id="orderNumTop"/>';
		orderCoreRemarkTopDiv += '<div><span id="orderCoreRemarkTop" style="line-height: 18px;word-spacing: 10px;"></span></div>';
		orderCoreRemarkTopDiv += '<div style="margin-left: 3px;">【<span id="remarkOrderNum"></span>】</div>';
		orderCoreRemarkTopDiv += '<div style="margin-left: 3px;">';
		orderCoreRemarkTopDiv += '	<textarea style="width: 150px" id="orderCoreRemarkNewTop" rows="5" cols="12" class="easyui-validatebox" data-options="required:true,validType:\'length[0,1000]\'"></textarea>';
		orderCoreRemarkTopDiv += '</div>';
		orderCoreRemarkTopDiv += '<div style="margin-left: 3px;"><a href="#" onclick="addOrderCoreRemark();">提交新增备注</a></div>';
		$("#remarkGreatDiv").accordion("add",{
			title:"订单备注",
			selected: false,
			collapsible:true,
			content:orderCoreRemarkTopDiv
		});
	}
}

function addOrderCoreRemark(){
	var orderNum = $("#orderNumTop").val();
	var remark = $("#orderCoreRemarkNewTop").val();
	if(xyzIsNull(orderNum)){
		top.$.messager.alert("警告","目标对象为空，不能提交！","warning");
		return;
	}
	if(xyzIsNull(remark)){
		top.$.messager.alert("警告","新增备注为空，不能提交！","warning");
		return;
	}
	$.ajax({
		url : "../OrderContentWS/addOrderContentRemark.do",
		type : "POST",
		data : {
			orderNum : orderNum,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","新备注提交成功!","info");
				
				var newStr = "<span title='"+'修改人：'+currentUserUsername+'修改日期'+new Date().Format("yyyy-MM-dd hh:mm:ss")+"' href='#'>【"+remark+"】"+"</span><br/>";
				
				var tt = $("#orderCoreRemarkTop").html();
				tt += newStr;
				
				$("#orderCoreRemarkTop").html(tt);
				$("#orderCoreRemarkNewTop").val("");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addCustomerConsultRemark(){
	var numberCode=$("#customerConsultNumTop").val();
	var remark=$("#customerConsultRemarkNewTop").val();
	var dateInfo=$("#dateInfo").datebox("getText");
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("警告","目标对象为空，不能提交！","warning");
		return;
	}
	if(xyzIsNull(remark)){
		top.$.messager.alert("警告","新增备注为空，不能提交！","warning");
		return;
	}
	$.ajax({
		url : "../CustomerConsultWS/addCustomerRemark.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			remark:remark,
			dateInfo:dateInfo
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功！","info");
				
				var newStr = "<span title='"+'修改人：'+currentUserUsername+'修改日期'+new Date().Format("yyyy-MM-dd hh:mm:ss")+"' href='#'>【"+remark+"】"+"</span><br/>";

				var tt = $("#customerConsultRemarkTop").html();
				tt += newStr;
				$("#customerConsultRemarkTop").html(tt);
				$("#customerConsultRemarkNewTop").val("");
				$("#nextScheduledTime").datebox("setValue",null);
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function addCustomerContinueRemark(){
	var numberCode=$("#customerContinueNumTop").val();
	var remark=$("#customerContinueRemarkNewTop").val();
	var dateInfo=$("#customerContinueDateInfo").datebox("getText");
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("警告","目标对象为空，不能提交！","warning");
		return;
	}
	if(xyzIsNull(remark)){
		top.$.messager.alert("警告","新增备注为空，不能提交！","warning");
		return;
	}
	
	$.ajax({
		url : "../CustomerContinueWS/addCustomerRemark.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			dateInfo:dateInfo,
			remark:remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示","操作成功！","info");
				
				var newStr = "<span title='"+'修改人：'+currentUserUsername+'修改日期'+new Date().Format("yyyy-MM-dd hh:mm:ss")+"' href='#'>【"+remark+"】"+"</span><br/>";

				var tt = $("#customerContinueRemarkTop").html();
				tt += newStr;
				$("#customerContinueRemarkTop").html(tt);
				$("#customerContinueRemarkNewTop").val("");
				$("#customerContinueDateInfo").datebox("setValue",null);
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}