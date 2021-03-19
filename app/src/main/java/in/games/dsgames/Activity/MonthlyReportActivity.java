package in.games.dsgames.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import in.games.dsgames.R;

public class MonthlyReportActivity extends AppCompatActivity {
    TextView tv_title ,tv_wallet;
    ImageView iv_back;
      private TextView tvDate;
      private  TextView tvTillDate;
      private DatePickerDialog.OnDateSetListener setListener;
      private  DatePickerDialog.OnDateSetListener setListener2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);

        tvDate =findViewById(R.id.tv_date);
        tvTillDate=findViewById(R.id.tv_till_date);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_Title);
        tv_wallet = findViewById(R.id.tv_wallet);
        tv_wallet.setVisibility(View.GONE);
        tv_title.setText("Monthly Report");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        Calendar calendar = Calendar.getInstance();
         final int year = calendar.get(Calendar.YEAR);
         final int month = calendar.get(Calendar.MONTH);
         final int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar calendar2 = Calendar.getInstance();
        final int year2 = calendar2.get(Calendar.YEAR);
        final int month2 = calendar2.get(Calendar.MONTH);
        final int day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               DatePickerDialog datePickerDialog =new DatePickerDialog(MonthlyReportActivity.this,android.R.style.Theme_Material_Light_Dialog,setListener,year,month,day);
               datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               datePickerDialog.show();
            }
        });
        setListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                tvDate.setText(date);
            }
        };


        tvTillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog =new DatePickerDialog(MonthlyReportActivity.this,android.R.style.Theme_Material_Light_Dialog,setListener2,year2,month2,day2);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener2 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year2, int month2, int dayOfMonth2) {
                month2 = month2+1;
                String date2 = dayOfMonth2+"/"+month2+"/"+year2;
                tvTillDate.setText(date2);
            }
        };

    }
}