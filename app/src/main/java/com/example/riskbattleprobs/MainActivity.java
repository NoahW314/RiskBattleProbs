package com.example.riskbattleprobs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "RiskBattleMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onCalculateButtonClicked(View view){
        EditText attackingInput = findViewById(R.id.attacker_input);
        EditText defendingInput = findViewById(R.id.defender_input);
        int numAttacking = 0, numDefending = 0;
        try {
            numAttacking = Integer.parseInt(attackingInput.getText().toString());
            numDefending = Integer.parseInt(defendingInput.getText().toString());
        } catch(NumberFormatException e){
            Log.i(TAG, "Invalid troop numbers given");
        }
        if(numAttacking >= 2 && numDefending >= 1){
            //we can calculate in this case
            RiskProb prob = RiskProb.calculateRiskProb(numAttacking, numDefending);
            TextView results = findViewById(R.id.calculation_results);
            results.setText(prob.toString());
        }
        else {
            Toast toast = Toast.makeText(view.getContext(), "Invalid troop numbers", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}