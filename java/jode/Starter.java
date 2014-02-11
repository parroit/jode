package jode;
import javax.script.*;
import java.io.FileReader;
import java.io.FileNotFoundException;
import jode.EventLoop;

public class Starter {
    public static void main(String args[]) {
        if (args.length < 1) {
        	System.err.println("Usage: jode script.js");
        	System.exit(-1);
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine e = manager.getEngineByName("nashorn");
		
		EventLoop.INSTANCE.run(new Runnable () {
			public void run (){
				try {
					
					e.eval(new FileReader(args[0]));

				} catch (FileNotFoundException ex){
					System.err.println(ex.getMessage());
					System.exit(-1);
				} catch (ScriptException ex){
					System.err.println(ex.getMessage());
					System.exit(-1);
				}

			}
		});

		
		
		
		return;
        
    }

    
}

