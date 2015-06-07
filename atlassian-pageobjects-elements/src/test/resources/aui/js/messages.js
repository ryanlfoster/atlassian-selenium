/*->
#name>Messages
#javascript>Yes
#css>Yes
#description>Some Standard message types for different purposes used across Atlassian Products
#option>generic
#option>error
#option>warning
#option>success
#option>info
#option>hint
<-*/

(function (){
    /**
     * Utility methods to display different message types to the user.
     * Usage:
     * <pre>
     * AJS.messages.info("#container", {
     *   title: "Info",
     *   body: "You can choose to have messages without Close functionality.",
     *   closeable: false,
     *   shadowed: false
     * });
     * </pre>
     * @class messages
     * @namespace AJS
     */
    AJS.messages = {
        setup: function () {
            AJS.messages.createMessage("generic");
            AJS.messages.createMessage("error");
            AJS.messages.createMessage("warning");
            AJS.messages.createMessage("info");
            AJS.messages.createMessage("success");
            AJS.messages.createMessage("hint");
            AJS.messages.makeCloseable();
        },
        makeCloseable: function (message) {
            AJS.$(message || "div.aui-message.closeable").each(function () {
                var $this = AJS.$(this),
                    $icon = AJS.$('<span class="aui-icon icon-close"></span>').click(function () {
                        $this.closeMessage();
                    });
                $this.append($icon);
                $icon.each(AJS.icons.addIcon.init);
            });
        },
        template: '<div class="aui-message {type} {closeable} {shadowed}"><p class="title"><span class="aui-icon icon-{type}"></span><strong>{title}</strong></p>{body}</div><!-- .aui-message -->',
        createMessage: function (type) {
            AJS.messages[type] = function (context, obj) {
                if (!obj) {
                    obj = context;
                    context = "#aui-message-bar";
                }
                obj.closeable = (obj.closeable == false) ? false : true;
                obj.shadowed = (obj.shadowed == false) ? false : true;
                AJS.$(context).append(AJS.template(this.template).fill({
                    type: type,
                    closeable: obj.closeable ? "closeable" : "",
                    shadowed: obj.shadowed ? "shadowed" : "",
                    title: obj.title || "",
                    "body:html": obj.body || ""
                })).find(".svg-icon:empty").each(AJS.icons.addIcon.init);
                obj.closeable && AJS.messages.makeCloseable(AJS.$(context).find("div.aui-message.closeable"));
            };
        }
    };

    AJS.$.fn.closeMessage = function () {
        var $message = AJS.$(this);
        if ($message.hasClass("aui-message", "closeable")) {
            $message.trigger("messageClose", [this]).remove();
        }
    };

    AJS.$(function () {AJS.messages.setup();});
})();
