$(document).ready(function(){
	xyzTextbox("nameCn");
	
	initTable();
	
	$("#portQueryButton").click(function(){
		loadTable();
	});

});

function initTable(){
	var toolbar = []; 
	if(xyzControlButton("buttonCode_x20161207200502")){
		toolbar[toolbar.length]={
			text: '新增',  
			border:'1px solid #bbb',  
			iconCls: 'icon-add', 
			handler: function(){
				addPortButton();
			}
		};
	}
	
	if(xyzControlButton("buttonCode_x20161207200503")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '编辑',  
			border:'1px solid #bbb',  
			iconCls: 'icon-edit', 
			handler: function(){
				editPortButton();
			}
		};
	}	
	
	if(xyzControlButton("buttonCode_x20161207200504")){
		toolbar[toolbar.length]='-';
		toolbar[toolbar.length]={
			text: '删除',  
			border:'1px solid #bbb',  
			iconCls: 'icon-remove', 
			handler: function(){
				deletePortButton();
			}
		};
	}
	
	xyzgrid({
		table : 'portManagerTable',
		title : "港口列表",
		url:'../PortWS/queryPortList.do',
		toolbar:toolbar,
		singleSelect : false, 
		idField : 'numberCode',
		columns : [[
		      {field:'checkboxTemp',checkbox:true},      
			  {field:'numberCode',title:'编号',hidden:true,halign:'center'},
			  {field:'nameCn',title:'港口名称',halign:'center',
				  formatter:function(value ,row ,index){
					  return xyzGetDiv(value ,0 ,120);
				  }
			  },
			  {field:'country',title:'国家',halign:'center',
				  formatter:function(value ,row ,index){
					  return xyzGetDiv(value ,0 ,80);
				  }
			  },
			  {field:'longitude',title:'设置坐标',halign:'center',
				  formatter:function(value ,row ,index){
					  var html = "";
					  if(xyzControlButton("buttonCode_x20161207200505")){
		        		  html += xyzGetA("设置坐标","editPortCoordinate",[row.numberCode,row.longitude,row.latitude],"设置坐标","blue");
		        	  }
					  return html;
				  }
			  },
			  {field:'images',title:'图片',halign:'center',
				  formatter:function(value ,row ,index){
					  var count = 0;
					  if(!xyzIsNull(value)){
						  var arry = value.split(",");
						  count = arry.length;
					  }
					  return "图片("+ count +")";
				  }
			  },
			  {field:'address',title:'港口地址',width:150,halign:'center',
				  formatter:function(value ,row ,index){
					  return xyzGetDiv(value ,0 ,150);
				  }
			  },
			  {field:'remark',title:'简介',width:120,halign:'center',
				  formatter:function(value ,row ,index){
					  return xyzGetDiv(value ,0 ,100);
				  }
			  },
			  {field:'addDate',title:'添加时间',hidden:true,halign:'center',
				 formatter:function(value ,row ,index){
					 return xyzGetDivDate(value);
				 }
			  },
			  {field:'alterDate',title:'修改时间',width:83,halign:'center',
				 formatter:function(value ,row ,index){
					 return xyzGetDivDate(value);
				 }
			  }
		]]
	});
}

function loadTable(){
	var nameCn = $("#nameCn").val();
	
	$("#portManagerTable").datagrid('load',{
		nameCn : nameCn
	});
}

function addPortButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_addPortButton',
		title : '新增港口',
	    href : '../jsp_base/addPort.html',
	    fit : false,  
	    width: 450,
	    height : 500,
	    buttons:[{
			text:'确定',
			handler:function(){
				addPortSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addPortButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("addressForm");
			xyzTextbox("countryForm");
			
			xyzPicPreview.create({
				xyzPicPreview : 'detailsImgDiv',
				//初始化后要立即展示的链接
				imageUrls : "",
				maxCount : 4,
				moveWidth : 80
			});
			
			//上传容器
			xyzDropzone.create({
				xyzDropzone : 'xyzUploadifyButton',        //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params : {"derictoryCode":"port"},   //上传时需要同时提交的参数键值对
				acceptedExtName : ".jpg,.png,.jpeg,.gif,.bmp",  //允许文件类型
				maxFilesize : "1024kb",     //允许上传的单个文件大小（单位kb）
				btnText : '点击或拖拽图片至此',
				success : function(result){
					xyzPicPreview.addPic('detailsImgDiv', result.content.url);
				}
			});
		}
	});
}

