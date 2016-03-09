import java.util.ArrayList;

public class Event implements Comparable<Event> {

	private String title; // can be empty
	private String date; // can be empty
	private ArrayList<String> keywords;
	
	public Event(String title, String date, ArrayList<String> keywords) {
		this.title = title;
		this.date = date;
		this.keywords = keywords;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDate() {
		return date;
	}
	
	public ArrayList<String> getKeywords() {
		return keywords;
	}

	@Override
	public int compareTo(Event o) {
		String date1 = "9999-99-99";
		String date2 = "9999-99-99";
		if (date != "") {
			date1 = date;
		}
		if (o.date != "") {
			date2 = o.date;
		}
		return date1.compareTo(date2);
	}
	
	public String toString() {
		String keyword = "";
		for (String key: keywords) {
			keyword += key + " ";
		}
		return "Event: " + title + "; Date: " + date + "; Keywords: " + keyword;
	}
}
