package co.lightmasters.haunt.controller;

import co.lightmasters.haunt.model.Date;
import co.lightmasters.haunt.model.SwipeDto;
import co.lightmasters.haunt.model.SwipeResponse;
import co.lightmasters.haunt.service.DateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class HauntDateController {

    private final DateService dateService;

    @GetMapping(path = "/dateSuggestions")
    public ResponseEntity<List<Date>> fetchDateSuggestion(@RequestParam @Valid String username) {
        List<Date> dateSuggestions = dateService.getDateSuggestions(username);
        if (dateSuggestions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(dateSuggestions);
    }

    @PostMapping(path = "/swipeRight")
    public ResponseEntity<SwipeResponse> likeUser(@RequestBody @Valid SwipeDto swipeDto) {
        SwipeResponse swipeResponse = dateService.setLike(swipeDto);
        return ResponseEntity.ok(swipeResponse);
    }
}
