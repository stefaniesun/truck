$(document).ready(function() {

    initTable();

    xyzTextbox("numberCode");
    xyzTextbox("name");
    
    $("#distributorQueryButton").click(function(){
		loadTable();
	});
    
});

function initTable() {
    var toolbar = [];
    
    if (xyzControlButton("buttonCode_y20160809113702")) {
    	toolbar[toolbar.length] = '-';
    	toolbar[toolbar.length] = {
			text:'新增',
			iconCls:'icon-add',
			handler:function() {
				addDistributorButton();
			}
		};
	}
    
    if (xyzControlButton('buttonCode_y20160809113703')) {
        toolbar[toolbar.length] = {
            text:'编辑',
            border:'1px solid #bbb',
            iconCls:'icon-edit',
            handler:function() {
            	editDistributorButton();
            }
        };
        toolbar[toolbar.length] = '-';
    }
    
    if (xyzControlButton('buttonCode_y20160809113704')) {
    	toolbar[toolbar.length] = {
    			text:'删除',
    			border:'1px solid #bbb',
    			iconCls:'icon-remove',
    			handler:function() {
    				deleteDistributorButton();
    			}
    	};
    	toolbar[toolbar.length] = '-';
    }
    


    xyzgrid({
        table : 'distributorTable',
        title : '分销商列表',
        url : '../DistributorWS/queryDistributorList.do',
        pageList : [5, 10, 15, 30, 50],
        pageSize : 15,
        toolbar : toolbar,
        singleSelect : true,
        idField : 'username',
        columns : [[
            {field:'checkboxTemp',checkbox:true,hidden:true},
            {field:'numberCode',title:'编号',width:80,halign:'center',hidden:true,
            	formatter:function(value, row, index){
            		return xyzGetDiv(value,0,80);
            	}
            },
            {field:'name',title:'分销商名称',width:80,halign:'center',
            	formatter:function(value, row, index){
            		return xyzGetDiv(value,0,80);
            	}
            },
            {field:'type',title:'分销商类型',width:80,halign:'center',
            	formatter:function(value, row, index){
            		return xyzGetDiv(value,0,80);
            	}
            },
            {field:'linkUsername',title:'联系人',width:90,align:'center',
            	formatter:function(value, row, index){
            		return xyzGetDiv(value,0,80);
            	}
            },
            {field:'linkPhone',title:'联系人电话',width:80,align:'center',
            	formatter:function(value, row, index){
            		return xyzGetDivDate(value);
            	}
            },
            {field:'distributorTag',title:'分销等级编号',width:80,halign:'center',hidden:true,
            	formatter:function(value, row, index){
            		return xyzGetDiv(value,0,80);
            	}
            },
            {field:'distributorTagNameCn',title:'分销等级',width:80,halign:'center',
            	formatter:function(value, row, index){
            		return xyzGetDiv(value,0,80);
            	}
            },
            {field:'isEnable',title:'是否开启',halign:'center',align:'center',
            	formatter: function(value,row,index){
		    		return '<input style="width:42px;height:18px;" class="switchbutton isEnableSwitchbutton" data-options="checked:'+(value==1?'true':'false')+'" data-infoTableCode="'+row.numberCode+'"/>';
				}
            },
            {field:'addDate',title:'添加时间',hidden:true,halign:'center'},
            {field:'alertDate',title:'修改时间',width:85,halign:'center',
				formatter:function(value ,row ,index){
					return xyzGetDivDate(value);
				}
			},
            {field:'remark',title:'备注',width:70,halign:'center'}
        ]],
        onLoadSuccess : function(data){
			$('.isEnableSwitchbutton').switchbutton({ 
			      onText : '开',
			      offText : '关',
			      onChange: function(checked){ 
			    	  editIsEnable($(this).attr('data-infoTableCode'));
			      } 
			});
		}
    });
}

function editIsEnable(numberCode){
	xyzAjax({
		url : "../DistributorWS/editIsEnable.do",
		data : {
			numberCode : numberCode
		},
		success : function(data) {
			if(data.status !=1){
				top.$.messager.alert("警告",data.msg,"warning");
			}
		}
	});
}
function loadTable() {
    
	var numberCode = $("#numberCode").val();
	var name = $("#name").val();
	
    $("#distributorTable").datagrid("load", {
    	numberCode : numberCode, 
    	name : name
    });
}

function addDistributorButton(){
	
	xyzdialog({
		dialog : 'dialogFormDiv_addDistributorButton',
		title : '新增分销商',
	    href : '../jsp_distributor/addDistributor.html',
	    fit : false,  
	    width: 550,
	    height : 400,
	    buttons:[{
			text:'确定',
			handler:function(){
				addDistributorSubmit();
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_addDistributorButton").dialog("destroy");
			}
		}],
		onLoad : function() {
			xyzTextbox("nameForm");
			$("#typeForm").combobox({
				required:true,
		    	panelHeight:'auto',
		    	panelMaxHeight:500,
				data: [{value: 'PERSONAL',text: '个人'},
				{value: 'PLATFORM',text: '平台'},
				{value: 'noIgnore',text: '旅行社'},
				{value: 'loudan',text: '其他'}],
		    });
			xyzTextbox("linkUsernameForm");
			xyzTextbox("linkPhoneForm");
			xyzCombobox({
				required:true,
				combobox : 'distributorTagForm',
				url : '../ListWS/getDistributorTagList.do',
				mode: 'remote',
				lazy : false
			});
		}
	});
}

