package co.edu.uis.conectacampo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Comprar extends AppCompatActivity {

    private DatabaseReference mDatabaseuser;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseCarritoUser;
    private RecyclerView mRecyclerViewCarrito;
    private LinearLayoutManager mLayoutManagerCarrito;
    private DatabaseReference mUser;
    private int valortotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseuser= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabaseCarritoUser = FirebaseDatabase.getInstance().getReference().child("Carrito").child(mAuth.getCurrentUser().getUid().toString());//establece referencia a una parte de la base de datos
        mUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mRecyclerViewCarrito = findViewById(R.id.carritolistacomprar);
        mRecyclerViewCarrito.setHasFixedSize(true);


        mLayoutManagerCarrito = new LinearLayoutManager(this);//layout para recycler view
        mLayoutManagerCarrito.setReverseLayout(true);
        mLayoutManagerCarrito.setStackFromEnd(true);

        mRecyclerViewCarrito.setLayoutManager(mLayoutManagerCarrito);


    }


    public void comprarproducto(final View v)
    {



        mDatabaseuser.child("total").setValue("0");
        mDatabase.child("Carrito").child(mAuth.getCurrentUser().getUid().toString()).removeValue();

        Intent main = new Intent(Comprar.this, MainActivity.class);
        startActivity(main);

    }

    @Override
    protected void onStart() {





        super.onStart();




        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("total").getValue()==null){
                    mUser.child("total").setValue("0");
                }
                else
                {
                    valortotal = Integer.parseInt(dataSnapshot.child("total").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<Carrito,MainActivity.CarritoViewHolder> firebaseRecyclerAdaptercarrito = new FirebaseRecyclerAdapter<Carrito, MainActivity.CarritoViewHolder>(

                Carrito.class,
                R.layout.carritoitem,
                MainActivity.CarritoViewHolder.class,
                mDatabaseCarritoUser
        ) {
            @Override
            protected void populateViewHolder(MainActivity.CarritoViewHolder viewHolder, final Carrito model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setNombre(model.getNombre());
                viewHolder.setPrecio(model.getPrecio());
                viewHolder.setImagen(getApplicationContext(),model.getImagen());
                viewHolder.setCantidad(model.getCantidad());
                viewHolder.setTotal(model.getTotal());

                viewHolder.mView.findViewById(R.id.borraritem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        valortotal = valortotal- Integer.parseInt(model.getTotal());
                        mUser.child("total").setValue(Integer.toString(valortotal));
                        mDatabaseCarritoUser.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"Borrado", Toast.LENGTH_LONG);

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"No se pudo borrar", Toast.LENGTH_LONG);
                                }
                            }
                        });



                    }
                });

            }
        };
        mRecyclerViewCarrito.setAdapter(firebaseRecyclerAdaptercarrito);
    }
}
