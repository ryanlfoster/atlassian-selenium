if (window.Raphael) {
    Raphael.shadow = function(x, y, w, h, options) {
     		options = options || {};
     		var target = options.target || null,
     			colour = options.shadow || options.color || "#000", //i saw two usages of aui shadow with the two different methods
     			size = options.size * 10 || -4, //makes it sane with no size included. just here for backwards compatability
     			offsetSize = options.offsetSize || 5, //by default we want to offset by 5 pixels for pretty ness
     			zindex = options.zindex || 0,
     			radius = options.radius || 0,
     			paper,rect, $container = AJS.$('<div class="aui-shadow"></div>'), opacity = ".5" , blur = "3", absolute = "absolute";

     		w = w + size; h = h + size;

     		//from the old api, this meant you wanted a shadow drawn into the element
     		//so mimic this
     		if(target && x == y && x == 0) {
     		     x = AJS.$(target).offset().top;
     		     y = AJS.$(target).offset().left;
     		}

     		//ie9 should support svg so should support the opacity, until then tone the colour down
     		//also as the blur seems a little stronger in ie, we need to counter the offset

     	    if (AJS.$.browser.msie && ~~(AJS.$.browser.version) < 9) {
                colour = "#f0f0f0";
           	    offsetSize = 3;
           	    opacity = ".2";
            }

     		$container.css({position:absolute,left:y, top:x, zIndex: zindex,width: w, height: h});
     		//still no blur in safari 5.0 so until then, leave off the version number
     		//apart from growing the target there is no way to add a size, this would have an effect on scroll position
     		if (navigator.appVersion.indexOf("AppleWebKit") > -1 && navigator.appVersion.indexOf("Chrome") < 0 && target) {
     		        offsetSize = (options.offsetSize || 3) +"px";
     		        target.style.cssText += "-webkit-box-shadow: "+  offsetSize + " " + offsetSize + " 5px "+ colour + ";";
     		}
     		else {
     			if(target) {
     			    $container.insertBefore(target);
     		        // $container.insertBefore(target);
     			    //TODO: find out why this extra 10 seems to make everything happy figured size + offset would be the size
//     				paper = this($container[0],w+offsetSize + 10,h+offsetSize + 10,radius);
     				paper = this($container[0],w+offsetSize + 5,h+offsetSize + 5,radius);
     	        }
     			else{
     				paper = this(x,y,w,h,radius);
     			}
     			rect = paper.rect(offsetSize,offsetSize,w,h).attr({fill: colour,stroke: colour,blur:blur,opacity:opacity}); //stroke needed to get around an issue with VML and no stroke defaulting to #000
     			paper.canvas.style.position = "absolute";
     		}
     		return $container;
     	};

}
