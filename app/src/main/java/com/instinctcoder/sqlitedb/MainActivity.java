package com.instinctcoder.sqlitedb;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity implements android.view.View.OnClickListener{

    Button btnAdd,btnGetAll;
    TextView student_Id;
    ListView lv;
    SimpleAdapter adapter = null;

    @Override
    public void onClick(View view) {
        if (view== findViewById(R.id.btnAdd)){

            Intent intent = new Intent(this,StudentDetail.class);
            intent.putExtra("student_Id",0);
            startActivity(intent);

        }else {

            StudentRepo repo = new StudentRepo(this);

            ArrayList<HashMap<String, String>> studentList =  repo.getStudentList();
            if(studentList.size()!=0) {
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        student_Id = (TextView) view.findViewById(R.id.student_Id);
                        String studentId = student_Id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(),StudentDetail.class);
                        objIndent.putExtra("student_Id", Integer.parseInt( studentId));
                        startActivity(objIndent);
                    }
                });
                adapter = new SimpleAdapter( MainActivity.this,studentList, R.layout.view_student_entry, new String[] { "id","name"}, new int[] {R.id.student_Id, R.id.student_name});
                lv.setAdapter(adapter);
            }else{
                Toast.makeText(this,"No student!",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnGetAll = (Button) findViewById(R.id.btnGetAll);
        btnGetAll.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.list);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
                }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    adapter.getFilter().filter("");
                    lv.clearTextFilter();
                }else {
                    adapter.getFilter().filter(newText.toString());
                }
                return true;
            }

        });



        return true;
    }

}