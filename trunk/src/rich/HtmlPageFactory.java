package rich;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import fitnesse.html.*;
import java.util.*;
/**
 *
 * @author Administrator
 */
public class HtmlPageFactory extends fitnesse.html.HtmlPageFactory
{
    public HtmlPageFactory( Properties properties ){
        
    }
    @Override
    public HtmlPage newPage()
    {
            HtmlPage page = super.newPage();
            page.head.add(HtmlUtil.makeJavascriptLink("Dette er v�r egen javascript-path"));
            return page;
    }
    
}