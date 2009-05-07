
var LINK_TEXT = "Experior";

var MAX_ADD_LINK_TRIES = 3;
var MILLISECOND_SLEEP = 200;
var tries = 0;



function GetAttributeValue( node, attribute ) {
	for( var i=0;i<node.attributes.length;i++ ) {
		if( node.attributes[i].nodeName.toUpperCase() == attribute.toUpperCase() )
			return node.attributes[i].nodeValue;
	}
	return "";
}

function getDiv( tagName,attributeName,attributeValue,retryCall ) {
	tries++;
	var elements = document.getElementsByTagName( tagName ); 
	
	for( var i=0; i<elements.length; i++ ) {
		var value = GetAttributeValue( elements[i],attributeName );
		if( value==attributeValue ) {
			return elements[i];
		}
	}
	//still here, not found, try again.
	if( tries<MAX_ADD_LINK_TRIES ) {
		setTimeout( retryCall,MILLISECOND_SLEEP );
	}
}

function AddExperiorLink(){

		var editLink = getDiv("a","accesskey","e", AddExperiorLink);
		if( editLink==null )
			return;
		
		var div = getDiv("div","class","actions", AddExperiorLink);
		
		var link = document.createElement( 'A' );
		link.innerHTML = LINK_TEXT;
		
		var pathname = location.pathname;
		
		if( pathname=="/" )
			pathname += "FrontPage";
		
		link.href = pathname + "?Experior";
		div.insertBefore( link,editLink );
	
}

AddExperiorLink();
