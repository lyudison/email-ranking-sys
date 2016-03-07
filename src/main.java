import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;

public class main {

	public static void main(String[] args) {
		
		// 1. input list of email (in string format)
		String[] emails = {
			"Hi Jack, I am Emily. Bill Gates will go to the library this Sunday. See you then!",
			"The deal will expire 2 hours later! (registration closes TODAY)",
			"Nothing but a meeeeeeeess.",
			"Assignment 1 will be due on 3 March. Please ensure that you can finish.",
		};
		
		// 2. rank the emails with respect of urgency and importance
		ArrayList<ParsedEmail> rankedEmails = rank(emails);
		
		// 3. output result
		for (ParsedEmail email: rankedEmails) {
			System.out.println( " ---- \n" + email.getText() + "-->" + email.getFirstDate());
		}
	}
	
	private static ArrayList<ParsedEmail> rank(String[] emails) {
		
		// 1. Extract dates of all the emails
		
		// config the parser
		Properties props = new Properties();
	    AnnotationPipeline pipeline = new AnnotationPipeline();
		pipeline.addAnnotator(new TokenizerAnnotator(false));
	    pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
	    pipeline.addAnnotator(new POSTaggerAnnotator(false));
	    pipeline.addAnnotator(new TimeAnnotator("sutime", props));
	    
	    // get current date
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = new Date();
	    String currentDate = dateFormat.format(date).substring(0, 10);
	    
	    // create sortable emails list for later sorting
	    ArrayList<ParsedEmail> parsedEmails = new ArrayList<ParsedEmail>();
	    
    	for (String email: emails) {
    		
    		// config
	    	Annotation annotation = new Annotation(email);
		    annotation.set(CoreAnnotations.DocDateAnnotation.class, currentDate);
		    pipeline.annotate(annotation);
		    
		    // parsing
		    List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
		    
		    // get result
		    ArrayList<String> dates = new ArrayList<String>();
		    for (CoreMap cm : timexAnnsAll) {
		    	List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
		    	String normalizedDate = cm.get(TimeExpression.Annotation.class).getTemporal().toString();
		    	dates.add(normalizedDate);
	      	}
		    
		    // save result
		    parsedEmails.add(new ParsedEmail(email, dates));
	    }
    	
    	// TODO: 2. Extract events
    	// ...
		
		// 3. Sort emails 
    	// sort the date in the parsed email first
    	for (ParsedEmail parsedEmail: parsedEmails) {
    		parsedEmail.sortDates();
    	}
    	// sort the email
		Collections.sort(parsedEmails);
		
		return parsedEmails;
	}
}
