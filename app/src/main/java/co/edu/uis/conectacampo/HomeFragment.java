package co.edu.uis.conectacampo;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewCarrito;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager mLayoutManagerCarrito;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCarritoUser;
    private DatabaseReference mUser;
    private TextView mTotal;
    private DatabaseReference mDatabasetotal;
    private ListView mListView;
    private FirebaseAuth mAuth;
    private int  valortotal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home,container, false);




        Log.e("frag", "entre");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Producto");


        mRecyclerView = (RecyclerView) v.findViewById(R.id.productos_lista);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerViewCarrito = v.findViewById(R.id.carritolista);
        mRecyclerViewCarrito.setHasFixedSize(true);


        mLayoutManagerCarrito = new LinearLayoutManager(getActivity());//layout para recycler view
        mLayoutManagerCarrito.setReverseLayout(true);
        mLayoutManagerCarrito.setStackFromEnd(true);

        mLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTotal = v.findViewById(R.id.valor_total);

        mAuth = FirebaseAuth.getInstance();//inicializa auth para maneho de usuario

        mDatabasetotal = FirebaseDatabase.getInstance().getReference().child("Total");//establece referencia a una parte de la base de datos

        if(mAuth.getCurrentUser()!=null)
        {
            mDatabaseCarritoUser = FirebaseDatabase.getInstance().getReference().child("Carrito").child(mAuth.getCurrentUser().getUid().toString());//establece referencia a una parte de la base de datos

            mUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }
        else
        {
            mDatabaseCarritoUser = FirebaseDatabase.getInstance().getReference().child("Carrito");//establece referencia a una parte de la base de datos

            mUser = FirebaseDatabase.getInstance().getReference().child("Users");
        }






        mRecyclerViewCarrito.setLayoutManager(mLayoutManagerCarrito);
        mRecyclerView.setLayoutManager(mLayoutManager);
        return v;


    }


    @Override
    public void onStart() {
        super.onStart();




        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("total").getValue()==null){
                    mTotal.setText("0");
                    mUser.child("total").setValue("0");
                }
                else
                {
                    mTotal.setText(dataSnapshot.child("total").getValue().toString());
                    valortotal = Integer.parseInt(dataSnapshot.child("total").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //adaptador necesario para llenar recyclerview
        FirebaseRecyclerAdapter<Producto,MainActivity.ProductosViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Producto, MainActivity.ProductosViewHolder>(
                Producto.class,
                R.layout.producto_simple,
                MainActivity.ProductosViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MainActivity.ProductosViewHolder viewHolder, Producto model, int position) {

                final String post_key = getRef(position).getKey();
                Log.e("error",post_key);

                viewHolder.setNombre(model.getNombre());
                viewHolder.setPrecio(model.getPrecio());
                viewHolder.setImagen(getActivity().getApplicationContext(),model.getImagen());
                viewHolder.setUser(model.getUser());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent vistaRutaIntent = new Intent(getActivity() , VistaProducto.class);
                        vistaRutaIntent.putExtra("ruta_id",post_key);
                        startActivity(vistaRutaIntent);

                        //Toast.makeText(getActivity(), post_key , Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

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
                viewHolder.setImagen(getActivity().getApplicationContext(),model.getImagen());
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
                                    Toast.makeText(getActivity(),"Borrado", Toast.LENGTH_LONG);

                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"No se pudo borrar", Toast.LENGTH_LONG);
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
