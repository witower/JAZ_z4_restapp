package domain;

import java.util.ArrayList;
import java.util.List;

public class Movie extends DbElement{
	
	private String title;
	private String description;
	private Double meanRate;
	private int nextCommentId = 0;
	
	private List<Actor> actors = new ArrayList<>();
	private List<String> actorsStrings = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();
	private List<Rate> ratings = new ArrayList<>();
	
	//SETTERS GETTERS
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Actor> getActors() {
		return actors;
	}
//	public void setActors(List<Actor> actors) {
//		this.actors = actors;
//	}
	public List<Comment> getComments() {
		return comments;
	}
	  public void setComments(List<Comment> comments) {
	        this.comments = comments;
	        repairCommentsIds();
	    }
	public List<Rate> getRatings() {
		return ratings;
	}
	public void setRatings(List<Rate> ratings) {
	    this.ratings = ratings;
	    updateMeanRate();
	}	
	public Double getMeanRate() {
		return meanRate;
	}
	public void setMeanRate(Double meanRate) {
		this.meanRate = meanRate;
		updateMeanRate();
	}
	public List<String> getActorsStrings() {
		return actorsStrings;
	}
	public void setActorsStrings(List<String> actorsStrings) {
		this.actorsStrings = actorsStrings;
	}
	
	//METHODS
	public void repairCommentsIds() {
		for (Comment c : comments) {
			c.setId(comments.indexOf(c));
		}
		nextCommentId = comments.size();
	}
	public void addComment(Comment c) {
		c.setId(nextCommentId++);
		this.comments.add(c);
	}
	public void addRate(Rate rate) {
		if (this.ratings.add(rate))
			updateMeanRate();
	}
	public void updateMeanRate() {
		if (!this.ratings.isEmpty()){
			int i=0;
			Double r = 0.0;
			Double sum=0.0;
			for (Rate rate : this.ratings) {
				r = rate.getRate();
				sum+=r;
				i++;
			}
			this.meanRate = sum/i;
		}
	}
	public void addActorsString(Actor a) {
		actorsStrings.add(a.getFullname() + " [#" + a.getId() + "]");
	}
	public void rebuildActorsStrings() {
		actorsStrings.clear();
		if (!actors.isEmpty()) {
			for (Actor a : actors) {
				addActorsString(a);
			}
		}
	}

}