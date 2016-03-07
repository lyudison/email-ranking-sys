
public class ScoredEmail implements Comparable<ScoredEmail> {
	
	public String email = "";
	public double score = 0.0; 
	
	public ScoredEmail(String email, double score) {
		this.email = email;
		this.score = score;
	}

	@Override
	public int compareTo(ScoredEmail o) {
		return score - o.score < 0? -1: 1;
	}

	@Override
	public String toString() {
		return email;
	}
}
