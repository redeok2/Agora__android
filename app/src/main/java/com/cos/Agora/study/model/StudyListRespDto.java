package com.cos.Agora.study.model;

import java.util.Date;

import lombok.Data;

@Data
public class StudyListRespDto {
    // server로부터 받을 데이터

    private long id;            // id
    private String title;       // 스터디제목
    private String interest;    // 관심분야
    private Date createDate;    // 생성날짜
    private int limit;          // 수용인원
    private double latitude;    // 사용자 위도
    private double longitude;    // 사용자 경도
    private double distance;
    private int current;        // 현재인원
}
