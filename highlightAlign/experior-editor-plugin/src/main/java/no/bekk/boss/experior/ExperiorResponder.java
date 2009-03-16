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
import fitnesse.components.SaveRecorder;
import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.html.TagGroup;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.SecureResponder;
import fitnesse.wiki.MockingPageCrawler;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

public class ExperiorResponder implements SecureResponder
{
	public static final String CONTENT_INPUT_NAME = "pageContent";
	public static final String SAVE_ID = "saveId";
	public static final String TICKET_ID = "ticketId";

	protected String content;
	protected WikiPage page;
	protected WikiPage root;
	protected PageData pageData;
	protected Request request;

	public ExperiorResponder()
	{

	}

	public Response makeResponse(FitNesseContext context, Request request) throws Exception {
		boolean nonExistent = request.hasInput("nonExistent") ? true : false;
		return doMakeResponse(context, request, nonExistent);
	}

	public Response makeResponseForNonExistentPage(FitNesseContext context, Request request) throws Exception {
		return doMakeResponse(context, request, true);
	}

	protected Response doMakeResponse(FitNesseContext context, Request request, boolean firstTimeForNewPage)
	throws Exception {
		initializeResponder(context.root, request);

		SimpleResponse response = new SimpleResponse();
		String resource = request.getResource();
		WikiPagePath path = PathParser.parse(resource);
		PageCrawler crawler = context.root.getPageCrawler();
		if (!crawler.pageExists(root, path)) {
			crawler.setDeadEndStrategy(new MockingPageCrawler());
			page = crawler.getPage(root, path);
		} else
			page = crawler.getPage(root, path);

		pageData = page.getData();
		content = createPageContent();

		String html = doMakeHtml(resource, context);

		response.setContent(html);
		response.setMaxAge(0);

		return response;
	}

	protected void initializeResponder(WikiPage root, Request request)
	{
		this.root = root;
		this.request = request;
	}

	protected String createPageContent() throws Exception {
		return pageData.getContent();
	}

	public String makeHtml(String resource, FitNesseContext context) throws Exception {
		return doMakeHtml(resource, context);
	}


	public String doMakeHtml(String resource, FitNesseContext context) throws Exception
	{
		HtmlPage html = context.htmlPageFactory.newPage();
		html.title.use("Edit with Experior " + resource);//vises i tittellinjen
		html.head.add( HtmlUtil.makeJavascriptLink( "/files/javascript/codepress.js" ) );

		HtmlTag breadCrumbs = HtmlUtil.makeBreadCrumbsWithPageType(resource, "Edit Page with Experior:");

		html.header.use(breadCrumbs);
		html.main.use(makeEditForm(resource));

		return html.html();
	}


	public HtmlTag makeEditForm( String resource ) throws Exception
	{
		HtmlTag div = new HtmlTag("div");

		div.addAttribute("name", "placeholder");		

		div.add(createExperior( resource ) );
		div.add( createHiddenField(resource) );

		return div;
	}	

	private HtmlTag createExperior( String resource )
	{
		HtmlTag form = new HtmlTag( "form" );
		form.addAttribute( "name", "f" );
		form.addAttribute("action", resource);
		form.addAttribute("method", "post");

		form.add(HtmlUtil.makeInputTag("hidden", "responder", "saveData"));
		form.add(HtmlUtil.makeInputTag("hidden", SAVE_ID, String.valueOf(SaveRecorder.newIdNumber())));
		form.add(HtmlUtil.makeInputTag("hidden", TICKET_ID, String.valueOf((SaveRecorder.newTicket()))));
		if (request.hasInput("redirectToReferer") && request.hasHeader("Referer")) {
			String redirectUrl = request.getHeader("Referer").toString();
			int questionMarkIndex = redirectUrl.indexOf("?");
			if (questionMarkIndex > 0)
				redirectUrl = redirectUrl.substring(0, questionMarkIndex);
			redirectUrl += "?" + request.getInput("redirectAction").toString();
			form.add(HtmlUtil.makeInputTag("hidden", "redirect", redirectUrl));
		}

		form.add( createTextarea() );	

		return form;
	}

	private HtmlTag createTextarea() 
	{
		HtmlTag textarea = new HtmlTag( "textarea" );
		textarea.addAttribute("id", "codepress");
		textarea.addAttribute("rows", "50");
		textarea.addAttribute("cols", "130");		
		textarea.addAttribute("class", "codepress javascript linenumbers-off");
		textarea.addAttribute("tabindex", "1");
		textarea.add(content);
		return textarea;
	}

	private HtmlTag createHiddenField( String resource )
	{		
		HtmlTag divHidden = new HtmlTag( "div" );
		divHidden.addAttribute("class", "hidden");	
		
		HtmlTag form = new HtmlTag( "form" );
		form.addAttribute( "name", "foo" );
		form.addAttribute("action", resource);
		form.addAttribute("method", "post");

		form.add(HtmlUtil.makeInputTag("hidden", "responder", "saveData"));
		form.add(HtmlUtil.makeInputTag("hidden", SAVE_ID, String.valueOf(SaveRecorder.newIdNumber())));
		form.add(HtmlUtil.makeInputTag("hidden", TICKET_ID, String.valueOf((SaveRecorder.newTicket()))));
		if (request.hasInput("redirectToReferer") && request.hasHeader("Referer")) {
			String redirectUrl = request.getHeader("Referer").toString();
			int questionMarkIndex = redirectUrl.indexOf("?");
			if (questionMarkIndex > 0)
				redirectUrl = redirectUrl.substring(0, questionMarkIndex);
			redirectUrl += "?" + request.getInput("redirectAction").toString();
			form.add(HtmlUtil.makeInputTag("hidden", "redirect", redirectUrl));
		}	

		HtmlTag textarea = new HtmlTag("textarea");
		textarea.addAttribute("class", CONTENT_INPUT_NAME);
		textarea.addAttribute("name", CONTENT_INPUT_NAME);
		textarea.addAttribute("id", "skjult");
		textarea.addAttribute("rows", "30");
		textarea.addAttribute("cols", "70");
		textarea.addAttribute("tabindex", "1");
		textarea.add( getWikiCommands() );
		
		divHidden.add( textarea );

		form.add(divHidden);
		
		form.add( createSaveButton() );
		return form;		
	}

	private HtmlTag createSaveButton()
	{
		HtmlTag savebutton = new HtmlTag("input");
		savebutton.addAttribute("type", "submit");
		savebutton.addAttribute("value", "Save");
		savebutton.addAttribute("name", "save");
		savebutton.addAttribute("onClick", "moveText()");
		return savebutton;
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
			for (Class<?> oneType = Class.forName(fixtureClassName); DoFixture.class.isAssignableFrom(oneType.getSuperclass()); oneType = oneType.getSuperclass())
			{
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