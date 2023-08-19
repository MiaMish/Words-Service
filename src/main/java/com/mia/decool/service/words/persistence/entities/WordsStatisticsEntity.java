package com.mia.decool.service.words.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WordsStatisticsEntity implements Serializable {

    // Since the assignment states that we do not need to consider extensibility, this class is not critical.
    // However, differentiating between the entity and the DTO is a good practice, so I have done so.

    private List<String> top5;
    private int median;
    private int least;

}
