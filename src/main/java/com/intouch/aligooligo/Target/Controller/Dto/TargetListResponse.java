package com.intouch.aligooligo.Target.Controller.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TargetListResponse {
    private Integer id;
    private Integer userId;
    private String goal;
    private Integer successVote;
    private Integer voteTotal;
    private Integer achievementPer;

}