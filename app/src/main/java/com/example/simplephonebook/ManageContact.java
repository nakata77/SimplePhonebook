package com.example.simplephonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ManageContact extends AppCompatActivity {

    protected String Id;
    protected EditText updateName, updateInfo, updatePhone;
    protected Button btnUpdate, btnDelete;
    protected Spinner spinner;

    protected void CloseActivity() {
        finishActivity(200);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contact);

        updateName  = findViewById(R.id.updateName);
        updateInfo = findViewById(R.id.updateInfo);
        updatePhone  = findViewById(R.id.updatePhone);
        btnDelete  = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            Id = b.getString("Id");
            updateName.setText(b.getString("Name"));
            updateInfo.setText(b.getString("Information"));
            updatePhone.setText(b.getString("Phone"));
            String Category= b.get("Category").toString();
            int i = 0;
            switch (Category) {
                case "Acquaintance":
                    i = 0;
                    break;
                case "Colleague":
                    i = 1;
                    break;
                case "Family":
                    i = 2;
                    break;
                case "Other":
                    i = 3;
                    break;
            }

            spinner.setSelection(i);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = null;

                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "contacts.db",
                            null
                    );

                    String name = updateName.getText().toString();
                    String phone = updatePhone.getText().toString();
                    String information = updateInfo.getText().toString();
                    String category = spinner.getSelectedItem().toString();
                    String query = "UPDATE Contacts SET Name = ?, Phone = ?, Information = ? , Category = ? ";
                    query += " WHERE Id = ?; ";

                    db.execSQL(query, new Object[] { name, phone, information, category, Id });

                    Toast.makeText(getApplicationContext(), "Updated successfully!", Toast.LENGTH_LONG).show();
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;

                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "contacts.db",
                            null
                    );

                    String query = "DELETE FROM Contacts WHERE Id = ? ";

                    db.execSQL(query, new Object[] { Id });

                    Toast.makeText(getApplicationContext(), "Deleted successfully!", Toast.LENGTH_LONG).show();
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
