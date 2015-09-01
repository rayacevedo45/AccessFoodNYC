package rayacevedo45.c4q.nyc.accessfoodnyc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PicActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE = 1;
    private Uri imageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takePic();

        setContentView(R.layout.activity_pic);
        imageView = (ImageView) findViewById(R.id.imageID);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pic, menu);
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
    public void takePic() {

        String mediaStorageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File mediaFile = new File(mediaStorageDir + File.separator + "IMG_" + timeStamp + ".jpg");
        imageUri = Uri.fromFile(mediaFile);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
        this.startActivityForResult(takePictureIntent, CAPTURE_IMAGE);



//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }
}
