package com.skywalker.ratingsdataservice.resources;

import com.skywalker.ratingsdataservice.model.Rating;
import com.skywalker.ratingsdataservice.model.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rating")
public class RatingResource {

    @GetMapping("/{movieId}")
    public Rating getRatingByMovieId(@PathVariable("movieId")String movieId){
        return new Rating(movieId, 5);
    }

    @GetMapping("users/{userId}")
    public UserRating getRatingByUserId(@PathVariable("userId") String userId){
        List<Rating> ratings = Arrays.asList(
                new Rating("100",4),
                new Rating("200", 3)
        );
        return new UserRating(ratings);
    }


}
