import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.NERClassifierCombiner;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.EntityMentionsAnnotator;
import edu.stanford.nlp.pipeline.NERCombinerAnnotator;
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

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		// 1. input list of email (in string format)
		String[] emails = {
			"His talk is scheduled for this Thursday, March 10th at 12:30 PM in Annenberg 303.",
			"Tech Expo Job Fair will be held on Jan. 8.",
			"Hi Jack, I am Emily. Bill Gates will go to the library this Sunday. See you then!",
			"The deal will expire 2 hours later! (registration closes TODAY)",
			"Nothing but a meeeeeeeess.",
			"Assignment 1 will be due on 3 March. Please ensure that you can finish.",
			"TEDxNorthwesternU 2016 is April 9, and the theme is “Beyond Boundaries.", 
		};
		
		// 2. rank the emails with respect of urgency and importance
		ArrayList<ParsedEmail> rankedEmails = rank(emails);
		
		// 3. output result
		for (ParsedEmail email: rankedEmails) {
			System.out.println(email.getText());
		}
	}
	
	private static ArrayList<ParsedEmail> rank(String[] emails) throws ClassNotFoundException, IOException {
		
		// 1. Extract dates and corresponding event of all the emails
		
		// config the parser
		Properties props = new Properties();
	    AnnotationPipeline pipeline = new AnnotationPipeline();
		pipeline.addAnnotator(new TokenizerAnnotator(false));
	    pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
	    pipeline.addAnnotator(new POSTaggerAnnotator(false));
	    pipeline.addAnnotator(new TimeAnnotator("sutime", props));
	    pipeline.addAnnotator(new EntityMentionsAnnotator());
	    pipeline.addAnnotator(new NERCombinerAnnotator(false));
	    
	    // get current date
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = new Date();
	    String currentDate = dateFormat.format(date).substring(0, 10);
	    
	    // create sortable emails list for later sorting
	    ArrayList<ParsedEmail> parsedEmails = new ArrayList<ParsedEmail>();
	    
    	for (String email: emails) {
    		
    		// config for the whole email
	    	Annotation document = new Annotation(email);
		    document.set(CoreAnnotations.DocDateAnnotation.class, currentDate);
		    
		    // parsing the whole document (email)
		    pipeline.annotate(document);
		    
			// read all the sentenses in the document (email)
		    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    	// traversing the words in all the sentences in the email
		    ArrayList<Event> events = new ArrayList<Event>();
		    for(CoreMap sentence: sentences) {
		    	
		    	// find all the nouns in the sentence except PERSON and DATE
		    	ArrayList<String> nouns = new ArrayList<String>();
		    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		    		// this is the text of the token
		    		String word = token.get(TextAnnotation.class);
		    		// this is the POS tag of the token
		    		String pos = token.get(PartOfSpeechAnnotation.class);
		    		// this is the NER label of the token
		    	    String ne = token.get(NamedEntityTagAnnotation.class);
		    	    
		    	    // find noun
		    	    if (pos.contains("NN") && ne == "0") {
		    	    	nouns.add(word);
		    	    }
		    	}
		    	
		    	// find and normalize datetime in the sentence
		    	List<CoreMap> timexAnnsAll = sentence.get(TimeAnnotations.TimexAnnotations.class);
			    ArrayList<String> dates = new ArrayList<String>();
			    for (CoreMap cm : timexAnnsAll) {
			    	String normalizedDate = cm.get(TimeExpression.Annotation.class).getTemporal().toString();
			    	dates.add(normalizedDate);
			    }
			    
			    if (dates.isEmpty()) {
			    	continue;
			    }
			    
			    // use opencyc to determine whether the nouns found
			    String event = "";
			    if (nouns.size() == 1) {
			    	event = nouns.get(0);
			    } else if (nouns.size() >= 2) {
			    	
			    	// TODO: if detect too many nouns, how to determine which should count as event
			    	// ...
			    	
			    	for (String noun: nouns) {
			    		event += noun;
			    	}
			    }
			    
			    Collections.sort(dates);
			    events.add(new Event(event, dates.get(0)));
		    }
		    
		    // save result
		    parsedEmails.add(new ParsedEmail(email, events));
	    }
    	
		
		// 3. Sort emails 
		Collections.sort(parsedEmails);
		
		return parsedEmails;
	}
}
