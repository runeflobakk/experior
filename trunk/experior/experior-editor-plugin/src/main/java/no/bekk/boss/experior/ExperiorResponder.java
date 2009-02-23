package no.bekk.boss.experior;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fitlibrary.DoFixture;
import fitnesse.FitNesseContext;
import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureReadOperation;
import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.SecureResponder;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

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
		textarea.addAttribute("id", "codepress");
		textarea.addAttribute("rows", "50");
		textarea.addAttribute("cols", "130");
		textarea.addAttribute("class", "codepress javascript linenumbers-off");
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
	    String fixtureClassName = new Scanner(content).findInLine("(?<=\\!\\|\\-?)[\\w|\\.]+(?=\\-?\\|)");
		if( fixtureClassName == null )
			return "";


		StringBuilder wikiCommands = new StringBuilder();
		List<Class<?>> types = new ArrayList<Class<?>>();

		try
		{
			for (Class<?> oneType = Class.forName(fixtureClassName); DoFixture.class.isAssignableFrom(oneType.getSuperclass()); oneType = oneType.getSuperclass()) {
				types.add(oneType);
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		if( types != null)
		{
			for(Class<?> type : types)
			{
				for(Method method : type.getDeclaredMethods())
				{
					wikiCommands.append(toWikiCommand(method.getName()) + "\n" );
				}
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