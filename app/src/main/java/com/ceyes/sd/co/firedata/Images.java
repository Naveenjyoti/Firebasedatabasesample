package com.ceyes.sd.co.firedata;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Images extends AppCompatActivity {
    public static final int REQUEST_PERMISSON_SORAGE = 1;
    public static final int REQUEST_PERMISSON_CAMERA = 2;
    public static final int SELECT_GALLERY_IMAGE_CODE = 7;
    public static final int SELECT_GALLERY_IMAGE_CODE1 = 8;
    public static final int SELECT_GALLERY_IMAGE_CODE12 = 81;
    public static final int SELECT_GALLERY_IMAGE_CODE123 = 811;
    public static final int SELECT_GALLERY_IMAGE_CODE1234 = 8111;
    public static final int SELECT_GALLERY_IMAGE_CODE12345 = 8112;
    public static final int TAKE_PHOTO_CODE = 8;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    ImageView img_user,img_user2,img_user3,img_user4,img_user5;
    private String path,thePath,url;
    private Uri photoURI = null;
    public static int count = 0;
    Uri outputFileUri;
    Bitmap bitmaps;
    File destination;
    private Bitmap bitmap;
    private final int RESULT_CROP = 400;
    Button button;
    DatabaseReference databaseArtists1;
    String id123="",name="",email="",phone="",password="";
    StorageReference storageReference;
    ProgressDialog progressDialog ;
    EditText username;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        Toolbar toolbar = findViewById(R.id.toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor= pref.edit();
        storageReference = FirebaseStorage.getInstance().getReference();
        Intent ii=getIntent();
        username=(EditText)findViewById(R.id.username);
        id123=ii.getStringExtra("id");
        name=ii.getStringExtra("name");
        email=ii.getStringExtra("email");
        phone=ii.getStringExtra("phone");
        password=ii.getStringExtra("password");
        databaseArtists1 =FirebaseDatabase.getInstance().getReference("users");
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(Images.this);
        img_user=(ImageView)findViewById(R.id.img_user);
        img_user2=(ImageView)findViewById(R.id.img_user2);
        img_user3=(ImageView)findViewById(R.id.img_user3);
        img_user4=(ImageView)findViewById(R.id.img_user4);
        img_user5=(ImageView)findViewById(R.id.img_user5);
        button=(Button)findViewById(R.id.button);
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Images.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.popup);
                LinearLayout text = (LinearLayout) dialog.findViewById(R.id.l1);
                LinearLayout text1 = (LinearLayout) dialog.findViewById(R.id.l2);
                LinearLayout text12 = (LinearLayout) dialog.findViewById(R.id.l3);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectFromCam();
                        dialog.dismiss();
                    }
                });
                text1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectFromAblum();
                        dialog.dismiss();
                    }
                });
                text12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // selectFromAblum();
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals(""))
                {
                    username.setError("Enter about me.");
                }
                else {
                    BitmapDrawable drawable = (BitmapDrawable) img_user.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    uploadImage(bitmap);

                    BitmapDrawable drawable1 = (BitmapDrawable) img_user2.getDrawable();
                    Bitmap bitmap1 = drawable1.getBitmap();
                    uploadImage1(bitmap1);

                    BitmapDrawable drawable2 = (BitmapDrawable) img_user3.getDrawable();
                    Bitmap bitmap2 = drawable2.getBitmap();
                    uploadImage2(bitmap2);

                    BitmapDrawable drawable3 = (BitmapDrawable) img_user4.getDrawable();
                    Bitmap bitmap3 = drawable3.getBitmap();
                    uploadImage3(bitmap3);

                    BitmapDrawable drawable4 = (BitmapDrawable) img_user5.getDrawable();
                    Bitmap bitmap4 = drawable4.getBitmap();
                    uploadImage4(bitmap4);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("url is in"+pref.getString("url1",null));
                        System.out.println("url is in"+pref.getString("url2",null));
                        System.out.println("url is in"+pref.getString("url3",null));
                        System.out.println("url is in"+pref.getString("url4",null));
                        String id = databaseArtists1.push().getKey();
                        Users artist = new Users(id, name, phone,email,password,pref.getString("url",null),pref.getString("url1",null),pref.getString("url2",null),pref.getString("url3",null),pref.getString("url4",null));
                        databaseArtists1.child(id).setValue(artist);
                        startActivity(new Intent(Images.this, Mains.class));
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }
                }, 10000);


            }
        });
        img_user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("imgg 2");
                selectm1();
            }
        });
        img_user3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAblum2();
            }
        });
        img_user4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAblum3();
            }
        });
        img_user5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAblum4();
            }
        });

    }
    private void selectFromAblum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openAblumWithPermissionsCheck();
        } else {
            openAlbum();
        }
    }
    private void selectm1() {

            System.out.println("Naveen rajput");
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, 81);
        //}
    }
    private void selectAblum2() {

            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, 811);
        //}
    }
    private void selectAblum3() {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 8111);
        //}
    }
    private void selectAblum4() {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 8112);
        //}
    }
    private void selectFromCam() {
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/maydmedia/";
        File newdir = new File(dir);
        newdir.mkdirs();
        count++;
        String file = dir+count+".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }
        outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }
    private void openAblumWithPermissionsCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_SORAGE);
            return;
        }
        openAlbum();
    }
    public String getRealPathFromURI1(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            System.out.println("column_index of selected image is:"+column_index);
            cursor.moveToFirst();
            System.out.println("selected image path is:"+cursor.getString(column_index));
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }
    private void openAlbum() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, SELECT_GALLERY_IMAGE_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_GALLERY_IMAGE_CODE:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    performCrop(picturePath);
                   // img_user.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    break;

                case TAKE_PHOTO_CODE:
                    path =getRealPathFromURI1(outputFileUri).toString();
                    try {
                       img_user.invalidate();
                       // Picasso.with(getApplicationContext()).load(outputFileUri).into(img_user);
                        performCrop(path);
//                        l0.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // simpleDraweeView.setOnTouchListener(new ImageMatrixTouchHandler(Productdetail.this));
                    break;
                case ACTION_REQUEST_EDITIMAGE:
                 //   handleEditorImage(data);
                    break;


                case RESULT_CROP:
                    Uri imageUri = data.getData();
                    Bitmap bitmap123= null;
                    try {
                        bitmap123 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img_user.setImageBitmap(bitmap123);
                    break;
                case REQUEST_PERMISSON_CAMERA:
                    InputStream stream = null;
                    try {


                        // recyle unused bitmaps
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                        stream = getContentResolver().openInputStream(data.getData());
                        bitmap = BitmapFactory.decodeStream(stream);

                      //  img_user.setImageBitmap(bitmap);
                        //l0.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    finally{
                        if (stream != null)
                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                    break;


                case SELECT_GALLERY_IMAGE_CODE12:
                    Uri selectedImage1 = data.getData();
                    String[] filePathColumn1 = { MediaStore.Images.Media.DATA };

                    Cursor cursor1 = getContentResolver().query(selectedImage1,
                            filePathColumn1, null, null, null);
                    cursor1.moveToFirst();

                    int columnIndex1 = cursor1.getColumnIndex(filePathColumn1[0]);
                    String picturePath1 = cursor1.getString(columnIndex1);
                    cursor1.close();
                    //performCrop(picturePath);
                    img_user2.setImageBitmap(BitmapFactory.decodeFile(picturePath1));
                    break;

                case SELECT_GALLERY_IMAGE_CODE123:
                    Uri selectedImage12 = data.getData();
                    String[] filePathColumn12 = { MediaStore.Images.Media.DATA };

                    Cursor cursor12 = getContentResolver().query(selectedImage12,
                            filePathColumn12, null, null, null);
                    cursor12.moveToFirst();

                    int columnIndex12 = cursor12.getColumnIndex(filePathColumn12[0]);
                    String picturePath12 = cursor12.getString(columnIndex12);
                    cursor12.close();
                    //performCrop(picturePath);
                    img_user3.setImageBitmap(BitmapFactory.decodeFile(picturePath12));
                    break;
                case SELECT_GALLERY_IMAGE_CODE1234:
                    Uri selectedImage123 = data.getData();
                    String[] filePathColumn123 = { MediaStore.Images.Media.DATA };

                    Cursor cursor123 = getContentResolver().query(selectedImage123,
                            filePathColumn123, null, null, null);
                    cursor123.moveToFirst();

                    int columnIndex123 = cursor123.getColumnIndex(filePathColumn123[0]);
                    String picturePath123 = cursor123.getString(columnIndex123);
                    cursor123.close();
                    //performCrop(picturePath);
                    img_user4.setImageBitmap(BitmapFactory.decodeFile(picturePath123));
                    break;
                case SELECT_GALLERY_IMAGE_CODE12345:
                    Uri selectedImage1234 = data.getData();
                    String[] filePathColumn1234 = { MediaStore.Images.Media.DATA };

                    Cursor cursor1234 = getContentResolver().query(selectedImage1234,
                            filePathColumn1234, null, null, null);
                    cursor1234.moveToFirst();

                    int columnIndex1234 = cursor1234.getColumnIndex(filePathColumn1234[0]);
                    String picturePath1234 = cursor1234.getString(columnIndex1234);
                    cursor1234.close();
                    //performCrop(picturePath);
                    img_user5.setImageBitmap(BitmapFactory.decodeFile(picturePath1234));
                    break;
            }
        }
    }
    private void handleSelectFromAblum(Intent data) {
        path = data.getStringExtra("imgPath");
       // loadImage(path);
    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void uploadImage(Bitmap bitmap) {
        progressDialog.setTitle("Profile pic uploading");
        progressDialog.show();
        final StorageReference ref = storageReference.child(System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               progressDialog.dismiss();
               // Toast.makeText(Images.this, "Uploaded", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                            editor.putString("url", downUri.toString());
                            editor.commit();


                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               progressDialog.dismiss();
                Toast.makeText(Images.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadImage1(Bitmap bitmap) {
        progressDialog.setTitle("Images uploading");
        progressDialog.show();

        final StorageReference ref = storageReference.child(System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
               // Toast.makeText(Images.this, "Uploaded", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                           url=downUri.toString();
                            editor.putString("url1", downUri.toString());
                            editor.commit();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Images.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage2(Bitmap bitmap) {
        progressDialog.setTitle("Images uploading");
        progressDialog.show();

        final StorageReference ref = storageReference.child(System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
               // Toast.makeText(Images.this, "Uploaded", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                            url=downUri.toString();
                            editor.putString("url2", downUri.toString());
                            editor.commit();

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Images.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage3(Bitmap bitmap) {
        progressDialog.setTitle("Images uploading");
        progressDialog.show();

        final StorageReference ref = storageReference.child(System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
               // Toast.makeText(Images.this, "Uploaded", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                            url=downUri.toString();
                            editor.putString("url3", downUri.toString());
                            editor.commit();

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Images.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage4(Bitmap bitmap) {
        progressDialog.setTitle("Images uploading");
        progressDialog.show();

        final StorageReference ref = storageReference.child(System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(Images.this, "Uploaded", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                            url=downUri.toString();
                            editor.putString("url4", downUri.toString());
                            editor.commit();

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Images.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
