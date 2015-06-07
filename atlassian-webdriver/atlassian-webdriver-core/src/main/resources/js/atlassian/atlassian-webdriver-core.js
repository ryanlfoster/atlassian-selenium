/**
 * Core script that defines ATLWD namespace and performs additional initialization.
 *
 */
(function(window) {
    var ATLWD = window.ATLWD || {};

    var oldjQuery = window.jQuery;
    var old$ = window.$;

    // Silence console calls if there is no console.
    if(typeof window.console !== 'object') {
        window.console = {
            log: function() {}, alert: function() {}, warn: function() {}, info: function() {},
            time: function() {}, timeEnd: function() {}, error: function() {}
        };
    }

    // util functions
    ATLWD.loadJquery = function() {
        ATLWD.$ = $.noConflict();

        if (oldjQuery) {
            window.jQuery = oldjQuery;
        }

        if (old$) {
            window.$ = old$;
        }
    };

    ATLWD.arrayContains = function(array, obj) {
        for (var i = 0; i < array.length; i++) {
            if (array[i] === obj) {
                return true;
            }
        }
        return false;
    };

    window.ATLWD = ATLWD;

})(window);

