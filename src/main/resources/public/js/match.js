/**
 * Created by ThanhND20 on 12/23/2016.
 */
$(function () {
	var currentListMatch;
	var playerCounts;
    $('.list-group.checked-list-box .list-group-item').each(function () {

        // Settings
        var $widget = $(this),
            $checkbox = $('<input type="checkbox" class="hidden" />'),
            color = ($widget.data('color') ? $widget.data('color') : "primary"),
            style = ($widget.data('style') == "button" ? "btn-" : "list-group-item-"),
            settings = {
                on: {
                    icon: 'glyphicon glyphicon-check'
                },
                off: {
                    icon: 'glyphicon glyphicon-unchecked'
                }
            };

        $widget.css('cursor', 'pointer')
        $widget.append($checkbox);

        // Event Handlers
        $widget.on('click', function () {
            var numItems = $('.glyphicon-check').length

            if (numItems > 7 && !$checkbox.is(':checked')) {
                alert("Hết slot rồi nhé !!!")
                return;
            }

            $checkbox.prop('checked', !$checkbox.is(':checked'));
            $checkbox.triggerHandler('change');
            updateDisplay();
        });
        $checkbox.on('change', function () {
            updateDisplay();
        });


        // Actions
        function updateDisplay() {
            var isChecked = $checkbox.is(':checked');

            // Set the button's state
            $widget.data('state', (isChecked) ? "on" : "off");

            // Set the button's icon
            $widget.find('.state-icon')
                .removeClass()
                .addClass('state-icon ' + settings[$widget.data('state')].icon);

            // Update the button's color
            if (isChecked) {
                $widget.addClass(style + color + ' active');
            } else {
                $widget.removeClass(style + color + ' active');
            }
            
            playerCounts = $('.glyphicon-check').length
            var countText = "Điểm danh: " + playerCounts;
            $playerCountsLabel = $("#players-count");
            $playerCountsLabel.text(countText);
            if (playerCounts == 8) {
            	$playerCountsLabel.css('color', 'DarkRed');
            } else {
            	$playerCountsLabel.css('color', 'black');
            }
        }

        // Initialization
        function init() {

            if ($widget.data('checked') == true) {
                $checkbox.prop('checked', !$checkbox.is(':checked'));
            }

            updateDisplay();

            // Inject the icon if applicable
            if ($widget.find('.state-icon').length == 0) {
                $widget.prepend('<span class="state-icon ' + settings[$widget.data('state')].icon + '"></span>');
            }
        }
        init();
    });
    var keoText1;
    var keoText2;

    $('#get-checked-data').on('click', function(event) {
        event.preventDefault();
        var checkedItems = {}, counter = 0;
        var idPlayer = "";

        $("#check-list-box li.active").each(function(idx, li) {

            idPlayer = idPlayer + "," + $(li).text();
            counter++;
        });

        checkedItems['id'] = idPlayer;

        console.log(JSON.stringify(checkedItems));
        $.ajax({
            url : '/chiakeo',
            type: "POST",
            dataType : 'json',
            data : JSON.stringify(checkedItems),
            contentType: "application/json",
            success : function(data) {
                console.log(data);
                currentListMatch = data.matchList;
                keokhacCounter = 0;
                showMatch(data.matchList[0]);
            }
        });
        //$('#display-json').html(JSON.stringify(checkedItems, null, '\t'));
    });

    var keokhacCounter = 0;
    var keoText;
    $('#get-next-match').on('click', function(event) {
        event.preventDefault();
        keokhacCounter ++;
        if (keokhacCounter >= currentListMatch.length) {
            keokhacCounter = 0;
        }
        showMatch(currentListMatch[keokhacCounter]);

    });
    
    $('#get-last-match').on('click', function(event) {
        event.preventDefault();
        keokhacCounter --;
        if (keokhacCounter < 0) {
            keokhacCounter = currentListMatch.length - 1;
        }
        showMatch(currentListMatch[keokhacCounter]);

    });

    $('.match').on('click', function(event) {
        event.preventDefault();
        var team1 = keoText1[0].name;
        var team2 = keoText2[0].name;
        for(var i = 1; i <keoText1.length; i++){
            team1 = team1 + " " + keoText1[i].name;
        }for(var i = 1; i <keoText2.length; i++){
            team2 = team2 + " " + keoText2[i].name;
        }

        var match;
        match = team1 + " vs " + team2;
        console.log(match);
        copyToClipboard(match);
    });
    
    $('#update-score').on('click', function(event) {
        event.preventDefault();
        var inputScore = $('#input-score').val();
        var request = {};
        request['score'] = inputScore;
        request['match'] = currentListMatch[keokhacCounter];
        $.ajax({
            url : '/score',
            type: "POST",
            dataType : 'json',
            data : JSON.stringify(request),
            contentType: "application/json",
            success : function(data) {
                console.log(data);
                if (data.status == "SUCCESS") {
                	alert("Cập nhật thành công!");
                } else {
                	alert("Có lỗi trong quá trình cập nhật !!");
                }
            }
        });
    });
    
    $('#history-match').on('click', function(event) {
        event.preventDefault();
        var request = {};
        request['match'] = currentListMatch[keokhacCounter];
        $(".history-match-result").empty();
        $.ajax({
            url : '/history/match',
            type: "POST",
            dataType : 'json',
            data : JSON.stringify(request),
            contentType: "application/json",
            success : function(data) {
                console.log(data);
                var text = data.text;
                $('.history-match-result').append(text)
            }
        });
    });

    function copyToClipboard(text) {
        window.prompt("Copy to clipboard: Ctrl+C, Enter", text);
    }
    
    function showGroupPlayers() {
    	
    }
    function showMatch(match) {
    	var team1 = match.team1;
        var team2 = match.team2;

        var players1 = team1.players;
        var players2 = team2.players;

        keoText1 = players1;
        keoText2 = players2;

        $(".group-team1").empty();
        $(".group-team2").empty();
        $(".group-match").empty();
        
        for(var i = 0; i < players1.length; i++) {
            var button = "<button type='button' class='playera btn btn-success'>" + players1[i].name + " <span class='badge'> " + players1[i].playerPoint.toFixed(1) + " </span></button>";
            $('.group-team1').append(button)
        }

        for(var i = 0; i < players2.length; i++) {
            var button = "<button type='button' class='btn btn-success playerb'>" + players2[i].name + " <span class='badge'> " + players2[i].playerPoint.toFixed(1) + " </span></button>";
            $('.group-team2').append(button)
        }

        var button = "<button type='button' class='btn btn-danger match'>" + match.matchPoint.toFixed(4);"</button>";
        $('.group-match').append(button)

        $('.group-team1').focus();
    }
});
