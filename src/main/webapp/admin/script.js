function loadusers(){
    $("#appwindow").load("editusers.html");
}

function loadcertify(){
    $("#appwindow").load("certify.html");
}

$(document).ready(function () {
    checkUserLoggedIn("admin");
});