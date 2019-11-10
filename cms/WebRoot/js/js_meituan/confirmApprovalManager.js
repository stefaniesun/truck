$(document).ready(function(){
	xyzTextbox("mtOrderId");
	initTable();
	$("#confirmApprovalButton").click(function(){
		loadTable();
	});
});

function initTable(){
	xyzgrid({
		table : 'confirmApprovalManagerTable',
		url : "../MeituanWS/queryConfirmApprovalList.do",
		idField : 'numberCode',
		columns:[[
            {field:'operator1',title:'操作',width:50,
	            formatter:function(value,row,index){
	            	if(row.status==0){
	            		return xyzGetA("确认","approvalConfirm",[row.mtOrderid],"","");
	            	}else{
	            		return "";
	            	}
	            }
            },
			{field:'mtOrderid',title:'美团订单编号',width:100},
			{field:'status',title:'状态',width:80,
				formatter:function(value,row,index){
					var text="";
					if(value==0){
						text="待确认";
					}else if(value==1){
						text="有库存";
					}else if(value==-1){
						text="无库存";
					}
					return text;
				}
			},
			
			{field:'sku',title:'我方sku',width:80},
			{field:'travelDate',title:'出行日期',width:80},
			{field:'quantity',title:'购买数量',width:80},
			{field:'totalPrice',title:'总金额',width:80},
			{field:'contactName',title:'联系人姓名',width:80},
			{field:'contactPhone',title:'联系人电话',width:120},
			{field:'addDate',title:'添加日期',width:80,
				formatter:function(value,row,index){
					return xyzGetDivDate(value);
		        }
			},
			{field:'alterDate',title:'修改日期',width:80,
				formatter:function(value,row,index){
					return xyzGetDivDate(value);
				}
			}
		]]
	});
	
}


function approvalConfirm(mtOrderid){
	var contentHtml='<center><form><table style="margin-top:20px;">';
	contentHtml+='<tbody>';
	contentHtml+='<tr><td><input type="radio" value="1" name="accept"/>库存充足，确认订单</td></tr>';
	contentHtml+='<tr><td><input type="radio" value="0" name="accept"/>库存不足</td></tr>';
	contentHtml+='</tbody>';
	contentHtml+='</table></form></center>';
	
	xyzdialog({
		dialog : 'dialogFormDiv_approval',
		title : "订单确认",
	    content:contentHtml,
	    fit:false,
	    height:350,
	    width:450,
	    buttons:[{
			text:'确定',
			handler:function(){
				approvalConfirmSubmit(mtOrderid);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_approval").dialog("destroy");
			}
		}]
	});
	
}

function approvalConfirmSubmit(mtOrderid){
	if(!$("form").form('validate')){
		return false;
	}
	var accept=$("input[name='accept']:checked").val();
	xyzAjax({
		url : "../MeituanWS/affirmApprol.do",
		type : "POST",
		data : {
			mtOrderid : mtOrderid,
			code : accept,
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.status==1){
				top.$.messager.alert("提示",data.content,"info");
				$("#dialogFormDiv_approval").dialog("destroy");
				$("#confirmApprovalManagerTable").datagrid("reload");
			}else{
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
	
}

function loadTable(){
	var mtOrderid = $("#mtOrderId").textbox("getValue");
	$("#confirmApprovalManagerTable").datagrid('load',{
		mtOrderId : mtOrderid,
	});
}
function showOrderCore(value){
	xyzdialog({
		dialog : 'dialogFormDiv_showOrderCore',
		title : "美团订单编号【"+value+"】",
		content : "<iframe id='dialogFormDiv_showOrderCoreIframe' frameborder='0' name='"+value+"'></iframe>",
	    buttons:[{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_showOrderCore").dialog("destroy");
			}
		}]
	});
	var tempWidth = $("#dialogFormDiv_showOrderCore").css("width");
	var tempHeight = $("#dialogFormDiv_showOrderCore").css("height");
	var tempWidth2 = parseInt(tempWidth.split("px")[0]);
	var tempHeight2 = parseInt(tempHeight.split("px")[0]);
	$("#dialogFormDiv_showOrderCoreIframe").css("width",(tempWidth2-20)+"px");
	$("#dialogFormDiv_showOrderCoreIframe").css("height",(tempHeight2-50)+"px");
	$("#dialogFormDiv_showOrderCoreIframe").attr("src","../jsp_meituan/showOrderCore.html");
}

