package at.tugraz.ist.agileRecommender.lucene.app;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class ParseApp {

	  public static void main(String[] args) {
	    ParserGetter kit = new ParserGetter();
	    HTMLEditorKit.Parser parser = kit.getParser();
	    HTMLEditorKit.ParserCallback callback = new ReportAttributes();

	    try {
	      URL u = new URL("https://play.google.com/store/apps?hl=en");
	      InputStream in = u.openStream();
	      InputStreamReader r = new InputStreamReader(in);
	      parser.parse(r, callback, false);
	    } catch (IOException e) {
	      System.err.println(e);
	    }
	  }
	  
	  public static void getAppList(String query) {
		    ParserGetter kit = new ParserGetter();
		    HTMLEditorKit.Parser parser = kit.getParser();
		    HTMLEditorKit.ParserCallback callback = new ReportAttributes();

		    try {
		    	String url = "https://play.google.com/store/search?q=REPLACE&c=apps&hl=en";
		    	url = url.replaceAll("REPLACE", query);
			    URL u = new URL(url);
			    InputStream in = u.openStream();
			    InputStreamReader r = new InputStreamReader(in);
			    parser.parse(r, callback, false);
		    } catch (IOException e) {
		      System.err.println(e);
		    }
	  }
	  
	}


	class ReportAttributes extends HTMLEditorKit.ParserCallback {

	  App appToBeAdded = null;
		
	  public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
	     this.listAttributes(attributes);
	  }

	  private void listAttributes(AttributeSet attributes) {
		
	    Enumeration e = attributes.getAttributeNames();
	    int size = 0;
	    
	    while (e.hasMoreElements()) {
	      size = AppList.appList.size();
	      Object name = e.nextElement();
	      Object value = attributes.getAttribute(name);
	      if (!attributes.containsAttribute(name.toString(), value)) {
	       // System.out.println("containsAttribute() fails");
	      }
	      if (!attributes.isDefined(name.toString())) {
	       // System.out.println("isDefined() fails");
	      }
	      //System.out.println(name.toString()+" = "+value.toString());
	      
		  if(name.toString().contains("alt") )
			  appToBeAdded = new App(value.toString());
		  
		  if( appToBeAdded!=null && name.toString().contains("href"))
			  appToBeAdded = appToBeAdded;
		  
		  if(appToBeAdded!=null){   
			  if(name.toString().contains("href") && appToBeAdded.getHref()==null){
				  appToBeAdded.setHref(value.toString());
			  	  AppList.addNewApp(appToBeAdded);
			  	  System.out.println("App #"+size+ " = Title:" + appToBeAdded.getTitle()+ ", Href:" + appToBeAdded.getHref());
			  	  appToBeAdded = null;
			  }
			  
//			  if(name.toString().contains("class") && value.toString().contains("description")&& appToBeAdded.getContent()==null ) {
//				  appToBeAdded.setContent(value.toString());
//				  if(appToBeAdded.getTitle()!=null && appToBeAdded.getContent()!=null && appToBeAdded.getHref()!=null){
//					  AppList.addNewApp(appToBeAdded);
//					  System.out.println("App #"+size+ " = Title:" + appToBeAdded.getTitle()+ ", Content:" + appToBeAdded.getContent());
//				  }
//				  appToBeAdded = null;
//			  }
		        
	      } 
		      
	    }
	    
	  }

	  public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
	    this.listAttributes(attributes);
	  }
	}

	class ParserGetter extends HTMLEditorKit {
	  public HTMLEditorKit.Parser getParser() {
	    return super.getParser();
	  }
	}

