$(function(){
    var url = "";
    url == "" ? url = window.location.href : "";
    urlSplited = url.split("=");
    gpValue = urlSplited[1];
    url = "http://localhost:8080/api/game_view/"+ gpValue+"";

//[STARTS RANDOM BACKGROUND]
    var images = ['background-image1.jpg', 'background-image2.jpg', 'background-image3.jpg', 'background-image4.jpg'];
    $('body').css({'background-image': 'url(resources/' + images[Math.floor(Math.random() * images.length)] + ')'})
             .css({'background-size':'cover',
                   'background-repeat': 'no-repeat'});
//[ENDS RANDOM BACKGROUND]

//[START CALLS]
    $.getJSON(url, function(data){
        getPlayersInfo(data);
        getGrid(data, "myShipGrid");
        getGrid(data, "notMySalvoGrid");
        colorGrid(data);
        onClickSomeTd();
    });
//[END CALLS]

//[USER INTERACTION FUNCTIONS]
    function onClickSomeTd(){
    var clickedCellNumber;
    var clickedTable;
    var clickedTdID;
        $('td').click(function(){
             clickedItemID = this.id.split('_');
             clickedCellNumber = clickedItemID[1];
             clickedTable = clickedItemID[0];

            wantToFire(clickedCellNumber, clickedTable);

        });
    }

    function wantToFire(place, tableIdent){
        console.log(place);
        console.log(tableIdent);
        tableIdent == "notMySalvoGrid" ? alert("Do you want to fire on: " +place) : alert("You can't fire on your grid!");
    }

//[FUNCTIONS]
    function getGrid(data, tableId){
        tableId = tableId+"";
        $("#"+tableId)
            .append(appendTr_thead())
            .append(appendTr_tbody(data, tableId));
    }

    function appendTr_thead(){
        var x = $('<thead>');
        var y = $('<tr>');
        var headArray = [" ","1","2","3","4","5","6","7","8","9","10"];
        x.append(y);
        $(headArray).each(function(){
            y.append($('<th>').append(this));
            });
        return x;
    }

    function appendTr_tbody(data, tableId){
        var x = $('<tbody>');
        var headArray = ["A","B","C","D","E","F","G","H","I","J"];
        $(headArray).each(function(){
            x.append(appendTd_Tr(this, tableId));
            });
        return x;
    }

    function appendTd_Tr(current, tableId){
        var x = $('<tr>');
            x.append($('<td>').append(current));
            for (let i = 1; i <= 10; i++){
                i= ""+i +"";
                x.append($('<td>',{"id": tableId+"_"+current+ i}));
            }
        return x;
    }

    function appendTd_Tr_Tbody(){
        var x = $('<td>');
        for (let i = 0; i<10; i++){
            x.append('1');
            }
        return x;
    }

    function colorGrid(data){
        colorMyShipsLocation(data);
        colorAllSalvoes(data);
    }

    function colorAllSalvoes(data){
        $(data.AllSalvOfGame).each(function(){
            this.gamePlayerId === data.id ? colorSalvoPJ_Place(this, '#notMySalvoGrid_', 'mySalvo')
            : colorSalvoPJ_Place(this, '#myShipGrid_', 'enemySalvo');
        });
    }

    function colorSalvoPJ_Place(player, grid, theClass){
        $(player.Salvoes).each(function(){
            var Locations = this["Locations"];
            var Turn = this["Turn"];
            $(Locations).each(function(){
                var currentLocation = $(grid+this);
                currentLocation.addClass(theClass);
                if(currentLocation.hasClass('ship enemySalvo')== true){
                    currentLocation.addClass('hit');
                }else{
                    if(currentLocation.hasClass('enemySalvo')==true){
                        currentLocation.addClass('notHit');
                    }
                }
            });
        });
    }

    function colorMyShipsLocation(data){
        $(data.Ships).each(function(){
            var shipLocation = this["location"];
            for(let i = 0; i< shipLocation.length; i++){
                i == 0 ? $('#myShipGrid_'+shipLocation[i]).addClass('shipProa'): "";
                i == shipLocation.length - 1 ? $('#myShipGrid_'+shipLocation[i]).addClass('shipPopa') : "";
                i != shipLocation.length - 1 && i != 0 ? $('#myShipGrid_'+shipLocation[i]).addClass('shipMiddle') : "";
            }
       });
    }

    function getPlayersInfo(data){
       $(data.GamePlayers).each(function(){
               if(data.id === this.id){
                   let ownerPlayer = this.Player["name"] + "(you)";
                   $('#displayPlayers').append($('<div>', {
                                                    "id": "ownerPlayer",
                                                    "class": "ownerPlayer",
                                                    }).append(ownerPlayer));
               }else{
                   let otherPlayer = this.Player["name"];
                   $('#displayPlayers').append($('<div>',{
                                                    "id": "notOwnerPlayer",
                                                    "class": "notOwnerPlayer",
                                                    }).append(otherPlayer));
               }
         });
         $("#displayPlayers > div:nth-child(1)").after($('<div>',{
                                                        "id": "vs",
                                                        "class": "vs",
                                                        }).append(' -VS- '));
   }
})