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
		
		// TODO: rank by more complicated things
		// ...
		
		return 0;
	}
	
	public String getText() {
		return text;
	}
}
