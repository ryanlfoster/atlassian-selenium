/*->
#name>Tabs
#javascript>Yes
#css>Yes
#description>A set of switchable Tabs styled for Atlassian products
#func>setup 
#func>change
#option>horizontal-tabs
#option>vertical-tabs 
<-*/

(function (){
    var $tabs,
        $tabMenu,
        REGEX = /#.*/,
        ACTIVE_TAB = "active-tab",
        ACTIVE_PANE = "active-pane";

    AJS.tabs = {
        setup: function () {
            $tabs = AJS.$("div.aui-tabs");
            for (var i=0, ii = $tabs.length; i < ii; i++) {
                $tabMenu = AJS.$("ul.tabs-menu", $tabs[i]);

                // Set up click event for tabs
                AJS.$("a", $tabMenu).click(function (e) {
                    AJS.tabs.change(AJS.$(this), e);
                    e && e.preventDefault();
                });

            };
        },
        change: function ($a, e) {
            var $pane = AJS.$($a.attr("href").match(REGEX)[0]);
            $pane.addClass(ACTIVE_PANE).siblings()
                                       .removeClass(ACTIVE_PANE);
            $a.parent("li.menu-item").addClass(ACTIVE_TAB)
                                     .siblings()
                                     .removeClass(ACTIVE_TAB);
            $a.trigger("tabSelect", {
                tab: $a,
                pane: $pane
            });
        }
    };
    AJS.$(AJS.tabs.setup);
})();
