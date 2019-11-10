$(document).ready(function(){
	
	xyzTextbox("mark");
	xyzTextbox("nameCn");
	xyzTextbox("location");
	
	initTable();
	
	$("#portDiv").hide();
	if(xyzControlButton("buttonCode_x20170724141701")){
		$("#portDiv").show();
	}
	
	/*上传港口*/
	$("#importPortButton").click(function(){
		importExcelDataButton();
	});
	
	/*查询*/
	$("#portQueryButton").click(function(){
		loadTable();
	});

});

function initTable(){
	var toolbar = [];

	xyzgrid({
		table : 'rPortManagerTable',
		title : "港口列表",
		url : '../R_PortWS/queryRPortList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'港口编号',hidden:true,halign:'center'},
			{field:'mark',title:'港口标识',halign:'center'},
			{field:'nameCn',title:'港口名称',halign:'center',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 80);
        	  }
			},
			{field:'location',title:'所在地区',halign:'center',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 80);
        	  }
			}
		] ]
	});
}

function loadTable(){
	var mark = $("#mark").val();
	var nameCn = $("#nameCn").val();
	var location = $("#location").val();

	$("#rPortManagerTable").datagrid('load', {
		mark : mark,
		nameCn : nameCn,
		location : location
	});
}

function importExcelDataButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_importExcelDataButton',
		title : '导入Excel',
		content : '<div id="xyzUploadifyGridDiv"></div><br/><div id="xyzUploadifyButton" style="margin-left:100px;width:180px;height:20px;"></div>',
		fit : false,
		width : 450,          
		height : 450,
	    buttons : [{
			text:'确定',
			handler:function(){
				importExcelDataSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_importExcelDataButton").dialog("destroy");
			}
		}],onOpen : function(){
			
			//初始化图片容器
			xyzPicPreview.create({
				xyzPicPreview : 'xyzUploadifyGridDiv',
				//初始化后要立即展示的链接
				maxCount : 1
			});
			//上传容器
			xyzDropzone.create({
				xyzDropzone : 'xyzUploadifyButton',  //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				params : {"derictoryCode":"tkview"}, //上传时需要同时提交的参数键值对
				maxFiles : 1,                        //本控件最多允许上传的文件数量 默认10
				acceptedExtName : ".xls,.xlsx",      //允许文件类型
				maxFilesize : "3072kb",              //允许上传的单个文件大小（单位kb）
				xyzDropzone : 'xyzUploadifyButton',  //容器div，允许使用逗号分隔传入多个ID则同时初始化多个xyzDropzone
				btnText : '点击或拖拽文件至此',
				acceptedExtName : ".xls,.xlsx",      //允许文件类型
				params : {"derictoryCode":"tkview"}, //上传时需要同时提交的参数键值对
				maxFilesize : "1024kb",              //允许上传的单个文件大小（单位kb）
				maxFiles : 100,                      //本控件最多允许上传的文件数量 默认10
				success : function(result){
					xyzPicPreview.addPic('xyzUploadifyGridDiv',result.content.url);
				}
			});
			
		}
	});
	
}

function importExcelDataSubmit(){
	var path = xyzPicPreview.getAllPic("xyzUploadifyGridDiv").join(",");
	if(xyzIsNull(path)){
		top.$.messager.alert("提示","请先上传Excel文件!","info");
		return false;
	}
	
	$.ajax({
		url : "../R_PortWS/importExcelPortOper.do",
		type : "POST",
		data : {
			excelPath : path
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#dialogFormDiv_importExcelDataButton").dialog("destroy");
				$("#rPortManagerTable").datagrid("reload");
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