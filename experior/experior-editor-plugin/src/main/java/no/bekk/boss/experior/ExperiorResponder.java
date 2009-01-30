package no.bekk.boss.experior;

import fitnesse.FitNesseContext;
import fitnesse.authentication.*;
import fitnesse.components.SaveRecorder;
import fitnesse.html.*;
import fitnesse.http.*;
import fitnesse.responders.SecureResponder;
import fitnesse.wiki.*;
import java.util.*;
import fitnesse.wikitext.Utils;

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
    
    
    /*
     * Arvet metode fra SecureResponder, må implementeres 
     * 
     */
    
    
    public Response makeResponse(FitNesseContext context, Request request) throws Exception
    {
        
            initializeResponder(context.root, request);

            SimpleResponse response = new SimpleResponse();
            String resource = request.getResource();
            WikiPagePath path = PathParser.parse( resource );
            PageCrawler crawler = context.root.getPageCrawler();
            
            if( !crawler.pageExists( root, path ) )
            {
                    crawler.setDeadEndStrategy( new MockingPageCrawler() );
                    page = crawler.getPage(root, path);
            }
            else
                    page = crawler.getPage(root, path);
           

            pageData = page.getData();
            content = createPageContent();
        
            

            String html = makeHtml(resource, context);
            html = new String(html.getBytes(),"UTF-8");
          
            response.setContent(html);
            response.setMaxAge(0);

            return response;
    }

    protected void initializeResponder(WikiPage root, Request request)
    {
            this.root = root;
            this.request = request;
    }

    protected String createPageContent() throws Exception
    {
            return pageData.getContent();
    }

    public String makeHtml(String resource, FitNesseContext context) throws Exception
    {
            HtmlPage html = context.htmlPageFactory.newPage();
            html.title.use("Edit " + resource);
            html.header.childTags.clear();
           
           HtmlTag breadCrumbs = HtmlUtil.makeBreadCrumbsWithPageType(resource, "Edit Page");
            
            breadCrumbs.childTags.removeLast();
            html.header.use(breadCrumbs);
            html.header.addAttribute("style", "height:50px;border-bottom: solid 0px #FF8000;");
            
            html.main.use(makeEditForm(resource));
            html.sidebar.addAttribute("style", "display:none;");
            html.mainbar.addAttribute("style", "left:0px;width:100%;");
            return html.html();
    }

    public HtmlTag makeEditForm(String resource) throws Exception
    {
            HtmlTag form = new HtmlTag("form");
            form.addAttribute("name", "f");
            form.addAttribute("action", resource);
            form.addAttribute("method", "post");
            form.add(HtmlUtil.makeInputTag("hidden", "responder", "Experior"));
            form.add(HtmlUtil.makeInputTag("hidden", SAVE_ID, String.valueOf(SaveRecorder.newIdNumber())));
            form.add(HtmlUtil.makeInputTag("hidden", TICKET_ID, String.valueOf((SaveRecorder.newTicket()))));
            if(request.hasInput("redirectToReferer") && request.hasHeader("Referer"))
            {
                    String redirectUrl = request.getHeader("Referer").toString();
                    int questionMarkIndex = redirectUrl.indexOf("?");
                    if(questionMarkIndex > 0)
                            redirectUrl = redirectUrl.substring(0, questionMarkIndex);
                    redirectUrl += "?" + request.getInput( "redirectAction" ).toString();
                    form.add(HtmlUtil.makeInputTag("hidden", "redirect", redirectUrl));
            }

            form.add(createTextarea());
            
            TagGroup group = new TagGroup();
            group.add(form);

            return group;
    }

   

    private HtmlTag createTextarea()
    {
            HtmlTag textarea = new HtmlTag("textarea");
            textarea.addAttribute("name", CONTENT_INPUT_NAME);
            textarea.addAttribute("rows", "25");
            textarea.addAttribute("cols", "70");
            textarea.addAttribute("tabindex", "1");
        
            
            textarea.add(content);
            return textarea;
    }

    /*
     * Arvet metode fra SecureResponder, må implementeres 
     * 
     */
    
    public SecureOperation getSecureOperation()
    {
            return new SecureReadOperation();
    }
}