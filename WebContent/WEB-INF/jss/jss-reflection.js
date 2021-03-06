/*!
 * jss-reflection.js
 * http://jss-framework.net/
 *
 * Copyright 2010, Julien Dreux
 * 
 * Date: Wed Dec 1st, 2010. 
 */

Object.prototype.getAllMethods = function() {
	var memberArray = new Array();
	for ( var method in this) {
		if (typeof this[method] == 'function') {
			memberArray.push(method);
		}
		;
	}
	;
	return memberArray;
};

Object.prototype.getOwnMethods = function() {
	var memberArray = new Array();
	for ( var method in this) {
		if (typeof this[method] == 'function' && this.hasOwnProperty(method)) {
			memberArray.push(method);
		}
		;
	}
	;
	return memberArray;
};

var temp = this.getAllMethods().toString().split(',');
var result = [];
for(var i=0; i<temp.length; i++){
	var key = temp[i]; 
	if(key!='println' && key!='print' && key!='getAllMethods' && key!='getOwnMethods'){
		result.push(key);
	}
}

result.toString();