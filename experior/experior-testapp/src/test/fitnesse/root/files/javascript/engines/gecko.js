/*
 * CodePress - Real Time Syntax Highlighting Editor written in JavaScript - http://codepress.org/
 * 
 * Copyright (C) 2007 Fernando M.A.d.S. <fermads@gmail.com>
 *
 * Developers:
 *		Fernando M.A.d.S. <fermads@gmail.com>
 *		Michael Hurni <michael.hurni@gmail.com>
 * Contributors: 	
 *		Martin D. Kirk
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation.
 * 
 * Read the full licence: http://www.opensource.org/licenses/lgpl-license.php
 */
var methods;
var firstLine;

CodePress = {
		scrolling : false,
		autocomplete : true,

		// set initial vars and start sh
		initialize : function() {
	if(typeof(editor)=='undefined' && !arguments[0]) return;
	body = document.getElementsByTagName('body')[0];
	body.innerHTML = body.innerHTML.replace(/\n/g,"");
	chars = '|32|46|62|8|'; // charcodes that trigger syntax highlighting
	cc = '\u2009'; // carret char
	editor = document.getElementsByTagName('pre')[0];
	document.designMode = 'on';
	document.addEventListener('keypress', this.keyHandler, false);
	window.addEventListener('scroll', function() { if(!CodePress.scrolling) CodePress.syntaxHighlight('scroll') }, false);
	completeChars = this.getCompleteChars();
	completeEndingChars =  this.getCompleteEndingChars();
	firstline = CodePress.getCode().split("\n");
	firstline = firstline[0];
},

//treat key bindings
keyHandler : function(evt) {	
	keyCode = evt.keyCode;	
	charCode = evt.charCode;

	fromChar = String.fromCharCode(charCode);

	if((evt.ctrlKey || evt.metaKey) && evt.shiftKey && charCode!=90)  { // shortcuts = ctrl||appleKey+shift+key!=z(undo) 
		CodePress.shortcuts(charCode?charCode:keyCode);
	}
	else if( (completeEndingChars.indexOf('|'+fromChar+'|')!= -1 || completeChars.indexOf('|'+fromChar+'|')!=-1) && CodePress.autocomplete) { // auto complete
		if(!CodePress.completeEnding(fromChar))
			CodePress.complete(fromChar);
	}
	else if(chars.indexOf('|'+charCode+'|')!=-1||keyCode==13) { // syntax highlighting
		top.setTimeout(function(){CodePress.syntaxHighlight('generic');},100);
	}
	else if(keyCode==9 || evt.tabKey) {  // snippets activation (tab)
		CodePress.snippets(evt);
	}
	else if( charCode==124)
	{	
		evt.preventDefault();
		evt.stopPropagation();
		CodePress.align();	
	}
	else if(keyCode==46||keyCode==8) { // save to history when delete or backspace pressed
		CodePress.actions.history[CodePress.actions.next()] = editor.innerHTML;
	}

	else if((charCode==122||charCode==121||charCode==90) && evt.ctrlKey) { 
		// undo and redo
		(charCode==121||evt.shiftKey) ? CodePress.actions.redo() :  CodePress.actions.undo(); 
		evt.preventDefault();
	}
	else if(charCode==118 && evt.ctrlKey)  { // handle paste
		CodePress.getRangeAndCaret();
		top.setTimeout(function(){CodePress.syntaxHighlight('generic');},100);
	}
	else if(charCode==99 && evt.ctrlKey)  { // handle cut
		//alert(window.getSelection().getRangeAt(0).toString().replace(/\t/g,'FFF'));
	}

},

//put cursor back to its original position after every parsing
findString : function() {
	if(self.find(cc))
		window.getSelection().getRangeAt(0).deleteContents();
},

//split big files, highlighting parts of it
split : function(code,flag) {
	if(flag=='scroll') {
		this.scrolling = true;
		return code;
	}
	else {
		this.scrolling = false;
		mid = code.indexOf(cc);
		if(mid-2000<0) {ini=0;end=4000;}
		else if(mid+2000>code.length) {ini=code.length-4000;end=code.length;}
		else {ini=mid-2000;end=mid+2000;}
		code = code.substring(ini,end);
		return code;
	}
},

getEditor : function() {
	if(!document.getElementsByTagName('pre')[0]) {
		body = document.getElementsByTagName('body')[0];
		if(!body.innerHTML) return body;
		if(body.innerHTML=="<br>") body.innerHTML = "<pre> </pre>";
		else body.innerHTML = "<pre>"+body.innerHTML+"</pre>";
	}
	return document.getElementsByTagName('pre')[0];
},

//syntax highlighting parser
syntaxHighlight : function(flag, methods2) {
	if( methods2 == "feil" )
		alert("Fant ikke angitt klasse. Highlighting fungerer dermed ikke!");
	if( methods2 != null )
		methods = methods2;

	lines = methods.split('\n');
	lines.pop();

	if(flag != 'init') { window.getSelection().getRangeAt(0).insertNode(document.createTextNode(cc)); }

	editor = CodePress.getEditor();
	o = editor.innerHTML;

	o = o.replace(/<br>/g,'\n');
	o = o.replace(/<.*?>/g,'');
	x = z = this.split(o,flag);
	x = x.replace(/\n/g,'<br>');
	x = x.replace(/&nbsp;/g, '&nbsp;');

	if(arguments[1]&&arguments[2]) x = x.replace(arguments[1],arguments[2]);
	var sRegExInput;
	for(i=0;i<Language.syntax.length;i++) 
		x = x.replace(Language.syntax[i].input,Language.syntax[i].output);

	var temp;
	var temp2;
	var temp3;
	var evalstring = "";		
	var temp3;
	var test2;

	for(j=0;j<lines.length; j++) {
		lines[j] = lines[j].replace(/\s+$/,"");
		temp = lines[j].split(' ');

		temp2 = temp.join( "(\\s+|(?:&nbsp;)*\\s*\\|.*?\\|\\s*)" );

		temp3 = temp.join(" ");		

		test2 = "";
		for (var i = 0; i<temp.length; i++) {

			test2 += "<s>"+temp[i]+"</s>";

			if (i != temp.length-1)
				test2 += "$"+(i+1);
		}		

		eval('x = x.replace(/'+temp2+'/g, \"'+ test2 + '\")');
	}

	editor.innerHTML = this.actions.history[this.actions.next()] = (flag=='scroll') ? x : o.replace(z,x);
	if(flag!='init') this.findString();
},

getLastWord : function() {

},

align : function()
{
	var range = window.getSelection().getRangeAt(0);
	var startNode = document.getElementsByTagName("pre").item(0);

	var startOffset = 0;	

	range.setStartBefore( startNode );

	var div = document.createElement('div');
	div.appendChild(range.cloneContents());

	var linjearray = div.innerHTML.split("<br>");

	for( var i = 0; i < linjearray.length; i++ )
	{
		linjearray[i] = this.removeTags( linjearray[i] );
	}

	range.collapse( false );


	if( linjearray.length <= 1 || linjearray[linjearray.length-2] == 0 || linjearray[linjearray.length-2].search(/span/) > 0 )
	{		
		this.createTextnode();
		return;
	}

	var currentline = linjearray[linjearray.length-1].split("|");

	if (linjearray.length > 0 && linjearray.length-2 > 0 ) 
		var previousline = linjearray[linjearray.length-2].split("|");

	var nbspstring = "";

	if( previousline[currentline.length-1] != null)
	{

		for (var i = 0; i < (previousline[currentline.length-1].replace(/&nbsp;/gi,' ').length)-(currentline[currentline.length-1].length); i++)
		{	
			var node = window.document.createTextNode( "\u00a0" );
			var range = window.getSelection().getRangeAt(0);

			var selct = window.getSelection();
			var range2 = range.cloneRange();
			selct.removeAllRanges();
			range.deleteContents();
			range.insertNode(node);
			range2.selectNode(node);		
			range2.collapse(false);
			selct.removeAllRanges();
			selct.addRange(range2);
		}
	}
	else
	{
		CodePress.createTextnode();
		return;
	}


	var node = window.document.createTextNode( "|" );
	var range = window.getSelection().getRangeAt(0);

	var selct = window.getSelection();
	var range2 = range.cloneRange();
	selct.removeAllRanges();
	range.deleteContents();
	range.insertNode(node);
	range2.selectNode(node);		
	range2.collapse(false);
	selct.removeAllRanges();
	selct.addRange(range2);



},

createTextnode : function()
{
	var node = window.document.createTextNode( "|" );
	var range = window.getSelection().getRangeAt(0);

	var selct = window.getSelection();
	var range2 = range.cloneRange();
	selct.removeAllRanges();
	range.deleteContents();
	range.insertNode(node);
	range2.selectNode(node);		
	range2.collapse(false);
	selct.removeAllRanges();
	selct.addRange(range2);
},

insertMethod : function( name )
{
	var node = window.document.createTextNode( "!|" + name + "|" );
	var range = window.getSelection().getRangeAt(0);

	var selct = window.getSelection();
	var range2 = range.cloneRange();
	selct.removeAllRanges();
	range.deleteContents();
	range.insertNode(node);
	range2.selectNode(node);		
	range2.collapse(false);
	selct.removeAllRanges();
	selct.addRange(range2);

	this.syntaxHighlight();
},


removeTags : function( code )
{
	code = code.replace(/<pre>/g,'');
	code = code.replace(/<\/pre>/g,'');
	code = code.replace(/<s>/g,'');
	code = code.replace(/<\/s>/g,'');
	code = code.replace(/&lt;/g,'<');
	code = code.replace(/&gt;/g,'>');
	code = code.replace(/&amp;/gi,'&');		
	return code;
},

snippets : function(evt) {
	var snippets = Language.snippets;	
	var trigger = this.getLastWord();
	for (var i=0; i<snippets.length; i++) {
		if(snippets[i].input == trigger) {
			var content = snippets[i].output.replace(/</g,'&lt;');
			content = content.replace(/>/g,'&gt;');
			if(content.indexOf('$0'
			)<0) content += cc;
			else content = content.replace(/\$0/,cc);
			content = content.replace(/\n/g,'<br>');
			var pattern = new RegExp(trigger+cc,'gi');
			evt.preventDefault(); // prevent the tab key from being added
			this.syntaxHighlight('snippets',pattern,content);
		}
	}
},

readOnly : function() {
	document.designMode = (arguments[0]) ? 'off' : 'on';
},

complete : function(trigger) {
	window.getSelection().getRangeAt(0).deleteContents();
	var complete = Language.complete;
	for (var i=0; i<complete.length; i++) {
		if(complete[i].input == trigger) {
			var pattern = new RegExp('\\'+trigger+cc);
			var content = complete[i].output.replace(/\$0/g,cc);
			parent.setTimeout(function () { CodePress.syntaxHighlight('complete',pattern,content)},0); // wait for char to appear on screen
		}
	}
},

getCompleteChars : function() {
	var cChars = '';
	for(var i=0;i<Language.complete.length;i++)
		cChars += '|'+Language.complete[i].input;
	return cChars+'|';
},

getCompleteEndingChars : function() {
	var cChars = '';
	for(var i=0;i<Language.complete.length;i++)
		cChars += '|'+Language.complete[i].output.charAt(Language.complete[i].output.length-1);
	return cChars+'|';
},

completeEnding : function(trigger) {
	var range = window.getSelection().getRangeAt(0);
	try {
		range.setEnd(range.endContainer, range.endOffset+1)
	}
	catch(e) {
		return false;
	}
	var next_character = range.toString()
	range.setEnd(range.endContainer, range.endOffset-1)
	if(next_character != trigger) return false;
	else {
		range.setEnd(range.endContainer, range.endOffset+1)
		range.deleteContents();
		return true;
	}
},

shortcuts : function() {
	var cCode = arguments[0];
	if(cCode==13) cCode = '[enter]';
	else if(cCode==32) cCode = '[space]';
	else cCode = '['+String.fromCharCode(charCode).toLowerCase()+']';
	for(var i=0;i<Language.shortcuts.length;i++)
		if(Language.shortcuts[i].input == cCode)
			this.insertCode(Language.shortcuts[i].output,false);
},

getRangeAndCaret : function() {	
	var range = window.getSelection().getRangeAt(0);

	var range2 = range.cloneRange();
	var node = range.endContainer;			
	var caret = range.endOffset;

	range2.selectNode(node);	
	return [range2.toString(),caret];
},

insertCode : function(code,replaceCursorBefore) {
	var range = window.getSelection().getRangeAt(0);
	var node = window.document.createTextNode(code);
	var selct = window.getSelection();
	var range2 = range.cloneRange();
	// Insert text at cursor position
	selct.removeAllRanges();
	range.deleteContents();
	range.insertNode(node);
	// Move the cursor to the end of text
	range2.selectNode(node);		
	range2.collapse(replaceCursorBefore);
	selct.removeAllRanges();
	selct.addRange(range2);
},

//get code from editor
getCode : function() {
	if(!document.getElementsByTagName('pre')[0] || editor.innerHTML == '')
	{
		editor = CodePress.getEditor();	
	}
	var code = editor.innerHTML;

	code = code.replace(/<p>/g,'\n');
	code = code.replace(/<br>/g,'\n');
	code = code.replace(/&nbsp;/gi,'');
	code = code.replace(/\u2009/g,'');
	code = code.replace(/<.*?>/g,'');
	code = code.replace(/&lt;/g,'<');
	code = code.replace(/&gt;/g,'>');
	code = code.replace(/&amp;/gi,'&');		
	return code;
},

//put code inside editor
setCode : function() {
	var code = arguments[0];
	code = code.replace(/\u2009/gi,'');

	code = code.replace(/&/gi,'&amp;');
	code = code.replace(/</g,'&lt;');
	code = code.replace(/>/g,'&gt;');
	editor.innerHTML = code;

	if (code == '')
		document.getElementsByTagName('body')[0].innerHTML = '';

},

checkFirstLine : function( url ) {	
	var tekst = CodePress.getCode().split("\n");


	if( tekst[0] != firstline )
	{		
		var range = window.getSelection().getRangeAt(0);
		var startNode = document.getElementsByTagName("pre").item(0);
		var startOffset = 0;	
		range.setStartBefore( startNode );

		var div = document.createElement('div');
		div.appendChild(range.cloneContents());		

		var selectedCode = CodePress.removeTags( div.innerHTML );
		range.collapse( false );

		if( firstline.length < selectedCode.length )
		{
			firstline = tekst[0];
			CodePress.loadXMLString( url );
		}	
	}
},

loadXMLString : function( url) {

	var pos = url.indexOf("?");
	var url = url.substring(0, pos );
	url += "?save"
		alert( url );


	httpRequest=null;
	if(window.XMLHttpRequest)
	{
		//code for IE7, Firefox, Opera, etc.
		httpRequest=new XMLHttpRequest();
	}
	else if (window.ActiveXObject)
	{
		//code for IE6, IE5
		httpRequest=new ActiveXObject("Microsoft.XMLHTTP");
	}
	if(httpRequest!=null)
	{

		httpRequest.open("GET",url,true);
		httpRequest.onreadystatechange=CodePress.state_Change();
		httpRequest.overrideMimeType('text/xml');
		httpRequest.send(null);
	}
	else
	{
		alert("Your browser does not support XMLHTTP.");
	}
},


state_Change : function() {
	if (httpRequest.readyState==4)
	{
		if (httpRequest.status == 404)
		{
			alert('Requestet URL is not found');
		}
		else if (httpRequest.status == 403)
		{
			alert('Access Denied');
		}
		else if (httpRequest.status==200)
		{                
			alert(httpRequest.status);                
		}
		else
		{
			alert("Problem retrieving XML data:" + httpRequest.statusText);
		}
	}       
},


alignStart : function( code ) {	
	var tekst = code.split("\n");	
	var linjearray = new Array();
	var template = new Array();
	var utskrift;
	firstline = tekst[0];


	var tablestart = 0;

	for( var i = 0; i < tekst.length; i++ )
	{	
		linjearray[i] = tekst[i].split("|");

	} 
	var utskrift = "";
	for( var i=0; i < linjearray.length-1; i++ ){ // Går igjennom hver linje


		if( linjearray[i][0].search(/\!/) != -1  ) //det er et utropstegn
		{
			template = new Array();

			for( var j = 0; j < linjearray[i].length-1; j++ )
			{
				template[j] = linjearray[i][j].length;
			}		
			tablestart = i;
		}


		if( linjearray[i].length > 1 ) //linjen er ikke tom
		{ 
			for( var k = 0; k < linjearray[i].length; k++ )
			{			 
				if( linjearray[i][k].length > template[k] && linjearray[i][k].search(/\!\d/) == -1 )
				{
					template[k] = linjearray[i][k].length;
				}
			}

			if( i > 0 && linjearray[i-1].length == 1 ) //det er ikke første linje og forrige linje er tom
			{
				tablestart = i;
				for( var k = 0; k < linjearray[i].length; k++ )
				{			 
					if( linjearray[i][k].length > template[k] && linjearray[i][k].search(/\!\d/) == -1 )
					{
						template[k] = linjearray[i][k].length;
					}
				}
			}

			if( i > 0 && linjearray[i-1][0].search(/\!/) != -1 ) //det er ikke første linje og det står en pipe på forrige linje
			{				
				for( var k = 1; k < linjearray[i].length-1; k++ )
				{			 
					if( linjearray[i][k].search(/\!\d/) == -1 )
					{
						template[k] = linjearray[i][k].length;
					}
				}
			}

			if( linjearray[i+1].length == 1  || linjearray[i+1][0].search(/\!/) != -1 )
			{
				for( var l = tablestart; l <= i; l++ )
				{
					for( var m = 0; m < linjearray[l].length-1; m++ )
					{		
						var nbspstring = "";
						if( linjearray[l][0].search(/\!/) == -1 )
						{
							for( var n = linjearray[l][m].length; n < template[m]; n++)
							{
								nbspstring += "&nbsp;";					 
							}
						}
						linjearray[l][m] += nbspstring + "|";
					}
				}				
			}
		}
	}

	for( var i = 0; i < linjearray.length; i++ )
	{
		for( var j = 0; j < linjearray[i].length; j++ )
			utskrift += linjearray[i][j];

		if( i < linjearray.length-1)
			utskrift += "\n";
	}

	editor.innerHTML = utskrift;
},



//undo and redo methods
actions : {
	pos : -1, // actual history position
	history : [], // history vector

	undo : function() {
	editor = CodePress.getEditor();
	if(editor.innerHTML.indexOf(cc)==-1){
		if(editor.innerHTML != " ")
			window.getSelection().getRangeAt(0).insertNode(document.createTextNode(cc));
		this.history[this.pos] = editor.innerHTML;
	}
	this.pos --;
	if(typeof(this.history[this.pos])=='undefined') this.pos ++;
	editor.innerHTML = this.history[this.pos];
	if(editor.innerHTML.indexOf(cc)>-1) editor.innerHTML+=cc;
	CodePress.findString();
},

redo : function() {
	// editor = CodePress.getEditor();
	this.pos++;
	if(typeof(this.history[this.pos])=='undefined') this.pos--;
	editor.innerHTML = this.history[this.pos];
	CodePress.findString();
},

next : function() { // get next vector position and clean old ones
	if(this.pos>20) this.history[this.pos-21] = undefined;
	return ++this.pos;
}
}
}

Language={};
window.addEventListener('load', function() { CodePress.initialize('new'); }, true);

Language.syntax = [
                   { input : /\"(.*?)(\"|<br>|<\/P>)/g, output : '<s>"$1$2</s>' }, // strings double quote
                   { input : /\'(.*?)(\'|<br>|<\/P>)/g, output : '<s>\'$1$2</s>' }, // strings single quote
                   { input : /\b(reject|show|check)\b/g, output : '<b>$1</b>' }, // reserved words
                   { input : /([\(\){}])/g, output : '<em>$1</em>' }, // special chars;
                   { input : /([^:]|^)\/\/(.*?)(<br|<\/P)/g, output : '$1<i>//$2</i>$3' }, // comments //
                   { input : /\/\*(.*?)\*\//g, output : '<i>/*$1*/</i>' } // comments /* */
                	   ]

                	   Language.snippets = []

                	                        Language.complete = [
                	                                             { input : '\'', output : '\'$0\'' },
                	                                             { input : '"', output : '"$0"' },
                	                                             { input : '(', output : '\($0\)' },
                	                                             { input : '[', output : '\[$0\]' },
                	                                             { input : '{', output : '{\n\t$0\n}' }		
                	                                             ]

                	                                             Language.shortcuts = []