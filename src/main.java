import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class main {

	public static void main(String[] args) {
		// 1. input list of email (in string format)
		String[] emails = {
			"Assignment 1 will be due on 3 March. Please ensure that you can finish.",
			"REGISTER AT THE NIGHT (9PM)! (registration closes TODAY)",
			"Hi Jack, I am Emily. Nice to meet you. Bye!",
			"Nothing but a meeeeeeeess."
		};
		
		// 2. rank the emails with respect of urgency and importance
		ScoredEmail[] rankedEmails = rank(emails);
		
		// 3. output result
		for (ScoredEmail email: rankedEmails) {
			System.out.println(email);
		}
	}
	
	private static ScoredEmail[] rank(String[] emails) {
		ScoredEmail[] res = new ScoredEmail[emails.length];
		
		// judge the urgency and importance of all the emails
		for (int i = 0; i < emails.length; i++) {
			System.out.println(" ---- Email "+i+" ----");
			System.out.println(emails[i]);
			res[i] = new ScoredEmail(emails[i], measureScore(emails[i]));
		}
		
		// sort emails according to their scores
		Arrays.sort(res);
		
		return res;
	}
	
	private static double measureScore(String email) {
		
		// get dates from email
		System.out.println(" ---- Dates ----");
		List<String> dates = parse(email, "DATE");
        for (String date: dates) {
        	System.out.print(date + " ");
        }
        System.out.println();
        
        // get persons from email
        System.out.println(" ---- Persons ----");
		List<String> persons = parse(email, "PERSON");
        for (String person: persons) {
        	System.out.print(person+ " ");
        }
        System.out.println();
        
		return 0.0;
	}
	
	private static List<String> parse(String input, String type) {
		List<String> res = new ArrayList<String>();
		// Create a document. No computation is done yet.
        Document doc = new Document(input);
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
//            List<String> lemmas = sent.lemmas();
        	List<String> words = sent.words();
            List<String> entities = sent.nerTags(); // named entity recognition
            for (int i = 0; i < entities.size(); i++) {
            	if (entities.get(i) == type) {
//            		res.add(lemmas.get(i));
            		res.add(words.get(i));
            	}
            }
        }
        return res;
	}
	
}
