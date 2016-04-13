package Code.Entites;

public class Data {

	private String version = "version:1.3.1";
	//private String urlData = "http://192.168.8.64/WuHan3/";
 private String urlData = "http://192.168.15.61/Service1.asmx";
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUrlData() {
		return urlData;
	}

	public void setUrlData(String urlData) {
		this.urlData = urlData;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
