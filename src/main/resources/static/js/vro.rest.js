// vRealize REST Api Calls
function ConnectToVro(ip,user,pass){ 
    var concreds = btoa(user + ":" + pass);
    $.ajax({ 
        type: "GET",
        dataType: "json",
        url: "https://" + ip + ":8281/vco/api/workflows/",
        beforeSend : function(xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + concreds);
        },
        success: function(){ 
            document.getElementById('result').innerHTML = "";
            $('form').fadeOut(500);
            $('.wrapper').addClass('form-success');
            setTimeout(function(){        
                createCookie('vro_ip',ip,1);
                createCookie('vro_credentials',concreds,1);
                window.location.replace("dashboard.html");            
            }, 1000);    
	},
	error: function(){
            document.getElementById('result').innerHTML = "Error connection to vRealize Orchestrator";	
	}
    });
}

function GetVroVersion(ip,token){
    $.ajax({ 
        type: "GET",
        dataType: "json",
        url: "https://" + ip + ":8281/vco/api/about",
	beforeSend : function(xhr) {
            xhr.setRequestHeader("Authorization", "Basic " + token);
        },
	success: function(data){ 
            writeVersion(data);
	},
	error: function(jqXHR){
            document.getElementById('result').innerHTML = JSON.stringify(jqXHR);	
	}
    });
}
