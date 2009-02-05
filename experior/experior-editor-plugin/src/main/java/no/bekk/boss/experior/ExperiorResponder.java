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
     * Arvet metode fra SecureResponder, må implementeres 
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
            html.title.use("Edit with Experior " + resource); //vises i tittellinjen
                       
            HtmlTag breadCrumbs = HtmlUtil.makeBreadCrumbsWithPageType(resource, "Edit Page with Experior:");
                    
            html.header.use(breadCrumbs);
                     
            html.main.use(makeEditForm(resource));
         
            return html.html();
    }

    public HtmlTag makeEditForm(String resource) throws Exception
    {
            HtmlTag form = new HtmlTag("form");
            form.addAttribute("name", "f");
            form.addAttribute("action", resource);
            form.addAttribute("method", "post");
        
            form.add(createExperior());
            
            return form;
           
    }

   

    private HtmlTag createExperior()
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