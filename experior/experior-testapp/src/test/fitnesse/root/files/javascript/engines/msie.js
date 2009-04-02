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

CodePress = {
		scrolling : false,
		autocomplete : true,

		initialize : function() {
	if(typeof(editor)=='undefined' && !arguments[0]) return;
	chars = '|32|46|62|'; // charcodes that trigger syntax highlighting
	cc = '\u2009'; // carret char
	editor = document.getElementsByTagName('pre')[0];
	editor.contentEditable = 'true';
	document.getElementsByTagName('body')[0].onfocus = function() {editor.focus();}
	document.attachEvent('onkeydown', this.metaHandler);
	document.attachEvent('onkeypress', this.keyHandler);

	window.attachEvent('onscroll', function() { if(!CodePress.scrolling) setTimeout(function(){CodePress.syntaxHighlight('scroll')},1)});

	completeChars = this.getCompleteChars();

	completeEndingChars =  this.getCompleteEndingChars();
	setTimeout(function() { window.scroll(0,0) },50); // scroll IE to top
},

//treat key bindings
keyHandler : function( evt )
{
	charCode = evt.keyCode;
	fromChar = String.fromCharCode(charCode);

	/*
	if( (completeEndingChars.indexOf('|'+fromChar+'|')!= -1 || completeChars.indexOf('|'+fromChar+'|')!=-1  )&& CodePress.autocomplete)
	{ // auto complete
		if( !CodePress.completeEnding( fromChar ) )
			CodePress.complete(fromChar);
	}
	 */
	if(chars.indexOf('|'+charCode+'|')!=-1||charCode==13)
	{ // syntax highlighting
		CodePress.syntaxHighlight( 'generic', methods );
	}
},

metaHandler : function(evt)
{
	keyCode = evt.keyCode;

	if(keyCode==9 || evt.tabKey)
	{ 
		CodePress.snippets();
	}

	else if( keyCode==220)
	{
		evt.preventDefault();
		evt.stopPropagation();
		CodePress.align();		
	}

	else if((keyCode==122||keyCode==121||keyCode==90) && evt.ctrlKey)
	{ // undo and redo
		(keyCode==121||evt.shiftKey) ? CodePress.actions.redo() :  CodePress.actions.undo(); 
		evt.returnValue = false;
	}
	else if(keyCode==34||keyCode==33)
	{ // handle page up/down for IE
		self.scrollBy(0, (keyCode==34) ? 200 : -200); 
		evt.returnValue = false;
	}
	else if(keyCode==46||keyCode==8)
	{ // save to history when delete or backspace pressed
		CodePress.actions.history[CodePress.actions.next()] = editor.innerHTML;
	}
	else if((evt.ctrlKey || evt.metaKey) && evt.shiftKey && keyCode!=90)
	{ // shortcuts = ctrl||appleKey+shift+key!=z(undo) 
		CodePress.shortcuts(keyCode);
		evt.returnValue = false;
	}		
	else if(keyCode==86 && evt.ctrlKey)
	{ // handle paste

		top.setTimeout( function(){ CodePress.syntaxHighlight('paste');} );
	}		
	else if(keyCode==67 && evt.ctrlKey)
	{
		// handle cut
		/*window.clipboardData.setData('Text',x[0]);
			code = window.clipboardData.getData('Text');
		 */

	}
},

//put cursor back to its original position after every parsing

findString : function() {
	range = self.document.body.createTextRange();
	if(range.findText(cc)){
		range.select();
		range.text = '';
	}
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
		return code.substring(code.indexOf('<P>'),code.lastIndexOf('</P>')+4);
	}
},

