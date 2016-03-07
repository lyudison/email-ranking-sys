import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ParsedEmail implements Comparable<ParsedEmail> {
	
	private String text;
	private ArrayList<String> dates; 
	
	public ParsedEmail(String text, ArrayList<String> dates) {
		this.text = text;
		this.dates = dates;
	}
	
	public void sortDates() {
		Collections.sort(dates);
	}

	@Override
	public int compareTo(ParsedEmail o) {
		
		// TODO: rank by more complicated things
		// ...
		
		// rank just by date
		if (o.dates.isEmpty()) {
			return 1;
		}
		if (dates.isEmpty()) {
			return 1;
		}
		return dates.get(0).compareTo(o.dates.get(0));
	}
	
	public String getText() {
		return text;
	}
	
	public String getFirstDate() {
		if (dates.isEmpty()) {
			return "no date";
		}
		return dates.get(0);
	}
}
