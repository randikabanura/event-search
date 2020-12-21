package com.fidenz.eventsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AverageCounterDTO {
    private float avgForWeek;
    private float avgForDay;
    private float avgForHour;

    public void setAvgForWeek(float avgForWeek) {
        this.avgForWeek = avgForWeek;
    }

    public void setAvgForDay(float avgForDay) {
        this.avgForDay = avgForDay;
    }

    public void setAvgForHour(float avgForHour) {
        this.avgForHour = avgForHour;
    }
}
