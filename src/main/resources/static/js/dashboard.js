// JavaScript actions for the dashboard page
// Read server IP and Secure Token String
var vroServer = readCookie('vro_ip');
var vroToken = readCookie('vro_credentials');

// Get Data from REST API when page is loaded.
$( document ).ready(function() {
  GetVroVersion(vroServer,vroToken);
});

// Non jQuery functions
// Cookie management
function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name,"",-1);
}

// Parse data
function writeVersion(data){
    document.getElementById('result').innerHTML = data.version;
}