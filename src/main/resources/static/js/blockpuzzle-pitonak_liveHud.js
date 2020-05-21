var hudInterval = setInterval(function () {
    getScore();
    getSeconds();
    getState();
}, 500);

function getScore() {
    return $.ajax({
        url: '/blockpuzzle-pitonak/score',
        dataType: 'json',
        success: function(score) {
            $('#live-score').text(score);
        },
        async: false
    });
}

function getSeconds() {
    return $.ajax({
        url: '/blockpuzzle-pitonak/seconds',
        dataType: 'json',
        success: function(seconds) {
            $('#live-seconds').text(seconds);
        },
        async: false
    });
}

function getState() {
    return $.ajax({
        url: '/blockpuzzle-pitonak/state',
        dataType: 'json',
        success: function(state) {
            if (state == 1) {
                clearInterval(hudInterval);
                clearInterval(stateInterval);
            }
        },
        async: false
    });
}