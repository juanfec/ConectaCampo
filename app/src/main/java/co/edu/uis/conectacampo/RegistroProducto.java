package co.edu.uis.conectacampo;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class RegistroProducto extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorageReference;
    private EditText mNombre;
    private EditText mPrecio;
    private DatabaseReference mDatabaseuser;
    private FirebaseAuth mAuth;
    private String username;
    private DatabaseReference mProductos;
    private ImageButton mImagebutton;

    private  String downloadUriGlobal;
    private Uri mImageUri= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_producto);

        mAuth = FirebaseAuth.getInstance();

        mImagebutton = findViewById(R.id.imageButton);

        mStorageReference = FirebaseStorage.getInstance().getReference().child("imagen_producto");
        mProductos = FirebaseDatabase.getInstance().getReference().child("Producto");

        mNombre = findViewById(R.id.nombreproductosubir);
        mPrecio = findViewById(R.id.precioproductosubir);

        mDatabaseuser = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseuser.child(mAuth.getCurrentUser().getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username =  dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void subirImagen(View v){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public void publicarproducto(View v)
    {
        final String name= mNombre.getText().toString().trim();
        final String precio= mPrecio.getText().toString().trim();


        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(precio)&& mImageUri!=null)
        {

            StorageReference filepath = mStorageReference.child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    downloadUriGlobal=taskSnapshot.getDownloadUrl().toString();
                    HashMap<String, String> producto= new HashMap<>();
                    producto.put("nombre",name);
                    producto.put("precio",precio);
                    producto.put("user",username);
                    producto.put("imagen",downloadUriGlobal);

                    mProductos.push().setValue(producto);

                }
            });





        }
        else
        {
            Snackbar.make(v, "Debes llenar todos los campos y subir una imagen", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        onBackPressed();






    }
}
