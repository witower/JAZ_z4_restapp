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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Actor;
import domain.Movie;
import domain.services.ActorService;
import domain.services.MovieService;

@Path("/actors")
public class ActorResources {
	
	private ActorService dbActors = new ActorService();
	private MovieService dbMovies = new MovieService();

// Wyświetlenie wszystkich aktorów
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		GenericEntity<List<Actor>> generic = new GenericEntity<List<Actor>>(dbActors.getAll()){}; 
		return Response.status(200).entity(generic).build();
	}
	
// Dodanie aktorów
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Add(List<Actor> actors) {
		for (Actor actor : actors) {
			dbActors.add(actor);
		}
		return Response.status(201).entity(actors.size()).build();
	}
	
// Wyświetlenie aktora o podanym id
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") int id) {
		Actor actor = dbActors.get(id);
		if(actor==null) {
			return Response.status(404).build();
		}
		return Response.ok(actor).build();
	}
	
// Zaktualizowanie informacji o aktorze
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Actor actor) {
		if(dbActors.get(id)==null)
			return Response.status(404).build();
		actor.setId(id);
		dbActors.update(actor);
		return Response.ok().build();
	}
	
// Usunięcie aktora
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") int id) {
		Actor result = dbActors.get(id);
		if(result==null)
			return Response.status(404).build();
		dbActors.delete(result);
		return Response.ok().build();
	}
	
// Przydzielenie filmów danemu aktorowi
	@POST
	@Path("/{actorId}/movies")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMovies(@PathParam("actorId") int actorId, List<Movie> movies) {
		Actor actor = dbActors.get(actorId);
		if(actor==null)
			return Response.status(404).build();
		int count = 0;
		for (Movie movie : movies) {
			movie = dbMovies.get(movie.getId());
			if(actor==null) continue;
			movie.getActors().add(actor);
			actor.getMovies().add(movie);
			count++;
		}
		return Response.status(201).entity(count).build();
	}

// Wyświetlenie filmów danego aktora
	
	@GET
	@Path("/{id}/movies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showMovies(@PathParam("id") int id) {
		Actor actor = dbActors.get(id);
		if(actor==null) {
			return Response.status(404).build();
		}
		GenericEntity<List<Movie>> generic = new GenericEntity<List<Movie>>(actor.getMovies()){}; 
		return Response.status(200).entity(generic).build();
	}
}