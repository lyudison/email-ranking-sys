import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ParsedEmail implements Comparable<ParsedEmail> {
	
	private String text; // content of email
	private ArrayList<Event> events; 
	
	public ParsedEmail(String text, ArrayList<Event> events) {
		this.text = text;
		this.events = events;
	}

	@Override
	public int compareTo(ParsedEmail o) {
		
		// use the closest date to compute score 
		String date1 = "9999-12-30";
		String date2 = "9999-12-30";
		if (!events.isEmpty()) {
			date1 = events.get(0).getDate();
		}
		if (!o.events.isEmpty()) {
			date2 = o.events.get(0).getDate();
		}

		// use difference between current date and event date 
        DateFormat defaultDf = DateFormat.getDateInstance();
		Date d1 = null;
		try {
			d1 = defaultDf.parse(date1);
		} catch (ParseException e) {
			System.out.println("maybe cannot parse date format like xxxx-xx-xx");
			e.printStackTrace();
		}
		Date d2 = null;
		try {
			d2 = defaultDf.parse(date2);
		} catch (ParseException e) {
			System.out.println("maybe cannot parse date format like xxxx-xx-xx");
			e.printStackTrace();
		}
		Date now = new Date();
		long days1 = TimeUnit.MILLISECONDS.convert(now.getTime() - d1.getTime(),TimeUnit.DAYS);
		long days2 = TimeUnit.MILLISECONDS.convert(now.getTime() - d2.getTime(),TimeUnit.DAYS);
		if (days1 < 0) {
			days1 = Long.MAX_VALUE;
		}
		if (days2 < 0) {
			days2 = Long.MAX_VALUE;
		}
		
		// priority of date is measured with 1/(x+1): the closer, the urgenter
		double closestDateScore1 = 1./(days1+1.0);
		double closestDateScore2 = 1./(days2+1.0);
		
		// priority of event is measured with importance table in main.java 
		double mostImportantEventTypeScore1 = 0.0;
		for (Event event: events) {
			mostImportantEventTypeScore1 = Math.max(mostImportantEventTypeScore1, main.importance.get(event.getType()));
		}
		double mostImportantEventTypeScore2 = 0.0;
		for (Event event: o.events) {
			mostImportantEventTypeScore2 = Math.max(mostImportantEventTypeScore2, main.importance.get(event.getType()));
		}
		
		double score1 = 0.7 * closestDateScore1 + 0.3 * mostImportantEventTypeScore1;
		double score2 = 0.7 * closestDateScore2 + 0.3 * mostImportantEventTypeScore2;
		return (score1 - score2) < 0? -1: 1;
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
