function loadinfo(){
    $("#appwindow").load("infocard.html");
}

function loadtests(){
    $("#appwindow").load("bloodtests.html");
}

function loadrendezvous(){
    $("#appwindow").load("rendezvous.html");
}

function loaddoctors(){
    $("#appwindow").load("doctors.html");
}

function loadmessages(){
    $("#appwindow").load("messages.html");
}

const excludedMessages = [];

function checkForDonation() {
    if(userdata["blooddonor"] !== 1) {
        return;
    }
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if(xhr.status === 200 && xhr.readyState === 4) {
            var reply = JSON.parse(xhr.responseText);
            for(let i in reply["messages"]) {
                var msg = JSON.parse(reply["messages"][i]);
                if(!(excludedMessages.includes(msg["message_id"]))) {
                    excludedMessages.push(msg["message_id"]);

                    alert(msg["message"]);
                }
            }
        }
    }

    xhr.open("GET", "/hy359_project_war_exploded/MessagesAPI?bloodtype="+userdata["bloodtype"]);
    xhr.send();
}


// window.setInterval(function () {
//     checkForDonation();
// }, 5000);

$(document).ready(function () {
    checkUserLoggedIn("user");
    loadinfo();

});