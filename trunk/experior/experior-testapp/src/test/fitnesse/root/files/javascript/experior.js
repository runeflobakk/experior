
var CREATE_EXPERIOR_LINK = true;



var MAX_ADD_LINK_TRIES = 3;
var MILLISECOND_SLEEP = 200;
var tries = 0;
function GetAttributeValue(node,attribute){
	for(var i=0;i<node.attributes.length;i++){
		if(node.attributes[i].nodeName.toUpperCase()==attribute.toUpperCase())
			return node.attributes[i].nodeValue;
	}
	return "";
}
function SearchAndWaitForByAttribute(tagName,attributeName,attributeValue,retryCall){
	tries++;
	var elements = document.getElementsByTagName(tagName);
	for(var i=0;i<elements.length;i++){
		var value = GetAttributeValue(elements[i],attributeName);
		if(value==attributeValue){
			return elements[i];
		}
	}
	//still here, not found, try again.
	if(tries<MAX_ADD_LINK_TRIES){
		setTimeout(retryCall,MILLISECOND_SLEEP);
	}
}
function AddExperiorLink(){
	if(CREATE_EXPERIOR_LINK){
		var editLink = SearchAndWaitForByAttribute("a","accesskey","e",AddExperiorLink);
		if(editLink==null)return;
		
		var div = SearchAndWaitForByAttribute("div","class","actions",AddExperiorLink);
		
		var richLink = document.createElement('A');
		richLink.innerHTML = "Experior";
		var pathname = location.pathname;
		if(pathname=="/")pathname += "FrontPage";
		richLink.href = pathname + "?RichEdit";
		div.insertBefore(richLink,editLink);
	}
}
AddExperiorLink();
