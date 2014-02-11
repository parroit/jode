





/*load("javascript/timers.js");

*/
load("javascript/console.js");
load("javascript/module.js");


print(__DIR__);

function require(moduleName) {
    return _module._require(moduleName, __DIR__);
}

var fact = require("./fact");


print( fact(10) );


/*
var counter = 0,
	intv = setInterval(function(){
		counter++;
		console.log("ciao", counter);	
		if (counter == 10) {
			clearInterval(intv);
		}
	},1000);

print(intv);	
*/

/*
console.log ("Questo è il %s test","log");
console.log ("Questo è il %s test con %d numeri","log",1);
console.log ("Questo è senza nulla");
*/