function addPortSubmit(){
	if(!$("form").form('validate')){
		return false;
	}

	var nameCn = $("#nameCnForm").textbox("getValue");
	var country = $("#countryForm").textbox("getValue");
	var address = $("#addressForm").textbox("getValue");
	var details = xyzPicPreview.getAllPic("detailsImgDiv").join(",");
	var remark = $("#remarkForm").val();
	var urls = xyzPicPreview.getAllPic("detailsImgDiv").join(",");
	
	$.ajax({
		url : "../PortWS/addPort.do",
		type : "POST",
		data : {
			nameCn : nameCn,
			country : country,
			address : address,
			details : details,
			remark : remark,
			images : urls
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#portManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_addPortButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editPortButton(){
	var ports = $("#portManagerTable").datagrid("getChecked");
	if(ports.length != 1){
		top.$.messager.alert("提示","请先选中单个对象!","info");
		return;
	}
	var row = ports[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editPortButton',
		title : '编辑【'+ row.nameCn +'】',
	    href : '../jsp_base/editPort.html',
	    fit : false,  
	    width: 450,
	    height : 500,
	    buttons : [{
			text : '确定',
			handler:function(){
				editPortSubmit(row.numberCode);
			}
		},{
			text : '取消',
			handler:function(){
				$("#dialogFormDiv_editPortButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			xyzTextbox("nameCnForm");
			xyzTextbox("addressForm");
			xyzTextbox("countryForm");
			
			$("#nameCnForm").textbox('setValue',row.nameCn);
			$("#countryForm").textbox('setValue',row.country);
			$("#addressForm").textbox('setValue',row.address);
			$("#remarkForm").val(row.remark);
			
			$("#nameCnForm").textbox({
				required:true
			});
			
			xyzPicPreview.create({
				xyzPicPreview : 'detailsImgDiv',
				//初始化后要立即展示的链接
				imageUrls : row.images,
				maxCount : 4,
				moveWidth : 80
			});
			
			//上传容器
			xyzDropzone.create({
				xyzDropzone : 'xyzUploadifyButton',        //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params : {"derictoryCode":"port"},   //上传时需要同时提交的参数键值对
				acceptedExtName : ".jpg,.png,.jpeg,.gif,.bmp",  //允许文件类型
				maxFilesize : "1024kb",     //允许上传的单个文件大小（单位kb）
				btnText : '点击或拖拽文件至此',
				success : function(result){
					xyzPicPreview.addPic('detailsImgDiv', result.content.url);
				}
			});
			
		}
	});
}

function editPortSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var nameCn = $("#nameCnForm").textbox("getValue");
	var country = $("#countryForm").textbox("getValue");
	var address = $("#addressForm").textbox("getValue");
	var details = xyzPicPreview.getAllPic("detailsImgDiv").join(",");
	var remark = $("#remarkForm").val();
	var urls = xyzPicPreview.getAllPic("detailsImgDiv").join(",");
	
	$.ajax({
		url : "../PortWS/editPort.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nameCn : nameCn,
			country : country,
			address : address,
			details : details,
			remark : remark,
			images : urls
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#portManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editPortButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deletePortButton(){
	var ports = $.map($("#portManagerTable").datagrid("getChecked"),
		function(p){
	       return p.numberCode;
	    }
	).join(",");
	if(xyzIsNull(ports)){
		top.$.messager.alert("提示","请先选中需要删除的对象!","info");
		return;
	}
	
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		if(r){
			$.ajax({
				url : "../PortWS/deletePort.do",
				type : "POST",
				data : {
					numberCodes : ports
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if(data.status==1){
						$("#portManagerTable").datagrid("reload");
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

function editPortCoordinate(numberCode, longitude, latitude){
	 
	xyzdialog({
		dialog : 'dialogFormDiv_editPortCoordinate',
		title : "设置坐标",
		href : '../jsp_base/editPortCoordinate.html',
	    fit : false,
	    width : 450,
	    height : 400,
	    buttons:[{
			text : '确定',
			handler:function(){
				editPortCoordinateSubmit(numberCode);
			}
	    },{
	    	text : '取消',
	    	handler:function(){
	    		$("#dialogFormDiv_editPortCoordinate").dialog("destroy");
	    	}
	    }],
		onLoad : function(){
			$("#longitudeForm").val(longitude);
			$("#latitudeForm").val(latitude);
			
			$("#valueForm").focusout(function(){
				var value = $(this).val();
				var arry = value.split(",");
				$("#longitudeForm").val(arry[0]);
				$("#latitudeForm").val(arry[1]);
			});			
			
			window.open("http://api.map.baidu.com/lbsapi/getpoint/index.html");
		}
	});
}

function editPortCoordinateSubmit(numberCode){
	var value = $("#valueForm").val();
	var longitude = "";
	var latitude = "";
	if(!xyzIsNull(value)){
		var arry = value.split(",");
		longitude = arry[0];
		latitude = arry[1];
	}
	
	$.ajax({
		url : "../PortWS/editPortCoordinate.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			longitude : longitude,
			latitude : latitude
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#dialogFormDiv_editPortCoordinate").dialog("destroy");
				top.$.messager.alert("提示","操作成功!","info");
				$("#portManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});   
	
}