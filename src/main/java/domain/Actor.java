package domain;

import java.util.ArrayList;
import java.util.List;

public class Actor extends DbElement{
	
	private String Fullname;
	
	private List<Movie> movies = new ArrayList<>(); //ArrayList<Movie> robi?
	
	public String getFullname() {
		return Fullname;
	}
	public void setFullname(String Fullname) {
		this.Fullname = Fullname;
	}
	public List<Movie> getMovies() {
		return movies;
	}
//	public void setMovies(List<Movie> movies) {
//		this.movies = movies;
//	}
}
