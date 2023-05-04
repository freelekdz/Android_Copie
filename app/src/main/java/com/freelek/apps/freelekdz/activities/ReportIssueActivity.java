package com.freelek.apps.freelekdz.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.adapter.lists.ReportIssueAdapter;
import com.freelek.apps.freelekdz.controllers.sessions.SessionsController;
import com.freelek.apps.freelekdz.utils.CommunApiCalls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportIssueActivity extends AppCompatActivity implements ReportIssueAdapter.ItemClickListener {


    private List<String> reportIssues;
    private String customMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        Toolbar toolbar = findViewById(R.id.toolbar);

        Bundle args = getIntent().getExtras();
        if (args != null && args.containsKey("name"))
            toolbar.setTitle("Report Issue :" + args.getString("name"));


        //fill content from the app config file
        reportIssues = Arrays.asList(getResources().getStringArray(R.array.reportIssues));


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        setupAdapterRecyclerView();

    }


    private Map<String, String> retrieveReportData(String content) {

        Map<String, String> reportData = new HashMap<String, String>();
        Bundle args = getIntent().getExtras();
        if (args.containsKey("name"))
            reportData.put("title", "Reported " + args.getString("module") + args.getString("name") + "");

        if (args.containsKey("owner_id"))
            reportData.put("owner_user_id", String.valueOf(args.getInt("owner_id")));


        if (SessionsController.isLogged())
            reportData.put("reported_by_user_id", String.valueOf(SessionsController.getSession().getUser().getId()));

        if (args.containsKey("link") && content != null)
            reportData.put("content", "Reported content \"" + args.getString("link") + "\" Problem: \"" + content + "\" ");

        return reportData;
    }


    private void setupAdapterRecyclerView() {

        // data to populate the RecyclerView with
        ArrayList<String> issues = new ArrayList<>(reportIssues);

        // set up the RecyclerView
        RecyclerView reportListRV = findViewById(R.id.report_list_rv);

        ReportIssueAdapter mAdapter = new ReportIssueAdapter(ReportIssueActivity.this, issues);
        mAdapter.setClickListener(this);

        reportListRV.setLayoutManager(new LinearLayoutManager(this));
        reportListRV.setAdapter(mAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {

        customMessage = reportIssues.get(position);

        customIssuePopup(view.getContext());


    }


    private void customIssuePopup(final Context context) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.report_other_issues);
        alertDialog.setMessage(R.string.provide_more_detal_for_issue);

        final EditText input = new EditText(ReportIssueActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_report_problem);

        alertDialog.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (customMessage != null) {
                            customMessage = customMessage + " : " + input.getText().toString();
                        } else {
                            customMessage = input.getText().toString();
                        }

                        //report issue api
                        CommunApiCalls.contentReport(ReportIssueActivity.this, retrieveReportData(customMessage));
                    }
                });

        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
            overridePendingTransition(R.anim.righttoleft_enter, R.anim.righttoleft_exit);
        }

        return super.onOptionsItemSelected(item);
    }


}