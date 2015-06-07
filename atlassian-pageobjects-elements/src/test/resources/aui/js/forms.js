/*->
#name>Forms
#javascript>no
#css>yes
#description> Standardised form patterns for Atlassian Products
<-*/

/**
 * Forms: Inline Help - toggles visibility of inline help content.
 *
 * @method inlineHelp
 * @namespace AJS
 * @for AJS
 */
AJS.inlineHelp = function () {
    AJS.$(".icon-inline-help").click(function(){
        var $t = AJS.$(this).siblings(".field-help");
        if ($t.hasClass("hidden")){
            $t.removeClass("hidden");
        } else {
            $t.addClass("hidden");
        }
    });
};