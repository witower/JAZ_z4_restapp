package domain.services;

import java.util.ArrayList;
import java.util.List;

import domain.Movie;

public class MovieService{
	
	private static List<Movie> db = new ArrayList<Movie>();
	private static int currentId = 0;
	
	public List<Movie> getAll() {
		return db;
	}
	
	public Movie get(int id) {
		for (Movie m : db) {
			if (m.getId() == id)
				return m;
		}
		return null;
	}
	
	public void add(Movie movie) {
		movie.setId(++currentId);
		db.add(movie);
	}
	
	public boolean update(Movie movie) {
		Movie m = get(movie.getId());
		if (m != null) {//wstawic kontrole pustego/pominietego argumentu
			m.setTitle(movie.getTitle());
			m.setDescription(movie.getDescription());
			m.setComments(movie.getComments());
			m.setRatings(movie.getRatings());
			m.setMeanRate(movie.getMeanRate());
			return true;
		}
		return false;
	}
	
	public void delete(Movie movie) {
		db.remove(movie); // można to robić po movie.getId();
	}
}
