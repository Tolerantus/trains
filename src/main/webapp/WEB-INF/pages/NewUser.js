/**
 * 
 */
function validate(){
	reset();
	var user = $('#username').val();
	var pas1 = $('#password1').val();
	var pas2 = $('#password2').val();
	var patt = new RegExp("^[A-Za-z0-9_]{4,15}$");
	var error = false;
	if (user==""||!patt.test(user)){
		$('#username').addClass('error-box');
		$('#login-div').attr("title","Invalid user name");
		error = true;
	}
	if (pas1==""||!patt.test(pas1)){
		$('#password1').addClass('error-box');
		$('#pass-div1').attr("title","Invalid password");
		error = true;
	}
	
	if (pas2==""||!patt.test(pas2)){
		$('#password2').addClass('error-box');
		$('#pass-div2').attr("title","Invalid password");
		error = true;
	}
	if (pas1!=pas2){
		$('#password1').addClass('error-box');
		$('#password2').addClass('error-box');
		$('#pass-div2').attr("title","Passwords not match");
		$('#pass-div1').attr("title","Passwords not match");
		error = true;
	}
	if (!error){
		$('#reg').submit();
	}
}
function reset(){
	$(".status").empty();
	$('#username').removeClass('error-box');
	$('#password1').removeClass('error-box');
	$('#password2').removeClass('error-box');
	$('#pass-div2').removeAttr("title");
	$('#pass-div1').removeAttr("title");
	
}
