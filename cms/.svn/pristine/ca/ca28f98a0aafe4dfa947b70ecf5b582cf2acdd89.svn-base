$(document).ready(function() {
	initTable();
	
	xyzTextbox("numberCode");
	xyzTextbox("nameCn");
	
	$("#channelQueryButton").click(function() {
		loadTable();
	});
});

function initTable(){
	var toolbar = [];
	if (xyzControlButton("buttonCode_y20161209094201")) {
		toolbar[toolbar.length] = {
			text : '新增',
			border : '1px solid #bbb',
			iconCls : 'icon-add',
			handler : function() {
				addChannelButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_y20161209094202")) {
		toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editChannelButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_y20161209094203")) {
		toolbar[toolbar.length] = {
			text : '删除',
			border : '1px solid #bbb',
			iconCls : 'icon-remove',
			handler : function() {
				deleteChannelButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_y20171106115301")) {
		toolbar[toolbar.length] = {
			text : '更新到系统内存',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editSystemMemory();
			}
		};
	}

	xyzgrid({
		table : 'channelManagerTable',
		title : "渠道列表",
		url : '../ChannelWS/queryChannelList.do',
		toolbar : toolbar,
		fit : true,
		singleSelect : true,
		idField : 'numberCode',
		//queryParams : {},
		columns : [ [
			{field:'checkboxTemp',checkbox:true,hidden:true},
			{field:'numberCode',title:'渠道编号',align:'center',hidden:true},
			{field:'nameCn',title:'渠道名称',halign:'center',
				formatter:function(value ,row ,index){
		    		 return xyzGetDiv(value ,0 ,50);
		    	 }
			},
			{field:'authorize',title:'授权',align:'center',
				formatter:function(value ,row ,index){
					var isAuthorize = row.isAuthorize;
					var authorize = "";
					if(isAuthorize == 0) {
                        authorize = "×";
                    }else{
                        authorize = "√";
                    }
					if(xyzControlButton("buttonCode_y20161209094205")){
						if(isAuthorize == 0){
							authorize += " "+xyzGetA("授权","setAuthorizeButton",[row.numberCode,row.code,row.appKey,row.redirectUri],"点我授权!","");
						}
					}
					return authorize;
				},
                styler:function(value, row, index) {
                	var isAuthorize = row.isAuthorize;
                    if(isAuthorize == 1) {
                       return "background-color:green";
                    } 
                    return "background-color:red";
                }
			},
			{field:'priceStrategy',title:'操作',halign:'center',
				formatter:function(value ,row ,index){
					var html = "";
					if(xyzControlButton("buttonCode_y20171104115601")){
						html +=xyzGetA("加价设置","priceStrategy",[row.numberCode,row.nameCn],"","");
					}else{
						html += " ";
					}
					return html;
		    	 }
			},
		    {field:'remark',title:'备注',
				formatter:function(value ,row ,index){
		    		 return xyzGetDiv(value ,0 ,200);
		    	 }
			},
			{field:'addDate',title:'添加时间',width:70,hidden:true,align:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时间',align:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			}
		] ]
	});
}

function loadTable(){
	var numberCode = $("#numberCode").val();
	var nameCn = $("#nameCn").val();

	$("#channelManagerTable").datagrid('load', {
		numberCode : numberCode,
		nameCn : nameCn
	});
}

function addChannelButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addChannelButton',
		title : '新增渠道',
	    href : '../jsp_resources/addChannel.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				addChannelSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addChannelButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("appSecretForm");
			xyzTextbox("codeForm");
			xyzTextbox("appKeyForm");
			xyzTextbox("redirectUriForm");
			xyzTextbox("remarkForm");
		}
	});
}

