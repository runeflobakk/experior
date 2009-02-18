package no.bekk.boss.experior;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import fitnesse.FitNesseContext;
import fitnesse.authentication.*;
import fitnesse.components.SaveRecorder;
import fitnesse.html.*;
import fitnesse.http.*;
import fitnesse.responders.SecureResponder;
import fitnesse.wiki.*;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import fitlibrary.DoFixture;

import fitnesse.wikitext.Utils;
import java.lang.ClassLoader;

public class ExperiorResponder implements SecureResponder
{
	public static final String CONTENT_INPUT_NAME = "pageContent";


	protected String content;
	protected WikiPage page;
	protected WikiPage root;
	protected PageData pageData;
	protected Request request;


	public ExperiorResponder()
	{

	}

	protected void initializeResponder(WikiPage root, Request request)
	{
		this.root = root;
		this.request = request;
	}


	/*
	 * Arvet metode fra SecureResponder, m� implementeres 
	 * 
	 */        
	public Response makeResponse(FitNesseContext context, Request request) throws Exception
	{	
		SimpleResponse response = new SimpleResponse();
		initializeResponder(context.root, request);


		String resource = request.getResource();
		WikiPagePath path = PathParser.parse( resource );

		PageCrawler crawler = context.root.getPageCrawler();

		page = crawler.getPage(root, path);
		pageData = page.getData();        
		content = pageData.getContent();

		String html = makeHtml(resource, context);

		response.setContent(html);

		return response;     	
	}



	public String makeHtml(String resource, FitNesseContext context) throws Exception
	{
		HtmlPage html = context.htmlPageFactory.newPage();
		html.title.use("Edit with Experior " + resource);//vises i tittellinjen
		html.head.add( HtmlUtil.makeJavascriptLink( "/files/javascript/codepress.js" ) );
		//html.head.add( attachStylesheet() );
		
		HtmlTag breadCrumbs = HtmlUtil.makeBreadCrumbsWithPageType(resource, "Edit Page with Experior:");

		html.header.use(breadCrumbs);
		html.main.use(makeEditForm(resource));

		return html.html();
	}
	
	private HtmlTag attachCodePressStylesheet() throws Exception
	{

		HtmlTag style = new HtmlTag("link");
		style.addAttribute("rel", "stylesheet");
		style.addAttribute("type", "text/css");
		style.addAttribute("href", "/files/javascript/codepress.css");
		return style;
	}
	
	
	public HtmlTag makeEditForm(String resource) throws Exception
	{
		HtmlTag form = new HtmlTag("form");
		form.addAttribute("name", "f");
		form.addAttribute("action", resource);
		form.addAttribute("method", "post");

		form.add(createExperior());
		form.add(createHiddenField());

		return form;

	}



	private HtmlTag createExperior()
	{
		HtmlTag textarea = new HtmlTag("textarea");
		textarea.addAttribute("id", "myCpWindow");
		textarea.addAttribute("rows", "50");
		textarea.addAttribute("cols", "130");
		//textarea.addAttribute("class", "codepress javascript linenumbers-off");
		textarea.addAttribute("tabindex", "1");
		content += getWikiCommands();
		textarea.add(content);
		return textarea;
	}
	
	private HtmlTag createHiddenField()
	{
		HtmlTag textarea = new HtmlTag("textarea");
		textarea.addAttribute("name", "skjult");
		textarea.addAttribute("id", "skjult");
		textarea.addAttribute("rows", "20");
		textarea.addAttribute("cols", "130");
		textarea.add("");
		return textarea;
	}

	public String getWikiCommands()
	{
		String innhold = content;

		String[] path = innhold.split("\n");
		innhold = path[0];

		if( innhold.length() > 0 )
			innhold = innhold.substring(2, innhold.length()-2);

		StringBuilder wikiCommands = new StringBuilder();
		Class t = null;
		try 
		{
			t = Class.forName(innhold);
		}
		catch (ClassNotFoundException e)
		{

		}
		if( t != null)
		{
			for(Method method : t.getDeclaredMethods())
			{
				wikiCommands.append(toWikiCommand(method.getName()) + "\n" );
			}
		}
		return wikiCommands.toString();   	    
	}

	private String toWikiCommand(String className)
	{
		StringBuilder builder = new StringBuilder();
		for(Character character : className.toCharArray())
		{
			if (isUpperCase(character))
			{
				builder.append(" " + toLowerCase(character));
			}
			else
			{
				builder.append(character);
			}
		}
		return builder.toString();
	}

	/*
	 * Arvet metode fra SecureResponder, m� implementeres 
	 * 
	 */

	public SecureOperation getSecureOperation()
	{
		return new SecureReadOperation();
	}
}