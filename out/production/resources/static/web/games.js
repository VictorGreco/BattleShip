$(function () {

//    var url = "http://localhost:8080/api/games";
    $.getJSON("http://localhost:8080/api/games", function (data) {
    console.log("JSON LOADED");
    getWelcome(data.User);
    createGamesList (data.Games);
    getRankingsTable(data.Games);
    });
});
function getWelcome(data){
    $('#welcomeCartel').append($('<h1>').append("Welcome: " + data.name + " ojla te parta un... digo Buenas xD"));
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

function getRankingsTable(data){
    var thead = $('<thead>');
    var tbody = $('<tbody>');

    var headers = ["Player","TotalScore","LostGames","DrawGmes","WinGames"]
    $(allPlayerList(data)).each(function(){
        var playerScores = playerTotalScoreAndGamesInfo(this+"", data);
        var tr = $('<tr>');
        tr.append($('<td>').append(this));
        getSingleScores(playerScores, tr);
        tbody.append(tr);
    });

    $(headers).each(function(){
        thead.append($('<th>').append(this+""));
    });
    $('#rankings').append(thead).append(tbody);
}

function getSingleScores(playerScores, tr){
    var totalPoints = playerScores[0];
    var totalLosts = playerScores[1];
    var totalDraws = playerScores[2];
    var totalWins = playerScores[3];

    tr
        .append($('<td>').append(totalPoints))
        .append($('<td>').append(totalLosts))
        .append($('<td>').append(totalDraws))
        .append($('<td>').append(totalWins));

    return tr;
}

function allPlayerList(data){
    var PlayerList =[];
    $(data).each(function(){
        $(this.GamePlayers).each(function(){
            PlayerList.indexOf(this.Player["name"]) == -1 ? PlayerList.push(this.Player["name"]) : "";
        });
    });
    return  PlayerList.sort();
}

function playerTotalScoreAndGamesInfo(wantedPlayer, data){
    var totalScore = 0;
    var lostGames = 0;
    var drawGames = 0;
    var winGames = 0;
    var scores = [];
     $(data).each(function(){
            $(this.Scores).each(function(){
                this["name"]===wantedPlayer ? totalScore += this["Points"]: "";
                this["name"]===wantedPlayer && this["Points"]==0 ? lostGames++ : "";
                this["name"]===wantedPlayer && this["Points"]==1 ? drawGames++ : "";
                this["name"]===wantedPlayer && this["Points"]==2 ? winGames++ : "";
            });
        });
        scores.push(totalScore);
        scores.push(lostGames);
        scores.push(drawGames);
        scores.push(winGames);
    return scores;
}

