/*
 * CodePress - Real Time Syntax Highlighting Editor written in JavaScript - http://codepress.org/
 * 
 * Copyright (C) 2006 Fernando M.A.d.S. <fermads@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation.
 * 
 * Read the full licence: http://www.opensource.org/licenses/lgpl-license.php
 */
var experior;
var hidden;
Experior = function(textarea, obj2)
{
	experior = document.createElement('iframe');
	experior.textarea = textarea;
	
	hidden = obj2;		
	experior.textarea.disabled = true;
	experior.textarea.style.overflow = 'hidden';
	experior.style.height = screen.height - 330 + 'px';//experior.textarea.clientHeight +'px';
	experior.style.width = screen.width - 170 + 'px';//experior.textarea.clientWidth +'px';
	experior.textarea.style.overflow = 'auto';
	experior.style.border = '1px solid gray';
	experior.frameBorder = 0; // remove IE internal iframe border
	experior.style.visibility = 'hidden';
	experior.style.position = 'absolute';
	experior.options = experior.textarea.className;

	experior.initialize = function() {

		experior.editor = experior.contentWindow.Experior;
		experior.contentWindow.focus();
		experior.editor.body = experior.contentWindow.document.getElementsByTagName('body')[0];

		experior.editor.alignStart( experior.textarea.value);
		experior.setOptions();
		experior.editor.syntaxHighlight('init', hidden.value); //hidden.value henter metodenavn fra hidden field
		experior.textarea.style.display = 'none';
		experior.style.position = 'static';
		experior.style.visibility = 'visible';
		experior.style.display = 'inline';		

		var methods = hidden.value.split("\n");
		experior.editor.createMethodsDiv( methods);
	}

	// obj can by a textarea id or a string (code)
	experior.edit = function(textarea,language) {
		if(textarea) experior.textarea.value = document.getElementById(textarea) ? document.getElementById(textarea).value : textarea;
		if(!experior.textarea.disabled) return;
		experior.language = language ? language : experior.getLanguage();

		experior.src = Experior.path+'experior.html?';

		if(experior.attachEvent)
		{
			experior.attachEvent('onload',experior.initialize);
		}
		else 
		{
			experior.addEventListener('load',experior.initialize,false);
		}
	}

	experior.getLanguage = function() {
		for (language in Experior.languages) 
			if(experior.options.match('\\b'+language+'\\b')) 
				return ExperiorPress.languages[language] ? language : 'generic';
	}

	experior.setOptions = function() {
		if(experior.options.match('autocomplete-off')) experior.toggleAutoComplete();
		if(experior.options.match('readonly-on')) experior.toggleReadOnly();
		if(experior.options.match('linenumbers-off')) experior.toggleLineNumbers();
	}

	experior.getCode = function() {
		return experior.textarea.disabled ? experior.editor.getCode() : experior.textarea.value;
	}

	experior.setCode = function(code) {
		experior.textarea.disabled ? experior.editor.setCode(code) : experior.textarea.value = code;
	}

	experior.toggleAutoComplete = function() {
		experior.editor.autocomplete = (experior.editor.autocomplete) ? false : true;
	}


	experior.toggleReadOnly = function() {
		experior.textarea.readOnly = (experior.textarea.readOnly) ? false : true;
		if(experior.style.display != 'none') // prevent exception on FF + iframe with display:none
			experior.editor.readOnly(experior.textarea.readOnly ? true : false);
	}

	experior.toggleLineNumbers = function() {
		var cn = experior.editor.body.className;
		experior.editor.body.className = (cn==''||cn=='show-line-numbers') ? 'hide-line-numbers' : 'show-line-numbers';
	}

	experior.toggleEditor = function() {
		if(experior.textarea.disabled) {
			experior.textarea.value = getCode();
			experior.textarea.disabled = false;
			experior.style.display = 'none';
			experior.textarea.style.display = 'inline';
		}

		else {
			experior.textarea.disabled = true;
			experior.setCode(experior.textarea.value);
			experior.editor.syntaxHighlight('init');
			experior.style.display = 'inline';
			experior.textarea.style.display = 'none';
		}
	}


	experior.edit();
	return experior;
}



Experior.run = function() {
	s = document.getElementsByTagName('script');
	for(var i=0,n=s.length;i<n;i++) {
		if(s[i].src.match('experior.js')) {
			Experior.path = s[i].src.replace('experior.js','');
		}
	}
	
	var textarea = document.getElementById('experior');
	var hidden = document.getElementById('hiddenfield'); //gets the hidden field
	
			id = textarea.id;
			eval(id+' = new Experior(textarea, hidden)');
			textarea.parentNode.insertBefore(eval(id), textarea);	
}

if(window.attachEvent) {
	window.attachEvent('onload',Experior.run);
}
else window.addEventListener('DOMContentLoaded',Experior.run,false);


//Gets the text from the editor to the invisible textarea
function moveText()
{
	hidden.value = experior.getCode();
}

function saveAndExit()
{
	hidden.value = experior.getCode();
	var form = document.getElementById( "hiddenfieldform" );
}

function alignClick()
{	
	experior.editor.alignStart( experior.getCode() );
	experior.setOptions();
	experior.editor.syntaxHighlight('init', hidden.value); //hidden.value henter metodenavn fra hidden field
}

//Gets the text from the hidden field 
function moveTextUp()
{	
	experior.setCode( hidden.value );
}

function insertMethod(method)
{
	var methods = hidden.value.split("\n");
	experior.editor.insertMethod(method);
	experior.contentWindow.focus();
}

function checkFirstLine()
{
	var url = window.location.href;
	
	experior.editor.checkFirstLine( url );
}