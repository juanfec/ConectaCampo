package co.edu.uis.conectacampo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class VistaProducto extends AppCompatActivity {

    private String post_key;
    private DatabaseReference mDatabase;
    private TextView precioproducto;
    private TextView nombreproducto;
    private TextView usernombre;
    private EditText cantidad;
    private DatabaseReference mDatabasetotal;
    private int totalsumar;
    private int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombreproducto =  findViewById(R.id.nombre_productovista);
        precioproducto =  findViewById(R.id.precioproductovista);
        usernombre =  findViewById(R.id.nombreuservista);
        cantidad = findViewById(R.id.cantidad);
        final CircleImageView mCircle = (CircleImageView) findViewById(R.id.imagenproductovista);


        post_key = getIntent().getExtras().getString("ruta_id");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Producto").child(post_key);
        mDatabasetotal = FirebaseDatabase.getInstance().getReference().child("Total");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombreproducto.setText((String)dataSnapshot.child("nombre").getValue());
                precioproducto.setText((String)dataSnapshot.child("precio").getValue());
                usernombre.setText((String)dataSnapshot.child("user").getValue());
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("imagen").getValue().toString()).into(mCircle);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabasetotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total = Integer.parseInt(dataSnapshot.getValue().toString());
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



        totalsumar=totalsumar+total;
        mDatabasetotal.setValue(Integer.toString(totalsumar));

        onBackPressed();

    }
}