//syntax highlighting parser
syntaxHighlight : function(flag, methods2) {

	if( methods2 != null )
		methods = methods2;
	
	lines = methods.split('\n');
	lines.pop();

	if(flag!='init') document.selection.createRange().text = cc;
	o = editor.innerHTML;

	if(flag=='paste') { // fix pasted text
		o = o.replace(/\u2008/g,'\t');
	}

	o = o.replace(/<P>/g,'\n');
	o = o.replace(/<\/P>/g,'\r');
	o = o.replace(/<.*?>/g,'');
	//o = o.replace(/&nbsp;/g,'');			
	o = '<PRE><P>'+o+'</P></PRE>';
	o = o.replace(/\n\r/g,'<P></P>');
	o = o.replace(/\n/g,'<P>');
	o = o.replace(/\r/g,'<\/P>');
	o = o.replace(/<P>(<P>)+/,'<P>');
	o = o.replace(/<\/P>(<\/P>)+/,'</P>');
	o = o.replace(/<P><\/P>/g,'<P><BR/></P>');
	x = z = this.split(o,flag);

	x = x.replace(/nbsp;/g, '&nbsp;');

	if(arguments[1]&&arguments[2]) x = x.replace(arguments[1],arguments[2]);
	var sRegExInput;
	for(i=0;i<Language.syntax.length;i++) 
		x = x.replace(Language.syntax[i].input,Language.syntax[i].output);

	var temp;
	var temp2;
	var temp3;
	var evalstring = "";		
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

snippets : function(evt) {
	var snippets = Language.snippets;
	var trigger = this.getLastWord();
	for (var i=0; i<snippets.length; i++) {
		if(snippets[i].input == trigger) {
			var content = snippets[i].output.replace(/</g,'&lt;');
			content = content.replace(/>/g,'&gt;');
			if(content.indexOf('$0')<0) content += cc;
			else content = content.replace(/\$0/,cc);
			content = content.replace(/\n/g,'</P><P>');
			var pattern = new RegExp(trigger+cc,"gi");
			this.syntaxHighlight('snippets',pattern,content);

		}
	}
},

readOnly : function() {
	editor.contentEditable = (arguments[0]) ? 'false' : 'true';
},

complete : function(trigger) {
	var complete = Language.complete;
	for (var i=0; i<complete.length; i++) {
		if(complete[i].input == trigger) {
			var pattern = new RegExp('\\'+trigger+cc);
			//alert( pattern );
			var content = complete[i].output.replace(/\$0/g,cc);
			setTimeout(function () { CodePress.syntaxHighlight('complete',pattern,content)},0); // wait for char to appear on screen
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
	var range = document.selection.createRange();
	try {
		range.moveEnd('character', 1)
	}
	catch(e) {
		return false;
	}
	var next_character = range.text
	range.moveEnd('character', -1)
	if(next_character != trigger )  return false;
	else {
		range.moveEnd('character', 1)
		range.text=''
			return true;
	}
},	

shortcuts : function() {
	var cCode = arguments[0];
	if(cCode==13) cCode = '[enter]';
	else if(cCode==32) cCode = '[space]';
	else cCode = '['+String.fromCharCode(keyCode).toLowerCase()+']';
	for(var i=0;i<Language.shortcuts.length;i++)
		if(Language.shortcuts[i].input == cCode)
			this.insertCode(Language.shortcuts[i].output,false);
},

getLastWord : function()
{
	var rangeAndCaret = CodePress.align();
	//alert( rangeAndCaret );
	
	//alert( this.getCode().substring( 0, rangeAndCaret ) );
	
	var i = rangeAndCaret;
	//alert( this.getCode().substring( 0, i ) );
	

	/*
	for( var i = 0; i < rangeAndCaret; i++ )
	{
		alert( "NEI");
	}
	*/
	//alert( rangeAndCaret[0]);
	//alert( rangeAndCaret[1]);
	
/*
	words = rangeAndCaret[0].substring(0,rangeAndCaret[1]);
	
	
	words = words.split('\n');
	
	return words[words.length-1].replace(/[\W]/gi,'').toLowerCase();
	*/
},

align : function()
{	
	var range = document.selection.createRange();
	
	var caret = document.selection.createRange();
	var range = document.selection.createRange();
	
	caret.moveStart( 'character', -this.getCode().length );
	
	var tekst = CodePress.getCodeFromCaret( caret.htmlText );
		
	var linjearray = tekst.split("\n");
	
	var currentline = linjearray[linjearray.length-1].split("|");
	if (linjearray.length > 0) 
		var previousline = linjearray[linjearray.length-2].split("|");
	
	var nbspstring = "";
	
	for (var i = 0; i < (previousline[currentline.length-1].replace(/&nbsp;/gi,' ').length)-(currentline[currentline.length-1].length); i++)
		nbspstring += "&nbsp;";
	
	range.pasteHTML( nbspstring  );
},

insertCode : function(code,replaceCursorBefore) {
	var repdeb = '';
	var repfin = '';

	if(replaceCursorBefore) { repfin = code; }
	else { repdeb = code; }

	if(typeof document.selection != 'undefined') {
		var range = document.selection.createRange();
		range.text = repdeb + repfin;
		range = document.selection.createRange();
		range.move('character', -repfin.length);
		range.select();	
	}	
},

getCodeFromCaret : function( code ) {
	
	code = code.replace(/<br>/g,'\n');
	code = code.replace(/<p>/i,' '); // IE first line fix
	code = code.replace(/<p>/gi,'\n');
	//code = code.replace(/&nbsp;/gi,'');
	code = code.replace(/\u2009/g,'');
	code = code.replace(/<.*?>/g,'');
	code = code.replace(/&lt;/g,'<');
	code = code.replace(/&gt;/g,'>');
	code = code.replace(/&amp;/gi,'&');
	return code;
},

//get code from editor	
getCode : function() {
	var code = editor.innerHTML;
	code = code.replace(/<br>/g,'\n');
	code = code.replace(/<p>/i,''); // IE first line fix
	code = code.replace(/<p>/gi,'\n');
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
	//code = code.replace(/&nbsp;/gi, ' ');
	code = code.replace(/&/gi,'&amp;');		
	code = code.replace(/</g,'&lt;');
	code = code.replace(/>/g,'&gt;');
	editor.innerHTML = '<pre>'+code+'</pre>';
},

alignStart : function( code ) {	
	var tekst = code.split("\n");
	var linjearray = new Array();
	var template = new Array();
	var utskrift;

	var tablestart = 0;

	for( var i = 0; i < tekst.length; i++ )
	{	
		linjearray[i] = tekst[i].split("|");

	} 
	var utskrift = "";
	for( var i=0; i < linjearray.length-1; i++ ){ // Går igjennom hver linje


		if( linjearray[i][0].search(/\!/) != -1 ) //det er et utropstegn
		{
			template = new Array();

			for( var j = 0; j < linjearray[i].length-1; j++ )
			{
				template[j] = linjearray[i][j].length;
			}		
			tablestart = i;
		}

		if( linjearray[i].length > 1 )
		{ 
			for( var k = 0; k < linjearray[i].length; k++ )
			{			 
				if( linjearray[i][k].length > template[k] )
				{
					template[k] = linjearray[i][k].length;
				}
			}

			if( i > 0 && linjearray[i-1].length == 1 ) {
				tablestart = i;
				for( var k = 0; k < linjearray[i].length; k++ )
				{			 
					if( linjearray[i][k].length > template[k] )
					{
						template[k] = linjearray[i][k].length;
					}
				}
			}

			if( linjearray[i+1].length == 1  || linjearray[i+1][0].search(/\!/) != -1)
			{
				for( var l = tablestart; l <= i; l++ )
				{
					for( var m = 0; m < linjearray[l].length-1; m++ )
					{		
						var nbspstring = "";	
						for( var n = linjearray[l][m].length; n < template[m]; n++)
						{
							nbspstring += "nbsp;";					 
						}
						linjearray[l][m] += nbspstring + "|";

					}
				}				
			}
		}
	}
	for( var i = 0; i < linjearray.length-1; i++ )
	{
		for( var j = 0; j < linjearray[i].length; j++ )
			utskrift += linjearray[i][j];

		utskrift += "\n";
	}

	this.setCode(utskrift);
},

//undo and redo methods
actions : {
	pos : -1, // actual history position
	history : [], // history vector

	undo : function() {
	if(editor.innerHTML.indexOf(cc)==-1){
		document.selection.createRange().text = cc;
		this.history[this.pos] = editor.innerHTML;
	}
	this.pos--;
	if(typeof(this.history[this.pos])=='undefined') this.pos++;
	editor.innerHTML = this.history[this.pos];
	CodePress.findString();
},

redo : function() {
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
window.attachEvent('onload', function() { CodePress.initialize('new');});



Language.syntax = [

                   { input : /\"(.*?)(\"|<br>|<\/P>)/g, output : '<s>"$1$2</s>' }, // strings double quote
                   { input : /\'(.*?)(\'|<br>|<\/P>)/g, output : '<s>\'$1$2</s>' }, // strings single quote
                   { input : /\b(abstract|for|new|switch|default|goto|boolean|do|if|private|this|break|double|protected|throw|byte|else|import|public|throws|case|return|catch|extends|int|short|try|char|final|interface|static|void|class|finally|long|const|float|while|function|label)\b/g, output : '<b>$1</b>' }, // reserved words
                   { input : /([\(\){}])/g, output : '<em>$1</em>' }, // special chars;
                   { input : /([^:]|^)\/\/(.*?)(<br|<\/P)/g, output : '$1<i>//$2</i>$3' }, // comments //
                   //	{ input : /test/g, output : 'testere' }, // comments //
                   { input : /\/\*(.*?)\*\//g, output : '<i>/*$1*/</i>' } // comments /* */
                	   ]

//              	   Language.syntax.push("input : /test/g, output : 'testere'");


                	   Language.snippets = []

                	                        Language.complete = [
                	                                             { input : '\'', output : '\'$0\'' },
                	                                             { input : '"', output : '"$0"' },
                	                                             { input : '(', output : '\($0\)' },
                	                                             { input : '[', output : '\[$0\]' },
                	                                             { input : '{', output : '{\n\t$0\n}' }		
                	                                             ]

                	                                             Language.shortcuts = []