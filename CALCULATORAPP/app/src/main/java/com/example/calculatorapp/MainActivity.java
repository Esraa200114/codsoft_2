package com.example.calculatorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    TextView operations, result;
    boolean flag = true, is_output_overflown = false;
    String number = "", operator = "", first_operand = "", second_operand = "", remaining_txt = "";
    BigDecimal first_big_decimal, second_big_decimal, output_big_decimal;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.background_color));

        InitializeComponents();
    }

    public void InitializeComponents() {
        operations = findViewById(R.id.operation_tv);
        result = findViewById(R.id.result_tv);
    }

    public void onNumberClickEvent(View view) {

        if (flag)
            result.setText("");
        flag = false;

        number = result.getText().toString();

        switch (view.getId()) {
            case R.id.number_0_btn:
                number += '0';
                break;
            case R.id.number_1_btn:
                number += '1';
                break;
            case R.id.number_2_btn:
                number += '2';
                break;
            case R.id.number_3_btn:
                number += '3';
                break;
            case R.id.number_4_btn:
                number += '4';
                break;
            case R.id.number_5_btn:
                number += '5';
                break;
            case R.id.number_6_btn:
                number += '6';
                break;
            case R.id.number_7_btn:
                number += '7';
                break;
            case R.id.number_8_btn:
                number += '8';
                break;
            case R.id.number_9_btn:
                number += '9';
                break;
            case R.id.number_dot_btn: {
                if ((number.contains(".")) || (!number.isEmpty() && number.charAt(number.length() - 1) == '.')) {
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.isEmpty()) {
                    number += "0";
                }
                number += '.';
                break;
            }
        }

        if (number.length() > 10) {
            number = number.substring(0, Math.min(number.length(), 10));
            Toast.makeText(getApplicationContext(), "Input exceeds the 10-character limit", Toast.LENGTH_SHORT).show();
        }

        result.setText(number);
    }

    public void onOperatorClickEvent(View view) {

        if (!operator.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid Operator", Toast.LENGTH_SHORT).show();
            return;
        }

        flag = true;
        first_operand = result.getText().toString();

        if (first_operand.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Missing Operands", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (view.getId()) {
            case R.id.operator_add_btn:
                operator = "+";
                break;
            case R.id.operator_subtract_btn:
                operator = "-";
                break;
            case R.id.operator_multiply_btn:
                operator = "x";
                break;
            case R.id.operator_divide_btn:
                operator = "/";
                break;
        }

        operations.setText(first_operand + operator);
    }

    public void onEqualClickEvent(View view) {

        second_operand = result.getText().toString();

        if (second_operand.isEmpty() || first_operand.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Missing Operands", Toast.LENGTH_SHORT).show();
            return;
        }

        first_big_decimal = new BigDecimal(first_operand);
        second_big_decimal = new BigDecimal(second_operand);

        if (operator.equals("/") && second_big_decimal.compareTo(BigDecimal.ZERO) == 0) {
            operations.setTextColor(Color.parseColor("#e74c3c"));
            result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            result.setTextColor(Color.parseColor("#e74c3c"));
            result.setText("Can't divide by 0");
            return;
        }

        switch (operator) {
            case "+":
                output_big_decimal = first_big_decimal.add(second_big_decimal);
                break;
            case "-":
                output_big_decimal = first_big_decimal.subtract(second_big_decimal);
                break;
            case "x":
                output_big_decimal = first_big_decimal.multiply(second_big_decimal);
                break;
            case "/":
                output_big_decimal = first_big_decimal.divide(second_big_decimal, 5, RoundingMode.HALF_UP);
                break;
        }

        if (output_big_decimal.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) > 0) {
            is_output_overflown = true;
            result.setTextColor(Color.parseColor("#e74c3c"));
            result.setText("Overflow occurred");
        } else {
            is_output_overflown = false;
            result.setText(FormatBigDecimal(output_big_decimal));
        }

        operations.setText(result.getText().toString());
        first_operand = "";
        second_operand = "";
        operator = "";
        number = "";
    }


    public void onAllClearClickEvent(View view) {

        operations.setTextColor(Color.parseColor("#ced6e0"));
        operations.setText("");

        result.setTextColor(Color.parseColor("#FF000000"));
        result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
        result.setText("");

        flag = true;
        number = "";
        operator = "";
        first_operand = "";
        second_operand = "";
        remaining_txt = "";
        is_output_overflown = false;

    }

    public String FormatBigDecimal(BigDecimal bigDecimal){
        if (bigDecimal.stripTrailingZeros().scale() <= 0) {
            return bigDecimal.toBigInteger().toString();
        } else {
            return bigDecimal.stripTrailingZeros().toPlainString();
        }
    }

    public void onClearClickEvent(View view) {
        remaining_txt = result.getText().toString();
        remaining_txt = remaining_txt.substring(0, remaining_txt.length() - 1);
        result.setText(remaining_txt);
    }
}