function addDistributorSubmit(){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var name = $("#nameForm").val();
	var type = $("#typeForm").combobox("getText");
	var linkUsername = $("#linkUsernameForm").val();
	var linkPhone = $("#linkPhoneForm").val();
	var distributorTag = $("#distributorTagForm").combobox("getValue");
	var remark = $("#remarkForm").val();
	
	var phoneReg = /^1[0-9]{10}$/;
	if(!phoneReg.test(linkPhone)){
		top.$.messager.alert("提示","手机号格式不正确!","info");
		return false;
	}
	
	$.ajax({
		url : "../DistributorWS/addDistributor.do",
		type : "POST",
		data : {
			name : name,
			type : type,
			linkUsername : linkUsername,
			linkPhone : linkPhone,
			distributorTag : distributorTag,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#distributorTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_addDistributorButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function editDistributorButton(){
	
	var distributor = $("#distributorTable").datagrid("getChecked")[0];
	if (xyzIsNull(distributor)) {
		top.$.messager.alert("提示", "请选择单个对象!", "info");
		return;
	}
	
	xyzdialog({
		dialog : 'dialogFormDiv_editDistributorButton',
		title : '编辑分销商',
	    href : '../jsp_distributor/editDistributor.html',
	    fit : false,  
	    width: 500,
	    height : 600,
	    buttons:[{
			text:'确定',
			handler:function(){
				editDistributorSubmit(distributor.numberCode);
			}
		},{
			text:'取消',
			handler:function(){
				$("#dialogFormDiv_editDistributorButton").dialog("destroy");
			}
		}],
		onLoad : function() {
			xyzTextbox("nameForm");
			$("#typeForm").combobox({
				required:true,
		    	panelHeight:'auto',
		    	panelMaxHeight:500,
				data: [{value: 'PERSONAL',text: '个人'},
				{value: 'PLATFORM',text: '平台'},
				{value: 'noIgnore',text: '旅行社'},
				{value: 'loudan',text: '其他'}],
		    });
			xyzTextbox("linkUsernameForm");
			xyzTextbox("linkPhoneForm");
			xyzCombobox({
				required:true,
				combobox : 'distributorTagForm',
				url : '../ListWS/getDistributorTagList.do',
				mode: 'remote',
				lazy : false
			});
			
			$("#nameForm").textbox("setValue",distributor.name); 
			$("#linkUsernameForm").textbox("setValue",distributor.linkUsername);
			$("#linkPhoneForm").textbox("setValue",distributor.linkPhone);
			$("#typeForm").combobox("select",distributor.type);
			$("#distributorTagForm").combobox("select",distributor.distributorTag);
			$("#remarkForm").val(distributor.remark);

		}
	});
}

function editDistributorSubmit(numberCode){
	
	if(!$("form").form('validate')){
		return false;
	}
	
	var name = $("#nameForm").val();
	var type = $("#typeForm").combobox("getText");
	var linkUsername = $("#linkUsernameForm").val();
	var linkPhone = $("#linkPhoneForm").val();
	var distributorTag = $("#distributorTagForm").combobox("getValue");
	var remark = $("#remarkForm").val();
	
	var phoneReg = /^1[0-9]{10}$/;
	if(!phoneReg.test(linkPhone)){
		top.$.messager.alert("提示","手机号格式不正确!","info");
		return false;
	}
	
	$.ajax({
		url : "../DistributorWS/editDistributor.do",
		type : "POST",
		data : {
			numberCode : numberCode,
			name : name,
			type : type,
			linkUsername : linkUsername,
			linkPhone : linkPhone,
			distributorTag : distributorTag,
			remark : remark
		},
		async : false,
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				$("#distributorTable").datagrid("reload");
				top.$.messager.alert("提示", "操作成功!", "info");
				$("#dialogFormDiv_editDistributorButton").dialog("destroy");
			} else {
				top.$.messager.alert("警告", data.msg, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
		}
	});
}

function deleteDistributorButton(){
	
	var distributor = $("#distributorTable").datagrid("getChecked")[0];
	if (xyzIsNull(distributor)) {
		top.$.messager.alert("提示", "请选择单个对象!", "info");
		return;
	}
	$.messager.confirm('确认', '您确认想要删除记录吗？',function(r) {
		 if(r){
			  $.ajax({
				url : "../DistributorWS/deleteDistributor.do",
				type : "POST",
				data : {
					numberCodes : distributor.numberCode
				},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.status == 1) {
						$("#distributorTable").datagrid("reload");
						top.$.messager.alert("提示", "操作成功!", "info");
						$("#dialogFormDiv_editDistributorButton").dialog("destroy");
					} else {
						top.$.messager.alert("警告", data.msg, "warning");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					top.window.AjaxError(XMLHttpRequest, textStatus, errorThrown);
				}
			});
		 }
	});
}




