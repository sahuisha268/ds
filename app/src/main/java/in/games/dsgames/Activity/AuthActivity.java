package in.games.dsgames.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.games.dsgames.Config.Module;
import in.games.dsgames.R;

public class AuthActivity extends AppCompatActivity {
    private final String TAG=AuthActivity.class.getSimpleName();
    String code="";
    Module module;
    public static String mVerificationId="";
    Button btn_send;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    TextInputEditText et_phone;
    Button btn_send_opt;
    String mobile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initViews();
    }

    private void initViews() {
        FirebaseApp.initializeApp(AuthActivity.this);
        btn_send_opt=findViewById(R.id.btn_send_opt);
        et_phone=findViewById(R.id.et_phone);
        module=new Module(AuthActivity.this);
        mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                code=phoneAuthCredential.getSmsCode();
//                Toast.makeText(getActivity(),"onVerificationCompleted-  "+code,Toast.LENGTH_LONG).show();
            }
//
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
//                Toast.makeText(getActivity(),"onVerificationFailed-  "+e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId=verificationId;
                Log.e(TAG, "onCodeSent: "+mVerificationId );

//                Toast.makeText(getActivity(),"onCodeSent- "+mVerificationId,Toast.LENGTH_LONG).show();
            }
        };
        btn_send_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhoneNumberVerification(mobile);
            }
        });
    }

    private void startPhoneNumberVerification(String mobile)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ mobile,
                60,
                TimeUnit.SECONDS,
                AuthActivity.this,
                mCallBacks);
    }
}