// JavaScript actions for the index page
$("#login-button").click(function(event){
    // override default behaviour
    event.preventDefault();	 
    document.getElementById('result').innerHTML = "Connecting...";
    
    // verify if user can connect to vRo Server
    ConnectToVro($("#vroserver").val(),$("#username").val(),$("#password").val());
});

$("#vroserver").focus(function(){
   document.getElementById('result').innerHTML = ""; 
});

$("#username").focus(function(){
   document.getElementById('result').innerHTML = ""; 
});

$("#password").focus(function(){
   document.getElementById('result').innerHTML = ""; 
});


// Non jQuery functions
// Cookie management
function createCookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

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