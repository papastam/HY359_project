let userdata= {"lat":0,"lon":0};

function updateuserdata(jsonreply){
    userdata["gender"]=jsonreply["gender"];
    userdata["height"]=jsonreply["height"];
    userdata["weight"]=jsonreply["weight"];
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

