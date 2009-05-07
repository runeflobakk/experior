/*
 * CodePress regular expressions for generic syntax highlighting
 */
 
// generic languages

Language.syntax = [
	{ input : /\b(reject|show|check)\b/g, output : '<b>$1</b>' }, // reserved words
	{ input : /([\(\)])/g, output : '<em>$1</em>' }, // special chars;
	{ input : /([^:]|^)\!3(.*?)(<br|<\/P)/g, output : '$1<span class=comment3>!3$2</span>$3' },
	{ input : /([^:]|^)\!2(.*?)(<br|<\/P)/g, output : '$1<span class=comment2>!2$2</span>$3' },
	{ input : /([^:]|^)\!1(.*?)(<br|<\/P)/g, output : '$1<span class=comment1>!1$2</span>$3' },
	{ input : /\{{3}(.*?)\}{3}/gim, output : '<span class=disabled>{{{$1}}}</span>' } // comments /* */
	//{ input : /\{{{(.*?)\}}}\//g, output : '<i>/*$1*/</i>' } // comments /* */
]

Language.snippets = []
                     Language.complete = [
                                      	{ input : '\'', output : '\'$0\'' },
                                      	{ input : '"', output : '"$0"' },
                                      	{ input : '(', output : '\($0\)' },
                                      	{ input : '[', output : '\[$0\]' },
//                                      	{ input : '{', output : '{\n\t$0\n}' }		
                                      ]
Language.shortcuts = []
