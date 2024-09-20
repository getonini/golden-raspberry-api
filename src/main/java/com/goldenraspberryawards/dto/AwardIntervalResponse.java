package com.goldenraspberryawards.dto;

import java.util.List;

public class AwardIntervalResponse {
    private List<AwardInterval> min;
    private List<AwardInterval> max;

    public AwardIntervalResponse(List<AwardInterval> min, List<AwardInterval> max) {
        this.min = min;
        this.max = max;
    }

    public List<AwardInterval> getMin() {
        return min;
    }

    public void setMin(List<AwardInterval> min) {
        this.min = min;
    }

    public List<AwardInterval> getMax() {
        return max;
    }

    public void setMax(List<AwardInterval> max) {
        this.max = max;
    }
}
