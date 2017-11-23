package co.edu.uis.conectacampo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by jufea on 11/23/2017.
 */

public class ProductosViewHolder extends RecyclerView.ViewHolder {

    View mView;


    public ProductosViewHolder(View itemView) {
        super(itemView);

        mView = itemView;


    }

    public void setNombre(String nombre)
    {
        TextView nombreproducto = mView.findViewById(R.id.nombre_producto);
        nombreproducto.setText(nombre);

    }

    public void setPrecio(String precio){
        TextView precioproducto = mView.findViewById(R.id.precioproducto);
        precioproducto.setText(precio);
    }

    public void setUser(String user){
        TextView usernombre = mView.findViewById(R.id.nombreuser);
        usernombre.setText(user);

    }

    public void setImagen(final Context ctx, String imagen){
        ImageView img = (ImageView) mView.findViewById(R.id.imagenproducto);
        String image_url = imagen;
        Picasso.with(ctx).load(image_url).into(img);

    }

}
