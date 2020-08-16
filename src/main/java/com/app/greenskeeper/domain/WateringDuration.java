package com.app.greenskeeper.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum WateringDuration {

    DAILY,
    WEEKLY,
    MONTHLY;

}
