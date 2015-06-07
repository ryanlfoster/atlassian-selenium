
/**
 * A collection of common controls
 *
 * @module Controls
 * @requires AJS, jQuery, jQuery.myKeys
 */

/**
 * Keyboard commands with syntactic sugar.
 *
 * <strong>Usage:</strong>
 * <pre>
 * AJS.whenIType("gh").or("gd").goTo("/secure/Dashboard.jspa");
 * AJS.whenIType("c").click("#create_link");
 * </pre>
 *
 * @class whenIType
 * @constuctor whenIType
 * @namespace AJS
 * @param keys - Key combinations, modifier keys are "+" deliminated. e.g "ctrl+b"
 */

AJS.whenIType = function (keys) {


    var executer,

        bindKeys = function (keys) {

            keys = keys.toString();

            jQuery(document).bind('keypress', keys, function () {
                if (!AJS.popup.current  && executer) {
                    executer();
                }
            });

            // Override browser/plugins
            jQuery(document).bind('keypress keyup', keys, function (e) {
                e.preventDefault();
            });

        },

        addShortcutTitle = function (selector) {
            var elem = jQuery(selector),
                title = elem.attr("title") || "",
                keyArr = keys.split("");

            if (elem.data("kbShortcutAppended")) {
                appendShortcutTitle(elem, keyArr, title);
                return;
            }

            title += " ( " + AJS.params.keyType + " '" + keyArr.shift() + "'";
            jQuery.each(keyArr, function () {
                title += " " + AJS.params.keyThen + " '" + this + "'";
            });
            title += " )";
            elem.attr("title", title);
            elem.data("kbShortcutAppended", true);
        },

        appendShortcutTitle = function (elem, keyArr, title) {
            title = title.replace(/\)$/, " OR ");
            title += "'" + keyArr.shift() + "'";
            jQuery.each(keyArr, function () {
                title += " " + AJS.params.keyThen + " '" + this + "'";
            });
            title += " )";
            elem.attr("title", title);
        };

    bindKeys(keys);

    return {

        /**
         * Scrolls to and adds <em>focused</em> class to the next item in the jQuery collection
         *
         * @method moveToNextItem
         * @param selector
         */
        moveToNextItem: function (selector) {
            executer = function () {
                var index,
                    items = jQuery(selector),
                    focusedElem = jQuery(selector + ".focused");

                if (!executer.blurHandler) {
                    jQuery(document).one("keypress", function (e) {
                        if (e.keyCode === jQuery.ui.keyCode.ESCAPE && focusedElem) {
                            focusedElem.removeClass("focused");
                        }
                    });
                }

                if (focusedElem.length === 0) {
                    focusedElem = jQuery(selector).eq(0);
                } else {
                    focusedElem.removeClass("focused");
                    index = jQuery.inArray(focusedElem.get(0), items);
                    if (index < items.length-1) {
                        index = index +1;
                        focusedElem = items.eq(index);
                    } else {
                        focusedElem.removeClass("focused");
                        focusedElem = jQuery(selector).eq(0);
                    }
                }
                if (focusedElem && focusedElem.length > 0) {
                    focusedElem.addClass("focused");
                    focusedElem.moveTo();
                    focusedElem.find("a:first").focus();
                }
            };
        },
        /**
         * Scrolls to and adds <em>focused</em> class to the previous item in the jQuery collection
         *
         * @method moveToPrevItem
         * @param selector
         */
        moveToPrevItem: function (selector) {
            executer = function () {

                var index,
                    items = jQuery(selector),
                    focusedElem = jQuery(selector + ".focused");

                if (!executer.blurHandler) {
                    jQuery(document).one("keypress", function (e) {
                        if (e.keyCode === jQuery.ui.keyCode.ESCAPE && focusedElem) {
                            focusedElem.removeClass("focused");
                        }
                    });
                }

                if (focusedElem.length === 0) {
                    focusedElem = jQuery(selector + ":last");
                } else {

                    focusedElem.removeClass("focused");
                    index = jQuery.inArray(focusedElem.get(0), items);
                    if (index > 0) {
                        index = index -1;
                        focusedElem = items.eq(index);
                    } else {
                        focusedElem.removeClass("focused");
                        focusedElem = jQuery(selector + ":last");
                    }
                }
                if (focusedElem && focusedElem.length > 0) {
                    focusedElem.addClass("focused");
                    focusedElem.moveTo();
                    focusedElem.find("a:first").focus();
                }
            };
        },

        /**
         * Clicks the element specified by the <em>selector</em> argument.
         *
         * @method click
         * @param selector - jQuery selector for element
         */
        click: function (selector) {

            addShortcutTitle(selector);

            executer = function () {
                var elem = jQuery(selector);
                if (elem.length > 0) {
                    elem.click();
                }
            };
        },

        /**
         * Navigates to specified <em>location</em>
         *
         * @method goTo
         * @param {String} location - http location
         */
        goTo: function (location) {

            executer = function () {
                window.location.href = location;
            };
        },

        /**
         * navigates browser window to link href
         *
         * @method followLink
         * @param selector - jQuery selector for element
         */
        followLink: function (selector) {

            addShortcutTitle(selector);

            executer = function () {
                var elem = jQuery(selector);
                if (elem.length > 0 && elem.attr("nodeName").toLowerCase() === "a") {
                    window.location.href = elem.attr("href");
                }
            };
        },

        /**
         * Executes function
         *
         * @method execute
         * @param {function} func
         */
        execute: function (func) {
            executer = function () {
                func();
            };
        },

        /**
         * Scrolls to element if out of view, then clicks it.
         *
         * @method moveToAndClick
         * @param selector - jQuery selector for element
         */
        moveToAndClick: function (selector) {

            addShortcutTitle(selector);

            executer = function () {
                var elem = jQuery(selector);
                if (elem.length > 0) {
                    elem.click();
                    elem.moveTo();
                }
            };
        },

        /**
         * Scrolls to element if out of view, then focuses it
         *
         * @method moveToAndFocus
         * @param selector - jQuery selector for element
         */
        moveToAndFocus: function (selector) {

            addShortcutTitle(selector);

            executer = function () {
                var elem = jQuery(selector);
                if (elem.length > 0) {
                    elem.focus();
                    elem.moveTo();
                }
            };
        },

        /**
         * Binds additional keyboard controls
         *
         * @method or
         * @param {String} keys - keys to bind
         * @return {Object}
         */
        or: function (keys) {
            bindKeys(keys);
            return this;
        }
    };
};

