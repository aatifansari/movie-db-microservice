package com.skywalker.moviecatalogservices.resources;

import com.skywalker.moviecatalogservices.models.CatalogItem;
import com.skywalker.moviecatalogservices.models.Movie;
import com.skywalker.moviecatalogservices.models.Rating;
import com.skywalker.moviecatalogservices.models.UserRating;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.LineNumberInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String MOVIE_CATALOG_SERVICE = "movie-catalog-service";

    @GetMapping("/{userId}")
    @CircuitBreaker(name = MOVIE_CATALOG_SERVICE, fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/rating/users/" + userId, UserRating.class);

        return userRating.getRatings().stream()
                .map(rating ->{
                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

                    return new CatalogItem(movie.getName(), "Description Test", rating.getRating());

                    })
                .collect(Collectors.toList());

    }

    // fallback method must have one extra argument with Exception
    public List<CatalogItem> getFallbackCatalog(String userId, Exception ex){
        return Arrays.asList(new CatalogItem("No movie", "", 0));
    }
}

    /*     Alternative WebClient way
                     Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8082/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();
     */
