// Required polyfills. Only the bare minimum.
// Look at https://github.com/shendepu/nashorn-polyfill for more complete list of Nashorn polyfills

var global = this;
var self = this;
var window = this;
var process = {env: {}};
var console = {};
console.debug = print;
console.warn = print;
console.log = print;
console.error = print;
console.trace = print;

// Polyfill for the missing WebSocket API in Nashorn
var WebSocket = Java.type('org.kjussakov.sochorn.WebSocket');