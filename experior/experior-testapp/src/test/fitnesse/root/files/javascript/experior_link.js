
var tries = 0;

AddExperiorLink();

function GetAttributeValue( node, attribute ) {
	for( var i=0;i<node.attributes.length;i++ ) {
		if( node.attributes[i].nodeName.toUpperCase() == attribute.toUpperCase() )
			return node.attributes[i].nodeValue;
	}
	return "";
}

function getDiv( tagName,attributeName,attributeValue,retryCall ) {
	//tries++;
	var elements = document.getElementsByTagName( tagName ); 
	
	for( var i=0; i<elements.length; i++ ) {
		var value = GetAttributeValue( elements[i],attributeName );
		if( value==attributeValue ) {
			return elements[i];
		}
	}
	
	if( tries<3 ) {
		setTimeout( retryCall,200 );
	}
	
}

function AddExperiorLink(){

		var editLink = getDiv("a","accesskey","e", AddExperiorLink);
		if( editLink==null )
			return;
		
		var div = getDiv("div","class","actions", AddExperiorLink);
		
		var link = document.createElement( 'A' );
		link.innerHTML = "Experior";
		
		var pathname = location.pathname;
		
		if( pathname=="/" )
			pathname += "FrontPage";
		
		link.href = pathname + "?Experior";
		link.style.marginBottom = "5px";			
				
		div.insertBefore( link,editLink );	
}