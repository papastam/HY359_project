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
            if(!(excludedMessages.includes(reply["message_id"]))) {
                excludedMessages.push(reply["message_id"]);
                for(let i in reply["messages"]) {
                    var msg = JSON.parse(reply["messages"][i]);

                    alert(msg["message"]);
                }
            }
        }
    }

    xhr.open("GET", "/Project_war_exploded/MessagesAPI?bloodtype="+userdata["bloodtype"]);
    xhr.send();
}


window.setInterval(function () {
    checkForDonation();
}, 3000);

$(document).ready(function () {
    checkUserLoggedIn("user");
    loadinfo();

});