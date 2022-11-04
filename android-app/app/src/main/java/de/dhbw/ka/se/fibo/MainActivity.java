package de.dhbw.ka.se.fibo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import de.dhbw.ka.se.fibo.adapters.CashflowAdapter;
import de.dhbw.ka.se.fibo.models.CashflowType;
import de.dhbw.ka.se.fibo.models.Cashflow;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Helpers.updateSupportActionBarText(
                getApplicationContext(),
                getSupportActionBar(),
                getText(R.string.app_activity_main_title)
        );

        final ListView list = findViewById(R.id.list);
        ArrayList<Cashflow> arrayList = new ArrayList<>();

        arrayList.add(new Cashflow(CashflowType.EXPENSE, BigDecimal.valueOf(12.5), new Date(), "dm"));
        arrayList.add(new Cashflow(CashflowType.INCOME, BigDecimal.valueOf(120.5), new Date(), "Gehalt"));

        ArrayAdapter<Cashflow> listAdapter = new CashflowAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cashflow clickedItem=(Cashflow) list.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, clickedItem.getOverallValue().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}