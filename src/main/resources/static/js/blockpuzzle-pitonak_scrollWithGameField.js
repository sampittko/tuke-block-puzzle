$(function() {

    var $sidebar   = $("#is-game-field"),
        $window    = $(window),
        offset     = $sidebar.offset(),
        topPadding = 70;

    $window.scroll(function() {
        if ($window.scrollTop() > offset.top) {
            $sidebar.stop().animate({
                marginTop: $window.scrollTop() - offset.top + topPadding
            });
        } else {
            $sidebar.stop().animate({
                marginTop: 0
            });
        }
    });
});