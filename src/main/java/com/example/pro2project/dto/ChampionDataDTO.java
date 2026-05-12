package com.example.pro2project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChampionDataDTO {
    private String id;    // Textové ID (např. "Aatrox")
    private String key;   // Číselné ID jako String (např. "266")
    private String title; // Popisek (např. "the Darkin Blade")
    private List<String> tags; // Classy (Mage, Fighter...)
}