$(document).ready(function() {
	var params = getUrlParameters();
	shipment = params.shipment;
	travelDate = params.travelDate;
	tripDays = params.tripDays;
	airwayNameCn = getUrlParam("airwayNameCn");
	
	initTable();
	
});

function getUrlParam(key){
    // 获取参数
    var url = window.location.search;
    // 正则筛选地址栏
    var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
    // 匹配目标参数
    var result = url.substr(1).match(reg);
    //返回参数值
    return result ? decodeURIComponent(result[2]) : null;
}

function initTable(){
	var toolbar = [];
	if (xyzControlButton("buttonCode_x20161213113304")) {
		toolbar[toolbar.length] = {
			text : '单个编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editVoyageButton();
			}
		};
	}
	if (xyzControlButton("buttonCode_x20161213113306")) {
		toolbar[toolbar.length] = '-';
		toolbar[toolbar.length] = {
			text : '完整编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editQuickVoyageButton();
			}
		};
	}
	
	var title = "行程路线列表【"+airwayNameCn+"】";

	xyzgrid({
		table : 'voyageManagerTable',
		title : title,
		url : '../ShipmentWS/queryVoyageListByShipment.do',
		toolbar : toolbar,
		idField : 'numberCode',
		queryParams : {
			shipment : shipment
		},
		singleSelect : false ,
		pagination : false,
		height : 560,
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'行程编号',hidden:true},
			{field:'time',title:'时间',align:'center'},
			{field:'timeType',title:'时间类型',halign:'center',
				formatter : function(value, row, index){
					//0:登船日、1:岸上观光日、2:航海日、3:离船日
					if(value == 0){
						return "登船日";
					}else if(value == 1){
						return "岸上观光日";
					}else if(value == 2){
						return "航海日";
					}else{
						return "离船日";
					}
				}
			},
			{field:'port',title:'港口编号',hidden:true},
			{field:'portNameCn',title:'港口名称',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 100);
				}
			},
			{field:'portAddress',title:'港口地址',hidden:true,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDiv(value, 0, 100);
				}
			},
			{field:'arrivalTime',title:'抵达时间',align:'center'},
			{field:'leaveTime',title:'起航时间',align:'center'},
			{field:'detail',title:'行程规划',width:300,halign:'center',
				formatter : function(value, row, index) {
					return xyzGetDiv(value,0,300);
				}
			},
			{field:'addDate',title:'添加时',hidden:true,align:'center',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'alterDate',title:'修改时',width:85,align:'center',halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:120,align:'center',halign:'center',
				formatter:function(value ,row ,index){
	    		  return xyzGetDiv(value ,0 ,100);
				}
            }
		]]
	});

}

function editVoyageButton(){
	var voyages = $("#voyageManagerTable").datagrid("getChecked");
	if (voyages.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = voyages[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editVoyageButton',
		title : '单个编辑行程路线',
	    href : '../jsp_base/editVoyage.html',
	    fit : false,
	    width: 450,
	    height : 450,
	    buttons:[{
			text:'确定',
			handler:function(){
				editVoyageSubmit(row.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editVoyageButton").dialog("destroy");
			}
		}],
		onLoad : function(){
			$("#timeForm").datebox({
				icons : [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).datebox('clear');
					}
				}]
			});
			xyzTextbox("timeForm");
			xyzTextbox("arrivalTimeForm");
			xyzTextbox("leaveTimeForm");
			
			$("#portForm").html(row.portNameCn);
			$("#timeForm").textbox("setValue",row.time);
			$("#timeTypeForm").combobox({
				value :	row.timeType
			});
			$("#arrivalTimeForm").textbox("setValue",row.arrivalTime);
			$("#leaveTimeForm").textbox("setValue",row.leaveTime);
			$("#detailForm").val(row.detail);
			$("#remarkForm").val(row.remark);
		}
	});
}

function editVoyageSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var time = $("#timeForm").textbox('getValue');
	var timeType = $("#timeTypeForm").combobox("getValue");
	var arrivalTime = $("#arrivalTimeForm").textbox("getValue");
	var leaveTime = $("#leaveTimeForm").textbox("getValue");
	var detail = $("#detailForm").val();
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : '../ShipmentWS/editShipmentVoyage.do',
		type : "POST",
		data : {
			numberCode : numberCode,
			time : time,
			timeType : timeType,
			arrivalTime : arrivalTime,
			leaveTime : leaveTime,
			detail : detail,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#voyageManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editVoyageButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function getVoyageData(){
	$.ajax({
		url : '../ShipmentWS/getVoyageByShipment.do',
		type : "POST",
		data : {
			shipment : shipment
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var voyageLkist = data.content;
				
				var html = '';
				for(var k = 0; k < voyageLkist.length; k++){
					var voyageObj = voyageLkist[k];
					var numberCode = voyageObj.numberCode;
					html += '<tr id="tr_'+ numberCode +'">';
					html += ' <td>';
					html += '  <input id="numberForm_'+ numberCode +'" type="hidden" value="'+ numberCode +'"/>';
					html += '  <input id="timeForm_'+ numberCode +'" type="text" style="width:200px;"/>';
					html += ' </td>';
					html += ' <td>';
					html += '  <input id="timeTypeForm_'+ numberCode +'" type="text" ';
					html += '   data-options="panelHeight : \'auto\', ';
					html += '   valueField : \'value\',textField :\'text\', ';
					html += '   data : [{value : \'0\',text : \'登船日\'},';
					html += '           {value : \'1\',text : \'岸上观光日\'},';
					html += '           {value : \'2\',text : \'航海日\'}, ';
					html += '           {value : \'3\',text : \'离船日\'}]"/> ';
					html += ' </td>';
					html += ' <td>'+ voyageObj.portNameCn +'</td>';
					html += ' <td>';
					html += '  <input id="arrivalForm_'+ numberCode +'" type="text" style="width:80px;"/>';
					html += ' </td>';
					html += ' <td>';
					html += '  <input id="leaveForm_'+ numberCode +'" type="text" style="width:80px;"/>';
					html += ' </td>';
					html += ' <td>';
					html += '  <input id="detailForm_'+ numberCode +'" type="text" style="width:200px;"/>';
					html += ' </td>';
					html += ' <td>';
					html += '  <input id="remarkForm_'+ numberCode +'" type="text" style="width:100px;"/>';
					html += ' </td>';
					html += '</tr>';
				}
				$("#voyageTbody").html(html);
				
				for(var h = 0; h < voyageLkist.length; h++){
					var voyageObj = voyageLkist[h];
					var tempId = voyageObj.numberCode;
					
					xyzTextbox("timeForm_"+tempId);
					xyzTextbox("arrivalForm_"+tempId);
					xyzTextbox("leaveForm_"+tempId);
					xyzTextbox("detailForm_"+tempId);
					xyzTextbox("remarkForm_"+tempId);
					$("#timeTypeForm").combobox({
						icons : [{
							iconCls:'icon-clear',
							handler: function(e){
								$(e.data.target).combobox('clear');
							}
						}]
					});
					$("#timeForm_"+tempId).textbox("setValue",voyageObj.time);
					$("#timeTypeForm_"+tempId).combobox({
						value : voyageObj.timeType
					});
					$("#arrivalForm_"+tempId).textbox("setValue",voyageObj.arrivalTime);
					$("#leaveForm_"+tempId).textbox("setValue",voyageObj.leaveTime);
					$("#detailForm_"+tempId).textbox("setValue",voyageObj.detail);
					$("#remarkForm_"+tempId).textbox("setValue",voyageObj.remark);
					
					$("#timeForm_"+tempId).textbox({
						required : true
					});
					$("#timeTypeForm_"+tempId).combobox({
						required : true
					});
				}
				
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editQuickVoyageButton(){
	xyzdialog({
		dialog : 'dialogFormDiv_editQuickVoyageButton',
		title : '完整编辑',
		href : '../jsp_base/editQuickVoyage.html',
		buttons : [ {
			text : '确定',
			handler : function() {
				editQuickVoyageSubmit();
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editQuickVoyageButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			getVoyageData();
		}
	});
}

function editQuickVoyageSubmit(){
	if(!$("form").form('validate')){
		return false;
	}
	
	var idList = $.map($("#voyageTbody tr[id^='tr_']"),
		function(p){
			return $(p).attr("id").split("_")[1];
		}
	);
	var voyageJson = [];
	for(var t = 0; t < idList.length; t++){
		var numberCode = idList[t];
		var time = $('#timeForm_'+numberCode).textbox('getValue');
		var timeType = $("#timeTypeForm_"+numberCode).combobox("getValue");
		var arrivalTime = $("#arrivalForm_"+numberCode).textbox("getValue");
		var leaveTime = $("#leaveForm_"+numberCode).textbox("getValue");
		var detail = $("#detailForm_"+numberCode).textbox("getValue");
		var remark = $("#remarkForm_"+numberCode).textbox("getValue");
		var voyage = {
			numberCode : numberCode,
			time : time,
			timeType : timeType,
			arrivalTime : arrivalTime,
			leaveTime : leaveTime,
			detail : detail,
			remark : remark
		};
		voyageJson[voyageJson.length] = voyage;
	}
	
	$.ajax({
		url : '../ShipmentWS/editQuickVoyage.do',
		type : "POST",
		data : {
			voyageJson : JSON.stringify(voyageJson)
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#voyageManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_editQuickVoyageButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}