package Code.Entites;

import java.util.Date;

/**
 * ндуб
 * 
 * @author Administrator
 * 
 */
public class Article {
	public Article() {
	}
	
	public Article(int id, String name, String content, Date date, String WID) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
		this.date = date;
		this.WID = WID;
	}

	private int id;
	private String name;
	private String content;
	private Date date;
	private String WID;
	private String Area;
	private String Arcode;



	public String getArcode() {
		return Arcode;
	}

	public void setArcode(String arcode) {
		Arcode = arcode;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public String getWID() {
		return WID;
	}

	public void setWID(String wID) {
		WID = wID;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContent() {
		return content;
	}
	
	

	public Date getDate() {
		return date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	

}
