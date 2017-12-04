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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;

public class Login extends AppCompatActivity {

    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;

    private DatabaseReference mDatabaseUsers;

    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mLoginEmailField = (EditText) findViewById(R.id.EditT_usuario);
        mLoginPasswordField = (EditText) findViewById(R.id.EditT_password);





    }

    public void login (View v){

        String email = mLoginEmailField.getText().toString().trim();
        String password= mLoginPasswordField.getText().toString().trim();
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);

        if(matcher.matches()&& !TextUtils.isEmpty(password))//si el email es correcto y el campo contraseña no esta vacio
        {
            mProgress.setMessage("Iniciando sesion ");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                                checkUserExist();
                    } else
                    {
                        mProgress.dismiss();
                        Toast.makeText(Login.this, "Usuario y/o contraseña incorrectos" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(Login.this, "Asegurate de que los campos no estan vacios y el correo esta escrito correctamente" , Toast.LENGTH_SHORT).show();
        }

    }

    public void registerButton(View v){
        Intent mainIntent = new Intent(Login.this, Registro.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
    }


    private void checkUserExist() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)){

                    Intent mainIntent = new Intent(Login.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                } else {
                    Toast.makeText(Login.this, "Usuario inexistente " , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
