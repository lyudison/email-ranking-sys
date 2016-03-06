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
			"Hi Jack, I am Emily. Bill Gates will go to the library. See you then!",
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
		
		// Create a document. No computation is done yet.
        Document doc = new Document(email);
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            List<String> lemmas = sent.lemmas();
            List<String> tags = sent.nerTags(); // named entity recognition
            
            System.out.println(" ---- Lemmas (Words in origin) ---- ");
            for (String lemma: lemmas) {
            	System.out.print(lemma+" ");
            }
            System.out.println();
            
            System.out.println(" ---- Tags ---- ");
            for (String tag: tags) {
            	System.out.print(tag+" ");
            }
            System.out.println();
        }
        
        // TODO: measure score (importance and urgency) of emails
        // ... 
        
		return 0.0;
	}
}