function addChannelSubmit(){
	if(!$("form").form('validate')){
		return false;
	}

	var code = $("#codeForm").val();
	var nameCn = $("#nameCnForm").val();
	var appKey = $("#appKeyForm").val();
	var appSecret = $("#appSecretForm").val();
	var redirectUri = $("#redirectUriForm").val();
	var remark = $("#remarkForm").val();
	$.ajax({
		url : "../ChannelWS/addChannel.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			code : code,
			appKey : appKey,
			appSecret : appSecret,
			redirectUri : redirectUri,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#channelManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addChannelButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editChannelButton(){
	var channel = $("#channelManagerTable").datagrid("getChecked");
	if(channel.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = channel[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editChannelButton',
		title : '编辑渠道【'+ row.nameCn +'】',
	    href : '../jsp_resources/editChannel.html',
	    fit : false,  
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				editChannelSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editChannelButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			
			if(row.authorize == 0){
				$("#authorizeForm").html("未授权");
			}else{
				$("#authorizeForm").html("已授权");
			}
			$("#nameCnForm").textbox("setValue",row.nameCn);
			$("#remarkForm").val(row.remark);
			
			$("#nameCnForm").textbox({    
			    required: true
			});  
		}
	});
}

function editChannelSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../ChannelWS/editChannel.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#channelManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editChannelButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteChannelButton(){
	var channels = $.map($("#channelManagerTable").datagrid("getChecked"),function(p){return p.numberCode;}).join(",");
	
	if(xyzIsNull(channels)){
		top.$.messager.alert("提示","请先勾选需要删除的对象！","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
		  $.ajax({
			url : "../ChannelWS/deleteChannel.do",
			type : "POST",
			data : {
				numberCodes : channels
			},
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					$("#channelManagerTable").datagrid("reload");
					top.$.messager.alert("提示","操作成功!","info");
				}else{
					top.$.messager.alert("警告",data.msg,"warning");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
			}
		}); 
	 }
 });
}

function setPossessorButton(numberCode,possessor){
	
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","参数错误！","info");
		return;
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_setPossessorButton',
		title : '设置机构',
	    href : '../jsp_resources/editPossessorByChannel.html',
	    fit : false,  
	    width: 400,
	    height : 300,
	    buttons:[{
			text:'确定',
			handler:function(){
				setPossessorSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_setPossessorButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			
			xyzCombobox({
				combobox:'possessorForm',
				url:'../ListWS/getPossessor.do',
				required:true,
				mode:'remote',
				lazy:false
			});
			
			$("#possessorForm").combobox('setValue',possessor);
		}
	});
	
}

