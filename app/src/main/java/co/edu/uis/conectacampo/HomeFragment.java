package co.edu.uis.conectacampo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private TextView mTotal;
    private DatabaseReference mDatabasetotal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home,container, false);


        Log.e("frag", "entre");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Producto");


        mRecyclerView = (RecyclerView) v.findViewById(R.id.productos_lista);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTotal = v.findViewById(R.id.valor_total);

        mDatabasetotal = FirebaseDatabase.getInstance().getReference().child("Total");



        mRecyclerView.setLayoutManager(mLayoutManager);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        mDatabasetotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTotal.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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




    }
}
