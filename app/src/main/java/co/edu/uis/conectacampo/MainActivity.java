package co.edu.uis.conectacampo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean viewIsAtHome;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Total");//establece referencia a una parte de la base de datos
        mDatabase.setValue("0");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this, Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayView(R.id.homeprincipal);
    }

    public static class ProductosViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public ProductosViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }

        public void setNombre(String nombre)
        {
            TextView nombreproducto = mView.findViewById(R.id.nombre_producto);
            nombreproducto.setText(nombre );

        }

        public void setPrecio(String precio){
            TextView precioproducto = mView.findViewById(R.id.precioproducto);
            precioproducto.setText(precio + " libra");
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

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public CarritoViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }

        public void setNombre(String nombre)
        {
            TextView nombreproducto = mView.findViewById(R.id.nombrecarrito);
            nombreproducto.setText(nombre );

        }

        public void setPrecio(String precio){
            TextView precioproducto = mView.findViewById(R.id.preciocarrito);
            precioproducto.setText(precio + " libra");
        }

        public void setCantidad(String user){
            TextView usernombre = mView.findViewById(R.id.cantidadcarrito);
            usernombre.setText(user);

        }

        public void setTotal(String user){
            TextView usernombre = mView.findViewById(R.id.totalcarrito);
            usernombre.setText(user);

        }

        public void setImagen(final Context ctx, String imagen){
            ImageView img = (ImageView) mView.findViewById(R.id.imagencarrito);
            String image_url = imagen;
            Picasso.with(ctx).load(image_url).into(img);

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the Home fragment
            displayView(R.id.home); //display the Home fragment
        } else {
            moveTaskToBack(true);  //If view is in home fragment, exit application
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.homeprincipal:
                fragment = new HomeFragment();
                title  = "";
                viewIsAtHome = true;

                break;
            case R.id.nav_gallery:
                fragment = new BlankFragment();
                title = "Perfil";
                viewIsAtHome = false;
                break;
         //   case R.id.nav_slideshow:
              //  fragment = new TusRutas();
              //  title = "Tus Rutas";
              //  viewIsAtHome = false;
          //      break;
            case R.id.nav_share:
                logout();
                break;
            /*case R.id.nav_tus_viajes:
                fragment = new TusViajes();
                title = "Tus Viajes";
                viewIsAtHome = false;
                break;*/
           // case R.id.nav_send:
                //sendEmail();
               // viewIsAtHome = false;
           //     break;
            default:
                fragment = new HomeFragment();
                title  = "";
                viewIsAtHome = true;
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    private void logout() {

        mAuth.signOut();
    }

}
