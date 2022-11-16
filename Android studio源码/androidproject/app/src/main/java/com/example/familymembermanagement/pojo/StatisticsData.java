package com.example.familymembermanagement.pojo;

import java.util.List;

/**
 * 统计图数据合并
 */
public class StatisticsData {

    private List<SexNumber> proportion;
    private BabyMonth month;
    private AgeGroup ageGroup;

    public List<SexNumber> getProportion() {
        return proportion;
    }

    public void setProportion(List<SexNumber> proportion) {
        this.proportion = proportion;
    }

    public BabyMonth getMonth() {
        return month;
    }

    public void setMonth(BabyMonth month) {
        this.month = month;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }
}
