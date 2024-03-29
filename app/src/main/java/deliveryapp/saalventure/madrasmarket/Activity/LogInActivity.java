package deliveryapp.saalventure.madrasmarket.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import deliveryapp.saalventure.madrasmarket.MainActivity;
import deliveryapp.saalventure.madrasmarket.R;
import deliveryapp.saalventure.madrasmarket.util.Session_management;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static deliveryapp.saalventure.madrasmarket.Config.BaseURL.dLogin;

public class LogInActivity extends AppCompatActivity {
    EditText Et_login_email, et_login_pass;
    RelativeLayout Btn_Sign_in;
    TextView tv_login_email;
    String getemail, getPass, deviceID;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        token = FirebaseInstanceId.getInstance().getToken();

        preferences = getSharedPreferences("person", MODE_PRIVATE);
        editor = preferences.edit();

//        String token1 = FirebaseInstanceId.getInstance().getToken();
//        String token = SharedPref.getString(LogInActivity.this,SharedPrefManager.getInstance(LogInActivity.this).getDeviceToken());
        Et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_pass = (EditText) findViewById(R.id.et_login_pass);
        tv_login_email = (TextView) findViewById(R.id.tv_login_email);
        Btn_Sign_in = (RelativeLayout) findViewById(R.id.btn_Sign_in);

        getemail = Et_login_email.getText().toString();
        getPass = et_login_pass.getText().toString();
        Btn_Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Et_login_email.equals("")) {
                    Toast.makeText(LogInActivity.this, "Please enter valid ID", Toast.LENGTH_SHORT).show();
                } else if (et_login_pass.equals("")) {
                    Toast.makeText(LogInActivity.this, "Please enter valid Password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    try {
                        makeLoginRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }


    private void makeLoginRequest() throws JSONException {
        progressDialog.show();
        final String UserName = Et_login_email.getText().toString().trim();
        final String UserPasswrd = et_login_pass.getText().toString().trim();
        JSONObject params = new JSONObject();
        params.put("phone_number", UserName);
        params.put("password", UserPasswrd);
        JsonObjectRequest  stringRequest = new JsonObjectRequest(Request.Method.POST,
                "https://madrasmarketplaceapi.azurewebsites.net/deliveryBoy/validateLogin",
                params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Login", response.toString());

                try {

//                    String msg = jsonObject.getString("message");
                    if (!response.equals("")) {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        String status = jsonObject.getString("status");
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        for (int i = 0; i < jsonArray.length(); i++) {

//                            JSONObject obj = jsonArray.getJSONObject(i);

                            String user_id = jsonObject.getString("boy_id");
                            String user_fullname = jsonObject.getString("boy_name");
                            String phoenNo = jsonObject.getString("phone_number");
                            String user_password = jsonObject.getString("password");
                            String check = jsonObject.getString("status");
                            editor.putString("userid", user_id);
                            editor.putString("phoenNo", phoenNo);
                            editor.putString("password", user_password);
                            editor.putString("status", check).apply();

                        editor.commit();

                            Session_management sessionManagement = new Session_management(LogInActivity.this);
                            sessionManagement.createLoginSession(user_id, user_fullname, user_password);
                            MyFirebaseRegister myFirebaseRegister = new MyFirebaseRegister(LogInActivity.this);
                            myFirebaseRegister.RegisterUser(user_id);
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                            Btn_Sign_in.setEnabled(false);
                            progressDialog.dismiss();
                            finish();
//                        }
                    } else {
                        progressDialog.dismiss();
                        Btn_Sign_in.setEnabled(true);
                        Toast.makeText(LogInActivity.this, "Please Put Your Currect Email-Id", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error [" + error + "]");

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 2;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);


    }


}
