import java.util.ArrayList;

public class Event implements Comparable<Event> {

	private String title; // can be empty
	private String date; // can be empty
	private String type; // can be empty
	private ArrayList<String> keywords;
	
	public Event(String title, String date, ArrayList<String> keywords) {
		this.title = title;
		this.date = date;
		this.keywords = keywords;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDate() {
		return date;
	}
	
	public ArrayList<String> getKeywords() {
		return keywords;
	}

	@Override
	public int compareTo(Event o) {
		String d1 = "9999-99-99";
		String d2 = "9999-99-99";
		if (date != "") {
			d1 = date;
		}
		if (o.date != "") {
			d2 = o.date;
		}
		return d1.compareTo(d2);
	}
	
	public String toString() {
		String keyword = "";
		for (String key: keywords) {
			keyword += key + " ";
		}
		return "Event: " + title + "; Type:" + type + "; Date: " + date + "; Keywords: " + keyword;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
