(function(window, undefined) {

ATLWD.byJquery = {};

ATLWD.byJquery.execute = function(jq, context) {
    console.log("ATLWD.byJquery.execute: " + jq + ", context:" + context);

    var result = ATLWD.$.makeArray(eval(jq));

    console.log("execute result: ");
    console.log(result);
    
    return result;
};

ATLWD.byJquery.executeOne = function(jq, context) {
    console.log("ATLWD.byJquery.executeOne: " + jq + ", context:" + context);
    var result = eval(jq)[0];

    console.log("executeOne result: " + result);
    return result;
};

})(window);