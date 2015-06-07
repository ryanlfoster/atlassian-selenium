/*->
#name>Dialog
#javascript>Yes
#css>Yes
#description>Creates a Modal Dialog that pops up over the screen, also dims the screen. 
#func>addHeader 
#func>addButton 
#func>addLink  
#func>addSubmit 
#func>addCancel 
#func>addButtonPanel 
#func>addPanel
#func>addPage
#func>nextPage 
#func>prevPage 
#func>gotoPage 
#func>getPanel 
#func>getPage
#func>getCurrentPanel
#func>gotoPanel
#func>show 
#func>hide 
#func>remove
#func>disable 
#func>enable 
#func>updateHeight 
<-*/

/**
 * Covers screen with semitransparent DIV
 * @method dim
 * @namespace AJS
 * @for AJS
 * @see undim
*/
AJS.dim = function (useShim) {
    if (!AJS.dim.dim) {
        AJS.dim.dim = AJS("div").addClass("aui-blanket");
        if (AJS.$.browser.msie) {
            AJS.dim.dim.css({width: "200%", height: Math.max(AJS.$(document).height(), AJS.$(window).height()) + "px"});
        }
        AJS.$("body").append(AJS.dim.dim);

        // Even if we do not want to use a shim, we are going to override it in the case of flash being on the page.
        // Flash will sit ontop of our blanket unless wmode is set to opaque in the object/embed tag...
        if (AJS.$.browser.msie && typeof AJS.hasFlash === "undefined" && useShim === false) {
            AJS.hasFlash = false;
            AJS.$("object, embed, iframe").each(function () {
                if (this.nodeName.toLowerCase() === "iframe") {
                    if (AJS.$(this).contents().find("object, embed").length) {
                        AJS.hasFlash = true;
                        return false;
                    }
                } else {
                    AJS.hasFlash = true;
                    return false;
                }
            });
        }

        // Add IFrame shim
        if (AJS.$.browser.msie && (useShim !== false || AJS.hasFlash)) {
            AJS.dim.shim = AJS.$('<iframe frameBorder="0" class="aui-blanket-shim" src="javascript:false;"/>');
            AJS.dim.shim.css({height: Math.max(AJS.$(document).height(), AJS.$(window).height()) + "px"});
            AJS.$("body").append(AJS.dim.shim);
        }

        // IE needs the overflow on the HTML element so scrollbars are hidden
        if (AJS.$.browser.msie && parseInt(AJS.$.browser.version,10) < 8) {
            AJS.$("html").css("overflow", "hidden");
        } else {
            AJS.$("body").css("overflow", "hidden");
        }
    }
};
/**
 * Removes semitransparent DIV
 * @method undim
 * @namespace AJS
 * @for AJS
 * @see dim
*/
AJS.undim = function () {
    if (AJS.dim.dim) {
        AJS.dim.dim.remove();
        AJS.dim.dim = null;
        if (AJS.dim.shim) {
            AJS.dim.shim.remove();
        }

        // IE needs the overflow on the HTML element so scrollbars are hidden
        if (AJS.$.browser.msie && parseInt(AJS.$.browser.version,10) < 8) {
            AJS.$("html").css("overflow", "");
        } else {
            AJS.$("body").css("overflow", "");
        }

        // Safari bug workaround
        if (AJS.$.browser.safari) {
            var top = AJS.$(window).scrollTop();
            AJS.$(window).scrollTop(10 + 5 * (top == 10)).scrollTop(top);
        }
    }
};

