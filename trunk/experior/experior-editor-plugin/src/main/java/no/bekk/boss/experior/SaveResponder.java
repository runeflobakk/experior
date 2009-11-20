package no.bekk.boss.experior;

import fitnesse.FitNesseContext;
import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureWriteOperation;
import fitnesse.components.RecentChanges;
import fitnesse.components.SaveRecorder;
import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.SecureResponder;
import fitnesse.responders.editing.ContentFilter;
import fitnesse.responders.editing.EditResponder;
import fitnesse.responders.editing.MergeResponder;
import fitnesse.wiki.*;

/**
 * Responder which performs saving of the test.
 */
public class SaveResponder implements SecureResponder {

	public static ContentFilter contentFilter;

	private String user;
	private long ticketId;
	private String savedContent;
	private PageData data;

	public SaveResponder() {

	}

	/**
	 * Make a standard response to a request.
	 *
	 * @param FitNesseContext context
	 * @param Request request
	 * @see fitnesse.Responder#makeResponse(fitnesse.FitNesseContext, fitnesse.http.Request)
	 */
	public Response makeResponse(FitNesseContext context, Request request) throws Exception {
		String resource = request.getResource();
		WikiPage page = getPage(resource, context);


		data = page.getData();
		user = request.getAuthorizationUsername();

		if (editsNeedMerge(request))
			return new MergeResponder(request).makeResponse(context, request);
		else {
			savedContent = (String) request.getInput(EditResponder.CONTENT_INPUT_NAME);
			if (contentFilter != null && !contentFilter.isContentAcceptable(savedContent, resource))
				return makeBannedContentResponse(context, resource);
			else
				return saveEdits(request, page);
		}
	}

	/**
	 * Make response if the content to be saved is banned.
	 *
	 * @param context
	 * @param resource
	 */
	private Response makeBannedContentResponse(FitNesseContext context, String resource) throws Exception {
		SimpleResponse response = new SimpleResponse();
		HtmlPage html = context.htmlPageFactory.newPage();
		html.title.use("Edit " + resource);
		html.header.use(HtmlUtil.makeBreadCrumbsWithPageType(resource, "Banned Content"));
		html.main.use(new HtmlTag("h3", "The content you're trying to save has been " +
		"banned from this site.  Your changes will not be saved!"));
		response.setContent(html.html());
		return response;
	}

	/**
	 * Redirects the user to the previous page if the Save&Exit-button was clicked.
	 *
	 * @param request
	 * @param page
	 */
	private Response saveEdits(Request request, WikiPage page) throws Exception {
		Response response = new SimpleResponse();
		setData();
		VersionInfo commitRecord = page.commit(data);
		response.addHeader("Previous-Version", commitRecord.getName());
		RecentChanges.updateRecentChanges(data);

		if (request.hasInput("redirect")) {
			response.redirect(request.getInput("redirect").toString());
		}
		else if( request.hasInput("saveexit") ) {
			response.redirect( request.getResource() );
		}
		else {
			response.redirect(request.getResource() + "?Experior");
		}

		return response;
	}

	/**
	 * Merge changes if the version to be saved is older than the existing. This can
	 * occur if two users edit a test at the same time.
	 *
	 * @param request
	 */
	private boolean editsNeedMerge(Request request) throws Exception {
		String saveIdString = (String) request.getInput(EditResponder.SAVE_ID);
		long saveId = Long.parseLong(saveIdString);

		String ticketIdString = (String) request.getInput(EditResponder.TICKET_ID);
		ticketId = Long.parseLong(ticketIdString);

		return SaveRecorder.changesShouldBeMerged(saveId, ticketId, data);
	}

	/**
	 * Gets the current page.
	 *
	 * @param resource
	 * @param context
	 */
	private WikiPage getPage(String resource, FitNesseContext context) throws Exception {
		WikiPagePath path = PathParser.parse(resource);
		PageCrawler pageCrawler = context.root.getPageCrawler();
		WikiPage page = pageCrawler.getPage(context.root, path);
		if (page == null)
			page = pageCrawler.addPage(context.root, PathParser.parse(resource));
		return page;
	}

	/**
	 * Saves the current changes.
	 *
	 */
	private void setData() throws Exception {
		data.setContent(savedContent);
		data.setAttribute(EditResponder.TICKET_ID, ticketId + "");
		SaveRecorder.pageSaved(data);
		if (user != null)
			data.setAttribute(WikiPage.LAST_MODIFYING_USER, user);
		else
			data.removeAttribute(WikiPage.LAST_MODIFYING_USER);
	}

	/**
	 * Implemented method from the interface SecureResponder.
	 */
	public SecureOperation getSecureOperation() {
		return new SecureWriteOperation();
	}
}