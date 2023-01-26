package com.vinodpatildev.googlesignin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    CallbackManager facebookCallbackManager;

    Button btnGoogle, btnTwitter, btnFacebook;
    ActivityResultLauncher<Intent> googleSignInActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        navigateToSecondActivity();
                    }else{
                        Toast.makeText(getApplicationContext(),"something went wrong.",Toast.LENGTH_LONG).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        AppEventsLogger.activateApp(getApplication());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);

        facebookCallbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile");

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if(acc != null){
            navigateToSecondActivity();
        }

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, permissionNeeds);
            }
        });

        LoginManager.getInstance().registerCallback(facebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {
//                        GraphRequest request = GraphRequest.newMeRequest(
//                                loginResults.getAccessToken(),
//                                new GraphRequest.GraphJSONObjectCallback() {
//                                    @Override
//                                    public void onCompleted(
//                                            JSONObject object,
//                                            GraphResponse response) {
//                                        // Application code
//                                        Log.v("ltltltltltltltltltl", response.toString());
//                                    }
//                                });
//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", "id,name,email,gender, birthday");
//                        request.setParameters(parameters);
//                        request.executeAsync();
                        Log.e("ltltltltltltltltltl","facebook success");
                    }
                    @Override
                    public void onCancel() {
                        Log.e("ltltltltltltltltltl","facebook login canceled");
                    }
                    @Override
                    public void onError(FacebookException e) {
                        Log.e("ltltltltltltltltltl", "facebook login failed error");

                    }
                });

    }

    private void googleSignIn() {
        Intent googleSignInIntent = gsc.getSignInIntent();
//        startActivityForResult(googleSignInIntent,1000);
        googleSignInActivityResultLauncher.launch(googleSignInIntent);
    }

    private void navigateToSecondActivity() {
        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}













