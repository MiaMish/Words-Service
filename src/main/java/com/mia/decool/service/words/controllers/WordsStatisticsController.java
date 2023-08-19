package com.mia.decool.service.words.controllers;

import com.mia.decool.service.words.dto.WordsDto;
import com.mia.decool.service.words.dto.WordsStatisticsDto;
import com.mia.decool.service.words.services.WordsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WordsStatisticsController {

    private final WordsService wordsService;

    @ApiOperation(value = "Add new words", response = WordsDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted new words"),
    })
    @PostMapping(path = "/words")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void acceptWords(
            @ApiParam("New words to add")
            @Validated
            @RequestBody WordsDto wordsDto
    ) {
        wordsService.append(wordsDto);
    }

    @ApiOperation(value = "Get words statistics", response = WordsDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Word statistics successfully retrieved")
    })
    @GetMapping("/wordsStatistics")
    public WordsStatisticsDto readWordStatistics() {
        return wordsService.getStatistics();
    }

}
