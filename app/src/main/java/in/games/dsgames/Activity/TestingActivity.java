package in.games.dsgames.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import in.games.dsgames.Adapter.PattiAdapter.TestingAdapter;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Model.PattiModel.TestingModel;
import in.games.dsgames.R;


import static in.games.dsgames.Config.InputData.single_pana_patti;

public class TestingActivity extends AppCompatActivity {
    Button btnAdd,btnSubmit;
    TestingAdapter testingAdapter;
    RecyclerView rv_select;
    ArrayList<TestingModel> list;
    Module module;
    AutoCompleteTextView tv_digit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        btnAdd=findViewById(R.id.add);
        rv_select=findViewById(R.id.rv_select);
        btnSubmit=findViewById(R.id.submit);
        tv_digit=findViewById(R.id.tv_number);

        module = new Module(TestingActivity.this);
        rv_select.setHasFixedSize(true);
        rv_select.setLayoutManager(new GridLayoutManager(TestingActivity.this, module.calculateNoOfColumns(TestingActivity.this,90)));
        testingAdapter=  new TestingAdapter(initData(), TestingActivity.this, new TestingAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, TestingModel digit, int position) {
                list.remove(position);
                testingAdapter.notifyDataSetChanged();
            }
        });



        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(TestingActivity.this,android.R.layout.simple_list_item_1,single_pana_patti);
        final AutoCompleteTextView etDgt= (AutoCompleteTextView)findViewById(R.id.tv_number);
        etDgt.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dgt=tv_digit.getText().toString();
                if (exitDigit(list,dgt))
                  {
                 Toast.makeText(TestingActivity.this,"Digit all ready exist",Toast.LENGTH_SHORT).show();
                  }
                   else {

                    if (!dgt.isEmpty()){
                        TestingModel model = new TestingModel(dgt);
                        model .setDigit(dgt);
                        list.add(model);
                    }
                    else if(!Arrays.asList(single_pana_patti).contains(dgt))
                    {
                        String message="Invalid Patti";
                        Toast.makeText(TestingActivity.this,message,Toast.LENGTH_SHORT).show();
                        //errorMessageDialog(SingleDigitActivity.this,message);
                        tv_digit.requestFocus();
                        return;

                    }
                        }


                rv_select.setAdapter(testingAdapter);
                tv_digit.setText("");
                tv_digit.requestFocus();
            }
        });


    }


    private ArrayList<TestingModel> initData() {
        list = new ArrayList<>();
         //  list.add(new SelectedDigitModel("WITHDRAW","JHGF NJHGF HGF JHGHYTF DCVJK JNBV"));
        //list.add(new NoticeBoardModel("notice", "hgvf hgft gfg ihjn xfcvg vygu vygu"));
        return list;
    }
boolean exitDigit(ArrayList<TestingModel> list1,String digit)
{
    boolean flag = false;
    if (list1.size()>0)
    {


    for (int i=0; i<list1.size();i++)
    {
        if (list1.get(i).getDigit().equals(digit))
        {
            flag=true;
        }
    }
    }
    return flag;
}

}