AJS.toInit(function($) {

    if ($.browser.msie) {
        
        var toolbarGroups = $(".aui-toolbar .toolbar-group");
       
        // Fix left,right borders on first, last toolbar items
        toolbarGroups.each(function (i, group) {
            $(group).children(":first").addClass("first");
            $(group).children(":last").addClass("last");
        });

        // IE7 spoon feeding zone.
        if (parseInt($.browser.version, 10) == 7) {

            // Add a class so we can style button containers
            function markItemsWithButtons () {
                $(".aui-toolbar button").closest(".toolbar-item").addClass("contains-button");
            };
  
            // force right toolbar to new row when it will fit without wrapping
            function forceRightSplitToRow() {
                $(".aui-toolbar .toolbar-split-right").each(function(i, right) {
    
                    var splitRight = $(right),
                        splitToolbar = splitRight.closest(".aui-toolbar"),
                        splitLeft = splitToolbar.find(".toolbar-split-left"),
                        leftWidth = splitToolbar.data("leftWidth"),
                        rightWidth = splitToolbar.data("rightWidth");
   
                    if(!leftWidth) {
                        leftWidth = splitLeft.outerWidth();
                        splitToolbar.data("leftWidth", leftWidth);
                    }
                    if (!rightWidth) {
                        rightWidth = 0;
                        $(".toolbar-item", right).each(function (i, item) {
                            rightWidth += $(item).outerWidth();
                        });
                        splitToolbar.data("rightWidth", rightWidth);
                    }
                    var toolbarWidth = splitToolbar.width(),
                        spaceAvailable = toolbarWidth - leftWidth;
    
                    if ( toolbarWidth > rightWidth && rightWidth > spaceAvailable ) {
                        splitLeft.addClass("force-split");
                    } else {
                        splitLeft.removeClass("force-split");
                    }
                });
            };

            // simulate white-space:nowrap because IE7 is refusing to do it right
            function simulateNowrapOnGroups () {
                toolbarGroups.each(function (i, group) {
                    var groupWidth = 0;
                    $(group).children(".toolbar-item").each(function (i, items) {
                        groupWidth += $(this).outerWidth();
                    });
                    $(this).width(groupWidth);
                });
            };

            // IE7 inits
            simulateNowrapOnGroups();
            markItemsWithButtons();
            
            // fire forceRightSplitToRow after reload
            var TO = false;
            $(window).resize(function(){
                if(TO !== false)
                    clearTimeout(TO);
                    TO = setTimeout(forceRightSplitToRow, 200);
            });

        }

    }
    
});
