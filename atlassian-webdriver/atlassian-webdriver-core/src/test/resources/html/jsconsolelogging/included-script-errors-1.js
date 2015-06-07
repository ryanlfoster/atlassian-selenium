(function($) {
    var result = 1;
    result += $("h1").text();
    $("h1").text(result);
})(document);