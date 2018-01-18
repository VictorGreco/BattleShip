       // drag and drop functions outside the 'ready'

        var shipsForPost = [{'type': 'Aircraft Carrier', 'shipPositions': null},
                            {'type': 'Battleship', 'shipPositions': null},
                            {'type': 'Submarine', 'shipPositions': null},
                            {'type': 'Destroyer', 'shipPositions': null},
                            {'type': 'Patrol Boat', 'shipPositions': null}];

        function allowDrop(ev) {
               ev.preventDefault();
            }

        function dragStart(ev) {
//             $('#'+ev.path[0].id+'').addClass('dragging');
            ev.dataTransfer.setData("text", ev.target.id);

        }
        function drag(ev){
            $('#'+ev.path[0].id+'').hide();

        }

        function drop(ev) {
            ev.preventDefault();
            var cellId = ev.target.id;
            var shipId =  ev.dataTransfer.getData("text");
//            $('#'+shipId+'').removeClass('dragging');
            $('#'+shipId+'').show();
            if(!$('#'+cellId+'').hasClass('hangar')){
                cellId = cellId.split('_');
                var shipLength = 0;
                var exclusionLetters = [];
                var setOfPositions = [];
                var letterArray = ['A','B','C','D','E','F','G','H','I','J'];
                shipId == 'drag0' ? (shipLength = 5 , exclusionLetters = ['G','H','I','J']): '';
                shipId == 'drag1' ? (shipLength = 4 , exclusionLetters = ['H','I','J']) : '';
                shipId == 'drag2' ? (shipLength = 3 , exclusionLetters = ['I','J']) : '';
                shipId == 'drag3' ? (shipLength = 3 , exclusionLetters = ['I','J']) : '';
                shipId == 'drag4' ? (shipLength = 2 , exclusionLetters = ['J']) : '';
                var idNumber = cellId[1].split('');
                var cellLetter = cellId[1].split('')[0];
                var cellNum = cellId[1].split('')[1];
                if($('#'+shipId+'').hasClass('dispHorizontal')){
                    idNumber.length == 3 ? idNumber = idNumber[1]+idNumber[2] : idNumber = idNumber[1];
                    if(1<=idNumber && idNumber <=(10-shipLength)+1){
                        ev.target.appendChild(document.getElementById(shipId));
                        for (let i = 0; i < shipLength; i++){
                            var number = +cellNum + i;
                            var position = cellLetter + number;
                            setOfPositions.indexOf(position) == -1 ? setOfPositions.push(position) : '';
                        }
                    }
                }else{
                    var holder = 'true';
                    $(exclusionLetters).each(function(){
                        idNumber[0] == this ? holder = 'false' : '';
                    });
                    holder == 'true' ? ev.target.appendChild(document.getElementById(shipId)) : '';
                    var posOfLetterInArray = letterArray.indexOf(cellLetter);
                    if( posOfLetterInArray != -1 && holder == 'true'){
                        for(let i = 0; i < shipLength; i++ ){
                            var cellLetter = letterArray[posOfLetterInArray+i];
                            var position = cellLetter + cellNum;
                            setOfPositions.indexOf(position) == -1 ? setOfPositions.push(position) : '';
                        }
                    }
                }
            }else{
                ev.target.appendChild(document.getElementById(shipId));
            }
            shipId == 'drag0' ?  shipsForPost[0].shipPositions = setOfPositions: '';
            shipId == 'drag1' ?  shipsForPost[1].shipPositions = setOfPositions: '';
            shipId == 'drag2' ?  shipsForPost[2].shipPositions = setOfPositions: '';
            shipId == 'drag3' ?  shipsForPost[3].shipPositions = setOfPositions: '';
            shipId == 'drag4' ?  shipsForPost[4].shipPositions = setOfPositions: '';
            console.log(shipsForPost);
        }
        $('#ships-confirm').click(function(){
            console.log(shipsForPost);
            var allowPlacement = 0;
            $(shipsForPost).each(function(){
                var currentPositionList = this.shipPositions;
                var currentType = this.type;
                currentPositionList != undefined ? allowPlacement++ : '';

            });
//            allowPlacement === 5 ? console.log('ready to place'): console.log('Don"t forget to place ships');
            if(allowPlacement === 5){
                var gp = window.location.href.split('=')[1];
                console.log('ready to place');
                $.post({
                  url: "/api/games/players/"+ gp+"/ships",
                  data: JSON.stringify(shipsForPost),
                  dataType: "text",
                  contentType: "application/json"
                })
                .done(function (response, status, jqXHR) {
                       window.location.reload();
                })
                .fail(function (jqXHR, status, httpError) {
                  alert("Failed to add pet: " + textStatus + " " + httpError);
                })
            }else{
                console.log('Don"t forget to place ships');
            }
        });
    //end of drag and drop features
