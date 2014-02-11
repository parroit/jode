function fact(n){
	if (n == 1)
		return 1;

	return n * fact(n-1);
}





load("javascript/timers.js");
load("javascript/console.js");



var counter = 0,
	intv = setInterval(function(){
		counter++;
		console.log("ciao", counter);	
		if (counter == 10) {
			clearInterval(intv);
		}
	},1000);

print(intv);	

