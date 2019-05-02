(function() {
    document.addEventListener("click", function(evt) {
        var node = evt.target;
        console.log("click@" + node);
//        switch (node.nodeName) {
//        case "A":
//            break;
//        default:
//        }
    });
//    document.addEventListener("mouseover", function(evt) {
//        var node = evt.target;
//        switch (node.nodeName) {
//        case "A":
//            console.log("mouseover@" + node);
//            break;
//        default:
//        }
//    });
//    document.addEventListener("mousemove", function(evt) {
//        var node = evt.target;
//        if (evt.which == 1) {
//            console.log("drag@" + node);
//        }
//    });
})();
