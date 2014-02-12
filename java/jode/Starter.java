package jode;
import javax.script.*;
import java.io.*;
import jode.EventLoop;
import jode.Module;

public class Starter {
    public static void main(final String args[]) {
        if (args.length < 1) {
            System.err.println("Usage: jode script.js");
            System.exit(-1);
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine e = manager.getEngineByName("nashorn");

        EventLoop.INSTANCE.run(new Runnable () {
            public void run () {
                try {
                    String dirname = new File(System.getProperty("user.dir")).getAbsolutePath();
                    String filename =  new File(dirname, args[0]).getAbsolutePath();
                    dirname = dirname.replace("\\","/");
                    filename = filename.replace("\\","/");
                    String source =  "var __dirname = '" + dirname + "';\n" +
                                     "var __filename = '" + filename + "';\n" +
                                     "load('javascript/module.js');\n" +
                                     "function require(moduleName) {\n" +
                                     "    return _module._require(moduleName, __dirname);\n" +
                                     "}\n\n" +
                                     "(function (global,exports) {\n" +
                                    "   var globals = require('./javascript/globals');\n"+
                                    "   globals.init(global);\n"+
                                     Module.INSTANCE.readFile(filename) +
                                     "\n})(this,{});";
                    //System.out.println(source);
                    e.eval(source);

                } catch (ScriptException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }

            }
        });




        return;

    }


}

