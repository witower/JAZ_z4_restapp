package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Actor;
import domain.Comment;
import domain.Movie;
import domain.Rate;
import domain.services.ActorService;
import domain.services.MovieService;

@Path("/movies")
public class MovieResources {
	
	private MovieService dbMovies = new MovieService();
	private ActorService dbActors = new ActorService();

// Wyświetlanie listy wszystkich filmów
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		
		GenericEntity<List<Movie>> generic = new GenericEntity<List<Movie>>(dbMovies.getAll()){}; 
		return Response.status(200).entity(generic).build();
		
		//Zwracania odpowiedzi
		//v1 return raw data //return db.getAll();
		//v2 return generic in response //GenericEntity<List<Movie>> generic = new GenericEntity<List<Movie>>(db.getAll()){};
		//v2 return generic in response //return Response.status(200).entity(generic).build();
		//v3 return original in response --not working //return Response.status(201).entity(fafarafa).build();
		//v4 można też tworzyć specjalne klasy-wrappery dla list, ale generic chyba robi to samo prościej
		//JAX-RS ma problem z serializacją kolekcji, bez wsparcia dodatkowych serializatorów...
	}
	
// Dodanie filmów
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Add(List<Movie> movies) {
		for (Movie movie : movies) {
			dbMovies.add(movie);
		}
		return Response.status(201).entity(movies.size()).build();
	}
	
// Wyświetlenie filmu o podanym id
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") int id) {
		Movie movie = dbMovies.get(id);
		if(movie==null) {
			return Response.status(404).build();
		}
		return Response.ok(movie).build();
	}
	
// Zaktualizowanie informacji o filmie
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Movie movie) {
		if(dbMovies.get(id)==null)
			return Response.status(404).build();
		movie.setId(id);
		dbMovies.update(movie);//podmiana danych, tylko wskazanych w MovieService.update();
		return Response.ok().build();
	}
	
// Usunięcie filmu
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") int id) {
		Movie result = dbMovies.get(id);
		if(result==null)
			return Response.status(404).build();
		dbMovies.delete(result);
		return Response.ok().build();
	}
	
// KOMENTARZE
// Wyświetlanie komentarzy danego filmu
	@GET
	@Path("/{id}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComments(@PathParam("id") int id) {
		Movie movie = dbMovies.get(id);
		
		if(movie==null)
			return Response.status(404).build();
		if(movie.getComments()==null)
			return Response.status(204).build(); //204=found but empty,movie.setComments(new ArrayList<Comment>());
		
		GenericEntity<List<Comment>> generic = new GenericEntity<List<Comment>>(movie.getComments()){};
		return Response.status(200).entity(generic).build();
	}
	
// Dodanie komentarza pod konkretny film
	@POST
	@Path("/{id}/comments")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComment(@PathParam("id") int id, Comment comment) {
		Movie movie = dbMovies.get(id);
		if(movie==null)
			return Response.status(404).build();		
		movie.addComment(comment);
		return Response.status(201).build();
	}
	
// Usuniecie komentarza ZMIEN NA QUERYPARAM
	@DELETE
	@Path("/{movieId}/comments/{commentId}")
	public Response delComment(@PathParam("movieId") int movieId, @PathParam("commentId") int commentId) {
		Movie movie = dbMovies.get(movieId);
		if(movie==null)
			return Response.status(404).build();
		List<Comment> comments = movie.getComments();
		if(commentId < 0 || commentId >= comments.size())
			return Response.status(404).build();
		comments.remove(commentId);
		return Response.ok().build();
	}
	
// OCENY
// Wystawienie oceny (aczkolwiek ocena filmu jest średnią wszystkich dodanych ocen pod dany	film)
	@POST
	@Path("/{id}/rate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRate(@PathParam("id") int id, Rate rate) {
		Movie movie = dbMovies.get(id);
		if(movie==null)
			return Response.status(404).build();		
		movie.addRate(rate);
		return Response.status(201).entity(movie).build();
	}
	
// AKTORZY
// Wyświetlenie aktorów danego filmu
	@GET
	@Path("/{id}/actors")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showActors(@PathParam("id") int id) {
		Movie movie = dbMovies.get(id);
		if(movie==null) {
			return Response.status(404).build();
		}
		GenericEntity<List<Actor>> generic = new GenericEntity<List<Actor>>(movie.getActors()){}; 
		return Response.status(200).entity(generic).build();
	}
// Przydzielenie aktorow danemu filmowi
	@POST
	@Path("/{id}/actors")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addActor(@PathParam("id") int movieId, List<Actor> actors) {
		Movie movie = dbMovies.get(movieId);
		if(movie==null)
			return Response.status(404).build();
		int count = 0;
		for (Actor actor : actors) {
			actor = dbActors.get(actor.getId());
			if(actor==null) continue;
			actor.getMovies().add(movie);
			movie.getActors().add(actor);
			movie.addActorsString(actor);
			count++;
		}
		return Response.status(201).entity(count).build();
	}
	
// Usuniecie aktora z filmu QUERYPARAM
	@DELETE
	@Path("/{movieId}/actors")
	public Response delActor(@PathParam("movieId") int movieId, @QueryParam("id") int actorId) {
		Movie movie = dbMovies.get(movieId);
		Actor actor = dbActors.get(actorId);
		if(movie==null || actor==null)
			return Response.status(404).build();
		if(movie.getActors().remove(actor))
			movie.rebuildActorsStrings();
		else
			return Response.status(404).build();
		return Response.ok().build();
	}

}
