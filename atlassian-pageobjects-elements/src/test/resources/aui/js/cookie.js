(function () {

    // Cookie handling functions

    var COOKIE_NAME = "AJS.conglomerate.cookie";

    function getValueFromConglomerate(name, cookieValue) {
        // a null cookieValue is just the first time through so create it
        cookieValue = cookieValue || "";
        var reg = new RegExp(name + "=([^|]+)"),
            res = cookieValue.match(reg);
        return res && res[1];
    }

    //either append or replace the value in the cookie string
    function addOrAppendToValue(name, value, cookieValue) {
        var reg = new RegExp("\\s*" + name + "=[^|]+(\\||$)");

        cookieValue = cookieValue || "";
        cookieValue = cookieValue.replace(reg, "") + (cookieValue ? "|" : "");
        
        if (value) {
            var pair = name + "=" + value;
            if (cookieValue.length + pair.length < 4020) {
                cookieValue += pair;
            }
        }
        return cookieValue;
    }

    function getCookieValue(name) {
        var reg = new RegExp(name + "=([^;]+)"),
            res = document.cookie.match(reg);
        return res && res[1];
    }

    function saveCookie(name, value, days) {
      var ex = "";
      if (days) {
          var d = new Date();
          d.setTime(+d + days * 24 * 60 * 60 * 1000);
          ex = "; expires=" + d.toGMTString();
      }
      document.cookie = name + "=" + value + ex + ";path=/";
    }

    /**
     * The Atlassian Conglomerate Cookie - to let us use cookies without running out.
     * @class Cookie
     * @namespace AJS
     */
    AJS.Cookie = {
        /**
         * Save a cookie.
         * @param name {String} name of cookie
         * @param value {String} value of cookie
         * @param expires {Number} number of days before cookie expires
         */
        save : function (name, value, expires) {
            var cookieValue = getCookieValue(COOKIE_NAME);
            cookieValue = addOrAppendToValue(name, value, cookieValue);
            saveCookie(COOKIE_NAME, cookieValue, expires || 365);
        },
        /**
         * Get the value of a cookie.
         * @param name {String} name of cookie to read
         * @param defaultValue {String} the default value of the cookie to return if not found
         */
        read : function(name, defaultValue) {
            var cookieValue = getCookieValue(COOKIE_NAME);
            var value = getValueFromConglomerate(name, cookieValue);
            if (value != null) {
                return value;
            }
            return defaultValue;
        },
        /**
         * Remove the given cookie.
         * @param name {String} the name of the cookie to remove
         */
        erase: function (name) {
            this.save(name, "");
        }
    };
    
})();

