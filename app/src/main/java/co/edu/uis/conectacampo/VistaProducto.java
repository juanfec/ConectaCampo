package co.edu.uis.conectacampo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView nombreproducto = (TextView) findViewById(R.id.nombre_productovista);
        final TextView precioproducto = (TextView) findViewById(R.id.precioproductovista);
        final TextView usernombre = (TextView) findViewById(R.id.nombreuservista);
        final CircleImageView mCircle = (CircleImageView) findViewById(R.id.imagenproductovista);


        post_key = getIntent().getExtras().getString("ruta_id");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Producto").child(post_key);

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
}
