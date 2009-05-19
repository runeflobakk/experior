var methods;
var firstLine;
var lines;

Experior = {

initialize : function() {
	
	body = document.getElementsByTagName('body')[0];
	body.innerHTML = body.innerHTML.replace(/\n/g,"");	
	
	editor = document.getElementsByTagName('pre')[0];
	document.designMode = 'on';
	document.addEventListener('keypress', this.keyListener, false);
	window.addEventListener('scroll', function() { if(!Experior.scrolling) Experior.highlightDocument('scroll') }, false);
	
	firstline = Experior.getText().split("\n");
	firstline = firstline[0];
	caret = '\u2009';
},

keyListener : function(evt) {
	
	keyCode = evt.keyCode;	
	charCode = evt.charCode;

	// Space or enter pressed
	if(charCode == 32 ||keyCode==13) {
		Experior.highlightDocument();
	}
	// Pipe (|)
	else if( charCode==124) {	
		evt.preventDefault();
		evt.stopPropagation();
		Experior.align();	
	}
	// Tab
	else if(keyCode==9 || evt.tabKey) {
		evt.preventDefault();
		evt.stopPropagation();
		Experior.tab();
	}
	// Delete or backspace
	else if(keyCode==46||keyCode==8) {		
		Experior.actions.history[Experior.actions.next()] = editor.innerHTML;
	}
	// Undo or redo
	else if((charCode==122||charCode==121||charCode==90) && evt.ctrlKey) { 
		(charCode==121||evt.shiftKey) ? Experior.actions.redo() :  Experior.actions.undo(); 
		evt.preventDefault();
	}
	// Paste
	else if(charCode==118 && evt.ctrlKey) {
		Experior.getRangeAndCaret();
		Experior.highlightDocument();
	}
},

updateDivWidth : function( width ) {

	var div = parent.document.getElementById("methodsDiv");
	
	if( width == "over" )
	{
		div.style.width = "auto";
		div.style.overflow = "auto";
		
		div.style.overflow = "-moz-scrollbars-vertical";
		div.style.minWidth = "135px";
		
		
	}
	else
	{
		div.style.width="135px";
		div.style.overflow = "hidden";
	}
},



createMethodsDiv : function( methodsInDiv ) {

	var newdiv = document.createElement('div'); 
	var divIdName = 'methodsDiv'; 

	var metodestring = "";

	parent.document.body.appendChild(newdiv);
	newdiv.setAttribute('id',divIdName);
	newdiv.onmouseover = new Function("Experior.updateDivWidth('over');");
	newdiv.onmouseout = new Function("Experior.updateDivWidth('out');");
	newdiv.style.width = "135px";
	newdiv.style.backgroundImage = "url(/files/javascript/divbackground.png)";
	newdiv.style.backgroundRepeat = "repeat";
	newdiv.style.whiteSpace = "nowrap";
	newdiv.style.overflow = "hidden";
	
	newdiv.style.left = "5px";
	newdiv.style.height = screen.height - 310 + 'px';
	newdiv.style.top = "120px"; 
	newdiv.style.position = "fixed";

	newdiv.style.textDecoration = "none";		

	if( methodsInDiv.length > 1 )
	{
		newdiv.innerHTML = "<h3>Methods</h3>";
		for( var i = 0; i < methodsInDiv.length; i++ )
		{	
			methodsInDiv[i].trim;

			metodestring += "<a style='margin-bottom:5px; display:block; text-decoration: none;' href=javascript:void(0) onclick=insertMethod(" + i + ")>" + methodsInDiv[i] + "</a>";		
		}
		newdiv.innerHTML += metodestring;
	}
	else
		newdiv.innerHTML = "";

},

updateMethodsDiv : function() {
	var metodestring = "";	

	parent.document.getElementById("methodsDiv").innerHTML = "";

	if( lines.length > 0 )
	{

		for( var i = 0; i < lines.length; i++ )
		{
			lines[i].trim;

			metodestring += "<a style='margin-bottom:5px; display:block; text-decoration: none;' href=javascript:void(0) onclick=insertMethod(" + i + ")>" + lines[i] + "</a>";
		}
		parent.document.getElementById("methodsDiv").innerHTML = "<h3>Methods</h3>" + metodestring;
	}
},

splitLargeTests : function(code,flag) {
	if(flag=='scroll') {
		this.scrolling = true;
		return code;
	}
	else {
		this.scrolling = false;
		var cursor = code.indexOf(caret);
		
		if(cursor-3000<0){
			var start=0;
			var end=4000;
		}
		else if(cursor+2000>code.length) {
			var start=code.length-4000;
			var end=code.length;
		}
		else {
			var start=cursor-2000;
			var end=cursor+2000;
		}
		
		code = code.substring(start,end);
		return code;
	}
},

highlightDocument : function(flag, methods2) {

	if( methods2 != null )
	{		
		methods = methods2;		
	}
	
	if( methods.length == 0 )
		lines = new Array();
	else
	{
		lines = methods.split('\n');
		lines.pop();
	}

	if(flag != 'init') { window.getSelection().getRangeAt(0).insertNode(document.createTextNode(caret)); }

	editor = Experior.getEditor();
	o = editor.innerHTML;

	o = o.replace(/<br>/g,'\n');
	o = o.replace(/<.*?>/g,'');
	x = z = this.splitLargeTests(o,flag);
	x = x.replace(/\n/g,'<br>');
	x = x.replace(/&nbsp;/g, '&nbsp;');

	if(arguments[1]&&arguments[2]) x = x.replace(arguments[1],arguments[2]);
	var sRegExInput;
	for(i=0;i<Language.syntax.length;i++) 
		x = x.replace(Language.syntax[i].input,Language.syntax[i].output);

	var words1;
	var words2;
	var evalstring = "";
	var words3;

	for(j=0;j<lines.length; j++) {
		lines[j] = lines[j].replace(/\s+$/,"");
		words1 = lines[j].split(' ');

		words2 = words1.join( "(\\s+|(?:&nbsp;)*\\s*\\|.*?\\|\\s*)" );

		words3 = "";
		for (var i = 0; i<words1.length; i++) {

			words3 += "<s>"+words1[i]+"</s>";

			if (i != words1.length-1)
				words3 += "$"+(i+1);
		}
		eval('x = x.replace(/'+words2+'/g, \"'+ words3 + '\")');
	}

	editor.innerHTML = this.actions.history[this.actions.next()] = (flag=='scroll') ? x : o.replace(z,x);
	if(flag!='init') this.findString();

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

getLastWord : function() {
	var rangeAndCaret = Experior.getRangeAndCaret();
	words = rangeAndCaret[0].substring(rangeAndCaret[1]-40,rangeAndCaret[1]);
	words = words.replace(/[\s\n\r\);\W]/g,'\n').split('\n');
	return words[words.length-1].replace(/[\W]/gi,'').toLowerCase();
},

findString : function() {
	if(self.find(caret))
	{
		window.getSelection().getRangeAt(0).deleteContents();		
	}
},

align : function()
{
	var range = window.getSelection().getRangeAt(0);
	var startNode = document.getElementsByTagName("pre").item(0);

	var startOffset = 0;	

	range.setStartBefore( startNode );

	var div = document.createElement('div');
	div.appendChild(range.cloneContents());

	var linearray = div.innerHTML.split("<br>");

	for( var i = 0; i < linearray.length; i++ )
	{
		linearray[i] = this.removeTags( linearray[i] );
	}

	range.collapse( false );


	if( linearray.length <= 1 || linearray[linearray.length-2] == 0 || linearray[linearray.length-2].search(/span/) > 0 )
	{		
		this.createTextnode();
		return;
	}

	var currentline = linearray[linearray.length-1].split("|");

	if (linearray.length > 0 && linearray.length-2 > 0 ) 
		var previousline = linearray[linearray.length-2].split("|");

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
		Experior.createTextnode();
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

insertMethod : function( id )
{
	var node = window.document.createTextNode( "!|" + lines[id] + "|" );
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

	this.highlightDocument();
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
			)<0) content += caret;
			else content = content.replace(/\$0/,caret);
			content = content.replace(/\n/g,'<br>');
			var pattern = new RegExp(trigger+caret,'gi');
			evt.preventDefault(); // prevent the tab key from being added
			this.highlightDocument('snippets',pattern,content);
		}
	}
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
getText : function() {
	if(!document.getElementsByTagName('pre')[0] || editor.innerHTML == '')
	{
		editor = Experior.getEditor();	
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
setText : function() {

	var code = arguments[0];
	code = code.replace(/\u2009/gi,'');

	code = code.replace(/&/gi,'&amp;');
	code = code.replace(/</g,'&lt;');
	code = code.replace(/>/g,'&gt;');


	if (code == '')
		document.getElementsByTagName('body')[0].innerHTML = '';

},

tab : function() {


	var selection = window.getSelection();

	var range = selection.getRangeAt(0);

	var nodevalue = range.endContainer.nodeValue; 


	if( nodevalue.search(/\|/) > -1  )
		range.setEnd( range.startContainer, range.startContainer.length );
	else if( range.endContainer.nextSibling != null ){
		alert(" ELSE !");
		range.setEnd( range.startContainer, range.nextSibling.length );
	}
	else if( range.endContainer.firstChild != null ) {
		alert( "ELSE !! " );
		range.setEnd( range.startContainer, range.firstChild.length );
	}


	range.collapse(false);
	//alert( range.commonAncestorContainer );

	//var selct = window.getSelection();
	/*
	var treeWalker = document.createTreeWalker(

		    document.body,
		    NodeFilter.SHOW_ELEMENT,

		    { acceptNode: function(node) { return NodeFilter.FILTER_ACCEPT; } },
		    false
		);

		var nodeList = new Array();
		while(treeWalker.nextNode()) 
			nodeList.push(treeWalker.currentNode);

		//alert( treeWalker.currentNode );



	 */
},

checkFirstLine : function( url ) {	
	var tekst = Experior.getText().split("\n");


	if( tekst[0] != firstline )
	{		
		var range = window.getSelection().getRangeAt(0);
		var startNode = document.getElementsByTagName("pre").item(0);
		var startOffset = 0;	
		range.setStartBefore( startNode );

		var div = document.createElement('div');
		div.appendChild(range.cloneContents());		

		var selectedCode = Experior.removeTags( div.innerHTML );
		range.collapse( false );

		if( firstline.length < selectedCode.length )
		{
			firstline = tekst[0];
			Experior.loadXMLString( url );
		}	
	}
},

loadXMLString : function( url) {

	var firstline = Experior.getText().split("\n");
	var url = "http://localhost:8080/FrontPage?responder=Commands&var=" + firstline[0];

	httpRequest=null;
	if(window.XMLHttpRequest)
	{
		//code for IE7, Firefox, Opera, etc.
		httpRequest=new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
		//code for IE6, IE5
		httpRequest=new ActiveXObject("Microsoft.XMLHTTP");
	}
	if(httpRequest!=null)
	{
		httpRequest.onreadystatechange= function()
		{			
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
					methods = httpRequest.getResponseHeader("json");

					var methodsarray;
					if( methods.length == 4 )
						methods = new Array();
					else
					{
						methodsarray = eval('('+ methods +')');
						methods = methodsarray.join("\n") + "\n";
					}

					Experior.highlightDocument();
					Experior.updateMethodsDiv();
				}
				else
				{
					alert("Problem retrieving XML data:" + httpRequest.statusText);
				}
			}			
		}
		httpRequest.open("GET",url,true);

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
	else
		alert( httpRequest.readyState);
},


alignAllPipesOnPageLoad : function( code ) {	
	var tekst = code.split("\n");	
	var linearray = new Array();
	var template = new Array();
	var utskrift;
	firstline = tekst[0];


	var tablestart = 0;

	for( var i = 0; i < tekst.length; i++ )
	{	
		linearray[i] = tekst[i].split("|");

	} 
	var utskrift = "";
	for( var i=0; i < linearray.length-1; i++ ){ // Går igjennom hver line


		if( linearray[i][0].search(/\!/) != -1  ) //det er et utropstegn
		{
			template = new Array();

			for( var j = 0; j < linearray[i].length-1; j++ )
			{
				template[j] = linearray[i][j].length;
			}		
			tablestart = i;
		}


		if( linearray[i].length > 1 ) //linen er ikke tom
		{ 
			for( var k = 0; k < linearray[i].length; k++ )
			{			 
				if( linearray[i][k].length > template[k] && linearray[i][k].search(/\!\d/) == -1 )
				{
					template[k] = linearray[i][k].length;
				}
			}

			if( i > 0 && linearray[i-1].length == 1 ) //det er ikke første line og forrige line er tom
			{
				tablestart = i;
				for( var k = 0; k < linearray[i].length; k++ )
				{			 
					if( linearray[i][k].length > template[k] && linearray[i][k].search(/\!\d/) == -1 )
					{
						template[k] = linearray[i][k].length;
					}
				}
			}

			if( i > 0 && linearray[i-1][0].search(/\!/) != -1 ) //det er ikke første line og det står en pipe på forrige line
			{				
				for( var k = 1; k < linearray[i].length-1; k++ )
				{			 
					if( linearray[i][k].search(/\!\d/) == -1 )
					{
						template[k] = linearray[i][k].length;
					}
				}
			}

			if( linearray[i+1].length == 1  || linearray[i+1][0].search(/\!/) != -1 )
			{
				for( var l = tablestart; l <= i; l++ )
				{
					for( var m = 0; m < linearray[l].length-1; m++ )
					{		
						var nbspstring = "";
						if( linearray[l][0].search(/\!/) == -1 )
						{
							for( var n = linearray[l][m].length; n < template[m]; n++)
							{
								nbspstring += "&nbsp;";					 
							}
						}
						linearray[l][m] += nbspstring + "|";
					}
				}				
			}
		}
	}

	for( var i = 0; i < linearray.length; i++ )
	{
		for( var j = 0; j < linearray[i].length; j++ )
			utskrift += linearray[i][j];

		if( i < linearray.length-1)
			utskrift += "\n";
	}

	editor.innerHTML = utskrift;
},

//undo and redo methods
actions : {
	pos : -1, // actual history position
	history : [], // history vector

	undo : function() {
	editor = Experior.getEditor();
	if(editor.innerHTML.indexOf(caret)==-1){
		if(editor.innerHTML != " ")
			window.getSelection().getRangeAt(0).insertNode(document.createTextNode(caret));
		this.history[this.pos] = editor.innerHTML;
	}
	this.pos --;
	if(typeof(this.history[this.pos])=='undefined') this.pos ++;
	editor.innerHTML = this.history[this.pos];
	if(editor.innerHTML.indexOf(caret)>-1) editor.innerHTML+=caret;
	Experior.findString();
},

redo : function() {
	// editor = Experior.getEditor();
	this.pos++;
	if(typeof(this.history[this.pos])=='undefined') this.pos--;
	editor.innerHTML = this.history[this.pos];
	Experior.findString();
},

next : function() { // get next vector position and clean old ones
	if(this.pos>20) this.history[this.pos-21] = undefined;
	return ++this.pos;
}
}
}



Language={};

window.addEventListener('load', function() { Experior.initialize('new'); }, true);
