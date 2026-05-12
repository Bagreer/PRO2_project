package com.example.pro2project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchDetailDTO {
    private InfoDTO info;

    @Getter @Setter
    public static class InfoDTO {
        private long gameDuration;
        private String gameMode;
        private List<ParticipantDTO> participants;
    }

    @Getter @Setter
    public static class ParticipantDTO {
        private String puuid;
        private String riotIdGameName;
        private String riotIdTagline;
        private int summonerLevel;
        private int kills;
        private int deaths;
        private int assists;
        private int totalDamageDealtToChampions;
        private int championId;
        private String win; // Riot vrací "true"/"false" jako string nebo boolean
    }
}