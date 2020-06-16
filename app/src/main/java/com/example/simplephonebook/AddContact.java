package com.example.simplephonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {

    protected EditText addName, addInfo, addPhone;
    protected Button addContact;
    protected Spinner spinner;

    protected void CloseActivity() {
        finishActivity(200);
        Intent i = new Intent(AddContact.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        addName = findViewById(R.id.addName);
        addInfo = findViewById(R.id.addInfo);
        addPhone = findViewById(R.id.addPhone);
        addContact = findViewById(R.id.btnInsert);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        addContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SQLiteDatabase db = null;

                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "contacts.db",
                            null
                    );

                    String name = addName.getText().toString();
                    String phone = addPhone.getText().toString();
                    String info = addInfo.getText().toString();
                    String category = spinner.getSelectedItem().toString();
                    String query = "INSERT INTO Contacts(Name, Phone, Information, Category) ";
                    query += "VALUES(?, ? ,? ,?)";

                    db.execSQL(query, new Object[] { name, phone, info, category });

                    Toast.makeText(getApplicationContext(), "Contact added!", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    if (db != null) {
                        db.close();
                        db = null;
                    }
                }

                CloseActivity();
            }
        });
    }
}
