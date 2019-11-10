$(document).ready(function() {
	xyzTextbox("username");
	xyzTextbox("otherInfo");
	$("#dateStart").datebox("setValue",new Date().Format("%y-%M-%d"));
	$("#dateEnd").datebox("setValue",new Date().Format("%y-%M-%d"));
	initTable();
	$("#userLogQueryButton").click(function(){
		loadTable();
	});
});

function initTable(){
	xyzgrid({
		table : 'userLogTable',
		title:'日志列表',
		url:'../LogWS/getLogOperList.do',
		idField:'iidd',
		singleSelect : false,
		columns:[[
			{field:'username',title:'用户名',align:'center'},
			{field:'isWork',title:'工作',align:'center',
				formatter: function(value,row,index){
					if (value == 1){
						return '<span style="font-size:18px;font-weight:bold;">√</span>';
					} else {
						return '<span style="font-size:18px;font-weight:bold;">×</span>';
					}
				},
				styler : function(value,row,index){
					if(value == 1){
						return "background-color:red";
					}else {
						return "background-color:green";
					}
				}
			},
			{field:'remark',title:'事由',
		    	formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 200);
				}
			},
			{field:'ipInfo',title:'IP地址',width:110},
			{field:'dataContent',title:'内容',width:400,
				formatter: function(value,row,index){
					return xyzGetDiv(value, 0, 100);
				}
			},
			{field:'addDate',title:'添加时间',width:140}
		]],
		onClickCell:function(rowIndex, field,value){
			var columnObj=$("#userLogTable").datagrid("getColumnOption",field);
			if(field=="dataContent"){
				xyzdialog({
					dialog : 'dialogFormDiv_userLogTable_dataContent',
					title : columnObj.title,
				    content : '<textarea id="myTextareaForm" style="width:418px;height:345px;margin-top:10px;"></textarea>',
				    width:450,
				    height:450,
				    fit:false,
				    buttons:[{
						text:'关闭',
						handler:function(){
							$("#dialogFormDiv_userLogTable_dataContent").dialog("destroy");
						}
					}],
					onOpen : function(){
						$("#myTextareaForm").val(value);
					}
				});
			}
		},
		queryParams : {
			dateStart : $("#dateStart").datebox("getText"),
			dateEnd : $("#dateEnd").datebox("getText")
		}
	});
}

function loadTable(){
	var otherInfo = $("#otherInfo").val();
	var username = $("#username").val();
	var dateStart = $("#dateStart").datebox("getText");
	var dateEnd = $("#dateEnd").datebox("getText");
	$("#userLogTable").datagrid('load',{
		username : username,
		otherInfo : otherInfo,
		dateStart : dateStart,
		dateEnd : dateEnd
	});
}