$(function(){
// url used to getJSON call.
    var url = "";
    url == "" ? url = window.location.href.split('=') : "";
    gpValue = url[1];
    url = "http://localhost:8080/api/game_view/"+ gpValue+"";

//[STARTS RANDOM BACKGROUND]
    var images = ['background-image1.jpg', 'background-image2.jpg', 'background-image3.jpg', 'background-image4.jpg'];
    $('body').css({'background-image': 'url(resources/' + images[Math.floor(Math.random() * images.length)] + ')'})
             .css({'background-size':'cover',
                   'background-repeat': 'no-repeat'});
//[ENDS RANDOM BACKGROUND]

//[START CALLS]
    $.getJSON(url, function(data){

    if (data.Error === "ERROR"){
        unauthorizedPage();
    }else{
        getPlayersInfo(data);
        getGrid("myShipGrid");
        getGrid("notMySalvoGrid");
        getGrid("modalShipsGrid");
        colorGrid(data);
        onClickSomeTd();
        $('#placeShipsBtn').click(placeShips);
        getHangar();
        }
    });
//[END CALLS]


function unauthorizedPage(){
    var row = $('<section>', {'class': 'row'});
    var col = $('<article>',{'class': 'col-lg-12'});
    var messageBox = $('<div>',{'id': 'errorMessage'});
    var message = 'Cheat is not allowed';

    messageBox.append(message);
    col.append(messageBox);
    row.append(col);
    $('#main').html(row);
}

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

    function wantToFire(place, tableId){
        tableId == "notMySalvoGrid" ? alert("Do you want to fire on: " +place + " ?") : '';
        tableId == "MySalvoGrid" ? alert("Can't fire on your grid") : '';
    }

//[FUNCTIONS]

    function placeShips(){

    }

    function postShips(){
       var url = window.location.href.split('=');
       $.post({
         url: '/api/games/players/'+url[1]+'/ships',
         data: JSON.stringify([ { "type": "destroyer", "shipPositions": ["A1", "B1", "C1"] },
         { "type": "patrol boat", "shipPositions": ["H5", "H6"] }
       ]),
         dataType: "text",
         contentType: "application/json"
       })
       .done(function (response, status, jqXHR) {
         window.location.reload();

       })
       .fail(function (jqXHR, status, httpError) {
         alert("Failed to add pet:"+ httpError);
       })
    }
    function getGrid(tableId){
        $("#"+tableId)
            .append(appendTr_thead())
            .append(appendTr_tbody( tableId));
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

    function appendTr_tbody(tableId){
        var x = $('<tbody>');
        var headArray = ["A","B","C","D","E","F","G","H","I","J"];
        $(headArray).each(function(){
            x.append(appendTd_Tr(this, tableId));
            });
        return x;
    }

    function appendTd_Tr(current, tableId){
        var x = $('<tr>');
        if(tableId == 'modalShipsGrid'){
            var cellClass = 'cell';
        }
            x.append($('<td>').append(current));
            for (let i = 1; i <= 10; i++){
                i= ""+i +"";
                x.append($('<td>',{
                "id": tableId+"_"+current+ i,
                'class': cellClass,
                'ondrop': 'drop(event)',
                'ondragover': 'allowDrop(event)'
                }));
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
                var currLoc = $(grid+this);
                currLoc.addClass(theClass);
                currLoc.hasClass('shipMiddle')== true && currLoc.hasClass('enemySalvo') == true ? currLoc.addClass('hit') : "";
                currLoc.hasClass('shipPopa')== true && currLoc.hasClass('enemySalvo') == true ? currLoc.addClass('hit') : "";
                currLoc.hasClass('shipProa')== true && currLoc.hasClass('enemySalvo') == true ? currLoc.addClass('hit') : "";
            });
        });
    }

    function colorMyShipsLocation(data){
        $(data.Ships).each(function(){
            var shipLocation = this["location"];
            var prePosNumber= 0;
            var verificationHolder = "";
            for(let i = 0; i< shipLocation.length; i++){
                i == 0 ? $('#myShipGrid_'+shipLocation[i]).addClass('shipProa'): "";
                i == shipLocation.length - 1 ? $('#myShipGrid_'+shipLocation[i]).addClass('shipPopa') : "";
                i != shipLocation.length - 1 && i != 0 ? $('#myShipGrid_'+shipLocation[i]).addClass('shipMiddle') : "";
                var currentPosNumber = shipLocation[i].split("");
                currentPosNumber = currentPosNumber[1];
                prePosNumber != 0 && prePosNumber === currentPosNumber ?  verificationHolder = "vertical" : verificationHolder = "horizontal";
                prePosNumber = currentPosNumber;
            }
            $(shipLocation).each(function(){
                verificationHolder === "vertical" ? $('#myShipGrid_' + this).addClass('vertical'): $('#myShipGrid_' + this).addClass('horizontal');
            });
       });
    }

    function getHangar(){
        for(var i = 0; i<5; i++){
            var hangar = $('<div>',{
                            'id': 'hangar'+i+'',
                            'class': 'hangar',
                            'ondrop': 'drop(event)',
                            'ondragover': 'allowDrop(event)',
                        });
             i == 0 ? shipClass = 'aircraft' : '';  i == 1 ? shipClass = 'battleship' : ''; i == 2 ? shipClass = 'submarine' : '';
             i == 3 ? shipClass = 'destroyer' : ''; i == 4 ? shipClass = 'petrolBoat' : '';
            var ship = $('<div>', {
                            'id': 'drag'+i+'',
                            'class': shipClass + ' dispHorizontal',
                            draggable: 'true',
                            ondragstart: 'dragStart(event)',
                            ondrag: 'drag(event)',
                            click: function(event){
                                var targetId = event.currentTarget.attributes[0].nodeValue;
                                var shipParentId = $('#'+targetId+'').parent()[0].id;
                                var shipParentIdLetter = shipParentId.split('_')[1].split('')[0];
                                var shipParentIdNum = shipParentId.split('_')[1].split('');
                                var holder = 'true';
                                 var setOfPositions = [];
                                var letterArray = ['A','B','C','D','E','F','G','H','I','J'];
                                shipParentIdNum.length == 3 ? shipParentIdNum = shipParentIdNum[1]+shipParentIdNum[2] : shipParentIdNum = shipParentIdNum[1];
                                var shipLength = 0; var exclusionLetters = [];
                                targetId == 'drag0' ? (shipLength = 5 , exclusionLetters = ['G','H','I','J']): '';
                                targetId == 'drag1' ? (shipLength = 4 , exclusionLetters = ['H','I','J']) : '';
                                targetId == 'drag2' ? (shipLength = 3 , exclusionLetters = ['I','J']) : '';
                                targetId == 'drag3' ? (shipLength = 3 , exclusionLetters = ['I','J']) : '';
                                targetId == 'drag4' ? (shipLength = 2 , exclusionLetters = ['J']) : '';
                                $(exclusionLetters).each(function(){
                                    shipParentIdLetter == this ? holder = 'false' : '';
                                });
                                if($('#'+targetId+'').hasClass('dispHorizontal') && holder != 'false'){
                                    $('#'+targetId+'').removeClass('dispHorizontal').addClass('dispVertical');
                                    var width = $('#'+targetId+'').css('width');
                                    var height = $('#'+targetId+'').css('height');
                                     $('#'+targetId+'').css({'width':height,'height':width});

                                }else{
                                    if(1<=shipParentIdNum && shipParentIdNum <=(10-shipLength)+1 && holder !='false'){
                                        $('#'+targetId+'').removeClass('dispVertical').addClass('dispHorizontal');
                                        var width = $('#'+targetId+'').css('width');
                                        var height = $('#'+targetId+'').css('height');
                                        $('#'+targetId+'').css({'width':height,'height':width});
                                    }
                                }
                            }
            }).css({'position': 'absolute'});
            $('#hangar').append($(hangar).append(ship));
        }
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