let userdata= {"lat":0,"lon":0};

function updateCachedInfo(jsonreply){
    usercachedinfo["gender"]=jsonreply["gender"];
    usercachedinfo["height"]=jsonreply["height"];
    usercachedinfo["weight"]=jsonreply["weight"];
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
    xhr.open('POST', '/Project_war_exploded/UserAPI');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(data));

}

function showlogin(){
    if($("#loginform").is(":visible")){
        $("#loginform").hide();
    }else{
        $("#loginform").show();
    }
}

function loadregister(){
    $("#appwindow").load("register.html");
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
                if(jsonreply["usertype"]===3){//0 unknown,1 user,2 doctor,3 admin
                    window.location.href = "admin";
                }else if(jsonreply["usertype"]===2){
                    window.location.href = "doctor";
                }else{
                    window.location.href = "user";
                }
            }
        }else if(xhr.readyState === 4 && xhr.status === 403){
            $("#ajaxContent").html("<p style='color:red'>Username or password is incorrect!</p>");
        }
    }

    // console.log("sent data");
    // console.log(data);
    xhr.open('POST', 'Login');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(data));
}

function logout(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.href = "/Project_war_exploded"

        } else if (xhr.status !== 200) {
            console.log('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('POST', '/Project_war_exploded/Logout');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function checkUserLoggedIn(usertype){
    var xhr = new XMLHttpRequest();
    xhr.onload = function (){
        if(xhr.readyState === 4 && xhr.status === 200){
            userdata = JSON.parse(xhr.responseText);
            if(userdata["usertype"]!=usertype){logout();}
        }else if(xhr.status!=200){
            window.location.href = "/Project_war_exploded";
            console.log("autologin failed with status code: "+xhr.status);
        }
    }

    xhr.open('POST', '/Project_war_exploded/GetInfo');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send("");
}

