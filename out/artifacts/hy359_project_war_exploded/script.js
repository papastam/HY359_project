function loaddoctors(){
    $("#appwindow").load("availableDoctors.html");
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
            $("#registerReply").html("<p style='color:green'>Your successfully registered</p>");
        } else if (xhr.status !== 200) {
            $("#registerReply").html("<p style='color:red'>Couldn't Register! Server responded with status code of: "+xhr.status+" </p><p>"+xhr.responseText+"</p>");
        }
    };
    delete data["cpassword"];
    delete data["eulaagree"];
    data["address"]+=" "+data["addrnumber"];
    delete data["addrnumber"];

    data["lat"]=userdata["lat"];
    data["lon"]=userdata["lon"];

    // console.log("sent data");
    console.log(JSON.stringify(data));
    xhr.open('POST', 'Register');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(data));
}

$(document).ready(function(){
    loaddoctors();
});