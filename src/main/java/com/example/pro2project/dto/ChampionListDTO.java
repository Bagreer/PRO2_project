package com.example.pro2project.dto;

import com.example.pro2project.dto.ChampionDataDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class ChampionListDTO {
    private String version;
    private Map<String, ChampionDataDTO> data; // Mapa, kde klíč je jméno (Aatrox)
}

