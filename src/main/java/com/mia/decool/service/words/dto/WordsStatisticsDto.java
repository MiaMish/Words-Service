package com.mia.decool.service.words.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class WordsStatisticsDto {

    @ApiModelProperty(value = "Top 5 recurring words", example = "[\"word1\",\"word2\",\"word3\",\"word4\",\"word5\"]")
    private List<String> top5;

    @ApiModelProperty(value = "Median word frequency", example = "5")
    private int median;

    @ApiModelProperty(value = "Minimum frequency among all words", example = "2")
    private int least;

}
