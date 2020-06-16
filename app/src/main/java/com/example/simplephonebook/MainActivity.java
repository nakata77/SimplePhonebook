package com.example.simplephonebook;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected ListView simpleList;
    protected Button btnAdd;

    protected void initDb() throws SQLException {
        SQLiteDatabase db = null;

        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/" + "contacts.db",
                null
        );

        String query = "CREATE TABLE IF NOT EXISTS Contacts(";
        query += " Id integer PRIMARY KEY AUTOINCREMENT, ";
        query += " Name text NOT NULL, ";
        query += " Phone text NOT NULL, ";
        query += " Information text NOT NULL, ";
        query += " Category text NOT NULL,";
        query += " UNIQUE(Name, Phone) );";

        db.execSQL(query);
        db.close();
    }

    public void selectDb() throws SQLException {
        SQLiteDatabase db = null;

        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/" + "contacts.db",
                null
        );

        ArrayList<String> listResult = new ArrayList<String>();
        String query = "SELECT * FROM Contacts ORDER BY Name;";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String information = cursor.getString(cursor.getColumnIndex("Information"));
            String phone = cursor.getString(cursor.getColumnIndex("Phone"));
            String category = cursor.getString(cursor.getColumnIndex("Category"));
            String id = cursor.getString(cursor.getColumnIndex("Id"));

            listResult.add(id + "\t" + name + "\t" + phone + "\t\n Category: " + category + "\t\n About: " + information);
        }
        simpleList.clearChoices();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_list_view,
                R.id.textView,
                listResult
        );
        simpleList.setAdapter(arrayAdapter);

        db.close();
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            selectDb();
        } catch (Exception ex) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        simpleList = findViewById(R.id.simpleList);

        try {
            initDb();
            selectDb();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(intent, 200);
            }
        });

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView clickedText = view.findViewById(R.id.textView);
                String selected = clickedText.getText().toString();

                String[] elements = selected.split("\t");
                String Id = elements[0];
                String Name = elements[1];
                String Phone = elements[2];
                String Category = elements[3].replace("\n Category: ", "");
                String Information = elements[4].replace("\n About: ", "");

                Intent intent = new Intent(MainActivity.this, ManageContact.class);
                Bundle b = new Bundle();
                b.putString("Id", Id);
                b.putString("Name", Name);
                b.putString("Information", Information);
                b.putString("Phone", Phone);
                b.putString("Category", Category);

                intent.putExtras(b);
                startActivityForResult(intent, 200, b);
            }
        });
    }
}

