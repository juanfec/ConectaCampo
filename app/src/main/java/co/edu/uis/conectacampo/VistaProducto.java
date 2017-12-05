package co.edu.uis.conectacampo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VistaProducto extends AppCompatActivity {

    private String post_key;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCarritoUser;
    private DatabaseReference mUser;
    private TextView precioproducto;
    private TextView nombreproducto;
    private TextView usernombre;
    private EditText cantidad;
    private String imagenlink;
    private DatabaseReference mDatabasetotal;
    private FirebaseAuth mAuth;
    private int totalsumar;
    private int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        nombreproducto =  findViewById(R.id.nombre_productovista);
        precioproducto =  findViewById(R.id.precioproductovista);
        usernombre =  findViewById(R.id.nombreuservista);
        cantidad = findViewById(R.id.cantidad);
        final CircleImageView mCircle = (CircleImageView) findViewById(R.id.imagenproductovista);


        post_key = getIntent().getExtras().getString("ruta_id");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Producto").child(post_key);
        mDatabaseCarritoUser = FirebaseDatabase.getInstance().getReference().child("Carrito").child(mAuth.getCurrentUser().getUid());
        mUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombreproducto.setText((String)dataSnapshot.child("nombre").getValue());
                precioproducto.setText((String)dataSnapshot.child("precio").getValue());
                usernombre.setText((String)dataSnapshot.child("user").getValue());
                imagenlink = dataSnapshot.child("imagen").getValue().toString();
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("imagen").getValue().toString()).into(mCircle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    total = Integer.parseInt(dataSnapshot.child("total").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void agregarCarrito(View v){

        int valor = Integer.parseInt(precioproducto.getText().toString());
        int cantidadsumar = Integer.parseInt(cantidad.getText().toString());
        totalsumar = valor*cantidadsumar;

        Map<String,String> item = new HashMap<>();
        item.put("total",Integer.toString(totalsumar));
        item.put("nombre",nombreproducto.getText().toString());
        item.put("cantidad",cantidad.getText().toString());
        item.put("precio",precioproducto.getText().toString());
        item.put("imagen",imagenlink);


        mDatabaseCarritoUser.push().setValue(item);
        mUser.child("total").setValue(Integer.toString(total+totalsumar));

        onBackPressed();

    }
}
