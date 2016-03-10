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
		return date.compareTo(o.date);
	}
	
	public String toString() {
		String keyword = "";
		for (String key: keywords) {
			keyword += key + " ";
		}
		return "Event: " + title + "; Date: " + date + "; Keywords: " + keyword;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
