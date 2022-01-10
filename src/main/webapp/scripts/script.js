function loaddoctors(){
    $("#appfield").load("availableDoctors.html");
}

function showlogin(){
    if($("#loginform").is(":visible")){
        $("#loginform").hide();
    }else{
        $("#loginform").show();
    }
}

function loadregister(){
    $("#appfield").load("register.html");
}

function sendAjaxLoginPOST(event){
	event.preventDefault();
	let loginData = new FormData(document.getElementById('loginform'));
	const data = {};
	loginData.forEach((value, key) => (data[key] = value));

	var xhr = new XMLHttpRequest();
	xhr.onload = function (){
		if(xhr.readyState === 4 && xhr.status === 200){
			// console.log(xhr.responseText);
			var jsonreply = JSON.parse(xhr.responseText);
			console.log(jsonreply);
			if(jsonreply["logged_in"]){
				if(jsonreply["usertype"]==="Project_war_exploded/admin"){
					window.location.href = "Project_war_exploded/admin";
				}else if(jsonreply["usertype"]==="doctor"){
					window.location.href = "Project_war_exploded/doctor";
				}else{
					window.location.href = "Project_war_exploded/user";
				}
			}
		}else if(xhr.readyState === 4 && xhr.status === 403){
			$("#ajaxContent").html("<p style='color:red'>Username or password is incorrect!</p>");
		}
	}

	// console.log("sent data");
	// console.log("data");
	xhr.open('POST', 'Login');
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.send(JSON.stringify(data));
}

$(document).ready(function(){

});