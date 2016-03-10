import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ParsedEmail implements Comparable<ParsedEmail> {
	
	private String text; // content of email
	private ArrayList<Event> events; 
	
	public ParsedEmail(String text, ArrayList<Event> events) {
		this.text = text;
		this.events = events;
	}

	@Override
	public int compareTo(ParsedEmail o) {
		// just consider the date for urgency
		String date1 = "9999-99-99";
		String date2 = "9999-99-99";
		if (!events.isEmpty()) {
			date1 = events.get(0).getDate();
		}
		if (!o.events.isEmpty()) {
			date2 = o.events.get(0).getDate();
		}
		return date1.compareTo(date2);
	}
	
	public String getText() {
		return text;
	}
	
	public Event getClosestEvent() {
		if (events.isEmpty()) {
			return null;
		}
		return events.get(0);
	}
	
	public ArrayList<Event> getEvents() {
		return events;
	}
}
