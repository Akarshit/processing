/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
Part of the Processing project - http://processing.org
Copyright (c) 2012-15 The Processing Foundation

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2
as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software Foundation, Inc.
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*/

package processing.mode.java.pdex;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JavadocHelper {

  public static void loadJavaDocp5(TreeMap<String, String> jdocMap, File p5Ref){
    Document doc;

    //Pattern pat = Pattern.compile("\\w+");
    try {
      if (p5Ref == null) {
        System.out.println("P5 Ref location null");
        p5Ref = new File(
                         "/home/quarkninja/Workspaces/processing-workspace/processing/build/linux/work/modes/java/reference");
      }
     
      FileFilter fileFilter = new FileFilter() {
        public boolean accept(File file) {
          if(!file.getName().endsWith("_.html"))
            return false;
          int k = 0;
          for (int i = 0; i < file.getName().length(); i++) {
            if(file.getName().charAt(i)== '_')
              k++;
            if(k > 1)
              return false;
          }
          return true;
        }
      };
      
        
        mapMethod(jdocMap, p5Ref, fileFilter);
      System.out.println("JDoc loaded "+jdocMap.size());
     /* File javaDocFile = new File(
                                  "/home/quarkninja/Workspaces/processing-workspace/processing/build/javadoc/core/processing/core/PApplet.html");
      //SimpleOpenNI.SimpleOpenNI
      doc = Jsoup.parse(javaDocFile, null);

      String msg = "";
      Elements elm = doc.getElementsByTag("pre");
//      Elements desc = doc.getElementsByTag("dl");
      //System.out.println(elm.toString());

      for (Iterator iterator = elm.iterator(); iterator.hasNext();) {
        Element element = (Element) iterator.next();

        //System.out.println(element.text());
//        if (element.nextElementSibling() != null)
//          System.out.println(element.nextElementSibling().text());
        //System.out.println("-------------------");
        msg = "<html><body> <strong><div style=\"width: 300px; text-justification: justify;\"></strong>"
            + element.html()
            + element.nextElementSibling()
            + "</div></html></body></html>";
        int k = 0;
        Matcher matcher = pat.matcher(element.text());
        ArrayList<String> parts = new ArrayList<String>();
        while (matcher.find()) {
//          System.out.print("Start index: " + matcher.start());
//          System.out.print(" End index: " + matcher.end() + " ");
          if (k == 0 && !matcher.group().equals("public")) {
            k = -1;
            break;
          }
          // System.out.print(matcher.group() + " ");
          parts.add(matcher.group());
          k++;
        }
        if (k <= 0 || parts.size() < 3)
          continue;
        int i = 0;
        if (parts.get(i).equals("public"))
          i++;
        if (parts.get(i).equals("static") || parts.get(i).equals("final")
            || parts.get(i).equals("class"))
          i++;
        if (parts.get(i).equals("static") || parts.get(i).equals("final"))
          i++;
//        System.out.println("Ret Type " + parts.get(i));

        i++; // return type

        //System.out.println("Name " + parts.get(i));
        jdocMap.put(parts.get(i), msg);
      }
      
//      for (String key : jdocMap.keySet()) {
//        System.out.println("Method: " + key);
//        System.out.println("Method: " + jdocMap.get(key));
//      }
 * 
 */
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
    
    public static void loadJavaDoc(TreeMap<String, String> jdocMap, File p5Ref){
        Document doc;

        //Pattern pat = Pattern.compile("\\w+");
        try {
          FileFilter fileFilter = new FileFilter() {
              public boolean accept(File file) {
                if(file.getName().contains("-")){
                  return false;
                }
                return true;
              }
            };
          mapMethod(jdocMap,p5Ref , fileFilter);// Thos method fills the jdocMap recursively
          System.out.println("JDoc loaded "+jdocMap.size());
        }
        catch(Exception e){
        	e.printStackTrace();
        }
  }

	private static void mapMethod(TreeMap<String, String> jdocMap, File p5Ref, FileFilter fileFilter)
			throws IOException, MalformedURLException {
		if(p5Ref.isDirectory()){
			for (File docFile : p5Ref.listFiles(fileFilter)) {
      		  mapMethod(jdocMap, docFile, fileFilter);
      	  }
		}
		else{
		
			Document doc;
			doc = Jsoup.parse(p5Ref, null);
			Elements elms = doc.getElementsByAttributeValue("name","method_detail");
			if(elms.size() > 0){
				elms = 	elms.first().siblingElements();
				String msg = "";
				String className = doc.getElementsByTag("title").text();
				String methodName = "";
				//System.out.println(methodName);
				for (Iterator<Element> it = elms.iterator(); it.hasNext();) {
				  Element ele = (Element) it.next();
				  int counter = 0;
				  if(ele.tagName() == "a"){
					  String s = ele.attr("name");
					  int diff = s.lastIndexOf(')') - s.indexOf('(');
					  methodName = s.substring(0,s.indexOf('(') + 1);
					  if(diff != 1){
						  counter = 1;
						  for( int i=0; i<s.length(); i++ ) {
							  if( s.charAt(i) == ',' ) {
								  counter++;
							  } 
						  }
					  }
					  methodName += counter + ")"; 
				  }
				  if(ele.tagName() == "ul"){
					  msg = "<html><body> <strong><div style=\"width: 300px; text-justification: justify;\"></strong><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"ref-item\">";
					  msg += ele.toString(); 
					  for(Element el : ele.children().first().children()){
//							  if(!el.tagName().equals("h4")){
//								  if(el.tagName() != "pre"){
//									  msg += el.toString();
//								  }
//								  else{
//									  msg += el.toString();//TODO should the class be added also ?
//								  }
//							  }
					  }
						msg += "</table></div></html></body></html>";          
					  jdocMap.put(className + methodName, msg);// Currently the key = "Classname.methodName(<no. of parameters>)" , value = "<documentation>"
				  }
				  //mat.replaceAll("");
		//		  msg = msg.replaceAll("img src=\"", "img src=\""
		//		      + p5Ref.toURI().toURL().toString() + "/");
				  //System.out.println(ele.text());
				}
			}
		}
	}
  
}
