$(document).ready(function() {
	
	xyzTextbox("areaMark");
	xyzTextbox("mark");
	
	initTable();
	
	/*读取ITINERARY.txt文件*/
	$("#importTxtButton").click(function(){
		importTxtButton();
	});
	
	/*查询*/
	$("#airwayQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = [];

	xyzgrid({
		table : 'airwayManagerTable',
		title : "航线列表",
		url : '../R_AirwayWS/queryRAirwayList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'area',title:'航区编号',hidden:true,halign:'center'},
			{field:'areaMark',title:'航区代码',halign:'center'},
			{field:'numberCode',title:'航线编号',hidden:true,halign:'center'},
			{field:'nameCn',title:'航线名称',halign:'center',
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value, 0, 80);
        	  }
			},
			{field:'mark',title:'航线代码',halign:'center'},
			{field:'days',title:'几天',hidden:true,halign:'center'},
			{field:'nights',title:'几晚',hidden:true,halign:'center'},
			{field:'route',title:'行程模版',halign:'center',
        	  formatter: function(value,row,index){
        		  var btnTemp = "";
        		  if(xyzControlButton("buttonCode_x20170725094002")){
        			  btnTemp = xyzGetA("行程模版","tripManager",[row.numberCode,row.nameCn],"点我管理行程模版","blue");
        		  }
        		  return btnTemp;
        	  }
			}
		] ]
	});
}

function importTxtButton(){
	
	$.ajax({
		url : "../R_TripWS/importTxtOper.do",
		type : "POST",
		data : {},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				$("#airwayManagerTable").datagrid("reload");
				top.$.messager.alert("提示",data.content,"info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}

function loadTable(){
	var areaMark = $("#areaMark").val();
	var mark = $("#mark").val();

	$("#airwayManagerTable").datagrid('load', {
		areaMark : areaMark,
		mark : mark
	});
}

function tripManager(numberCode ,nameCn){
	xyzdialog({
		dialog : 'dialogFormDiv_tripManager',
		title : "行程模版管理 【"+nameCn +"】",
	    content : "<iframe id='dialogFormDiv_tripManagerIframe' frameborder='0'></iframe>",
	    buttons:[{
			text:'返回',
			handler:function(){
				$("#dialogFormDiv_tripManager").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_tripManager").css("width");
	var tempHeight = $("#dialogFormDiv_tripManager").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_tripManagerIframe").css("width",(tempWidth2)+"px");
	$("#dialogFormDiv_tripManagerIframe").css("height",(tempHeight2)+"px");
	$("#dialogFormDiv_tripManagerIframe").attr("src","../jsp_rbase/rTripManager.html?airway="+numberCode);
}

function importTxtButton(){
	
	$.ajax({
		url : "../R_TripWS/importTxtOper.do",
		type : "POST",
		data : {},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status == 1){
				$("#airwayManagerTable").datagrid("reload");
				top.$.messager.alert("提示",data.content,"info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}