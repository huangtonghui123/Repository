var roleName = null;
var saveBtn = null;
var backBtn = null;

$(function(){
	userName = $("#userName");
	saveBtn = $("#save");
	backBtn = $("#back");

	$.ajax({
		type:"GET",//请求类型
		url:path+"/jsp/user.do",//请求的url
		data:{method:"getrolelist"},//请求参数
		dataType:"json",//ajax接口（请求url）返回的数据类型
		success:function(data){//data：返回数据（json对象）
			if(data != null){
				var rid = $("#rid").val();
				userRole.html("");
				var options = "<option value=\"0\">请选择</option>";
				for(var i = 0; i < data.length; i++){
					//alert(data[i].id);
					//alert(data[i].roleName);
					if(rid != null && rid != undefined && data[i].id == rid ){
						options += "<option selected=\"selected\" value=\""+data[i].id+"\" >"+data[i].roleName+"</option>";
					}else{
						options += "<option value=\""+data[i].id+"\" >"+data[i].roleName+"</option>";
					}

				}
				userRole.html(options);
			}
		},
		error:function(data){//当访问时候，404，500 等非200的错误状态码
			validateTip(userRole.next(),{"color":"red"},imgNo+" 获取用户角色列表error",false);
		}
	});


	roleName.on("focus",function(){
		validateTip(roleName.next(),{"color":"#666666"},"* 用户名长度必须是大于1小于10的字符",false);
	}).on("blur",function(){
		if(roleName.val() != null && roleName.val().length > 1
				&& roleName.val().length < 10){
			validateTip(roleName.next(),{"color":"green"},imgYes,true);
		}else{
			validateTip(roleName.next(),{"color":"red"},imgNo+" 用户名输入的不符合规范，请重新输入",false);
		}
		
	});

	saveBtn.on("click",function(){
		/*userName.blur();
		phone.blur();
		birthday.blur();
		userRole.blur();
		if(userName.attr("validateStatus") == "true" 
			&& phone.attr("validateStatus") == "true"
			&& birthday.attr("validateStatus") == "true"
			&& userRole.attr("validateStatus") == "true"){*/
			if(confirm("是否确认要提交数据？")){
				$("#userForm").submit();
			}
		// }
	});
	
	backBtn.on("click",function(){
		//alert("modify: "+referer);
		if(referer != undefined 
			&& null != referer 
			&& "" != referer
			&& "null" != referer
			&& referer.length > 4){
		 window.location.href = referer;
		}else{
			history.back(-1);
		}
	});
});