function setPossessorSubmit(numberCode){
	
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","参数错误！","info");
		return;
	}
	
	var possessor = $("#possessorForm").combobox('getValue');
	
	$.ajax({
		url : "../ChannelWS/editPossessorByChannel.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			possessor : possessor
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#channelManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_setPossessorButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function setAuthorizeButton(numberCode,code,appKey,redirectUri){
	
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","参数错误！","info");
		return;
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_setAuthorizeButton',
		title : '设置机构',
	    href : '../jsp_resources/setChannelAuthorize.html',
	    fit : false,  
	    width: 400,
	    height : 300,
	    buttons:[{
			text:'确定',
			handler:function(){
				setAuthorizeSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_setAuthorizeButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			
			$("#codeForm").val(code);
			$("#appKeyForm").val(appKey);
			$("#redirectUriForm").val(redirectUri);
			
			$('#codeForm').validatebox({    
			    required: true,
			    validType : 'length[2,500]'
			}); 
			$('#appKeyForm').validatebox({    
			    required: true,
			    validType : 'length[2,500]'
			}); 
			$('#redirectUriForm').validatebox({    
			    required: true,
			    validType : 'length[2,500]'
			}); 
			
			$("#getCode").click(function() {
				
				var appKey = $("#appKeyForm").val();
				var url = $("#redirectUriForm").val();
				
				var codeUrl = "https://oauth.taobao.com/authorize?";
					codeUrl += "response_type=code&client_id="+appKey+"&redirect_uri="+url+"";
					codeUrl += "&state=1212&view=web";
					window.open(codeUrl);
			});
			
		}
	});
	
}

function setAuthorizeSubmit(numberCode){
	
	if(xyzIsNull(numberCode)){
		top.$.messager.alert("提示","参数错误！","info");
		return;
	}
	
	var code = $("#codeForm").val();
	var appKey = $("#appKeyForm").val();
	var url = $("#redirectUriForm").val();
	
	$.ajax({
		url : "../ChannelWS/setAuthorizeOper.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			code : code,
			appKey : appKey,
			url : url
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#channelManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_setAuthorizeButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}
function priceStrategy(numberCode,nameCn) {
	$.ajax({
		url : '../ChannelWS/queryChannlListByNumberCode.do',
		type : "POST",
		data : {
			numberCode : numberCode
		},
		dataType : 'json',
		async : false,
		success : function(data) {
			if (data.status == 1) {
				var dataJson  = eval(data.content.priceStrategy) ;
				var content = "<div style='margin-top:30px'>";
				content += "<table align='center'  id='info' style='margin-top:10px;' cellspacing='10'>";
					content += "<tr>";
						content += "<td></td>"; 
						content += "<td></td>"; 
						content += "<td></td>"; 
						content += "<td></td>"; 
						content += "<td align='right'><img  onclick='addPriceStrategyHtml()' alt='点我添加加价区间' src='../image/other/addPage.gif' title='点我添加加价区间' style='cursor: pointer;' ></td>"; 
						content += "<td></td>"; 
					content += "</tr>";
				if(xyzIsNull(dataJson)){
					content += "<tr id='priceStrategy_0' class = 'priceStrategyClass' >";
						content += "<td>价格区间<input type='text'  id='minPrice_0'  style='width:80px'></td>"; 
						content += "<td>──</td>"; 
						content += "<td><input type='text'  id='maxPrice_0'  style='width:80px'></td>"; 
						content += "<td>加价</td>"; 
						content += "<td><input type='text'  id='price_0'  style='width:80px'>元</td>"; 
						content +='<td><img onclick ="deletePriceStrategyHtml(0)" alt="点我删除 区间!" src="../image/other/delete.png" title="点我删除 区间!" style="cursor: pointer;"></td>';
					content += "</tr>";
				}
				for(var i in dataJson){  
					content += "<tr id='priceStrategy_"+i+"' class = 'priceStrategyClass' >";
						content += "<td>价格区间<input type='text'  id='minPrice_"+i+"' value='"+dataJson[i].minPrice+"' style='width:80px'></td>"; 
						content += "<td>──</td>"; 
						content += "<td><input type='text'  id='maxPrice_"+i+"' value='"+dataJson[i].maxPrice+"' style='width:80px'></td>"; 
						content += "<td>加价</td>"; 
						content += "<td><input type='text'  id='price_"+i+"' value='"+dataJson[i].price+"' style='width:80px'>元</td>"; 
						content +='<td><img onclick ="deletePriceStrategyHtml(\''+i+'\')" alt="点我删除 区间!" src="../image/other/delete.png" title="点我删除 区间!" style="cursor: pointer;"></td>';
					content += "</tr>";
				}
				content += "</table>";
				content += "</div>";
				xyzdialog({
					dialog : 'dialogDiv_editpriceStrategy',
					title : nameCn+' 加价区间',
					content : content,
					width : 700,
					height : 600,
					fit : false,
					buttons:[{
							text:'确定',
							handler:function(){
								editpriceStrategySubmit(numberCode);
							}
						},{
							text:'取消', 
							handler:function(){
								$("#dialogDiv_editpriceStrategy").dialog("destroy");
							}
					}],
					onOpen : function(){
						$("input[id^='minPrice_']").each(function(i,n){
							$('#minPrice_'+i).numberbox({ 
								required:true,
								min:0,    
								max:20000,
							}); 
							$('#maxPrice_'+i).numberbox({ 
								required:true,
								min:0,    
								max:20000,
							}); 
							$('#price_'+i).numberbox({ 
								required:true,
								min:0,    
								max:20000,
							}); 
							
						});
					}
				});
			} 
		},
	});
}

function addPriceStrategyHtml(){ 
	var tempId = $($(".priceStrategyClass:last")).attr('id');
	tempLength = tempId.split('_')[1];
	tempLength = parseInt(tempLength)+1;
	var  addPriceStrategyHtml = '';
	addPriceStrategyHtml+='<tr  id = "priceStrategy_'+tempLength+'" class = "priceStrategyClass">';
	addPriceStrategyHtml += "<td>价格区间<input type='text'   id='minPrice_"+tempLength+"'  style='width:80px'></td>"; 
	addPriceStrategyHtml += "<td>──</td>"; 
	addPriceStrategyHtml += "<td><input type='text'  id='maxPrice_"+tempLength+"'  style='width:80px'></td>"; 
	addPriceStrategyHtml += "<td>加价</td>";
	addPriceStrategyHtml += "<td><input type='text'   id='price_"+tempLength+"'  style='width:80px'>元</td>"; 
	addPriceStrategyHtml +='<td><img onclick ="deletePriceStrategyHtml(\''+tempLength+'\')" alt="点我删除 区间!" src="../image/other/delete.png" title="点我删除渠道和宝贝!" style="cursor: pointer;"></td>';
	addPriceStrategyHtml +='</tr>';
	$("tr[id^=priceStrategy_]:last").after(addPriceStrategyHtml);
	
	$('#minPrice_'+tempLength).numberbox({ 
		required:true,
		min:0,    
		max:20000,
	});  
	$('#maxPrice_'+tempLength).numberbox({ 
		required:true,
		min:0,    
		max:20000,
	});  
	$('#price_'+tempLength).numberbox({ 
		required:true,
		min:0,    
		max:20000,
	});  
}

function deletePriceStrategyHtml(id){
	
	$("#priceStrategy_"+id).remove();
}

function editpriceStrategySubmit(numberCode){
	if(!$("#info").form('validate')){
		return false;
	}
	
	var minNum = 0;//最小值
	var maxNum = 0;//最大值
	var min = 0;//用于错误时返回的区间最小
	var max = 0;//用于错误时返回的区间最大
	var sequenceState = true;//区间大小比较状态
	var minState = true;//从小到大 排列时的状态
	var maxState = true;//从大到小排列的状态
	var priceStrategy = [];
	$(".priceStrategyClass").each(function(i,n){
		var minPrice = $(this).find("input[id^='minPrice_']").numberbox('getValue');
		var maxPrice = $(this).find("input[id^='maxPrice_']").numberbox('getValue');
		var price = $(this).find("input[id^='price_']").numberbox('getValue');
		if(parseInt(minPrice)>parseInt(maxPrice)){
			sequenceState = false;
			min = minPrice;
			max = maxPrice;
		}
		if(i==0){
			maxNum = maxPrice; //区间的最大值
			minNum = minPrice;
		}else{
			//区间从小到大
			if(parseInt(maxNum)>=parseInt(minPrice) ||parseInt(maxNum)>=parseInt(maxPrice)){
				minState=false;
				min =minPrice;
			}else{
				maxNum = maxPrice; 
			}
			//区间从大到小
			if(parseInt(minNum)>=parseInt(minPrice)||parseInt(minNum)>=parseInt(maxPrice)){
				minState=true;
				if(parseInt(minNum)<=parseInt(maxPrice)){
					maxState = false;
					max = maxPrice;
				}else {
					minNum = minPrice;
				}
			}
		}
		priceStrategy.push({"minPrice":minPrice,maxPrice:maxPrice,"price":price});
		
	 });
	if(sequenceState==false){
		top.$.messager.alert("提示",min+"和"+max+"请按从小到大的顺序输入，添加失败!","info");
		return ;
	}else if(minState==false){
		top.$.messager.alert("提示",min+"值存在区间内，添加失败!","info");
		return ;
	}else if(maxState==false){
		top.$.messager.alert("提示",max+"值存在区间内，添加失败!","info");
		return ;
	}
	$.ajax({
		url : "../ChannelWS/editPriceStrategyByNumberCode.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			priceStrategy : JSON.stringify(priceStrategy)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#channelManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogDiv_editpriceStrategy").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}
function editSystemMemory(){
	
	  $.ajax({
			url : "../ChannelWS/editSystemMemory.do",
			type : "POST",
			async : false,
			dataType : "json",
			success : function(data) {
				if(data.status==1){
					top.$.messager.alert("提示","操作成功!","info");
				}else{
					top.$.messager.alert("警告",data.msg,"warning");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
			}
		}); 
}