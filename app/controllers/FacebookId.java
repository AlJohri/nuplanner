import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;


public class FacebookId {
	public static void main(String args[]) {
		for (int i=1;i<52;i++)
		{
		Document doc;
		Document doc1;
		try {
			String[] name =new String[100];
			String[] name1 =new String[100];
			String[] name2 =new String[100];
			int[] id =new int[100];
			doc = Jsoup.connect("https://northwestern.collegiatelink.net/organizations?SearchType=None&SelectedCategoryId=0&CurrentPage="+ i).get();
			//System.out.println(doc);
			Element sa = doc.getElementById("results");
			//System.out.println(sa);
			Elements sai = sa.getElementsByTag("h5");
			//System.out.println(sai);
			int j=0;
			for(Element e:sai){
				//System.out.println(e);
				Element f = e.child(0);
				//name[j]=f.text();
				//System.out.println(f);
				//System.out.println(name[j]);
				//System.out.println(f.attr("href"));
				Document doc2=Jsoup.connect("https://northwestern.collegiatelink.net" + f.attr("href")).get();
				Elements fb=doc2.select("a[class*=icon-social facebook]");
				for (Element g:fb){
					if((g.attr("href").length()!=4)){
					name[j]=g.attr("href");//facebook link
					id[j]=name[j].lastIndexOf("/");
					name1[j]=name[j].substring(id[j]+1);//last string after / and groups end with a / in fb link
					if (!name1[j].isEmpty()){
						//System.out.println(name1[j]);
						name2[j]="https://graph.facebook.com/"+name1[j];//graph url
						System.out.println(name2[j]);
						InputStream is =new URL(name2[j]).openStream();
						try{
						BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
						StringBuilder sb = new StringBuilder();
						int cp;
						while((cp=rd.read())!=-1)
							sb.append((char)cp);
						String jsontext=sb.toString();
						JSONObject json;
						try {
							json = new JSONObject(jsontext);
							//System.out.println(json.toString());
							System.out.println(json.get("id"));//get id of string

						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						}
						finally
						{
							is.close();
						}

					j++;}
				}

			
			}
				
			}} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		}
			
		}
	}


