var usercachedinfo = {"logged_in":"","lat":0,"lon":0,"gender":"","height":0,"weight":0,"age":0};

function updateCachedInfo(jsonreply){
	usercachedinfo["gender"]=jsonreply["gender"];
	usercachedinfo["height"]=jsonreply["height"];
	usercachedinfo["weight"]=jsonreply["weight"];
	usercachedinfo["age"]=getAge(jsonreply["birthdate"]);
}

function getAge(dateString) {
	var today = new Date();
	var birthDate = new Date(dateString);
	var age = today.getFullYear() - birthDate.getFullYear();
	var m = today.getMonth() - birthDate.getMonth();
	if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
		age--;
	}
	return age;
}

function loadRegister(){
	$("#appfield").load("register.html");
}

function loadLogin(){
	$("#appfield").load("login.html");
}

function userlogedin(){
	$("#loginbtn").hide();
	$("#registerbtn").hide();
	$("#logoutbtn").show();
	$("#infobtn").show();
	$("#bmibtn").show();
	$("#doctorsbtn").show();

	showusrinfo();
}

function userlogedout(){

	var xhr = new XMLHttpRequest();
	xhr.onload = function () {
		if (xhr.readyState === 4 && xhr.status === 200) {
			$("#loginbtn").show();
			$("#registerbtn").show();
			$("#logoutbtn").hide();
			$("#infobtn").hide();
			$("#bmibtn").hide();
			$("#doctorsbtn").hide();

			loadLogin();
		} else if (xhr.status !== 200) {
			console.log('Request failed. Returned status of ' + xhr.status);
		}
	};

	xhr.open('POST', 'UserLogout');
	xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
	xhr.send();
}

function showusrinfo(){
	$("#appfield").load("infocard.html");
}

function showbmi(){
	$("#appfield").load("bmi.html");
}

function showdoctors(){
	$("#appfield").load("availableDoctors.html");
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
				$("#ajaxContent").html("<p style='color:green'>Succesfully logged in</p>");

				$("#loginbtn").hide();
				$("#registerbtn").hide();
				$("#logoutbtn").show();
				$("#infobtn").show();
				$("#bmibtn").show();
				$("#doctorsbtn").show();


				showusrinfo();
			}
		}else if(xhr.readyState === 4 && xhr.status === 403){
			$("#ajaxContent").html("<p style='color:red'>Username or password is incorrect!</p>");
		}
	}

	// console.log("sent data");
	// console.log("data");
	xhr.open('POST', 'UserLogin');
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.send(JSON.stringify(data));
}

function sendAjaxRegisterPOST(event){
	event.preventDefault();
	let myForm = document.getElementById('myForm');
	let formData = new FormData(myForm);
	const data = {};
	formData.forEach((value, key) => (data[key] = value));

	var xhr = new XMLHttpRequest();
	xhr.onload = function () {
		if (xhr.readyState === 4 && xhr.status === 200) {
			// console.log(xhr.responseText);
			$("#ajaxContent").html("<p style='color:green'>Your info was updated succesfully</p>");
		} else if (xhr.status !== 200) {
			$("#ajaxContent").html("<p style='color:red'>Your info wasn't updated! Server responded with status code of: "+xhr.status+" </p><p>"+xhr.responseText+"</p>");
		}
	};
	delete data["cpassword"];
	delete data["eulaagree"];
	data["address"]+=" "+data["addrnumber"];
	delete data["addrnumber"];

	data["lat"]=usercachedinfo["lat"];
	data["lon"]=usercachedinfo["lon"];

	updateCachedInfo(data);

	// console.log("sent data");
	// console.log(data);
	xhr.open('POST', 'Register');
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.send(JSON.stringify(data));
}

function sendAJAXUpdate(event) {
	event.preventDefault();
	let formData = new FormData(document.getElementById('editdataform'));
	const data = {};
	formData.forEach((value, key) => (data[key] = value));


	var xhr = new XMLHttpRequest();
	xhr.onload = function () {
		if (xhr.readyState === 4 && xhr.status === 200) {
			// console.log(xhr.responseText);

			$("#ajaxContent").html("<p style='color:green'>Your info was updated succesfully</p>");
		} else if (xhr.status !== 200) {
			$("#ajaxContent").html("<p style='color:red'>Your info wasn't updated! Server responded with status code of: "+xhr.status+" </p><p>"+xhr.responseText+"</p>");
		}
	};
	delete data["cpassword"];
	data["address"] += " " + data["addrnumber"];
	delete data["addrnumber"];
	data["username"] = usercachedinfo["logged_in"];

	data["lat"]=usercachedinfo["lat"];
	data["lon"]=usercachedinfo["lon"];

	updateCachedInfo(data);

	// console.log("sent data");
	// console.log(data);
	xhr.open('POST', 'UserAPI');
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.send(JSON.stringify(data));

}