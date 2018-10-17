package com.mission2019.dreamcricket.dreamcricket.Model.TeamStats.HeadToHead;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class HeadToHeadRunsVsBowlingStyles {
    @SerializedName("bowling_style")
    @Expose
    private String mBowlingStyle;

    @SerializedName("matches")
    @Expose
    private String mMatches;

    @SerializedName("balls")
    @Expose
    private String mBalls;

    @SerializedName("runs")
    private String mRuns;

    @SerializedName("wickets")
    private String mWickets;

    @SerializedName("strike_rate")
    private String mStrikeRate;

    public String getBowlingStyle() {
        return mBowlingStyle;
    }

    public void setBowlingStyle(String bowlingStyle) {
        mBowlingStyle = bowlingStyle;
    }

    public String getMatches() {
        return mMatches;
    }

    public void setMatches(String matches) {
        mMatches = matches;
    }

    public String getBalls() {
        return mBalls;
    }

    public void setBalls(String balls) {
        mBalls = balls;
    }

    public String getRuns() {
        return mRuns;
    }

    public void setRuns(String runs) {
        mRuns = runs;
    }

    public String getWickets() {
        return mWickets;
    }

    public void setWickets(String wickets) {
        mWickets = wickets;
    }

    public String getStrikeRate() {
        return mStrikeRate;
    }

    public void setStrikeRate(String strikeRate) {
        mStrikeRate = strikeRate;
    }
}