/**
 * Creates a generic popup that will be displayed in the center of the screen with a
 * grey blanket in the background.
 * Usage:
 * <pre>
 * new AJS.popup({
 *     width: 800,
 *     height: 400,
 *     id: "my-dialog"
 * });
 * </pre>
 * @class popup
 * @constructor
 * @namespace AJS
 * @param options {object} [optional] Permitted options and defaults are as follows:
 * width (800), height (600), keypressListener (closes dialog on ESC).
*/
AJS.popup = function (options) {
    var defaults = {
        width: 800,
        height: 600,
        closeOnOutsideClick: false,
        keypressListener: function (e) {
            if (e.keyCode === 27 && popup.is(":visible")) {
                res.hide();
            }
        }
    };
    // for backwards-compatibility
    if (typeof options != "object") {
        options = {
            width: arguments[0],
            height: arguments[1],
            id: arguments[2]
        };
        options = AJS.$.extend({}, options, arguments[3]);
    }
    options = AJS.$.extend({}, defaults, options);
    var popup = AJS("div").addClass("aui-popup");

    if (options.id) {
        popup.attr("id", options.id);
    }
    //find the highest z-index on the page to ensure any new popup that is shown is shown on top
    var highestZIndex = 3000;
    AJS.$(".aui-dialog").each(function() {
        var currentPopup = AJS.$(this);
        highestZIndex = (currentPopup.css("z-index") > highestZIndex) ? currentPopup.css("z-index") : highestZIndex;
    });

    var applySize = (function (width, height) {
        options.width = (width = (width || options.width));
        options.height = (height = (height || options.height));

        popup.css({
            marginTop: - Math.round(height / 2) +"px",
            marginLeft: - Math.round(width / 2) + "px",
            width: width,
            height: height,
            background: "#fff",
            "z-index": parseInt(highestZIndex,10) + 2  //+ 2 so that the shadow can be shown on +1 (underneath the popup but above everything else)
        });
        return arguments.callee;
    })(options.width, options.height);

    AJS.$("body").append(popup);

    popup.hide();

    popup.enable();
    /**
     * Popup object
     * @class Popup
     * @static
    */
    
    //blanket for reference further down
    var blanket = AJS.$(".aui-blanket");
    
    var res = {

        changeSize: function (w, h) {
            if ((w && w != options.width) || (h && h != options.height)) {
                applySize(w, h);
            }
            this.show();
        },

        /**
         * Shows the popup
         * @method show
        */
        show: function () {
            
            var show = function () {
                var offsetSize = 5;
         	    if (AJS.$.browser.msie && ~~(AJS.$.browser.version) < 9) {
               	    offsetSize = 3;
                }
                AJS.$(document).keydown(options.keypressListener);     
                AJS.dim();
                blanket = AJS.$(".aui-blanket");
                if(blanket.size()!=0 && options.closeOnOutsideClick){
                    blanket.click( function(){
                        if(popup.is(":visible")){
                            res.hide();
                        }
                    });
                }
                popup.show();
                if (!this.shadow) {
                    //as we are pretty close to the first sibling offset should be a cheap call
                    var offset = popup.offset();
                    this.shadow = Raphael.shadow(offset.top, offset.left, options.width, options.height, {
                        target: popup[0],
                        zindex: (popup.css("z-index") - 1)
                    });
                    this.shadow.css({position: 'fixed', top: '50%', left: '50%', marginLeft: -(options.width / 2 - offsetSize) + "px",  marginTop: -(options.height / 2 - offsetSize) + "px"});
                }

				AJS.popup.current = this;
				AJS.$(document).trigger("showLayer", ["popup", this]);
            };
            show.call(this);
            this.show = show;
        },
        /**
         * Hides the popup.
         * @method hide
        */
        hide: function () {
            AJS.$(document).unbind("keydown", options.keypressListener);
            blanket.unbind();
            this.element.hide();
            if (this.shadow) {
                this.shadow.remove();
                this.shadow = null;
            }

            //only undim if no other dialogs are visible
            if (AJS.$(".aui-dialog:visible").size()==0) {
                AJS.undim();
            }

			AJS.$(document).trigger("hideLayer", ["popup", this]);
			AJS.popup.current = null;
			this.enable();
        },
        /**
         * jQuery object, representing popup DOM element
         * @property element
        */
        element: popup,
        /**
         * Removes popup elements from the DOM
         * @method remove
        */
        remove: function () {
            if (this.shadow) {
                this.shadow.remove();
                this.shadow = null;
                this.shadowParent.remove();
                this.shadowParent = null;
            }
            popup.remove();
            this.element = null;
        },
        /**
         * disables the popup
         * @method disable
        */
        disable: function() {
            if(!this.disabled){
                this.popupBlanket = AJS.$("<div class='dialog-blanket'> </div>").css({
                    height: popup.height(),
                    width: popup.width()
                });
                popup.append(this.popupBlanket);
                this.disabled = true;
            }
        },
        /**
         * enables the popup if it is disabled
         * @method enable
        */
        enable: function() {
            if(this.disabled) {
                this.disabled = false;
                this.popupBlanket.remove();
                this.popupBlanket=null;
            }
        }
    };

    return res;
};