// Trigger this event on an iframe if you want its keypress events to be propagated (Events to work in iframes).
jQuery(document).bind("iframeAppended", function (e, iframe) {
    jQuery(iframe).load(function () {

        var target = jQuery(iframe).contents();

        target
        .bind("keyup keydown keypress", function (e) {
            // safari propagates keypress events from iframes
            if (jQuery.browser.safari && e.type === "keypress") {
                return;
            }
            if (!jQuery(e.target).is(":input")) {
                jQuery(document).trigger(e);
            }
        });
    });
});

/**
 * Creates keyboard commands and their actions from json data. Format looks like:
 *
 * <pre>
 * [
 *   {
 *        "keys":["gd"],
 *        "context":"global",
 *        "op":"followLink",
 *        "param":"#home_link"
 *    },
 *    {
 *        "keys":["gi"],
 *        "context":"global",
 *        "op":"followLink",
 *        "param":"#find_link"
 *    },
 *    {
 *        "keys":["/"],
 *        "context":"global",
 *        "op":"moveToAndFocus",
 *        "param":"#quickSearchInput"
 *    },
 *    {
 *        "keys":["c"],
 *        "context":"global",
 *        "op":"moveToAndClick",
 *        "param":"#create_link"
 *    }
 * ]
 * </pre>
 *
 * @method fromJSON
 * @static
 * @param json
 */
AJS.whenIType.fromJSON = function (json) {

    //AJS.keys is defined by the keyboard-shortcut plugin.
    if (json) {
        jQuery.each(json, function (i,item) {
            var operation = item.op,
                param = item.param;
            jQuery.each(item.keys, function () {
                if(operation === "execute") {
                    //need to turn function string into function object
                    param = new Function(param);
                }
                AJS.whenIType(this)[operation](param);
            });
        });
    }
};