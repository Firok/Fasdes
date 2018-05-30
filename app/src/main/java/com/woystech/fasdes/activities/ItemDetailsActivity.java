package com.woystech.fasdes.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.woystech.common.ApplicationConstants;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.models.Item;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.application.AppController;
import com.woystech.net.connectivity.NetworkReceiver;
import com.woystech.net.view.FadeInNetworkImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by firok on 4/3/2017.
 */

public class ItemDetailsActivity extends AppCompatActivity {

    private static final String TAG = ItemDetailsActivity.class.getSimpleName();

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.photo)
   ImageView photo;
    @BindView(R.id.share)
    ImageView share;

    String endpoint;
    Map<String, String> urlParams;

    private Item item;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Intent shareIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        checkConnection();

        item = getItemFromIntent();
        if (item != null) {
            name.setText(item.getName());
            if (item.getImagePath() != null) {
                String imagePath = Endpoint.IMAGE + "items/" + item.getImagePath();
                Picasso.with(this).load(imagePath).into(photo, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Setup share intent now that image has loaded
                        prepareShareIntent();
                    }

                    @Override
                    public void onError() {
                        // ...
                    }
                });

            }
        }

    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(ItemDetailsActivity.this, NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }

    @OnClick(R.id.share)
    public void onClickShare() {
        startActivity(Intent.createChooser(shareIntent, "Share image"));
    }

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void prepareShareIntent() {

       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            grantPermissions();
            return;
        }*/
        // Fetch Bitmap Uri locally
        Uri bmpUri = getLocalBitmapUri(photo); // see previous remote images section
        // Construct share intent as described above based on bitmap
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, item.getName());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            if (Build.VERSION.SDK_INT >= 7.0) {
                // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
                bmpUri = FileProvider.getUriForFile(ItemDetailsActivity.this, "com.woystech.fileprovider", file);
            } else
                bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    private Item getItemFromIntent() {
        Item item = null;
        if (getIntent().hasExtra(BundleKey.EXTRA_ITEM)) {
            item = getIntent().getParcelableExtra(BundleKey.EXTRA_ITEM);
        }
        return item;
    }

    private boolean needPermissionsRationale(List<String> permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    private void grantPermissions() {
        List<String> missingPermissions;

        missingPermissions = Arrays.asList(ApplicationConstants.SHARE_PERMISSIONS);
        if (needPermissionsRationale(missingPermissions)) {
           // Toast.makeText(this, getString(R.string.missing_permissions_msg), Toast.LENGTH_LONG)
                //    .show();
        }
        ActivityCompat.requestPermissions(this,
                missingPermissions.toArray(new String[missingPermissions.size()]),
                0);
    }
}
