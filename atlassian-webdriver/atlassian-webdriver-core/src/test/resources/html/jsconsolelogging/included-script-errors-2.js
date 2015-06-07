(function($) {
    var result = 1;
    throw Error("throw Error('bail')");
    result += $("h1").text();
    $("h1").text(result);
})(jQuery);