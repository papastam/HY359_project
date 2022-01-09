function loadinfo(){
    $("#appwindow").load("infocard.html");
}

function loadtests(){
    $("#appwindow").load("bloodtests.html");
}

function loadrendezvous(){
    $("#appwindow").load("rendezvous.html");
}

function loadrendezvousform(){
    $("#rendezvouswindow").load("rendezvousform.html")
}

function loadpatients(){
    $("#appwindow").load("patients.html");
}

function loadmessages(){
    $("#appwindow").load("messages.html");
}

$(document).ready(function () {
    loadinfo();
    checkUserLoggedIn("doctor");
});