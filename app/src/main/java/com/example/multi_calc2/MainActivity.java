package com.example.multi_calc2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private double operand1 = Double.NaN;
    private double operand2;
    private char currentOperation;
    private boolean operationPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        display = findViewById(R.id.display);

        setButtonClickListeners();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setButtonClickListeners() {
        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_decimal, R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply,
                R.id.btn_divide, R.id.btn_clear, R.id.btn_equal, R.id.btn_delete
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick((Button) v);
                }
            });
        }
    }

    private void onButtonClick(Button button) {
        String buttonText = button.getText().toString();

        if (buttonText.matches("\\d") || buttonText.equals(".")) { // Check if button is a digit or decimal
            if (operationPressed) {
                display.setText(buttonText); // Start typing operand 2
                operationPressed = false; // Reset operation pressed flag
            } else {
                display.append(buttonText); // Append to existing operand
            }
        } else if (buttonText.equals("+") || buttonText.equals("-") ||
                buttonText.equals("*") || buttonText.equals("/")) {
            if (!operationPressed && !display.getText().toString().isEmpty()) {
                operand1 = Double.parseDouble(display.getText().toString());
                currentOperation = buttonText.charAt(0);
                operationPressed = true;
                display.setText("");
            }
        } else if (buttonText.equals("C")) {
            display.setText("");
            operand1 = Double.NaN;
            operand2 = 0;
            currentOperation = ' ';
            operationPressed = false;
        } else if (buttonText.equals("=")) {
            if (!Double.isNaN(operand1) && currentOperation != ' ' && !display.getText().toString().isEmpty()) {
                operand2 = Double.parseDouble(display.getText().toString());
                String result = performOperation(operand1, operand2, currentOperation);
                display.setText(result);
                operand1 = Double.parseDouble(result);
                operationPressed = false;
            }
        } else if (!TextUtils.isEmpty(display.getText())) {
            String text = display.getText().toString();
            String newDisplay = text.substring(0, text.length() - 1);
            display.setText(newDisplay);
        }
    }

    private String performOperation(double operand1, double operand2, char operation) {
        double result;
        switch (operation) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                result = operand1 / operand2;
                break;
            default:
                result = 0.0;
        }
        // Check if the result is an integer
        if (result == (int) result) {
            return String.valueOf((int) result); // Display as integer
        } else {
            return String.valueOf(result); // Display as decimal

        }

    }
}
