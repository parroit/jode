var Platform = Java.type("jode.PlatformCommon");

Platform.INSTANCE.invoker = function(func){
    try {
        func();
    } catch (err) {
        err.stack.split("\n").filter(function(line){
            //print("L"+line);    
            return line.indexOf("javascript/module.js") === -1;
        }).join("\n");
    }
};