var fact = require("./a");

function A() {
    this.content = {
        saluto: "ciao",
        volte: 1,
        re: /./
    };
}

console.log("il fattoriale di 10 è %d", fact(10));

console.log(new A);
console.log(JSON.stringify(new A().content));
console.log(Math.floor(1.2));
