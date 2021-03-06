package com.mission2019.dreamcricket.dreamcricket.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mission2019.dreamcricket.dreamcricket.Common.Config;
import com.mission2019.dreamcricket.dreamcricket.Model.Schedule.ScheduleMatch;
import com.mission2019.dreamcricket.dreamcricket.Model.Schedule.ScheduleResponse;
import com.mission2019.dreamcricket.dreamcricket.Model.Schedule.ScheduleSeries;
import com.mission2019.dreamcricket.dreamcricket.R;
import com.mission2019.dreamcricket.dreamcricket.Rest.API;
import com.mission2019.dreamcricket.dreamcricket.Adapter.ScheduleRecyclerViewAdapter;
import com.mission2019.dreamcricket.dreamcricket.Common.Utility;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity implements ScheduleRecyclerViewAdapter.MatchCardItemClickListener {
    public static final String TAG = ScheduleActivity.class.getSimpleName();
    private static final String PERSIST_KEY_SCHEDULE = "PERSIST_KEY_SCHEDULE";
    private static final String PERSIST_KEY_SCHEDULE_DATE = "PERSIST_KEY_SCHEDULE_DATE";
    private String dateFormat = "dd-MM-yyyy";
    private ScheduleResponse mSchedule;
    private ArrayList<ScheduleMatch> mScheduleAdapterDataSet;
    private ScheduleRecyclerViewAdapter mScheduleRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mScheduleAdapterDataSet = new ArrayList<>();
        RecyclerView recyclerViewSchedule = findViewById(R.id.recyclerview_schedule);
        recyclerViewSchedule.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mScheduleRecyclerViewAdapter = new ScheduleRecyclerViewAdapter(mScheduleAdapterDataSet, this);
        recyclerViewSchedule.setAdapter(mScheduleRecyclerViewAdapter);
        recyclerViewSchedule.setLayoutManager(new LinearLayoutManager(this));
    }

    private void saveInstanceState() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();
        editor.putString(PERSIST_KEY_SCHEDULE, gson.toJson(mSchedule));
        editor.putString(PERSIST_KEY_SCHEDULE_DATE, Utility.getCurrentDate(dateFormat));
        editor.apply();
    }

    private void restoreInstanceState() {
        mSchedule = null;

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String savedDate = sharedPref.getString(PERSIST_KEY_SCHEDULE_DATE, "");
        String currentDate = Utility.getCurrentDate(dateFormat);
        if(savedDate.equals(currentDate)) {
            String scheduleJson = sharedPref.getString(PERSIST_KEY_SCHEDULE, "");
            Gson gson = new Gson();
            Type type = new TypeToken<ScheduleResponse>() {}.getType();
            if (scheduleJson.length() != 0) {
                mSchedule = gson.fromJson(scheduleJson, type);
            }
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreInstanceState();
        if (mSchedule != null) {
            updateScheduleOnUI(mSchedule.getSeriesList());
        } else {
            getSchedule();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSchedule != null) {
            saveInstanceState();
        }
    }

    public void getSchedule() {
        API.query().getSchedule().enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(@NonNull Call<ScheduleResponse> call,
                                   @NonNull Response<ScheduleResponse> response) {
                Log.e(TAG, call.request().toString());
                mSchedule = response.body();
                updateScheduleOnUI(mSchedule.getSeriesList());
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
    private void updateScheduleOnUI(ArrayList<ScheduleSeries> seriesArrayList) {
        if (seriesArrayList == null) {
            Log.e(TAG, "updateScheduleOnUI : seriesArrayList is null!!!!");
            return;
        }
        mScheduleAdapterDataSet.clear();
        for (ScheduleSeries series : seriesArrayList) {
            for (ScheduleMatch match: series.getMatches()) {
                match.setSeriesTitle(series.getTitle());
                mScheduleAdapterDataSet.add(match);
            }
        }
        mScheduleRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMatchCardItemClick(int pos) {
        Log.e(TAG, "onMatchCardItemClick : pos = " + pos);
        Intent intent = new Intent(ScheduleActivity.this, MatchActivity.class);
        ScheduleMatch match = (ScheduleMatch) mScheduleAdapterDataSet.get(pos);
        Gson gson = new Gson();
        intent.putExtra(MatchActivity.KEY_MATCH_DATA, gson.toJson(match));
        startActivity(intent);
    }
}
