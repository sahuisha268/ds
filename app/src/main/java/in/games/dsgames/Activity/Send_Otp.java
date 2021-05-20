package in.games.dsgames.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import in.games.dsgames.AppController;
import in.games.dsgames.Config.Common;
import in.games.dsgames.Config.Module;
import in.games.dsgames.Interface.OnGetConfigData;
import in.games.dsgames.Model.ConfigDataModel;
import in.games.dsgames.R;
import in.games.dsgames.utils.CustomJsonRequest;
import in.games.dsgames.utils.LoadingBar;

import static in.games.dsgames.Config.BaseUrl.URL_FORGOT_OTP;


public class Send_Otp extends AppCompatActivity {
    private static final String TAG = "SentOtpActivity";
    ImageView iv_back;
    Button btn,btn_whatSumbit;
    LoadingBar loadingBar ;
    Module module ;
    Common common;
//    private FirebaseAuth mAuth;
    TextInputLayout textInputLayout_Phone;
    TextInputEditText textInputEditText_phone;
//    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String forgot_whatsapp="",whatsapp_text="",status="",whatsappMessage="";
    LinearLayout lin_mobile,lin_whatsapp,lin_whatSumbit;
    TextView tv_whatsAppText,tv_number;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mobilePattern ="(0/91)?[7-9][0-9]{9}";
    String type ="";
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__otp);
//        mAuth = FirebaseAuth.getInstance();
        common = new Common(this);
        loadingBar = new LoadingBar(this);
        module = new Module(this);
        textInputEditText_phone=findViewById(R.id.et_phone);
        iv_back = findViewById(R.id.iv_back);
        textInputLayout_Phone=findViewById(R.id.tv_phone);
        btn=findViewById(R.id.btn_send_opt);
        lin_whatsapp = findViewById(R.id.lin_whatsapp);
        lin_mobile = findViewById(R.id.lin_mobile);
        tv_whatsAppText = findViewById(R.id.tv_whatsAppText);
        btn_whatSumbit = findViewById(R.id.btn_whatSumbit);
        lin_whatsapp=findViewById(R.id.lin_whatsapp);
        type = getIntent().getStringExtra("type");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
//                sendOTP();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        common.cofigData(new OnGetConfigData() {
            @Override
            public void onGetConfigData(ConfigDataModel model) {

                forgot_whatsapp = model.getForgot_whatsapp();
                whatsapp_text = model.getForgot_text();
                status = model.getMsg_status();
                whatsappMessage = model.getForgot_msg();
//                whatsappMessage = model.

                if (status.equals("0"))
                {
                    lin_whatsapp.setVisibility(View.VISIBLE);
                    tv_whatsAppText.setText(whatsapp_text);
//                    common.showToast(forgot_whatsapp);


                }
                else if (status.equals("1")){
                    lin_mobile.setVisibility(View.VISIBLE);
                }
                else {
                    lin_mobile.setVisibility(View.GONE);
                    lin_whatsapp.setVisibility(View.GONE);
                }
            }
        });

        btn_whatSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    common.showToast(forgot_whatsapp);
                    common.whatsapp(forgot_whatsapp,whatsappMessage);

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });



//        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//
//                Log.e("phoneAuthCredentialCode",phoneAuthCredential.toString());
//
//                signInWithPhoneAuthCredential(phoneAuthCredential);
//
////              String code=phoneAuthCredential.getSmsCode();
////
////
//////              Log.e("phoneAuthCredentialCode",code);
////                module.showToast("ytrewwer"+code.toString());
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//            }
//
//            @Override
//            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
////                module.showToast(s.toString());
//
//
//                Log.d(TAG, "onCodeSent:" + s);
//
//                // Save verification ID and resending token so we can use them later
////                mVerificationId = s;
//                mResendToken = forceResendingToken;
//
//            }
//        };
    }

    private void send() {

        boolean isValid = true;
        String mobile = textInputEditText_phone.getText().toString().trim();
        if (mobile.isEmpty())
        {
            textInputLayout_Phone.setError("Enter your Mobile");
            isValid = false;
        }
        else if (!mobile.matches(mobilePattern))
        {
            textInputLayout_Phone.setError("Enter valid mobile number");
        }
        else
            {
            textInputLayout_Phone.setErrorEnabled(false);
            sendOtpforPass(mobile,module.getRandomKey(4));
           }
    }
    private void sendOtpforPass(final String mobile, final String otp) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("otp",otp);

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_FORGOT_OTP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("gen",""+response.toString());
                loadingBar.dismiss();
                try
                {
                    String res=response.getString("status");
                    if(res.equalsIgnoreCase("success"))
                    {
                        common.showToast(response.getString("message"));
                        Intent intent=new Intent(Send_Otp.this,GetOtp.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("otp",otp);
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }
                    else
                    {
                        common.showToast(response.getString("message"));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    common.showToast("Something went wrong");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest);

    }
//    public void sendOTP(){
//        String mob = textInputEditText_phone.getText().toString().trim();
//        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+mob,60L, TimeUnit.SECONDS,Send_Otp.this,callbacks);
//    }
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//
//                            FirebaseUser user = task.getResult().getUser();
//                            // Update UI
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                            }
//                        }
//                    }
//                });
//    }

}