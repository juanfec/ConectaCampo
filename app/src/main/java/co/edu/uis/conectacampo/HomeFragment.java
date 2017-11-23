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
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home,container, false);


        Log.d("frag", "entre");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Producto");


        mRecyclerView = (RecyclerView) v.findViewById(R.id.productos_lista);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);


        mRecyclerView.setLayoutManager(mLayoutManager);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Producto,ProductosViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Producto, ProductosViewHolder>(
                Producto.class,
                R.layout.producto_simple,
                ProductosViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ProductosViewHolder viewHolder, Producto model, int position) {

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