// Scoping function
(function () {
    /**
     * @class Button
     * @constructor Button
     * @param page {number} page id
     * @param label {string} button label
     * @param onclick {function} [optional] click event handler
     * @param className {string} [optional] class name
     * @private
    */
    function Button(page, label, onclick, className) {
        if (!page.buttonpanel) {
            page.addButtonPanel();
        }
        this.page = page;
        this.onclick = onclick;
        this._onclick = function () {
            onclick.call(this, page.dialog, page);
        };
        this.item = AJS("button", label).addClass("button-panel-button");
        if (className) {
            this.item.addClass(className);
        }
        if (typeof onclick == "function") {
            this.item.click(this._onclick);
        }
        page.buttonpanel.append(this.item);
        this.id = page.button.length;
        page.button[this.id] = this;
    }
    
    /**
     * @class Link
     * @constructor Link
     * @param page {number} page id
     * @param label {string} button label
     * @param onclick {function} [optional] click event handler
     * @param className {string} [optional] class name
     * @private
    */
    function Link(page, label, onclick, className, url) {
        if (!page.buttonpanel) {
            page.addButtonPanel();
        }
        
        //if no url is given use # as default
        if(!url){
            url = "#";
        }
        
        this.page = page;
        this.onclick = onclick;
        this._onclick = function () {
            onclick.call(this, page.dialog, page);
        };
        this.item = AJS("a", label).attr("href", url).addClass("button-panel-link");
        if (className) {
            this.item.addClass(className);
        }
        if (typeof onclick == "function") {
            this.item.click(this._onclick);
        }
        page.buttonpanel.append(this.item);
        this.id = page.button.length;
        page.button[this.id] = this;
    }
    
    function itemMove (leftOrRight, target) {
        var dir = leftOrRight == "left"? -1 : 1;
        return function (step) {
            var dtarget = this.page[target];
            if (this.id != ((dir == 1) ? dtarget.length - 1 : 0)) {
                dir *= (step || 1);
                dtarget[this.id + dir].item[(dir < 0 ? "before" : "after")](this.item);
                dtarget.splice(this.id, 1);
                dtarget.splice(this.id + dir, 0, this);
                for (var i = 0, ii = dtarget.length; i < ii; i++) {
                    if (target == "panel" && this.page.curtab == dtarget[i].id) {
                        this.page.curtab = i;
                    }
                    dtarget[i].id = i;
                }
            }
            return this;
        };
    };
    function itemRemove(target) {
        return function () {
            this.page[target].splice(this.id, 1);
            for (var i = 0, ii = this.page[target].length; i < ii; i++) {
                this.page[target][i].id = i;
            }
            this.item.remove();
        };
    }
    /**
     * Moves item left in the hierarchy
     * @method moveUp
     * @method moveLeft
     * @param step {number} how many items to move, default is 1
     * @return {object} button
    */
    Button.prototype.moveUp = Button.prototype.moveLeft = itemMove("left", "button");
    /**
     * Moves item right in the hierarchy
     * @method moveDown
     * @method moveRight
     * @param step {number} how many items to move, default is 1
     * @return {object} button
    */
    Button.prototype.moveDown = Button.prototype.moveRight = itemMove("right", "button");
    /**
     * Removes item
     * @method remove
    */
    Button.prototype.remove = itemRemove("button");

    /**
     * Getter and setter for label
     * @method label
     * @param label {string} [optional] label of the button
     * @return {string} label, if nothing is passed in
     * @return {object} jQuery button object, if label is passed in
    */
    Button.prototype.html = function (label) {
        return this.item.html(label);
    };
    /**
     * Getter and setter of onclick event handler
     * @method onclick
     * @param onclick {function} [optional] new event handler, that is going to replace the old one
     * @return {function} existing event handler if new one is undefined
    */
    Button.prototype.onclick = function (onclick) {
        if (typeof onclick == "undefined") {
            return this.onclick;
        } else {
            this.item.unbind("click", this._onclick);
            this._onclick = function () {
                onclick.call(this, page.dialog, page);
            };
            if (typeof onclick == "function") {
                this.item.click(this._onclick);
            }
        }
    };

    /**
     * Class for panels
     * @class Panel
     * @constructor
     * @param page {number} page id
     * @param title {string} panel title
     * @param reference {string} or {object} jQuery object or selector for the contents of the Panel
     * @param className {string} [optional] HTML class name
     * @param panelButtonId {string} the unique id that will be put on the button element for this panel.
     * @private
    */
    var Panel = function (page, title, reference, className, panelButtonId) {
        if (!(reference instanceof AJS.$)) {
            reference = AJS.$(reference);
        }

        this.dialog = page.dialog;
        this.page = page;
        this.id = page.panel.length;
        this.button = AJS("button").html(title).addClass("item-button");

        if (panelButtonId) {
            this.button[0].id = panelButtonId;
        }

        this.item = AJS("li").append(this.button).addClass("page-menu-item");
        this.body = AJS("div").append(reference).addClass("dialog-panel-body").css("height", page.dialog.height + "px");
        this.padding = 10;
        if (className) {
            this.body.addClass(className);
        }
        var i = page.panel.length,
            tab = this;
        page.menu.append(this.item);
        page.body.append(this.body);
        page.panel[i] = this;
        var onclick = function () {
            var cur;
            if (page.curtab + 1) {
                cur = page.panel[page.curtab];
                cur.body.hide();
                cur.item.removeClass("selected");
                (typeof cur.onblur == "function") && cur.onblur();
            }
            page.curtab = tab.id;
            tab.body.show();
            tab.item.addClass("selected");
            (typeof tab.onselect == "function") && tab.onselect();
            (typeof page.ontabchange == "function") && page.ontabchange(tab, cur);
        };
        if (!this.button.click) {
            AJS.log("atlassian-dialog:Panel:constructor - this.button.click false");
            this.button.onclick = onclick;
        }
        else {
            this.button.click(onclick);
        }
        onclick();
        if (i == 0) {
            page.menu.css("display", "none"); // don't use jQuery hide()
        } else {
            page.menu.show();
        }
    };
    /**
     * Selects current panel
     * @method select
    */
    Panel.prototype.select = function () {
        this.button.click();
    };
    /**
     * Moves item left in the hierarchy
     * @method moveUp
     * @method moveLeft
     * @param step {number} how many items to move, default is 1
     * @return {object} panel
    */
    Panel.prototype.moveUp = Panel.prototype.moveLeft = itemMove("left", "panel");
    /**
     * Moves item right in the hierarchy
     * @method moveDown
     * @method moveRight
     * @param step {number} how many items to move, default is 1
     * @return {object} panel
    */
    Panel.prototype.moveDown = Panel.prototype.moveRight = itemMove("right", "panel");
    /**
     * Removes item
     * @method remove
    */
    Panel.prototype.remove = itemRemove("panel");
    /**
     * Getter and setter of inner HTML of the panel
     * @method html
     * @param html {string} HTML source to set up
     * @return {object} panel
     * @return {string} current HTML source
    */
    Panel.prototype.html = function (html) {
        if (html) {
            this.body.html(html);
            return this;
        } else {
            return this.body.html();
        }
    };
    /**
     * Default padding is 10 px. This method gives you ability to overwrite default value. Use it with caution.
     * @method setPadding
     * @param padding {number} padding in pixels
     * @return {object} panel
    */
    Panel.prototype.setPadding = function (padding) {
        if (!isNaN(+padding)) {
            this.body.css("padding", +padding);
            this.padding = +padding;
            this.page.recalcSize();
        }
        return this;
    };


    /**
     * Class for pages
     * @class Page
     * @constructor
     * @param dialog {object} dialog object
     * @param className {string} [optional] HTML class name
     * @private
    */
    var Page = function (dialog, className) {
        this.dialog = dialog;
        this.id = dialog.page.length;
        this.element = AJS("div").addClass("dialog-components");
        this.body = AJS("div").addClass("dialog-page-body");
        this.menu = AJS("ul").addClass("dialog-page-menu").css("height", dialog.height + "px");
        this.body.append(this.menu);
        this.curtab;
        this.panel = [];
        this.button = [];
        if (className) {
            this.body.addClass(className);
        }
        dialog.popup.element.append(this.element.append(this.menu).append(this.body));
        dialog.page[dialog.page.length] = this;
    };

    /**
     * Size updater for contents of the page. For internal use
     * @method recalcSize
    */
    Page.prototype.recalcSize = function () {
        var headerHeight = this.header ? 43 : 0;
        var buttonHeight = this.buttonpanel ? 43 : 0;
        for (var i = this.panel.length; i--;) {
            var dialogComponentsHeight = this.dialog.height - headerHeight - buttonHeight;
            this.panel[i].body.css("height", dialogComponentsHeight - this.panel[i].padding * 2);
            this.menu.css("height", dialogComponentsHeight - parseFloat(this.menu.css("padding-top")));
        }
    };

	/**
     * Adds a button panel to the bottom of dialog
     * @method addButtonPanel
    */
	Page.prototype.addButtonPanel = function () {
		this.buttonpanel = AJS("div").addClass("dialog-button-panel");
        this.element.append(this.buttonpanel);
	};

    /**
     * Method for adding new panel to the page
     * @method addPanel
     * @param title {string} panel title
     * @param reference {string} or {object} jQuery object or selector for the contents of the Panel
     * @param className {string} [optional] HTML class name
     * @param panelButtonId {string} [optional] The unique id for the panel's button.
     * @return {object} the page
    */
    Page.prototype.addPanel = function (title, reference, className, panelButtonId) {
        new Panel(this, title, reference, className, panelButtonId);
		this.recalcSize();
        return this;
    };
    /**
     * Method for adding header to the page
     * @method addHeader
     * @param title {string} panel title
     * @param className {string} [optional] HTML class name
     * @return {object} the page
    */
    Page.prototype.addHeader = function (title, className) {
        if (this.header) {
            this.header.remove();
        }
        this.header =  AJS("h2").html(title || '').addClass("dialog-title");
        className && this.header.addClass(className);
        this.element.prepend(this.header);
        this.recalcSize();
        return this;
    };
    /**
     * Method for adding new button to the page
     * @method addButton
     * @param label {string} button label
     * @param onclick {function} [optional] click event handler
     * @param className {string} [optional] class name
     * @return {object} the page
    */
    Page.prototype.addButton = function (label, onclick, className) {
        new Button(this, label, onclick, className);
        this.recalcSize();
        return this;
    };
    /**
     * Method for adding new link to the page
     * @method addLink
     * @param label {string} button label
     * @param onclick {function} [optional] click event handler
     * @param className {string} [optional] class name
     * @return {object} the page
    */
    Page.prototype.addLink = function (label, onclick, className, url) {
        new Link(this, label, onclick, className, url);
        this.recalcSize();
        return this;
    };
    
    /**
     * Selects corresponding panel
     * @method gotoPanel
     * @param panel {object} panel object
     * @param panel {number} id of the panel
    */
    Page.prototype.gotoPanel = function (panel) {
        this.panel[panel.id || panel].select();
    };
    /**
     * Returns current panel on the page
     * @method getCurrentPanel
     * @return panel {object} the panel
    */
    Page.prototype.getCurrentPanel = function () {
        return this.panel[this.curtab];
    };
    /**
     * Hides the page
     * @method hide
    */
    Page.prototype.hide = function () {
        this.element.hide();
    };
    /**
     * Shows the page, if it was hidden
     * @method show
    */
    Page.prototype.show = function () {
        this.element.show();
    };
    /**
     * Removes the page
     * @method remove
    */
    Page.prototype.remove = function () {
        this.element.remove();
    };



    /**
     * Constructor for a Dialog. A Dialog is a popup which consists of Pages, where each Page can consist of Panels,
     * Buttons and a Header. The dialog must be constructed in page order as it has a current page state. For example,
     * calling addButton() will add a button to the 'current' page.
     * <p>
     * By default, a new Dialog will have one page. If there are multiple Panels on a Page, a
     * menu is displayed on the left side of the dialog.
     * </p>
     * Usage:
     * <pre>
     * var dialog = new AJS.Dialog(860, 530);
     * dialog.addHeader("Insert Macro")
     * .addPanel("All", "&lt;p&gt;&lt;/p&gt;")
     * .addPanel("Some", "&lt;p&gt;&lt;/p&gt;")
     * .addButton("Next", function (dialog) {dialog.nextPage();})
     * .addButton("Cancel", function (dialog) {dialog.hide();});
     *
     * dialog.addPage()
     * .addButton("Cancel", function (dialog) {dialog.hide();});
     *
     * somebutton.click(function () {dialog.show();});
     * </pre>
     * @class Dialog
     * @namespace AJS
     * @constructor
     * @param width {number} dialog width in pixels, or an object containing the Dialog parameters
     * @param height {number} dialog height in pixels
     * @param id {number} [optional] dialog id
    */
    AJS.Dialog = function (width, height, id) {
        var options = {};
        if (!+width) {
            options = Object(width);
            width = options.width;
            height = options.height;
            id = options.id;
        }
        this.height = height || 480;
        this.width = width || 640;
        this.id = id;
        options = AJS.$.extend({}, options, {
            width: this.width,
            height: this.height,
            id: this.id
        });
        this.popup = AJS.popup(options);

        this.popup.element.addClass("aui-dialog");
        this.page = [];
        this.curpage = 0;

        new Page(this);
    };


    /**
     * Method for adding header to the current page
     * @method addHeader
     * @param title {string} panel title
     * @param className {string} [optional] HTML class name
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.addHeader = function (title, className) {
        this.page[this.curpage].addHeader(title, className);
        return this;
    };
    /**
     * Method for adding new button to the current page
     * @method addButton
     * @param label {string} button label
     * @param onclick {function} [optional] click event handler
     * @param className {string} [optional] class name
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.addButton = function (label, onclick, className) {
        this.page[this.curpage].addButton(label, onclick, className);
        return this;
    };
    
    /**
     * Method for adding new link to the current page
     * @method addButton
     * @param label {string} link label
     * @param onclick {function} [optional] click event handler
     * @param className {string} [optional] class name
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.addLink = function (label, onclick, className, url) {
        this.page[this.curpage].addLink(label, onclick, className, url);
        return this;
    };
    
    /**
    * Method for adding a submit button to the current page
    * @method addSubmit
    * @param label {string} link label
    * @param onclick {function} [optional] click event handler
    * @return {object} the dialog
    */
    AJS.Dialog.prototype.addSubmit = function (label, onclick) {
        this.page[this.curpage].addButton(label, onclick, "button-panel-submit-button");
        return this;
    };
    
    /**
    * Method for adding a cancel link to the current page
    * @method addCancel
    * @param label {string} link label
    * @param onclick {function} [optional] click event handler
    * @return {object} the dialog
    */
    AJS.Dialog.prototype.addCancel= function (label, onclick) {
        this.page[this.curpage].addLink(label, onclick, "button-panel-cancel-link");
        return this;
    };

	/**
     * Method for adding new button panel to the current page
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.addButtonPanel = function () {
        this.page[this.curpage].addButtonPanel();
        return this;
    };


    /**
     * Method for adding new panel to the current page.
     * @method addPanel
     * @param title {string} panel title
     * @param reference {string} or {object} jQuery object or selector for the contents of the Panel
     * @param className {string} [optional] HTML class name
     * @param panelButtonId {String} [optional] The unique id for the panel's button.
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.addPanel = function (title, reference, className, panelButtonId) {
        this.page[this.curpage].addPanel(title, reference, className, panelButtonId);
        return this;
    };
    /**
     * Adds a new page to the dialog and sets the new page as the current page
     * @method addPage
     * @param className {string} [optional] HTML class name
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.addPage = function (className) {
        new Page(this, className);
        this.page[this.curpage].hide();
        this.curpage = this.page.length - 1;
        return this;
    };
    /**
     * Making next page in hierarchy visible and active
     * @method nextPage
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.nextPage = function () {
        this.page[this.curpage++].hide();
        if (this.curpage >= this.page.length) {
            this.curpage = 0;
        }
        this.page[this.curpage].show();
        return this;
    };
    /**
     * Making previous page in hierarchy visible and active
     * @method prevPage
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.prevPage = function () {
        this.page[this.curpage--].hide();
        if (this.curpage < 0) {
            this.curpage = this.page.length - 1;
        }
        this.page[this.curpage].show();
        return this;
    };
    /**
     * Making specified page visible and active
     * @method gotoPage
     * @param num {number} page id
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.gotoPage = function (num) {
        this.page[this.curpage].hide();
        this.curpage = num;
        if (this.curpage < 0) {
            this.curpage = this.page.length - 1;
        } else if (this.curpage >= this.page.length) {
            this.curpage = 0;
        }
        this.page[this.curpage].show();
        return this;
    };
    /**
     * Returns specified panel at the current page
     * @method getPanel
     * @param pageorpanelId {number} page id or panel id
     * @param panelId {number} panel id
     * @return {object} the internal Panel object
    */
    AJS.Dialog.prototype.getPanel = function (pageorpanelId, panelId) {
        var pageid = (panelId == null) ? this.curpage : pageorpanelId;
        if (panelId == null) {
            panelId = pageorpanelId;
        }
        return this.page[pageid].panel[panelId];
    };
    /**
     * Returns specified page
     * @method getPage
     * @param pageid {number} page id
     * @return {object} the internal Page Object
    */
    AJS.Dialog.prototype.getPage = function (pageid) {
        return this.page[pageid];
    };
    /**
     * Returns current panel at the current page
     * @method getCurrentPanel
     * @return {object} the internal Panel object
    */
    AJS.Dialog.prototype.getCurrentPanel = function () {
        return this.page[this.curpage].getCurrentPanel();
    };

    /**
     * Selects corresponding panel
     * @method gotoPanel
     * @param pageorpanel {object} panel object or page object
     * @param panel {object} panel object
     * @param panel {number} id of the panel
    */
    AJS.Dialog.prototype.gotoPanel = function (pageorpanel, panel) {
        if (panel != null) {
            var pageid = pageorpanel.id || pageorpanel;
            this.gotoPage(pageid);
        }
        this.page[this.curpage].gotoPanel(typeof panel == "undefined" ? pageorpanel : panel);
    };

    /**
     * Shows the dialog, if it is not visible
     * @method show
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.show = function () {
        this.popup.show();
        return this;
    };
    /**
     * Hides the dialog, if it was visible
     * @method hide
     * @return {object} the dialog
    */
    AJS.Dialog.prototype.hide = function () {
        this.popup.hide();
        return this;
    };
    /**
     * Removes the dialog elements from the DOM
     * @method remove
    */
    AJS.Dialog.prototype.remove = function () {
        this.popup.hide();
        this.popup.remove();
    };
    /**
     * Disables the dialog if enabled
     * @method disable
    */
    AJS.Dialog.prototype.disable = function () {
        this.popup.disable();
        return this;
    };
    /**
     * Enables the dialog if disabled
     * @method disable
    */
    AJS.Dialog.prototype.enable = function () {
        this.popup.enable();
        return this;
    };
    /**
     * Gets set of items depending on query
     * @method get
     * @param query {string} query to search for panels, pages, headers or buttons
     * e.g.
     *      '#Name' will find all dialog components with the given name such as panels and buttons, etc
     *      'panel#Name' will find only panels with the given name
     *      'panel#"Foo bar"' will find only panels with given name
     *      'panel:3' will find the third panel
     */
    AJS.Dialog.prototype.get = function (query) {
        var coll = [],
            dialog = this;
        var nameExp = '#([^"][^ ]*|"[^"]*")';     // a name is a hash followed by either a bare word or quoted string
        var indexExp = ":(\\d+)";                 // an index is a colon followed by some digits
        var typeExp = "page|panel|button|header"; // one of the allowed types
        var selectorExp = "(?:" +                 // a selector is either ...
            "(" + typeExp + ")(?:" + nameExp + "|" + indexExp + ")?" + // a type optionally followed by either #name or :index
            "|" + nameExp +                       // or just a #name
            ")";
        var queryRE = new RegExp("(?:^|,)" +      // a comma or at the start of the line
            "\\s*" + selectorExp +                // optional space and a selector
            "(?:\\s+" + selectorExp + ")?" +      // optionally, followed by some space and a second selector
            "\\s*(?=,|$)", "ig");                 // followed by, but not including, a comma or the end of the string
        (query + "").replace(queryRE, function (all, name, title, id, justtitle, name2, title2, id2, justtitle2) {
            name = name && name.toLowerCase();
            var pages = [];
            if (name == "page" && dialog.page[id]) {
                pages.push(dialog.page[id]);
                name = name2;
                name = name && name.toLowerCase();
                title = title2;
                id = id2;
                justtitle = justtitle2;
            } else {
                pages = dialog.page;
            }
            title = title && (title + "").replace(/"/g, "");
            title2 = title2 && (title2 + "").replace(/"/g, "");
            justtitle = justtitle && (justtitle + "").replace(/"/g, "");
            justtitle2 = justtitle2 && (justtitle2 + "").replace(/"/g, "");
            if (name || justtitle) {
                for (var i = pages.length; i--;) {
                    if (justtitle || (name == "panel" && (title || (!title && id == null)))) {
                        for (var j = pages[i].panel.length; j--;) {
                            if (pages[i].panel[j].button.html() == justtitle || pages[i].panel[j].button.html() == title || (name == "panel" && !title && id == null)) {
                                coll.push(pages[i].panel[j]);
                            }
                        }
                    }
                    if (justtitle || (name == "button" && (title || (!title && id == null)))) {
                        for (var j = pages[i].button.length; j--;) {
                            if (pages[i].button[j].item.html() == justtitle || pages[i].button[j].item.html() == title || (name == "button" && !title && id == null)) {
                                coll.push(pages[i].button[j]);
                            }
                        }
                    }
                    if (pages[i][name] && pages[i][name][id]) {
                        coll.push(pages[i][name][id]);
                    }
                    if (name == "header" && pages[i].header) {
                        coll.push(pages[i].header);
                    }
                }
            } else {
                coll = coll.concat(pages);
            }
        });
        var res = {
            length: coll.length
        };
        for (var i = coll.length; i--;) {
            res[i] = coll[i];
            for (var method in coll[i]) {
                if (!(method in res)) {
                    (function (m) {
                        res[m] = function () {
                            for (var j = this.length; j--;) {
                                if (typeof this[j][m] == "function") {
                                    this[j][m].apply(this[j], arguments);
                                }
                            }
                        };
                    })(method);
                }
            }
        }
        return res;
    };

    /**
     * Updates height of panels, to contain content without the need for scroll bars.
     *
     * @method updateHeight
     */
    AJS.Dialog.prototype.updateHeight = function () {
        var height = 0;
        for (var i=0; this.getPanel(i); i++) {
            if (this.getPanel(i).body.css({height: "auto", display: "block"}).outerHeight() > height) {
                height = this.getPanel(i).body.outerHeight();
            }
            if (i !== this.page[this.curpage].curtab) {
                this.getPanel(i).body.css({display:"none"});
            }
        }
        for (i=0; this.getPanel(i); i++) {
            this.getPanel(i).body.css({height: height || this.height});
        }
        this.page[0].menu.height(height);
        this.height = height + 87;
        this.popup.changeSize(undefined, height + 87);
    };

    /**
     * Returns the current panel.
     * @deprecated Since 3.0.1 Use getCurrentPanel() instead.
     */
    AJS.Dialog.prototype.getCurPanel = function () {
        return this.getPanel(this.page[this.curpage].curtab);
    };

    /**
     * Returns the current button panel.
     * @deprecated Since 3.0.1 Use get() instead.
     */
    AJS.Dialog.prototype.getCurPanelButton = function () {
        return this.getCurPanel().button;
    };

})();