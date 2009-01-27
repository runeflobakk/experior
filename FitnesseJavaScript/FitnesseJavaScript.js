
   function readKey(e, textarea) {
     var oldcursorposition = getCursor(textarea);
    
    if (e.keyCode == '13'){
        visPosisjon(textarea);
        setCursor(textarea, oldcursorposition);
    }

   }
   function getCursor(textarea){

        var cursorPos = 0;
             if (document.selection)
            {
                  textarea.focus();
                       var tmpRange = document.selection.createRange();
                    tmpRange.moveStart('character',-textarea.value.length);
                  cursorPos = tmpRange.text.length;
                 }
            else     {
              if (textarea.selectionStart || textarea.selectionStart == '0')
                {
                 cursorPos = textarea.selectionStart;
                }
            }
        return cursorPos;
      }
function nl2br(text){
    text = escape(text);
    if(text.indexOf('%0D%0A') > -1){
        re_nlchar = /%0D%0A/g ;
    }else if(text.indexOf('%0A') > -1){
        re_nlchar = /%0A/g ;
    }else if(text.indexOf('%0D') > -1){
        re_nlchar = /%0D/g ;
    }
    return unescape( text.replace(re_nlchar,'<br />') );
}

function setCursor(obj, pos) {
    if(obj.createTextRange) {
        /* Create a TextRange, set the internal pointer to
           a specified position and show the cursor at this
           position
        */
        var range = obj.createTextRange();
        range.move("character", pos);
        range.select();
    } else if(obj.selectionStart) {
        /* Gecko is a little bit shorter on that. Simply
           focus the element and set the selection to a
           specified position
        */
        obj.focus();
        obj.setSelectionRange(pos, pos);
    }
}

   function isIE() {
    var ie = (navigator.userAgent.indexOf("MSIE")==-1)?false:true;
    return ie;
   }
   function visPosisjon( textarea )
   {

      var longest = 0;

      var tekst = textarea.value.split('\n'); //Splitter opp hver linje i hvert sitt arrayelement
      var substart,subend;
      var linjeArray = new Array();
      var template = new Array();
      var space;
      var content = "";   
      debug("");
      for(var i=0, len = tekst.length; i < len; i++) { // Går igjennom arrayen (linjene)
            substart = 0;
            subend = 0;
        
            if (tekst[i].substr(0,1) == "|") //Hvis linja starter med | skal første tegn kuttes
                substart = 1;
            if (tekst[i].substr(tekst[i].length-1,tekst[i].length) == "|") // Hvis den slutter med | skal siste tegn kuttes
                subend = 1;

            linjeArray[i] = tekst[i].substring(substart,tekst[i].length-subend).split('|'); // Splitter linja til en array der "radene" ligger

        
        for (var j=0, len2 = linjeArray[i].length; j < len2; j++) {
            linjeArray[i][j] = linjeArray[i][j].replace(/^\s+|\s+$/g, '');
            if (isIE() == true) {
        if (linjeArray[i][j] == "") linjeArray[i].pop();
       
        }

}
        
                  
      }
 
      for(var i=0, len = linjeArray.length; i < len; i++) {// Går igjennom hver linje
              for(var j=0, len2 = linjeArray[i].length; j < len2; j++) { // Går igjennom hvert element på linje
            if (template[j] == null)
                template[j] = 0;
            if (linjeArray[i][j].length > template[j]) {
                template[j] = linjeArray[i][j].length;
            }

        }
     }

   
   
   for(var i=0, len = linjeArray.length; i < len; i++) // Går igjennom hver linje
   {
       
    if (linjeArray[i][0] != "") {
        for(var j=0, len2 = linjeArray[i].length; j < len2; j++) // Går igjennom hvert element på linja
            {
                space = "";
                
                for(var k=0, len3 = template[j] - linjeArray[i][j].length; k < len3; k++)
                        space += " ";
          
                content += "|" + linjeArray[i][j] + space; // Legger linja med mellomrommene inn i content-variablen
 
            if (j == len2-1 && i != len-1)
                content += "|\n";
            else if (j == len2-1)
                content+= "|";
            
            }    
    } else if (i != len-1) {
        content += "\n";    
    }
        


    }
    
    document.getElementById("ta").value = content; // Legger innholdet i variablen content inn i textarea
    debug(nl2br(content));
 
  }
 
  function debug(text, append) {
    if (append == true)
        document.getElementById("debug").value += text + "\n";
    else
        document.getElementById("debug").value = text;
  }





