package com.example.pro2project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChampionDataDTO {
    private String id;
    private String key;
    private String title;
    private List<String> tags;
}