$(document).ready(function() {
	xyzTextbox("nickName");
	
	initTable();
	
	$("#truckQueryButton").click(function(){
		loadTable();
	});
});

function initTable(){
	var toolbar = [];
	toolbar[toolbar.length] = {
			text : '编辑',
			border : '1px solid #bbb',
			iconCls : 'icon-edit',
			handler : function() {
				editCustomerButton(); 
		    }
		};
	toolbar[toolbar.length] = '-';
		
	xyzgrid({
		table : 'truckManagerTable',
		title : "货车列表",
		url : '../TruckWS/queryTruckList.do',
		toolbar : toolbar,
		singleSelect : false,
		idField : 'numberCode',
		columns : [ [
			{field:'checkboxTemp',checkbox:true},
			{field:'numberCode',title:'货车编号',hidden:true},
			{field:'title',title:'车辆信息',width:200,
        	  formatter:function(value ,row ,index){
				return xyzGetDiv(value,0,150);
        	  }
			},
			{field:'price',title:'估价(万)',width:80,
	        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,150);
	        	  }
			},
			{field:'mile',title:'里程(万公里)',width:80,
	        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,150);
	        	  }
			},
			{field:'year',title:'车龄',width:80,
	        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,150);
	        	  }
			},
			{field:'cardDate',title:'上牌时间',width:100,
	        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value.substring(0,10),0,150);
	        	  }
			},
			{field:'checkDate',title:'年检时间',width:100,
	        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value.substring(0,10),0,150);
	        	  }
			},
			{field:'insuranceDate',title:'交强险时间',width:100,
	        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value.substring(0,10),0,150);
	        	  }
			},
			{field:'phone',title:'联系电话',width:150,
	        	  formatter:function(value ,row ,index){
					return xyzGetDiv(value,0,150);
	        	  }
			},
			{field:'images',title:'图片',align:'center',width:80,
				formatter:function(value, row, index){
					 var html = "";
					 var images=JSON.parse(value);
					 html = xyzGetA("图片["+images.length+"]","checkImages",[row.numberCode],"点我查看图片!","blue");
	        		  return html;
				}
			},
			{field:'status',title:'状态',width:50,
	        	  formatter:function(value ,row ,index){
	        		  if(value==0){
	        			  return "未审核";
	        		  }else  if(value==1){
	        			  return "<span style='color:green'>已审核</span>";
	        		  }
	        	  }
			},
			{field:'operTemp1',title:'操作',align:'center',width:70,
				formatter: function(value,row,index){
					var buttonTemp1 = "";
					if(row.status==0){
						buttonTemp1 =  xyzGetA("审核", "check", row.numberCode, "审核", "blue");
					}
					return buttonTemp1;
				}
			},
			{field:'enabled',title:'可见',align:'center',
				formatter: function(value,row,index){
					if (row.status>0){
						return '<span style="font-size:18px;font-weight:bold;">√';
					} else {
						return '<span style="font-size:18px;font-weight:bold;">×</span>';
					}
				},
				styler : function(value,row,index){
					if(row.status>0){
						return "background-color:green";
					}else {
						return "background-color:red";
					}
				}
			},
		    {field:'addDate',title:'申请时间',width:120,
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'passDate',title:'审核时间',width:120,
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
			{field:'remark',title:'备注',width:300,
				formatter:function(value ,row ,index){
	    		  return xyzGetDiv(value ,0 ,200);
				}
            }
		] ]
	});
}

function loadTable(){
	var nickName = $("#nickName").textbox("getValue");
	
	$("#customerManagerTable").datagrid('load', {
		nickName :nickName
	});
}

function editCustomerButton(){
	var customers = $("#customerManagerTable").datagrid("getChecked");
	if (customers.length != 1) {
		top.$.messager.alert("提示", "请先选中单个对象!", "info");
		return false;
	}
	var row = customers[0];
	
	xyzdialog({
		dialog : 'dialogFormDiv_editCustomerButton',
		title : '编辑客户',
		href : '../jsp_custom/editCustomer.html',
		fit : false,
		width : 450,
	    height : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				editCustomerSubmit(row.numberCode);
			}
		}, {
			text : '取消',
			handler : function() {
				$("#dialogFormDiv_editCustomerButton").dialog("destroy");
			}
		} ],
		onLoad : function() {
			xyzTextbox("nickNameForm");
			
			$("#nickNameForm").textbox("setValue", row.nickName);
			$("#remarkForm").val(row.remark);
		} 
	});
}

function editCustomerEnabled(numberCode){
	$.ajax({
		url : "../CustomerWS/editCustomerEnabled.do",
		type : "POST",
		data : {
			numberCode : numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#customerManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function check(numberCode){
	$.ajax({
		url : "../TruckWS/checkOper.do",
		type : "POST",
		data : {
			numberCode : numberCode
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				$("#truckManagerTable").datagrid("reload");
				top.$.messager.alert("提示","操作成功！","info");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function checkImages(numberCode){
	
	var customers = $("#truckManagerTable").datagrid("getRows");
	var row;
	for(var i=0;i<customers.length;i++){
		if(numberCode==customers[i].numberCode){
			row=customers[i];
		}
	}
	var images=JSON.parse(row.images);
	
	var htmlContent = "<div id='xyzUploadifyGridDiv'>";
	
	for(var j=0;j<images.length;j++){
		htmlContent+="<img src='"+images[j]+"' style='with:360px;height:300px;float:left;margin-top:10px;margin-left:10px;overflow: hidden;' >";
	}
	
	htmlContent+="</div><br/>";
	htmlContent+="<div id='xyzUploadifyButton' style='margin-left:100px;width:200px;height:20px;'>";
	htmlContent+="</div>";
	
	
	xyzdialog({
		dialog : 'dialogFormDiv_checkImages',
		title : '查看图片',
		content : htmlContent,
		fit : false,  
		height : 900,
		width: 1200,
		buttons:[{
			text:'关闭',
			handler:function(){
				$("#dialogFormDiv_checkImages").dialog("destroy");
			}
		}],
		onOpen : function(){
			
		}
	});
	
}

function editCustomerSubmit(numberCode){
	if(!$("form").form('validate')) {
		return false;
	}

	var nickName = $("#nickNameForm").textbox("getValue");
	var remark = $("#remarkForm").val();
	
	$.ajax({
		url : "../CustomerWS/editCustomer.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			nickName : nickName,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#customerManagerTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editCustomerButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}