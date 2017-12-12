console.log('asdasd');

$(function () {

//    var url = "http://localhost:8080/api/games";
    $.getJSON("http://localhost:8080/api/games", function (data) {
    console.log("a");
    console.log(data);
    createGamesList (data);

    });
});


function createGamesList (data) {
    $(data).each(function () {
        var currentPlayer = "";
        $(this.GamePlayers).each(function(){
            currentPlayer += this.Player.userName + " ";
        });
        currentPlayer = currentPlayer.split(" ");
        currentPlayer = currentPlayer[0]+ " -vs- " + currentPlayer[1];
        console.log(currentPlayer);
        $('#gamesList').append($('<li>').append(currentPlayer));

        });
}