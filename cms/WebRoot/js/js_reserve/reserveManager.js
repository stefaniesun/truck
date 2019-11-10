$(document).ready(function(){
	xyzTextbox("tkviewNameCn");
	xyzTextbox("linkMan");

	initTable();
	
	$("#reserveQueryButton").click(function(){
		loadTable();
	});
	
});

function initTable(){
	var toolbar = []; 
	
	xyzgrid({
		table : 'reserveManagerTable',
		title : "预约列表",
		url:'../SubscribeWS/queryReserveList.do',
		toolbar:toolbar,
		fit : true,  
		singleSelect : false, 
        height : 'auto',
		idField : 'numberCode',
		queryParams : {
			flag : -1
		},
		columns : [[
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'预约编号',hidden:true,halign:'center'},
			{field:'itemId',title:'宝贝编号',halign:'center'},
		    {field:'itemNameCn',title:'宝贝名称',width:130,halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDiv(value,0,130);
			   }
			},
			{field:'itemCount',title:'宝贝数量',align:'center'},
			{field:'flag',title:'状态',align:'center',
			   formatter:function(value ,row ,index){
				   if(value == 0){
					   return "等待审核";
				   }else if(value == 1){
					   return "审核失败";
				   }else{
					   return "审核成功";
				   }
			   }
			},
			{field:'auditing',title:'审核',width:40,halign:'center',
				formatter:function(value ,row ,index){
					var btnHtml = "";
					if(xyzControlButton("buttonCode_x20161128113503")){
						btnHtml = xyzGetA("审核","setFlagButton",[row.numberCode,row.itemNameCn,row.flag,row.flagRemark,row.remark],"点我审核预约信息","blue");
					}
					return xyzGetDiv(value,0,40) +" "+ btnHtml;
				}
			},
			{field:'flagRemark',title:'状态说明',halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDiv(value,0,80);
			   }
			},
			{field:'changes',title:'改动',width:35,align:'center',
				formatter:function(value, row, index) {
                    if (value == 0) {
                        return "×";
                    } else {
                        return "√";
                    }
                },
                styler:function(value, row, index) {
                    if (value == 1) {
                        return "background-color:green";
                    } else {
                        return "background-color:red";
                    }
                }
			},
			{field:'changesRemark',title:'改动说明',halign:'center',
				   formatter:function(value ,row ,index){
					  return xyzGetDiv(value,0,80);
				   }
				},
			{field:'channel',title:'渠道编号',hidden:true,halign:'center'},
			{field:'channelNameCn',title:'渠道名称',halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDiv(value,0,100);
			   }
			},
			{field:'personInfo',title:'出行人',width:60,hidden:true,halign:'center',
				formatter:function(value ,row ,index){
				   return xyzGetDiv(value,0,60);
				}
			},
			{field:'personCount',title:'出行人数',width:60,align:'center'},
			{field:'orderNum',title:'订单编号'},
		    {field:'clientCode',title:'票单号',width:100,hidden:true,halign:'center'},
		    {field:'tkview',title:'单品编号',hidden:true,halign:'center'},
		    {field:'tkviewNameCn',title:'单品名称',width:120,halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDiv(value,0,120);
			   }
			},
			{field:'linkMan',title:'联系人',width:70,halign:'center'},
			{field:'linkPhone',title:'联系人电话',width:93,halign:'center'},
			{field:'linkEmail',title:'联系人邮箱',width:90,hidden:true,halign:'center'},
			{field:'operator',title:'操作人',halign:'center'},
			{field:'addDate',title:'添加时间',hidden:true,halign:'center',
				   formatter:function(value ,row ,index){
					  return xyzGetDivDate(value);
				   }
			    },
		    {field:'alterDate',title:'修改时间',halign:'center',
			   formatter:function(value ,row ,index){
				  return xyzGetDivDate(value);
			   }
		    },
		    {field:'remark',title:'备注',width:130,halign:'center',
			   formatter:function(value ,row ,index){
				   return xyzGetDiv(value ,0 ,130);
			   }
		    }
		]]
	});
}

function loadTable(){
	var tkviewNameCn = $("#tkviewNameCn").val();
	var linkMan = $("#linkMan").val();
	var flag = xyzIsNull($("#flag").combobox("getValue"))==true?-1:$("#flag").combobox("getValue");

	$("#reserveManagerTable").datagrid('load', {
		tkviewNameCn : tkviewNameCn,
		linkMan : linkMan,
		flag : flag
	});
}

function getPersonData(reserve){
	$.ajax({
		url : '../SubscribeWS/getPersonListByReserve.do',
		type : "POST",
		data : {
			reserve : reserve
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				var personList = data.content;
				var html = '';
				for(var p = 0; p < personList.length; p++){
					var personObj = personList[p];
					html += '<tr>';
					html += ' <td>'+ personObj.realName +'</td>';
					html += ' <td>'+ personObj.ename +'</td>';
					html += ' <td>'+ personObj.sex +'</td>';
					html += ' <td>'+ personObj.birthday +'</td>';
					html += ' <td>'+ personObj.card +'</td>';
					html += ' <td>'+ personObj.personType +'</td>';
					html += ' <td>'+ personObj.nation +'</td>';
					html += ' <td>'+ personObj.domicile +'</td>';
					html += ' <td>'+ personObj.passport +'</td>';
					html += ' <td>'+ personObj.expireDate +'</td>';
					html += '</tr>';
				}
				html += '<tr>';
				html += ' <td colspan="10">';
				html += '  <hr/>';
				html += ' </td>';
				html += '</tr>';
				$("#personTbody").html(html);
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function setFlagButton(numberCode,itemNameCn,flag,flagRemark,remark){
	
	xyzdialog({
		dialog : 'dialogFormDiv_setFlagButton',
		title : '审核',
	    href : '../jsp_reserve/editFlag.html',
	    buttons:[{
			text:'确定',
			handler:function(){
				setFlagSubmit(numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_setFlagButton").dialog("destroy");
			}
		}],
		onLoad : function() {
			getPersonData(numberCode);
			$("#itemNameCnForm").html(itemNameCn);
			
			if(flag == 2){
				$("#flagTable").hide();
			}else{
				xyzTextbox("flagRemarkForm");
				xyzTextbox("remarkForm");
				$("#flagForm").combobox({
					required:true
				});
				
				$("#flagForm").combobox({
					value : flag
				});
				$("#flagRemarkForm").textbox("setValue",flagRemark);
				$("#remarkForm").textbox("setValue",remark);
			}
		}
	});
}

function setFlagSubmit(numberCode){
	if(!$("form").form('validate')){
		return false;
	}
	
	var flag = $("#flagForm").combobox("getValue");
	var flagRemark = $("#flagRemarkForm").textbox("getValue");
	var remark = $("#remarkForm").textbox("getValue");
	
	$.ajax({
		url : "../SubscribeWS/editReserveFlagByNumberCode.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			flag : flag,
			flagRemark : flagRemark,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#reserveManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功!","info");
				$("#dialogFormDiv_setFlagButton").dialog("destroy");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	
}