       // drag and drop functions outside the 'ready'
var shipsForPost = [{'type': 'Carrier', 'shipPositions': null},
                    {'type': 'Battleship', 'shipPositions': null},
                    {'type': 'Submarine', 'shipPositions': null},
                    {'type': 'Destroyer', 'shipPositions': null},
                    {'type': 'Patrol Boat', 'shipPositions': null}];


function allowDrop(ev) {
       ev.preventDefault();
    }

function dragStart(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
    $('td').removeClass(ev.target.id);

}
function drag(ev){
    $('#'+ev.path[0].id+'').hide();

}
function drop(ev, finalPositions) {
    ev.preventDefault();
    var cellId = ev.target.id;
    var shipId =  ev.dataTransfer.getData("text");
    $('#'+shipId+'').show();
    var finalPositions = [];
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
                for (var i = 0; i < shipLength; i++){
                    var number = +cellNum + i;
                    var position = cellLetter + number;
                    setOfPositions.indexOf(position) == -1 ? setOfPositions.push(position) : '';
                }
                var allow = true;
                $(setOfPositions).each(function(){
                    $('#modalShipsGrid_'+this).hasClass('drag0') ? allow = false: '';
                    $('#modalShipsGrid_'+this).hasClass('drag1') ? allow = false: '';
                    $('#modalShipsGrid_'+this).hasClass('drag2') ? allow = false: '';
                    $('#modalShipsGrid_'+this).hasClass('drag3') ? allow = false: '';
                    $('#modalShipsGrid_'+this).hasClass('drag4') ? allow = false: '';
                });
                if(allow){
                    $('td').removeClass(shipId);
                    $(setOfPositions).each(function(){
                        $('#modalShipsGrid_'+this).addClass(shipId);
                    });
                   ev.target.appendChild(document.getElementById(shipId));
                   shipId == 'drag0' ?  shipsForPost[0].shipPositions = setOfPositions: '';
                   shipId == 'drag1' ?  shipsForPost[1].shipPositions = setOfPositions: '';
                   shipId == 'drag2' ?  shipsForPost[2].shipPositions = setOfPositions: '';
                   shipId == 'drag3' ?  shipsForPost[3].shipPositions = setOfPositions: '';
                   shipId == 'drag4' ?  shipsForPost[4].shipPositions = setOfPositions: '';
                }
            }
        }else{
            var allow = true;
            var holder = true;
            $(exclusionLetters).each(function(){
                idNumber[0] == this ? holder = false : '';
            });
            var posOfLetterInArray = letterArray.indexOf(cellLetter);
            if( posOfLetterInArray != -1 && holder){
                for(var i = 0; i < shipLength; i++ ){
                    var cellLetter = letterArray[posOfLetterInArray+i];
                    var position = cellLetter + cellNum;
                    setOfPositions.indexOf(position) == -1 ? setOfPositions.push(position) : '';
                }
                 $(setOfPositions).each(function(){
                    $('#modalShipsGrid_'+this).hasClass('drag0') ? allow = false: '';
                    $('#modalShipsGrid_'+this).hasClass('drag1') ? allow = false: '';
                    $('#modalShipsGrid_'+this).hasClass('drag2') ? allow = false: '';
                    $('#modalShipsGrid_'+this).hasClass('drag3') ? allow = false: '';
                    $('#modalShipsGrid_'+this).hasClass('drag4') ? allow = false: '';
                });
                if(allow){
                $('td').removeClass(shipId);
                $(setOfPositions).each(function(){
                    $('#modalShipsGrid_'+this).addClass(shipId);
                });
                ev.target.appendChild(document.getElementById(shipId));
                shipId == 'drag0' ?  shipsForPost[0].shipPositions = setOfPositions: '';
                shipId == 'drag1' ?  shipsForPost[1].shipPositions = setOfPositions: '';
                shipId == 'drag2' ?  shipsForPost[2].shipPositions = setOfPositions: '';
                shipId == 'drag3' ?  shipsForPost[3].shipPositions = setOfPositions: '';
                shipId == 'drag4' ?  shipsForPost[4].shipPositions = setOfPositions: '';
                }
            }
        }
    }else{
        $('td').removeClass(shipId);
        ev.target.appendChild(document.getElementById(shipId));
        shipId == 'drag0' ?  shipsForPost[0].shipPositions = null: '';
        shipId == 'drag1' ?  shipsForPost[1].shipPositions = null: '';
        shipId == 'drag2' ?  shipsForPost[2].shipPositions = null: '';
        shipId == 'drag3' ?  shipsForPost[3].shipPositions = null: '';
        shipId == 'drag4' ?  shipsForPost[4].shipPositions = null: '';
    }
}

 $('#ships-confirm').click(function(){
            var allowPlacement = 0;
            $(shipsForPost).each(function(){
                var currentPositionList = this.shipPositions;
                var currentType = this.type;
                currentPositionList != undefined ? allowPlacement++ : '';
            });
            if(allowPlacement === 5){
                var gp = window.location.href.split('=')[1];
                $.post({
                  url: "/api/games/players/"+ gp +"/ships",
                  data: JSON.stringify(shipsForPost),
                  dataType: "text",
                  contentType: "application/json"
                })
            }
        });
       function mutate(audioId){
           var x = document.getElementById(audioId);
           x.pause();
           $('#mutate').hide();
           $('#speaker').show();
       }

       function myPlay(audioId, loop){
           var x = document.getElementById(audioId);
           x.loop = loop;
           x.play();
           $('#speaker').hide();
           $('#mutate').show();

       }
