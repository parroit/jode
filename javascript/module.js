(function (exports, global) {

    var Module = Java.type("jode.Module"),
        
        modules = {};

    function _define (moduleName, __dirname, moduleWrapper) {
        var module = {
            exports: {},
            name: moduleName
        },
            exports = module.exports,
            __filename;

        moduleWrapper(module, exports, __dirname, __filename);

        modules[moduleName] = module.exports;

    };

    function _require(moduleName, __dirname) {
       

        if (moduleName in modules) {
            return modules[moduleName];
        } else {

            var filename, dir, dirname;

            if (_module._builtins && _module._builtins.indexOf(moduleName) > -1) {
                dir = "/home/parroit/src/jode/javascript/builtins";
                filename = dir+"/" + moduleName + ".js";
                
            } else if (moduleName.charAt(0) === ".") {
                filename = __dirname + "/" + moduleName.substring(2) + ".js";
                dir = __dirname;
            } else {
                print(moduleName);
                var modulesDir =  __dirname ? __dirname + "/node_modules/" : "",
                    packageContent = localStorage.getItem(modulesDir +moduleName + "/package.json"),
                    pkg = JSON.parse(packageContent);

                
                   
                filename = modulesDir + moduleName + "/" + pkg.main + ".js";
                dir = moduleName;
            }

            dirname = filename.substring(0,filename.lastIndexOf("/"));
            
            
            var moduleSource = Module.INSTANCE.readFile(filename);
            
            //print("FILE: "+filename); 



            var wrapped =
                    "_module._define('" + moduleName + "','" + dir + "',function(module,exports) {\n" +
                    "var __dirname = '" + dirname +"', __filename = '" + filename + "';\n" +
                    "function require(moduleName){\n" +
                    " return _module._require(moduleName,__dirname);\n" +
                    " }\n" +
                    moduleSource+ "\n" +
                    "});\n" 
                    ;
            
            try {
                eval(wrapped);     
            } catch (err) {
                print(err.stack);
            }
            

            return modules[moduleName];
        }

    };

    exports._module = {
        _define: _define,
        _require: _require
    }
})(this, this);