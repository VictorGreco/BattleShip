$(function () {

//    var url = "http://localhost:8080/api/games";
    $.getJSON("http://localhost:8080/api/games", function (data) {
    console.log(data);
    console.log("JSON LOADED");
    getWelcome(data.user);
    createGamesList (data.games);
    getRankingsTable(data.leader_board);

    $('#logout').click(logout);
    $('#sign-in').click(function(){
        $('#btn-login').hide();
        $('#btn-signin').show().click(signIn);

        });
    $('#login').click(function(){
        $('#btn-signin').hide();
        $('#btn-login').show().click(login);
        });
    });
});

function signIn(){
    var email = $('#email').val();
    var psw = $('#password').val();
    var token = "";

    if (email == ""){
     $('#error_emailMissed').show();
      token = "false";
        }else{
            $('#error_emailMissed').hide();
            token = "true";
        }
    if ( psw == ""){
         $('#error_pswMissed').show();
          token = "false";
        }else{
          $('#error_pswMissed').hide();
          token = "true";
        }
    if ( psw.length < 6){
         $('#error_pswToShort').show();
          token = "false";
        }else{
          $('#error_pswToShort').hide();
          token = "true";
         }


    if( token === "true"){
        $.post("/api/players", { username: email, password: psw})
                .done(function(){ login(email, psw); window.location.reload()})
                .fail(function(){console.log('cant sign-in')});
    }


}

function validateEmail(email) {
    var emailRegex = '^[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}$';
    return email.match(emailRegex);
}

function login(email, psw){
    if(email == null || psw == null){
        email = $('#email').val();
        psw = $('#password').val();
    }

    $.post("/api/login", { username: email, password: psw})
        .done(function(){window.location.reload()})
        .fail(function(){console.log('cant login')});

}
function logout(){
    $.post("/api/logout").done(function() { console.log("logged out!"); })
    window.location.reload();
}
function getWelcome(data){
    if(data.name != null){
        $('#sign-in').hide();
        $('#login').hide();
        $('#userActions')
        .append($('<li>',{"id": "userWelcome"}).append($('<a>').append("Welcome: " + data.name)))
        .append($('<li>').append($('<a>',{"id":"logout"}).append("Logout").append($('<span>',{"class": "glyphicon glyphicon-log-out"}))));
    }
}

function createGamesList (data) {
    var ListID = 0;
    $(data).each(function () {
        var currentPlayer = "";
        var date =  new Date(this.create);
        $(this.GamePlayers).each(function(){
            currentPlayer += this.Player['name'] + " ";
        });
        currentPlayer = currentPlayer.split(" ");
        currentPlayer = currentPlayer[0]+ " -vs- " + currentPlayer[1];

        $('#gamesList').append($('<a>', {"id":"ListID"+ListID+"",
                                        "href":"#",
                                        "class": "list-group-item"
                                        })
                                        .append($('<span>').append(currentPlayer +" ("+ date.toDateString() +")")));
        ListID++
        });
}

function addLinksToGamesList(){
    var LinksList= [];
}

function getRankingsTable(datLb){
    var thead = $('<thead>');
    var tbody = $('<tbody>');
    var trH = $('<tr>');

    var headArray = ["UserName", "WinGames", "DrawDames" , "LostGames"];
    $(headArray).each(function(){
        trH.append($('<th>').append(this));
    });
    thead.append(trH);

    $(datLb).each(function(){
        var trB = $('<tr>');
        trB.append($('<td>').append(this.name))
           .append($('<td>').append(this.WinGames))
           .append($('<td>').append(this.DrawGames))
           .append($('<td>').append(this.LostGames))
        tbody.append(trB);
    });
    
    $('#rankings').append(thead).append(tbody);
}

