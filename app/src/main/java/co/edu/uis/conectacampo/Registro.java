package co.edu.uis.conectacampo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;

public class Registro extends AppCompatActivity {


    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mAgeField;


    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mNameField = (EditText) findViewById(R.id.EditT_nombre);
        mEmailField = (EditText) findViewById(R.id.EditT_usuario);
        mPasswordField = (EditText) findViewById(R.id.EditT_password);
        mAgeField = findViewById(R.id.EditT_edad);


    }

    public void registerButton(View v){

        final String name= mNameField.getText().toString().trim();
        String email= mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        final String edad = mAgeField.getText().toString().trim();
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);



        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(edad)&& matcher.matches() && !TextUtils.isEmpty(password))
        {
            mProgress.setMessage("Iniciando sesion");
            mProgress.show();



            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String token = FirebaseInstanceId.getInstance().getToken();
                        String user_id = mAuth.getCurrentUser().getUid();

                        final DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("edad").setValue(edad);
                        current_user_db.child("device_token").setValue(token);






                        mProgress.dismiss();

                        Intent mainIntent = new Intent(Registro.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);


                    }else
                    {
                        Toast.makeText(Registro.this, "No se pudo crear la cuenta" , Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                }
            });
        }else{

            Toast.makeText(Registro.this, "Asegurate de que los campos no estan vacios, el correo esta escrito correctamente y adjuntaste una foto" , Toast.LENGTH_LONG).show();
            //Snackbar.make(v, "Asegurate de que los campos no estan vacios y el correo esta escrito correctamente", Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show();
        }
    }
}
