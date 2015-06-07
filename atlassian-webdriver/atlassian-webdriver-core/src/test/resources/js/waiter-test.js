
(function($){

    function delay(func) {
        setTimeout(func, 500);
    }

    $(function() {
        $("#dialog-one-show-button").click(function() {
            delay(function() {
                $("#dialog-one").show();
            });
        });

        $("#dialog-two-hide-button").click(function() {
            delay(function(){
                $("#dialog-two").hide();
            });
        });

        $("#dialog-three-create-button").click(function() {
            delay(function(){
                var el = $("<div id='dialog-three'>dialog 3 contents</div>");
                $("#dialog-three-container").append(el);
            });
        });

        $("#dialog-four-remove-button").click(function() {
            delay(function(){
                $("#dialog-four").remove();
            });
        });

        $("#dialog-five-select-button").click(function() {
            delay(function(){
                $("#dialog-five").click();
            });
        });

        $("#dialog-six-select-button").click(function() {
            delay(function(){
                $("#dialog-six").click();
            });
        });

        $("#dialog-seven-enable-button").click(function() {
            delay(function(){
                $("#dialog-seven").attr('disabled','');
            });
        });

        $("#dialog-eight-disable-button").click(function() {
            delay(function(){
                $("#dialog-eight").attr('disabled','disabled');
            });
        });

        $("#dialog-nine-addclass-button").click(function() {
            delay(function(){
                $("#dialog-nine").addClass("awesome");
            });
        });

        $("#dialog-ten-removeclass-button").click(function() {
            delay(function(){
                $("#dialog-ten").removeClass("awesome");
            });
        });

        $("#dialog-eleven-addtext-button").click(function() {
            delay(function(){
                $("#dialog-eleven").text("Dialog eleven");
            });
        });

        $("#dialog-twelve-addattribute-button").click(function() {
            delay(function(){
                $("#dialog-twelve").attr('data-test','value');
            });
        });



        $("#dialog-thirteen-addattributeandclass-button").click(function() {
            delay(function(){
                $("#dialog-thirteen").text('Dialog thirteen');
                delay(function() {
                    $("#dialog-thirteen").addClass('awesome');
                });
            });
        });

    });

})(jQuery);