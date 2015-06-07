/**
 * Creates an object with methods for template support.
 *
 * See <a href="http://confluence.atlassian.com/display/AUI/AJS.template">CAC Documentation</a>.
 *
 * @constructor
 * @class template
 * @namespace AJS
 */
AJS.template = (function ($) {
    var tokenRegex = /\{([^\}]+)\}/g, // matches "{xxxxx}"
        objNotationRegex = /(?:(?:^|\.)(.+?)(?=\[|\.|$|\()|\[('|")(.+?)\2\])(\(\))?/g, // matches ".xxxxx" or "["xxxxx"]" to run over object properties
        escapingRegex = /[<>"'&]/g, // matches HTML characters that need to be escaped
        apos = /([^\\])'/g, // matches not escaped apostrophes.
        
        // internal function
        // parses "{xxxxx}" and returns actual value from the given object that matches the expression
        replacer = function (all, key, obj, isHTML) {
            var res = obj;
            key.replace(objNotationRegex, function (all, name, quote, quotedName, isFunc) {
                name = name || quotedName;
                if (res) {
                    if (name + ":html" in res) {
                        res = res[name + ":html"];
                        isHTML = true;
                    } else if (name in res) {
                        res = res[name];
                    }
                    if (isFunc && typeof res == "function") {
                        res = res();
                    }
                }
            });
            // if not found restore original value
            if (res == null || res == obj) {
                res = all;
            }
            res = String(res);
            if (!isHTML) {
                res = T.escape(res);
            }
            return res;
        },
        /**
         * Escapes HTML characters
         * @private
         * @method escaper
         */
        escaper = function (chr) {
            return "&#" + chr.charCodeAt() + ";";
        },
        /**
         * Replaces tokens in the template with corresponding values without HTML escaping
         * @method fillHtml
         * @param obj {Object} to populate the template with
         * @return {Object} the template object
         */
        fillHtml = function (obj) {
            this.template = this.template.replace(tokenRegex, function (all, key) {
                return replacer(all, key, obj, true);
            });
            return this;
        },
        /**
         * Replaces tokens in the template with corresponding values with HTML escaping
         * @method fill
         * @param obj {Object} to populate the template with
         * @return {Object} the template object
         */
        fill = function (obj) {
            this.template = this.template.replace(tokenRegex, function (all, key) {
                return replacer(all, key, obj);
            });
            return this;
        },
        /**
         * Returns the current templated string.
         * @method toString
         * @return {String} the current template
         */
        toString = function () {
            return this.template;
        };

    // internal function
    var T = function (s) {
        function res() {
            return res.template;
        }

        /**
         * The current templated string
         * @property template
         */
        res.template = String(s);
        res.toString = res.valueOf = toString;
        res.fill = fill;
        res.fillHtml = fillHtml;
        return res;
    },
    cache = {},
    count = [];

    // returns template taken form the script tag with given title. Type agnostic, but better put type="text/x-template"
    T.load = function (title) {
        title = String(title);
        if (!cache.hasOwnProperty(title)) {
            count.length >= 1e3 && delete cache[count.shift()]; // enforce maximum cache size
            count.push(title);
            cache[title] = $("script[title='" + title.replace(apos, "$1\\'") + "']")[0].text;
        }
        return this(cache[title]);
    };
    // escape HTML dangerous characters
    T.escape = function (s) {
        return String(s).replace(escapingRegex, escaper);
    };
    return T;
})(window.jQuery);
