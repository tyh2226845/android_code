package Code.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import Code.Entites.Article;
import Code.Entites.PlanarWorkOut;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Xml;

public class HttpConnSoap_list {
	//private List<PlanarWorkOut> mList ;
	public  InputStream GetWebServer (String methodName,List<Article> articles,String urlData,String user) {
		String serverUrl = urlData;
		String soapAction = "http://tempuri.org/" +methodName;		
		String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
		+"<soap:Body>";
		String bps = "Article";	
		String tps="code",tps2="state",tps3="arcode",tps4="user";
		String vps,vps2,vps3;
		String ts;
		String mreakString = "";
		mreakString = "<"+methodName+" xmlns=\"http://tempuri.org/\">"+"<list>";
		for (int i = 0; i < articles.size(); i++) {
			Article article = new Article();
			article = articles.get(i);
			ts = "<"+bps+">"+"<"+tps+">"+article.getName().trim()+"</"+tps+">"
					+"<"+tps2+">"+article.getContent()+"</"+tps2+">"
					+"<"+tps3+">"+article.getArcode().trim()+"</"+tps3+">"
					+"<"+tps4+">"+user+"</"+tps4+">"+"</"+bps+">";
			mreakString+=ts;
		}
		mreakString +="</"+"list>"+"</"+methodName+">";
		String soap2 = "</soap:Body>"+"</soap:Envelope>";
		String requestData = soap+mreakString+soap2;
		System.out.println("requestData:"+requestData+"");
		
		try {
			URL url = new URL(serverUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			byte[] bytes = requestData.getBytes("utf-8");
			con.setDoInput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(6000);
			con.setRequestMethod("POST");
			con.addRequestProperty("Content-Type", "text/xml;charset=utf-8");
			con.addRequestProperty("SOAPAction", soapAction);
			con.addRequestProperty("Content-Length", ""+bytes.length);
			
			OutputStream os = con.getOutputStream();
			os.write(bytes);
			os.flush();
			os.close();
			
			InputStream is = con.getInputStream();
			System.out.println("is:"+is);
			return is;
			//return true;
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//return false;
			return null;
		}
		
	}
	
	public  InputStream GetWebServer_out (String methodName,List<Article> articles,String urlData,String user) {
		String serverUrl = urlData;
		String soapAction = "http://tempuri.org/" +methodName;		
		String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
		+"<soap:Body>";
		String bps = "Article_out";	
		String tps="code",tps2="state",tps3="WID",tps4="user";
		String vps,vps2,vps3;
		String ts;
		String mreakString = "";
		mreakString = "<"+methodName+" xmlns=\"http://tempuri.org/\">"+"<list>";
		for (int i = 0; i < articles.size(); i++) {
			Article article = new Article();
			article = articles.get(i);
			ts = "<"+bps+">"+"<"+tps+">"+article.getName().trim()+"</"+tps+">"
					+"<"+tps2+">"+article.getContent()+"</"+tps2+">"
					+"<"+tps3+">"+article.getWID().trim()+"</"+tps3+">"
					+"<"+tps4+">"+user+"</"+tps4+">"+"</"+bps+">";
			mreakString+=ts;
		}
		mreakString +="</"+"list>"+"</"+methodName+">";
		String soap2 = "</soap:Body>"+"</soap:Envelope>";
		String requestData = soap+mreakString+soap2;
		System.out.println("requestData:"+requestData+"");
		
		try {
			URL url = new URL(serverUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			byte[] bytes = requestData.getBytes("utf-8");
			con.setDoInput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(6000);
			con.setRequestMethod("POST");
			con.addRequestProperty("Content-Type", "text/xml;charset=utf-8");
			con.addRequestProperty("SOAPAction", soapAction);
			con.addRequestProperty("Content-Length", ""+bytes.length);
			
			OutputStream os = con.getOutputStream();
			os.write(bytes);
			os.flush();
			os.close();
			
			InputStream is = con.getInputStream();
			System.out.println("is:"+is);
			return is;
			//return true;
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//return false;
			return null;
		}
	}
	
		public String inputStreamtovaluelist(InputStream in, String MonthsName) throws IOException {
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];
			String s1 ="";
			ArrayList<String> Values = new ArrayList<String>();
			Values.clear();
			
			for (int n ; (n=in.read(b))!=-1; ) {
				s1 = new String(b,0,n);
				out.append(s1);
			}
			System.out.println("Values:"+out+"333");
			
			return out+"";
		}
		
		public boolean paraseResult(InputStream inputStream){
			boolean flag = false;
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(inputStream,"utf-8");
				int eventType = parser.getEventType();
				while (eventType!=XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						System.out.println("name:"+name);
						if (name.equalsIgnoreCase("insertOutCodeListResult")||name.equalsIgnoreCase("insertCodeListResult")) {
							eventType = parser.next();
							String result = parser.getText().trim();
							System.out.println("result:"+result);
							flag  = result.equals("true")?true:false;							
							break;
						}
					case XmlPullParser.END_TAG:
						break;
					}
					eventType = parser.next();
				}
				inputStream.close();
			} catch (Exception e) {
				// TODO: handle exception
				return flag;
			}
			return flag;
			
		}
		
		public  List<PlanarWorkOut> paraseC (InputStream inputStream) {
			List<PlanarWorkOut> list = null;
			PlanarWorkOut bean = null ;
			XmlPullParser parser = Xml.newPullParser();
			System.out.println("inputStream:"+inputStream);
			try {
				parser.setInput(inputStream, "utf-8");
				System.out.println("inputStream2:"+inputStream);
				System.out.println("parser:"+parser.getText()+"");
				int eventType = parser.getEventType();
				while (eventType!=XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						list = new ArrayList<PlanarWorkOut>();
						break;
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if (name.equalsIgnoreCase("PlanarWorkOut")) {
							bean = new PlanarWorkOut();
						} else if (name.equalsIgnoreCase("WorkID")) {
							eventType = parser.next();
							bean.setWorkId(parser.getText()+"");
							System.out.println("parser:"+parser.getText()+"");
						} else if (name.equalsIgnoreCase("Client")) {
							eventType = parser.next();
							bean.setClient(parser.getText()+"");
							System.out.println("parser:"+parser.getText()+"");
						} else if (name.equalsIgnoreCase("tranSport")) {
							eventType = parser.next();
							bean.setTranSport((parser.getText()+""));
							System.out.println("parser:"+parser.getText()+"");
						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equalsIgnoreCase("PlanarWorkOut")) {
							list.add(bean);
							bean = null;
						}
						break;
					}
					eventType = parser.next();
				}
				inputStream.close();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("list:"+list.get(0).getWorkId()+"");
			return list;
		}
		
		public  List<Article> paraseA (InputStream inputStream) {
			List<Article> list = null;
			Article article = null ;
			XmlPullParser parser = Xml.newPullParser();
			System.out.println("inputStream:"+inputStream);
			try {
				parser.setInput(inputStream, "utf-8");
				System.out.println("inputStream2:"+inputStream);
				System.out.println("parser:"+parser.getText()+"");
				int eventType = parser.getEventType();
				while (eventType!=XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						list = new ArrayList<Article>();
						break;
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if (name.equalsIgnoreCase("Article")) {
							article = new Article();
						} else if (name.equalsIgnoreCase("code")) {
							eventType = parser.next();
							article.setName(parser.getText()+"");
							System.out.println("parser:"+parser.getText()+"");
						} else if (name.equalsIgnoreCase("arcode")) {
							eventType = parser.next();
							article.setArcode(parser.getText()+"");
							System.out.println("parser:"+parser.getText()+"");
						} else if (name.equalsIgnoreCase("user")) {
							eventType = parser.next();
							article.setUser((parser.getText()+""));
							System.out.println("parser:"+parser.getText()+"");
						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equalsIgnoreCase("Article")) {
							list.add(article);
							article = null;
						}
						break;
					}
					eventType = parser.next();
				}
				inputStream.close();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("list:"+list.get(0).getUser()+"");
			return list;
		}
		
		public synchronized ArrayList<String> inputStreamtovaluelistnum(InputStream in, String MonthsName) throws IOException {
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];
			String s1 ="";
			ArrayList<String> Values = new ArrayList<String>();
			Values.clear();
			
			for (int n ; (n=in.read(b))!=-1; ) {
				s1 = new String(b,0,n);
				out.append(s1);
			}
			
			

			

			System.out.println(out);
			String[] s13 = s1.split("><");
			String ifString = MonthsName + "Result";
			String TS = "";
			String vs = "";

			Boolean getValueBoolean = false;
			for (int i = 0; i < s13.length; i++) {
				TS = s13[i];
				System.out.println(TS);
				int j, k, l;
				j = TS.indexOf(ifString);
				k = TS.lastIndexOf(ifString);

				if (j >= 0) {
					System.out.println(j);
					if (getValueBoolean == false) {
						getValueBoolean = true;
					} else {

					}

					if ((j >= 0) && (k > j)) {
						System.out.println("FFF" + TS.lastIndexOf("/" + ifString));
						//System.out.println(TS);
						l = ifString.length() + 1;
						vs = TS.substring(j + l, k - 2);
						//System.out.println("fff"+vs);
						Values.add(vs);
						System.out.println("ÍË³ö" + vs);
						getValueBoolean = false;
						return Values;
					}

				}
				if (TS.lastIndexOf("/" + ifString) >= 0) {
					getValueBoolean = false;
					return Values;
				}
				if ((getValueBoolean) && (TS.lastIndexOf("/" + ifString) < 0) && (j < 0)) {
					k = TS.length();
					//System.out.println(TS);
					vs = TS.substring(7, k - 8);
					//System.out.println("f"+vs);
					Values.add(vs);
				}

			}

			return Values;
		}
	}

