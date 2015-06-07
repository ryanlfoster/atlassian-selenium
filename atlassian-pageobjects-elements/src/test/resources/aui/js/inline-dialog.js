/*->
#name>Inline-Dialog
#javascript>yes
#css>yes
#description>Creates a popup dialog that is inline with content.
#option>onHover
#option>noBind
#option>fadeTime 
#option>hideDelay
#option>showDelay
#option>width
#option>offsetX 
#option>offsetY
#option>container 
#option>cacheContent 
#option>hideCallback 
#option>initCallback 
#option>isRelativeToMouse 
#option>closeOthers
#option>responseHandler
#option>onTop
#option>useLiveEvents 
#func>show()
#func>hide()
#func>refresh()
<-*/

(function($) {
    /**
     * Creates a new inline dialog.
     *
     * @class InlineDialog
     * @namespace AJS
     * @constructor
     * @param items jQuery object - the items that trigger the display of this popup when the user mouses over.
     * @param identifier A unique identifier for this popup. This should be unique across all popups on the page and a valid CSS class.
     * @param url The URL to retrieve popup contents.
     * @param options Custom options to change default behaviour. See AJS.InlineDialog.opts for default values and valid options.
     */
    AJS.InlineDialog = function(items, identifier, url, options) {
        var opts = $.extend(false, AJS.InlineDialog.opts, options);
        var hash;
        var hideDelayTimer;
        var showTimer;
        var beingShown = false;
        var shouldShow = false;
        var contentLoaded = false;
        var mousePosition;
        var targetPosition;
        var popup  = $('<div id="inline-dialog-' + identifier + '" class="aui-inline-dialog"><div class="contents"></div><div id="arrow-' + identifier + '" class="arrow"></div></div>');
        var arrow = $("#arrow-" + identifier, popup);
        var contents = popup.find(".contents");

        contents.css("width", opts.width + "px");
        contents.mouseover(function(e) {
            clearTimeout(hideDelayTimer);
            popup.unbind("mouseover");
            //e.stopPropagation();
        }).mouseout(function() {
            hidePopup();
        });

        var getHash = function () {
            if (!hash) {
                hash = {
                    popup: popup,
                    hide: function(){
                        hidePopup(0);
                    },
                    id: identifier,
                    show: function(){
                        showPopup();
                    },
                    reset: function () {
                        var popupLeft;    //position of the left edge of popup box from the left of the screen
                        var popupRight;   //position of the right edge of the popup box fron the right edge of the screen
                        var popupTop;   //position of the top edge of popup box
                        var arrowOffsetY = -7;    //the offsets of the arrow from the top edge of the popup, default is the height of the arrow above the popup
                        var arrowOffsetX;
                        var displayAbove;   //determines if popup should be displayed above the the trigger or not

                        var targetOffset = targetPosition.target.offset();
                        var padding = parseInt(targetPosition.target.css("padding-left")) + parseInt(targetPosition.target.css("padding-right"));
                        var triggerWidth = targetPosition.target.width() + padding; //The total width of the trigger (including padding)
                        var middleOfTrigger = targetOffset.left + triggerWidth/2;    //The absolute x position of the middle of the Trigger
                        var bottomOfViewablePage = (window.pageYOffset || document.documentElement.scrollTop) + $(window).height();
                        

                        //CONSTANTS
                        var SCREEN_PADDING = 10; //determines how close to the edge the dialog needs to be before it is considered offscreen

                        //DRAW POPUP
                        function drawPopup (popup, left, right, top, arrowOffsetX, arrowOffsetY, displayAbove) {
                            //Position the popup using the left and right parameters
                            popup.css({
                                left: left,
                                right: right,
                                top: top
                            });
                            //Only draw arrow if raphael exists
                            if (window.Raphael) {
                                if (!popup.arrowCanvas) {
                                    popup.arrowCanvas = Raphael("arrow-"+identifier, 16, 16);  //create canvas using arrow element
                                }
                                var arrowPath = "M0,8L8,0,16,8";
                                //Detect if arrow should be flipped
                                if (displayAbove) {
                                    arrowPath = "M0,8L8,16,16,8";
                                }
                                //draw the arrow
                                popup.arrowCanvas.path(arrowPath).attr({
                                    fill : "#fff",
                                    stroke : "#bbb"
                                });
                            }
                            //apply positioning to arrow
                            arrow.css({
                                position: "absolute",
                                left: arrowOffsetX,
                                right: "auto",
                                top: arrowOffsetY
                            });
                        }

                        popupTop = targetOffset.top + targetPosition.target.height() + opts.offsetY;
                        popupLeft = targetOffset.left + opts.offsetX;

                        var enoughRoomAbove = targetOffset.top > popup.height();
                        var enoughRoomBelow = (popupTop + popup.height()) < bottomOfViewablePage;

                        //Check if the popup should be displayed above the trigger or not (if the user has set opts.onTop to true and if theres enough room)
                        displayAbove =  (!enoughRoomBelow && enoughRoomAbove) || (opts.onTop && enoughRoomAbove);

                        //calculate if the popup will be offscreen
                        var diff = $(window).width() - (popupLeft  + opts.width + SCREEN_PADDING);

                        //check if the popup should be displayed above or below the trigger
                        if (displayAbove) {
                            popupTop = targetOffset.top - popup.height() - 8; //calculate the flipped position of the popup (the 8 allows for room for the arrow)
                            arrowOffsetY = popup.height() - 9; //calculate new offset for the arrow, 10 is the height of the shadow
                            if (AJS.$.browser.msie){
                                arrowOffsetY = popup.height() - 10; //calculate new offset for the arrow, 11 is the height of the shadow in IE
                            }
                        }
                        arrowOffsetX = middleOfTrigger - popupLeft;

                        //check if the popup should show up relative to the mouse
                        if(opts.isRelativeToMouse){
                            if(diff < 0){
                                popupRight = SCREEN_PADDING;
                                popupLeft = "auto";
                                arrowOffsetX = mousePosition.x - ($(window).width() - opts.width);
                            }else{
                                popupLeft = mousePosition.x - 20;
                                popupRight = "auto";
                                arrowOffsetX = mousePosition.x - popupLeft;
                            }
                        }else{
                            if(diff < 0){
                                popupRight = SCREEN_PADDING;
                                popupLeft = "auto";
                                arrowOffsetX = middleOfTrigger - ($(window).width() - opts.width);
                            }else if(opts.width <= triggerWidth/2){
                                arrowOffsetX = opts.width/2;
                                popupLeft = middleOfTrigger-opts.width/2;
                            }
                        }

                        drawPopup (popup, popupLeft, popupRight, popupTop, arrowOffsetX, arrowOffsetY, displayAbove);

                        // reset position of popup box
                        popup.fadeIn(opts.fadeTime, function() {
                            // once the animation is complete, set the tracker variables
                            // beingShown = false; // is this necessary? Maybe only the shouldShow will have to be reset?
                        });
                        if (popup.shadow) {
                            popup.shadow.remove();
                        }
                        popup.shadow = Raphael.shadow(0, 0, contents.width(), contents.height(), {
                            target: popup[0]
                        })
                        .hide()
                        .fadeIn(opts.fadeTime);

                        if (AJS.$.browser.msie) {
                            // iframeShim, prepend if it doesnt exist
                            if($('#inline-dialog-shim-'+identifier).length ==0){
                                $(popup).prepend($('<iframe class = "inline-dialog-shim" id="inline-dialog-shim-'+identifier+'" frameBorder="0" src="javascript:false;"></iframe>'));
                            }
                            //addjust height and width of shim according to the popup
                            $('#inline-dialog-shim-'+identifier).css({
                                width: contents.outerWidth(),
                                height: contents.outerHeight()
                            });
                        }
                    }
                };
            }
            return hash;
        };

        var showPopup = function() {
            if (popup.is(":visible")) {
                return;
            }
            showTimer = setTimeout(function() {
                if (!contentLoaded || !shouldShow) {
                    return;
                }
                $(items).addClass("active");
                beingShown = true;
                bindHideOnExternalClick();
                AJS.InlineDialog.current = getHash();
                AJS.$(document).trigger("showLayer", ["inlineDialog", getHash()]);
                // retrieve the position of the click target. The offsets might be different for different types of targets and therefore
                // either have to be customisable or we will have to be smarter about calculating the padding and elements around it

                getHash().reset();

            }, opts.showDelay);
        };

        var hidePopup = function(delay) {

            shouldShow = false;
            // only exectute the below if the popup is currently being shown
            if (beingShown) {
                delay = (delay == null) ? opts.hideDelay : delay;
                clearTimeout(hideDelayTimer);
                clearTimeout(showTimer);
                // store the timer so that it can be cleared in the mouseover if required
                //disable auto-hide if user passes null for hideDelay
                if (delay != null) {
                    hideDelayTimer = setTimeout(function() {
                        unbindHideOnExternalClick();
                        $(items).removeClass("active");
                        popup.fadeOut(opts.fadeTime, function() { opts.hideCallback.call(popup[0].popup); });
                        popup.shadow.remove();
                        popup.shadow = null;
                        popup.arrowCanvas.remove();
                        popup.arrowCanvas = null;
                        beingShown = false;
                        shouldShow = false;
                        AJS.$(document).trigger("hideLayer", ["inlineDialog", getHash()]);
                        AJS.InlineDialog.current = null;
                        if (!opts.cacheContent) {
                            //if not caching the content, then reset the
                            //flags to false so as to reload the content
                            //on next mouse hover.
                            contentLoaded = false;
                            contentLoading = false;
                        }

                    }, delay);
                }

            }
        };

        // the trigger is the jquery element that is triggering the popup (i.e., the element that the mousemove event is bound to)
        var initPopup = function(e,trigger) {
            opts.upfrontCallback.call({
                popup: popup,
                hide: function () {hidePopup(0);},
                id: identifier,
                show: function () {showPopup();}
            });

            popup.each(function() {
                if (typeof this.popup != "undefined") {
                    this.popup.hide();
                }
            });
            
            //Close all other popups if neccessary
            if (opts.closeOthers) {
                AJS.$(".aui-inline-dialog").each(function() {
                    this.popup.hide();
                });
            }
            
            //handle programmatic showing where there is no event
            if (!e) {
                mousePosition = { x: items.offset().left, y: items.offset().top };
                targetPosition = {target: items};
            } else {
                mousePosition = { x: e.pageX, y: e.pageY };
                targetPosition = {target: $(e.target)};  
            }
            


            if (!beingShown) {
                clearTimeout(showTimer);
            }
            shouldShow = true;
            var doShowPopup = function() {
                contentLoading = false;
                contentLoaded = true;
                opts.initCallback.call({
                    popup: popup,
                    hide: function () {hidePopup(0);},
                    id: identifier,
                    show: function () {showPopup();}
                });
                showPopup();
            };
            // lazy load popup contents
            if (!contentLoading) {
                contentLoading = true;
                if ($.isFunction(url)) {
                    // If the passed in URL is a function, execute it. Otherwise simply load the content.
                    url(contents, trigger, doShowPopup);
                } else {
                    //Retrive response from server
                    AJS.$.get(url, function (data, status, xhr) {
                        //Load HTML contents into the popup
                        contents.html(opts.responseHandler(data, status, xhr));
                        //Show the popup
                        contentLoaded = true;
                        opts.initCallback.call({
                            popup: popup,
                            hide: function () {hidePopup(0);},
                            id: identifier,
                            show: function () {showPopup();}
                        });
                        showPopup();
                    });
                }
            }
            // stops the hide event if we move from the trigger to the popup element
            clearTimeout(hideDelayTimer);
            // don't trigger the animation again if we're being shown
            if (!beingShown) {
                showPopup();
            }
            return false;
        };

        popup[0].popup = getHash();

        var contentLoading = false;
        var added  = false;
        var appendPopup = function () {
            if (!added) {
                $(opts.container).append(popup);
                added = true;
            }
        };
        if (opts.onHover) {
            if (opts.useLiveEvents) {
                $(items).live("mousemove", function(e) {
                    appendPopup();
                    initPopup(e,this);
                }).live("mouseout", function() {
                    hidePopup();
                });
            } else {
                $(items).mousemove(function(e) {
                    appendPopup();
                    initPopup(e,this);
                }).mouseout(function() {
                    hidePopup();
                });
            }
        } else {
            if (!opts.noBind) {   //Check if the noBind option is turned on
                if (opts.useLiveEvents) {
                    $(items).live("click", function(e) {
                        appendPopup();
                        initPopup(e,this);
                        return false;
                    }).live("mouseout", function() {
                        hidePopup();
                    });
                } else {
                    $(items).click(function(e) {
                        appendPopup();
                        initPopup(e,this);
                        return false;
                    }).mouseout(function() {
                        hidePopup();
                    });
                }
            }
        }
        
        // Be defensive and make sure that we haven't already bound the event
        var hasBoundOnExternalClick = false;
        var externalClickNamespace = identifier + ".inline-dialog-check";

        /**
         * Catch click events on the body to see if the click target occurs outside of this popup
         * If it does, the popup will be hidden
         */
        var bindHideOnExternalClick = function () {
            if (!hasBoundOnExternalClick) {
                $("body").bind("click." + externalClickNamespace, function(e) {
                    var $target = $(e.target);
                    // hide the popup if the target of the event is not in the dialog
                    if ($target.closest('#inline-dialog-' + identifier + ' .contents').length === 0) {
                        hidePopup(0);
                    }
                });
                hasBoundOnExternalClick = true;
            }
        };

        var unbindHideOnExternalClick = function () {
            if (hasBoundOnExternalClick) {
                $("body").unbind("click." + externalClickNamespace);
            }
            hasBoundOnExternalClick = false;
        };

        /**
         * Show the inline dialog.
         * @method show
         */
        popup.show = function (e) {
            if (e) {
                e.stopPropagation();
            }
            appendPopup();
            initPopup(null, this);
        };
        /**
         * Hide the inline dialog.
         * @method hide
         */
        popup.hide = function () {
            hidePopup(0);
        };
        /**
         * Repositions the inline dialog if being shown.
         * @method refresh
         */
        popup.refresh = function () {
            if (beingShown) {
               getHash().reset(); 
            }
        };
        
        popup.getOptions = function(){
            return opts;
        }
        
        return popup;
    };

    AJS.InlineDialog.opts = {
        onTop: false,
        responseHandler: function(data, status, xhr) {
            //assume data is html
            return data;
        },
        closeOthers: true,
        isRelativeToMouse: false,
        onHover: false,
        useLiveEvents: false,
        noBind: false,
        fadeTime: 100,
        hideDelay: 10000,
        showDelay: 0,
        width: 300,
        offsetX: 0,
        offsetY: 10,
        container: "body",
        cacheContent : true,
        hideCallback: function(){}, // if defined, this method will be exected after the popup has been faded out.
        initCallback: function(){}, // A function called after the popup contents are loaded. `this` will be the popup jQuery object, and the first argument is the popup identifier.
        upfrontCallback: function() {} // A function called before the popup contents are loaded. `this` will be the popup jQuery object, and the first argument is the popup identifier.
    };
})(jQuery);
