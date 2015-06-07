(function($){

    $(function() {
        $("#button-right").click(function() {
            $('<span id="button-right-clicked">Right button clicked!</span>').appendTo($("#messages"));
        });

        $("#button-bottom").click(function() {
            $('<span id="button-bottom-clicked">Bottom button clicked!</span>').appendTo($("#messages"));
        });
    });

})(jQuery);