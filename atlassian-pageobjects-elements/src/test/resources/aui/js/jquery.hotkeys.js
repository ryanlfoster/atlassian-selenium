/**
 * @module jQuery
 * @namespace jQuery.fn
 */

/**
 * A plugin that let you easily add and remove handlers for keyboard events anywhere in your code supporting any key combination.
 *
 * Original idea from: http://code.google.com/p/js-hotkeys/wiki/about
 *
 * <strong>Usage:</strong>
 * <pre>
 * jQuery(document).bind('keypress', keys, function () {
 *      // code here
 * });
 * </pre>
 *
 * @class bind
 * @since 3.0
 * @constructor
 * @requires jQuery 1.4.2 or greater
 * @param {String} event type - either "keydown, keyup or keypress". <strong>HIGHLY ADVISE keypress, "<", ">", "?"  do not fire on gecko.</strong>
 * @param {String} keys - combination of keys to trigger your function.
 *
 * <dl>
 * <dt>Examples</dt>
 * <dd>"abc" - <strong>a</strong> then <strong>b</strong> then <strong>c</strong></dd>
 * <dd>"alt+c" - <strong>alt</strong> then (or at the same time) <strong>c</strong></dd>
 * <dd>"meta+." - <strong>command</strong> then (or at the same time) <strong>.</strong></dd>
 * </dl>
 * @param {Function} callback - function to execute after command has been typed
 * @return {jQuery} element - jQuery wrapped HTMLelement that event is bound to
*/

(function( jQuery ) {

    function loadChar (c, elem) {

        function createTimeout () {
            elem.tm = window.setTimeout(function () {
                elem.loadedChars = "";
            }, 700);
        }

        elem.loadedChars = elem.loadedChars + c;

        if (!elem.tm) {
            createTimeout();
        } else {
            clearTimeout(elem.tm);
            createTimeout();
        }
    }

    function isValidField (event) {
        return !(this !== event.target && (/textarea|select/i.test(event.target.nodeName) ||
                                           event.target.type === "text" || event.target.type === "password"));
    }

	jQuery.hotKeys = {

		version: "0.8",

		specialKeys: {
			8: "backspace", 9: "tab", 13: "return", 16: "shift", 17: "ctrl", 18: "alt", 19: "pause",
			20: "capslock", 27: "esc", 32: "space", 33: "pageup", 34: "pagedown", 35: "end", 36: "home",
			37: "left", 38: "up", 39: "right", 40: "down", 45: "insert", 46: "del",
			96: "0", 97: "1", 98: "2", 91: "meta", 99: "3", 100: "4", 101: "5", 102: "6", 103: "7",
			104: "8", 105: "9", 106: "*", 107: "+", 109: "-", 110: ".", 111 : "/",
			112: "f1", 113: "f2", 114: "f3", 115: "f4", 116: "f5", 117: "f6", 118: "f7", 119: "f8",
			120: "f9", 121: "f10", 122: "f11", 123: "f12", 144: "numlock", 145: "scroll",
            188: ",", 190: ".", 191: "/", 224: "meta", 219: '[', 221: ']'
		},

        // These only work under Mac Gecko when using keypress (see http://unixpapa.com/js/key.html).
        keypressKeys: [ "<", ">", "?" ],

		shiftNums: {
			"`": "~", "1": "!", "2": "@", "3": "#", "4": "$", "5": "%", "6": "^", "7": "&",
			"8": "*", "9": "(", "0": ")", "-": "_", "=": "+", ";": ":", "'": "\"", ",": "<",
			".": ">",  "/": "?",  "\\": "|"
		}
	};

    jQuery.each(jQuery.hotKeys.keypressKeys, function (_, key) {
        jQuery.hotKeys.shiftNums[ key ] = key;
    });


	function keyHandler( handleObj ) {

        var origHandler,
            keys;

		// Only care when a possible input has been specified
		if ( typeof handleObj.data !== "string" ) {
			return;
		}

		origHandler = handleObj.handler;
        keys = handleObj.data.toLowerCase().split(" ");

        handleObj.loadedChars = "";

        jQuery(this).bind("keydown", function (event) {

            var special;

			// Don't fire in text-accepting inputs that we didn't directly bind to
            if (!isValidField(event)) {
                return;
            }

			// Keypress represents characters, not special keys
			special = jQuery.hotKeys.specialKeys[ event.which ];

            if ((special === "alt" || event.altKey)) {
                loadChar("alt+", handleObj);
            }

            if ((special === "ctrl" || event.ctrlKey) && !/ctrl\+/.test(handleObj.loadedChars)) {
                loadChar("ctrl+", handleObj);
            }

            if (((special !== "ctrl" && !event.ctrlKey) && (special === "meta" || event.metaKey)) &&
                !/meta\+/.test(handleObj.loadedChars)) {
                loadChar("meta+", handleObj);
            }
        });

        handleObj.handler = function( event ) {

            var i,
                special,
                character,
                possible;

			// Don't fire in text-accepting inputs that we didn't directly bind to
			if (!isValidField(event)) {
                return;
            }

			// Keypress represents characters, not special keys
			special = jQuery.hotKeys.specialKeys[ event.which ];
            character = String.fromCharCode( event.which ).toLowerCase();
            possible = {};

            if (special) {
                possible[special] = true;
            }

            // "$" can be triggered as "shift+4" or "$"
            if (event.shiftKey) {
                possible [handleObj.loadedChars + jQuery.hotKeys.shiftNums[character] || special] = true;
            } else {
                possible[handleObj.loadedChars + character] = true;
            }

			for (i = 0, l = keys.length; i < l; i++ ) {
                if ( possible[ keys[i] ]) {
                    handleObj.loadedChars = "";
					return origHandler.apply( this, arguments );
				} else if (keys[i].charAt(handleObj.loadedChars.length) === character) {
                    loadChar(character, handleObj);
                }
			}
		};
	}

	jQuery.each([ "keydown", "keyup", "keypress" ], function() {
		jQuery.event.special[ this ] = { add: keyHandler };
	});

})( jQuery );