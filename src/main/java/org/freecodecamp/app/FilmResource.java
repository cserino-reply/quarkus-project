package org.freecodecamp.app;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Consumes;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.sql.Timestamp;
import org.freecodecamp.app.model.Film;
import org.freecodecamp.app.repository.FilmRepository;
import org.freecodecamp.app.dto.CreateFilmRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@Path("/")
public class FilmResource {
    
    @Inject
    FilmRepository filmRepository;
    
    @Inject
    EntityManager entityManager; 
    
    @GET
    @Path("/helloWorld")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello World!"; 
    }

    @GET
    @Path("/film/{filmId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFilm(short filmId) {
        Optional<Film> film = filmRepository.getFilm(filmId);
        return film.isPresent() ? film.get().getTitle() : "No film was found!";
    }
    
    @GET
    @Path("/pagedFilms/{page}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN) 
    public String paged(long page, short minLength) {
        return filmRepository.paged(page, minLength)
                .map(f -> String.format("%s (%d min)", f.getTitle(), f.getLength()))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/actors/{startsWith}/{minLength}")
    @Produces(MediaType.TEXT_PLAIN)
    public String actors(String startsWith, short minLength) {
        return filmRepository.actors(startsWith, minLength)
                .map(f -> String.format("%s (%d min): %s",
                        f.getTitle(),
                        f.getLength(),
                        f.getActors().stream()
                                .map(a -> String.format("%s %s", a.getFirstName(), a.getLastName()))
                                .collect(Collectors.joining(", "))))
                .collect(Collectors.joining("\n"));
    }

    @GET
    @Path("/update/{minLength}/{rentalRate}")
    @Produces(MediaType.TEXT_PLAIN)
    public String update(short minLength, Float rentalRate) {
        filmRepository.updateRentalRate(minLength, rentalRate);
        return filmRepository.getFilms(minLength)
                .map(f -> String.format("%s (%d min) - $%f", f.getTitle(), f.getLength(), f.getRentalRate()))
                .collect(Collectors.joining("\n"));
    }

    @POST
    @Path("/film")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional  // ← IMPORTANTE: necessario per scrivere nel database
    public String createFilm(@Valid CreateFilmRequest request) {
        // Map DTO to entity
        Film film = new Film();
        film.setTitle(request.getTitle());
        film.setDescription(request.getDescription());
        film.setLanguageId(request.getLanguageId());
        film.setOriginalLanguageId(request.getOriginalLanguageId());
        film.setRentalDuration(request.getRentalDuration());
        film.setRentalRate(request.getRentalRate());
        film.setLength(request.getLength());
        film.setReplacementCost(request.getReplacementCost());
        film.setRating(request.getRating());
        film.setSpecialFeatures(request.getSpecialFeatures());
        film.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        filmRepository.createFilm(film);

        return String.format("%s (%d min) - $%f", film.getTitle(), film.getLength(), film.getRentalRate());
    }
    
    // VULNERABLE ENDPOINT - SQL INJECTION FOR TESTING CODEQL
    @GET
    @Path("/searchFilm/{title}")
    @Produces(MediaType.TEXT_PLAIN)
    public String searchFilmVulnerable(String title) {
        // VULNERABLE: Direct string concatenation in SQL query
        String sqlQuery = "SELECT f FROM Film f WHERE f.title LIKE '%" + title + "%'";
        Query query = entityManager.createQuery(sqlQuery);
        List<Film> results = query.getResultList();
        
        return results.stream()
                .map(f -> String.format("%s (%d min)", f.getTitle(), f.getLength()))
                .collect(Collectors.joining("\n"));
    }

}