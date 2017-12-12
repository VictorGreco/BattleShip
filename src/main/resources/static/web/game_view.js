$(function(){
    var url = "http://localhost:8080/api/game_view/1";
    $.getJSON(url, function(data){
        console.log(data);

        getGrid(data);

       $(data.Ships).each(function(){
            $(this["location"]).each(function(){
                $('#'+this).addClass('ship');
            });
       });




    });
    // ************************     FUNCTIONS    ********************
    function getGrid(data){
        $('#gridTable')
            .append(appendTr_thead())
            .append(appendTr_tbody(data));
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

    function appendTr_tbody(data){
        var x = $('<tbody>');
        var headArray = ["A","B","C","D","E","F","G","H","I","J"];
        $(headArray).each(function(){
            x.append($('<tr>').append($('<td>').append(this))
                              .append($('<td>',{"id": this + "1"}))
                              .append($('<td>',{"id": this + "2"}))
                              .append($('<td>',{"id": this + "3"}))
                              .append($('<td>',{"id": this + "4"}))
                              .append($('<td>',{"id": this + "5"}))
                              .append($('<td>',{"id": this + "6"}))
                              .append($('<td>',{"id": this + "7"}))
                              .append($('<td>',{"id": this + "8"}))
                              .append($('<td>',{"id": this + "9"}))
                              .append($('<td>',{"id": this + "10"})));
        });
        console.log(data);
        return x;
    }

    function appendTd_Tr_Tbody(){
        var x = $('<td>');

        for (var i = 0; i<10; i++){
            x.append('1');
        }

        return x;

    }

})