//end of drag and drop features
$(function(){
    var url = "";
    url == "" ? url = window.location.href.split('=') : "";
    gpValue = url[1];
    url = "http://localhost:8080/api/game_view/"+ gpValue+"";

//[STARTS RANDOM BACKGROUND]
    var images = ['background-image1.jpg', 'background-image3.jpg', 'background-image4.jpg'];
    $('body').css({'background-image': 'url(resources/' + images[Math.floor(Math.random() * images.length)] + ')'})
             .css({'background-size':'cover',
                   'background-repeat': 'no-repeat'});
//[ENDS RANDOM BACKGROUND]

//[START CALLS]
    var salvoPosition = [];
    var myData;
    $.getJSON(url, function(data){
    })
    .done(function(data){
        authorizedPage(data);
        myPlay('warAudio', true);
        myData = data;
        $('td').click(function(clickData){
            var targetId = clickData.currentTarget.id;
            var clickedCellNumber = targetId.split('_')[1];
            var clickedTable = targetId.split('_')[0];
            if(myData.OK.gameStatus == 'youPlay'){
                wantToFire(clickedCellNumber, clickedTable, salvoPosition);
            }
        });
    })
    .fail(function(){
        unauthorizedPage();
    });

    $('body').keypress(function(ev){
        if(ev.originalEvent.which == 13 && $('#inputChat').val()){
            var gp = window.location.href.split('=')[1];
            $.post({
                url: "/api/games/players/"+ gp +"/chat",
                data: JSON.stringify($('#inputChat').val()),
                dataType: "text",
                contentType: "application/json"
            }).done(function(){
                $('#inputChat').val("");
            })
        }
    });

   setInterval(function(){
        $.getJSON(url, function(data){
        })
        .done(function(data){
            statusAllowed(data);
            myData = data;
        })
        .fail(function(){
            unauthorizedPage();
        });
    }, 2000)


//[END CALLS]

function authorizedPage(data){
    getGrid("myShipGrid");
    getGrid("notMySalvoGrid");
    getGrid("modalShipsGrid");
    getPlayersInfo(data.OK);
    statusAllowed(data);
    getHangar();
}
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
function statusAllowed(data){
     if(data.OK.gameStatus == 'w84UrShips'){
            $('#placeShipsBtn').show();
        }else{
            if(data.OK.gameStatus == 'w84Opp'){
                $('#placeShipsBtn').hide();
                colorGrid(data.OK);
            }else{
                if(data.OK.gameStatus == 'w84OppShips'){
                    $('#placeShipsBtn').hide();
                    colorGrid(data.OK);
                    getChatSystem(data.OK.history);
                }else{
                    if(data.OK.gameStatus == 'youPlay'){
                        $('#placeShipsBtn').hide();
                        colorGrid(data.OK);
                        getChatSystem(data.OK.history);
                        getChatUsers(data.OK);
                    }else{
                        if(data.OK.gameStatus == 'OppPlay'){
                            $('#placeShipsBtn').hide();
                            colorGrid(data.OK);
                            getChatSystem(data.OK.history);
                            getChatUsers(data.OK);
                        }else{
                            data.OK.gameStatus == 'youLose' ? endGame('youLose') : '';
                            data.OK.gameStatus == 'youWin' ? endGame('youWin') : '';
                        }
                    }
                }
            }
        }
}


//[USER INTERACTION FUNCTIONS]
    function endGame(result){
        var message = '';
        result == 'youLose' ? (message = 'youLose', score = 0) : (message = 'youWin', score = 2);
        var $message = $('<h1>').addClass('message').append(message);
        var $div = $('<div>').addClass('endGame').append($message);
        $('#gridInterface').html($div);
        var gp = window.location.href.split('=')[1];
        $.post({
          url: "/api/games/players/"+ gp +"/score",
          data: JSON.stringify(score),
          dataType: "text",
          contentType: "application/json"
        })
    }
function wantToFire(CellNumber, clickedTable, salvoPosition){
    var cellId = clickedTable+"_"+CellNumber;
    var indexOfCell = salvoPosition.indexOf(CellNumber);
    if(salvoPosition.length <= 2){
        if(!$('#'+cellId).hasClass('futureFire') && indexOfCell == -1 && !$('#'+cellId).hasClass('hit') && !$('#'+cellId).hasClass('fail')){
            $('#'+cellId).addClass('futureFire');
            salvoPosition.push(CellNumber);

        }else{
            $('#'+cellId).removeClass('futureFire');
             salvoPosition.splice(indexOfCell);
        }
        salvoPosition.length == 3 ? postSalvoes(salvoPosition) : '';
    }else{
        $(salvoPosition).each(function(){
            $('#'+clickedTable+'_'+this).removeClass('futureFire');
            var expulse = salvoPosition.indexOf(this);
            salvoPosition.splice(expulse);
        });
    }

};
//[FUNCTIONS]
function getChatUsers(data){
    $('#playerMessage').html('');
    var textClass;

    $(data.chat).each(function(){
        $('#playerMessage').append($('<p>', {'text':this.message,
                                              'class': textClass}));
    });

}
function postSalvoes(salvoPosition){
    var allow = false;
    var fireLocations = "";
    $(salvoPosition).each(function(){
        fireLocations += this + "  ";
    });
    confirm("Do you want to fire on "+fireLocations+" ?") ? allow = true : allow = false;
    if(allow){
        var url = window.location.href.split('=');
        $.post({
             url: '/api/games/players/'+url[1]+'/salvos',
             data: JSON.stringify(salvoPosition),
             dataType: "text",
             contentType: "application/json"
           }).done(function(){
                $(salvoPosition).each(function(){
                    $('#notMySalvoGrid_'+this).addClass('fired');
                    $('#notMySalvoGrid_'+this).removeClass('futureFire');
                });
                salvoPosition = [];
           })
    }
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
        for (var i = 1; i <= 10; i++){
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
    for (var i = 0; i<10; i++){
        x.append('1');
        }
    return x;
}

function colorGrid(dataOk){
    colorMyShipsLocation(dataOk);
    colorAllSalvoes(dataOk.history);
}

function colorAllSalvoes(data){
    $(data).each(function(){
        if(this.playerTurn == "me"){
            $(this.shots).each(function(){
                var location = '#notMySalvoGrid_' + this.location;
                this.status ? $(location).addClass('hit') : $(location).addClass('fail');
            });
        }else{
             $(this.shots).each(function(){
                var location = '#myShipGrid_' + this.location;
                this.status ? $(location).addClass('hit') : $(location).addClass('fail');
            });
        }
    });
}

function colorMyShipsLocation(data){
    $(data.Ships).each(function(){
        var shipLocation = this["location"];
        var prePosNumber= 0;
        var verificationHolder = "";
        for(var i = 0; i< shipLocation.length; i++){
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
         i == 0 ? shipClass = 'carrier' : '';  i == 1 ? shipClass = 'battleship' : ''; i == 2 ? shipClass = 'submarine' : '';
         i == 3 ? shipClass = 'destroyer' : ''; i == 4 ? shipClass = 'patrolBoat' : '';
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
                            var holder = true;

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
                                shipParentIdLetter == this ? holder = false : '';
                            });
                            if($('#'+targetId+'').hasClass('dispHorizontal') && holder){
                                var posOfLetterInArray = letterArray.indexOf(shipParentIdLetter);

                                for(var i = 0; i < shipLength; i++ ){
                                    var cellLetter = letterArray[posOfLetterInArray+i];
                                    var position = cellLetter + shipParentIdNum;
                                    setOfPositions.indexOf(position) == -1 ? setOfPositions.push(position) : '';
                                }
                                var allow = true;
                                $(setOfPositions).each(function(){
                                    $('#modalShipsGrid_'+this).hasClass('drag0') && 'drag0' !=  targetId ? allow = false: '';
                                    $('#modalShipsGrid_'+this).hasClass('drag1') && 'drag1' !=  targetId? allow = false: '';
                                    $('#modalShipsGrid_'+this).hasClass('drag2') && 'drag2' !=  targetId? allow = false: '';
                                    $('#modalShipsGrid_'+this).hasClass('drag3') && 'drag3' !=  targetId? allow = false: '';
                                    $('#modalShipsGrid_'+this).hasClass('drag4') && 'drag4' !=  targetId? allow = false: '';
                                });
                                if(allow){
                                    $('td').removeClass(targetId);
                                    $(setOfPositions).each(function(){
                                        $('#modalShipsGrid_'+this).addClass(targetId);
                                    });
                                   targetId == 'drag0' ?  shipsForPost[0].shipPositions = setOfPositions: '';
                                   targetId == 'drag1' ?  shipsForPost[1].shipPositions = setOfPositions: '';
                                   targetId == 'drag2' ?  shipsForPost[2].shipPositions = setOfPositions: '';
                                   targetId == 'drag3' ?  shipsForPost[3].shipPositions = setOfPositions: '';
                                   targetId == 'drag4' ?  shipsForPost[4].shipPositions = setOfPositions: '';

                                   $('#'+targetId+'').removeClass('dispHorizontal').addClass('dispVertical');
                                   var width = $('#'+targetId+'').css('width');
                                   var height = $('#'+targetId+'').css('height');
                                   $('#'+targetId+'').css({'width':height,'height':width});
                                }


                            }else{
                                if(1<=shipParentIdNum && shipParentIdNum <=(10-shipLength)+1 && holder){
                                    for (var i = 0; i < shipLength; i++){
                                        var number = +shipParentIdNum + i;

                                        var position = shipParentIdLetter + number;
                                        setOfPositions.indexOf(position) == -1 ? setOfPositions.push(position) : '';
                                    }

                                    var allow = true;
                                    $(setOfPositions).each(function(){
                                        $('#modalShipsGrid_'+this).hasClass('drag0') && 'drag0' !=  targetId ? allow = false: '';
                                        $('#modalShipsGrid_'+this).hasClass('drag1') && 'drag1' !=  targetId ? allow = false: '';
                                        $('#modalShipsGrid_'+this).hasClass('drag2') && 'drag2' !=  targetId ? allow = false: '';
                                        $('#modalShipsGrid_'+this).hasClass('drag3') && 'drag3' !=  targetId ? allow = false: '';
                                        $('#modalShipsGrid_'+this).hasClass('drag4') && 'drag4' !=  targetId ? allow = false: '';
                                    });
                                    if(allow){
                                        $('td').removeClass(targetId);
                                        $(setOfPositions).each(function(){
                                            $('#modalShipsGrid_'+this).addClass(targetId);
                                        });
                                       targetId == 'drag0' ?  shipsForPost[0].shipPositions = setOfPositions: '';
                                       targetId == 'drag1' ?  shipsForPost[1].shipPositions = setOfPositions: '';
                                       targetId == 'drag2' ?  shipsForPost[2].shipPositions = setOfPositions: '';
                                       targetId == 'drag3' ?  shipsForPost[3].shipPositions = setOfPositions: '';
                                       targetId == 'drag4' ?  shipsForPost[4].shipPositions = setOfPositions: '';

                                       $('#'+targetId+'').removeClass('dispVertical').addClass('dispHorizontal');
                                       var width = $('#'+targetId+'').css('width');
                                       var height = $('#'+targetId+'').css('height');
                                       $('#'+targetId+'').css({'width':height,'height':width});
                                    }
                                }
                            }
                        }
        }).css({'position': 'absolute'});
        $('#hangar').append($(hangar).append(ship));
    }
}
function getChatSystem(data){
var x = 0;
$('#systemMessage').html('');
$(data).each(function(){
    var systemMessage;
    var player;
    var hits = '';
//    let shipStatusMessage = 'Sunk Ships:';
    if(this.playerTurn == 'me'){
        player= 'you';
//        $(this.enemyShipStatus).each(function(){
//            this.ShipStatus ? shipStatusMessage += " "+ this.ShipType : " ";
//        });
    }else{
        player = 'enemy';
//        $(this.myShipStatus).each(function(){
//            this.ShipStatus ? shipStatusMessage += " "+ this.ShipType : " ";
//        });
    }
    $(this.shots).each(function(){
        this.status ? hits += this.location+ " " : "";

    });

    if(hits != ''){
        systemMessage ="On Turn "+this.turnNumber +" " + player + " hit a ship on "+ hits;
    }else{
        systemMessage ="On Turn "+this.turnNumber +" " + player + " fail all the shots";
    }


    $('#systemMessage').append($('<p>', {'class':'systemMessage'}).append(systemMessage));
});
    var divHeight = $('#systemMessage').css('height').split('p')[0];
    $('#systemMessage').scrollTop(divHeight+1);
}
function getPlayersInfo(data){
   $(data.GamePlayers).each(function(){
           if(data.id === this.id){
               var ownerPlayer = this.Player["name"] + "(you)";
               $('#displayPlayers').append($('<div>', {
                                                "id": "ownerPlayer",
                                                "class": "ownerPlayer",
                                                }).append(ownerPlayer));
           }else{
               var otherPlayer = this.Player["name"];
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