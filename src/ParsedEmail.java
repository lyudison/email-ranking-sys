import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ParsedEmail implements Comparable<ParsedEmail> {

	private String text = ""; // content of email
	private ArrayList<Event> events = null; 

	public ParsedEmail(String text, ArrayList<Event> events) {
		this.text = text;
		this.events = events;
	}

	@Override
	public int compareTo(ParsedEmail o) {
		// use the closest date to compute score 
		String date1 = "2020-12-30";
		String date2 = "2020-12-30";
		if (!events.isEmpty() && events.get(0).getDate()!="") {
			date1 = events.get(0).getDate();
		}
		if (!o.events.isEmpty() && o.events.get(0).getDate()!="") {
			date2 = o.events.get(0).getDate();
		}

		// use difference between current date and event date
		Date d1 = null;
		try {
			d1 = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date d2 = null;
		try {
			d2 = new SimpleDateFormat("yyyy-MM-dd").parse(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date now = new Date();

		// priority of date is measured with 1/(x+1): the closer, the urgenter
		double total = Math.max(d1.getTime(), d2.getTime());
		double closestDateScore1 = d1.getTime()/total;
		double closestDateScore2 = d2.getTime()/total;

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
