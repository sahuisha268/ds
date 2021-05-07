package in.games.dsgames.Config;



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import in.games.dsgames.AppController;
import in.games.dsgames.Interface.OnAllGamesListener;
import in.games.dsgames.Model.GetGamesModel;
import in.games.dsgames.Model.TableModel;
import in.games.dsgames.R;
import in.games.dsgames.networkconnectivity.ConnectivityReceiver;
import in.games.dsgames.networkconnectivity.NoInternetConnection;

import static in.games.dsgames.Config.BaseUrl.URL_GET_GAMES;


public class Module {

    Context context;
    Dialog determinant_dialog;
  ProgressBar determinant_progress;

    public Module(Context context) {
        this.context = context;
    }

    public String getUniqueId(String type)
    {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyyhhmmss");
        return (type+simpleDateFormat.format(date).toString());
    }

    public void getAllGames(final OnAllGamesListener listener){

        HashMap<String,String> params=new HashMap<>();
        postRequest(URL_GET_GAMES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    ArrayList<GetGamesModel> list=new ArrayList<>();
                    JSONArray arr=new JSONArray(response);
                    for(int i=0; i<arr.length();i++){
                        GetGamesModel model=new GetGamesModel ();
                        JSONObject obj=arr.getJSONObject(i);
                        model.setGame_id(checkNull (obj.getString("game_id")));
                        model.setGame_name(checkNull (obj.getString("game_name")));
                        model.setName(checkNull (obj.getString("name")));
                        model.setPoints(checkNull (obj.getString("points")));
                        model.setStarline_points (checkNull (obj.getString ("starline_points")));
                        model.setIs_close(checkNull (obj.getString("is_close")));
                        model.setIs_deleted(checkNull (obj.getString("is_deleted")));
                        model.setIs_disabled(checkNull (obj.getString("is_disabled")));
                        model.setIs_starline_disable(checkNull (obj.getString("is_starline_disable")));
                        model.setIs_ds_disabled (checkNull (obj.getString("is_ds_disabled")));
                        list.add(model);
                    }
                    ArrayList<GetGamesModel> mList=new ArrayList<>();
                    ArrayList<GetGamesModel> sList=new ArrayList<>();

                    for(GetGamesModel m:list){
                        if(m.getIs_ds_disabled ().equals ("1")){
                            if(m.getIs_disabled ().equals("1")){
                                mList.add(m);
                            }
                        }
                        else if(m.getIs_ds_disabled ().equals ("0")){
                            mList.add (m);
                        }



                        if(m.getIs_starline_disable ().equals("0")){
                            sList.add(m);
                        }


                    }

                    listener.onMatkaGames(mList);
                    listener.onStarlineGames(sList);

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
    public void postRequest(String url, final HashMap<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        if (!ConnectivityReceiver.isConnected ( )) {
            showToast ("No Internet Connection");


            return;
        }
        Log.e ("url", "" + url);
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Log.e ("params", "" + params);
                return params;
            }
        };

        RetryPolicy mRetryPolicy = new DefaultRetryPolicy (
                Constants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy (mRetryPolicy);
        AppController.getInstance ( ).addToRequestQueue (stringRequest, "req");

    }
    public String checkNull(String s) {
        String str = "";
        if (s == null || s.isEmpty ( ) || s.equalsIgnoreCase ("null")) {
            str = "";
        } else {
            str = s;
        }
        return str;
    }

    public String getAccTimeFormat(String ctime, String sp_time)
    {
        //12:04 AM
        //12:09 a.m.
        String finalTm="";
        String[] ctm_arr=ctime.split(" ");
        String ctmFormat=ctm_arr[1].toString();
        String fm="";
        String[] sp_time_arr=sp_time.split(" ");
        if(ctmFormat.equalsIgnoreCase("a.m.") || ctmFormat.equalsIgnoreCase("p.m."))
        {
            String spTime_fm=sp_time_arr[1].toString();
            if(spTime_fm.equalsIgnoreCase("a.m."))
            {
                fm=sp_time_arr[0].toString()+" "+"a.m.";
            }
            else if(spTime_fm.equalsIgnoreCase("p.m."))
            {
                fm=sp_time_arr[0].toString()+" "+"p.m.";
            } else if(spTime_fm.equalsIgnoreCase("AM"))
            {
                fm=sp_time_arr[0].toString()+" "+"a.m.";
            } else if(spTime_fm.equalsIgnoreCase("PM"))

            {
                fm=sp_time_arr[0].toString()+" "+"p.m.";
            }
            finalTm=fm;
        }
        else if(ctmFormat.equalsIgnoreCase("AM") || ctmFormat.equalsIgnoreCase("PM"))
        {
            String spTime_fm=sp_time_arr[1].toString();
            if(spTime_fm.equalsIgnoreCase("AM"))
            {
                fm=sp_time_arr[0].toString()+" "+"AM";
            }
            else if(spTime_fm.equalsIgnoreCase("PM"))
            {
                fm=sp_time_arr[0].toString()+" "+"PM";
            }else if(spTime_fm.equalsIgnoreCase("a.m."))
            {
                fm=sp_time_arr[0].toString()+" "+"AM";
            }else if(spTime_fm.equalsIgnoreCase("p.m."))
            {
                fm=sp_time_arr[0].toString()+" "+"PM";
            }
            finalTm=fm;
        }
        return finalTm;
    }

//    public void addData(Context context, String digit, String point, String type, List<TableModel> list, TableAdaper tableAdaper, ListView list_table, Button btnSave)
//    {
//        list.add(new TableModel(digit,point,type));
//        tableAdaper=new TableAdaper(list, context,btnSave);
//        list_table.setAdapter(tableAdaper);
//        tableAdaper.notifyDataSetChanged();
//        int we=list.size();
//        int points=Integer.parseInt(point);
//        int tot_pnt=Integer.parseInt(getSumOfPoints(list));
//
//        btnSave.setText("(BIDS="+we+")(Points="+tot_pnt+")");
//
//    }
    public String getSumOfPoints(List<TableModel> list)
    {
        int sum=0;
        for(int i=0; i<list.size();i++)
        {
            sum=sum+Integer.parseInt(list.get(i).getPoints());
        }

        return String.valueOf(sum);
    }

    public long getTimeDiffInLong(String qDate, String stime)
    {
        long difference=0;
        DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date curr = new Date();
        String current_date = outputformat.format(curr);
        SimpleDateFormat dtm_Format=new SimpleDateFormat("hh:mm a");

        String tm=dtm_Format.format(curr);
        SimpleDateFormat format24 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        try {
           Date s_time = parseFormat.parse(getAccTimeFormat(tm, stime));
           String start_time = format24.format(s_time);
           String quiz_start = qDate + " " + start_time;
            DateFormat parseformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date c_time = parseformat.parse(current_date);
            s_time = parseformat.parse(quiz_start);
            difference = s_time.getTime() - c_time.getTime();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return difference;
        }

//    public void updateDbWallet(String user_id,String amt)
//    {
//        DatabaseReference user_ref= FirebaseDatabase.getInstance().getReference().child(USER_REF);
//        HashMap<String,Object> params=new HashMap<>();
//        params.put("wallet",amt);
//        user_ref.child(user_id).updateChildren(params).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful())
//                {
//                    Log.e("taskk","Succesffull..");
//                }
//                else {
//                    Toast.makeText(context, ""+task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//    }
public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
    int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
    return noOfColumns;
}



    public void showToast(String s)
    {
        Toast.makeText(context,""+s, Toast.LENGTH_SHORT).show();
    }

    public boolean existCounter(ArrayList<String> list, int count)
    {
        boolean flag=false;
        for(int i=0; i<list.size();i++)
        {
            if(i==count)
            {
                flag=true;
                break;
            }
        }
        return flag;
    }
    public static void startTimer(String time, final TextView txt_timer, final String type) {

        Date date = new Date();
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
        String cur_time = parseFormat.format(date);
        try {
            Date s_time = parseFormat.parse(cur_time.trim());
            Date e_time = parseFormat.parse(time.trim());
            long diff_e_s = e_time.getTime() - s_time.getTime();
            Log.e("diff", "" + diff_e_s);
//
            new CountDownTimer (diff_e_s, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String text = String.format(Locale.getDefault(), " %02d : %02d : %02d ",

                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                    if (type.equals("open")) {
                        txt_timer.setText("Bid Opens in :" + text);
                    } else if (type.equals("close")) {
                        txt_timer.setText("Bid closes in :" + text);
                    }


                }

                @Override
                public void onFinish() {


                }
            }.start();


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void startTimerCounter(CountDownTimer countDownTimer, String time, final TextView txt_timer, final String type, final String c_type) {

        Date date = new Date();
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
        String cur_time = parseFormat.format(date);
        try {
            final Date s_time = parseFormat.parse(cur_time.trim());
            Date e_time = parseFormat.parse(time.trim());
            long diff_e_s = e_time.getTime() - s_time.getTime();
            Log.e("diff", "" + diff_e_s);
            countDownTimer = new CountDownTimer(diff_e_s, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String text = String.format(Locale.getDefault(), " %02d : %02d : %02d ",

                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);

//                        if (type.equals("open")) {
//                            txt_timer.setText("Bid Opens in :" + text);
//                        }
//                        if (type.equals("close")) {
                    txt_timer.setText(text);
//                        }


                }

                @Override
                public void onFinish() {


                }
            }.start();


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public boolean validateEditText(TextInputEditText et, TextInputLayout tv_layout,String error) {
        if (et.getText().toString().trim().isEmpty()) {
//            lable.setTextColor(context.getResources().getColor(R.color.red));
            tv_layout.setError(""+error);
            requestFocus(et);
            return false;
        } else {
            tv_layout.setErrorEnabled(false);
        }

        return true;
    }
    public void validateRegEditText(TextInputEditText et, TextInputLayout tv_layout,String error) {
            tv_layout.setError(""+error);
            requestFocus(et);

    }
    private void requestFocus(EditText et) {

        et.requestFocus();
    }

    public String getRandomKey(int i)
    {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }
    public void chngeLabelColor(TextView label, Boolean b)
    {
        if (b)
        {
            label.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            label.setTextColor(context.getResources().getColor(R.color.browser_actions_bg_grey));
        }
    }

    public void setTextGradient(TextView textView)
    {

        TextPaint paint = textView.getPaint();
        float width = paint.measureText("Register");

        Shader textShader = new LinearGradient (0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
    }
    public String VolleyErrorMessage(VolleyError error)
    {
        String str_error ="";
        if (error instanceof TimeoutError) {
            str_error="Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error="Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof NetworkError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error="An Unknown error occur";
        }else if(error instanceof NoConnectionError){
            str_error="No Internet Connection";
        }

        return str_error;
    }


    public int getDiscount(String price, String mrp)
    {
        double mrp_d= Double.parseDouble(mrp);
        double price_d= Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df= Math.round(per);
        int d=(int)df;
        return d;
    }

    public void noInternetActivity()
    {
        Intent intent = new Intent (context, NoInternetConnection.class);
        context.startActivity(intent);
    }


    public void setRelativeTint(RelativeLayout mView, int color){
        Drawable wrappedDrawable = DrawableCompat.wrap(mView.getBackground());
        DrawableCompat.setTint(wrappedDrawable, color);
        mView.setBackgroundDrawable(wrappedDrawable);
    }


    public static int getDaysDifference(Date fromDate, Date toDate)
    {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }
    public int getDateDiff(String dt_str)
    {//02-07-2020
        int days=0;
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        SimpleDateFormat smpl=new SimpleDateFormat("yyyy-MM-dd");
        String inputString2 = dt_str;
        String c_date=smpl.format(date);

        try {
            Date date1 = myFormat.parse(c_date);
            Date date2 = myFormat.parse(inputString2);
            long diff = date1.getTime() - date2.getTime();
//            Log.e("days_count","Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            days=(int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public boolean checkNullString(String str){

        if(str == null || str.isEmpty() || str.equalsIgnoreCase("null")){
            return true;
        }else{
            return false;
        }
    }
}
