package ws.vika.deviceinfo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button convertTextButton;
    Button getSerialNumberButton;
    Button getImeiNumberButton;
    ImageView imageView;

    public static final int REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.convertTextField);
        convertTextButton = findViewById(R.id.convertButton);
        getSerialNumberButton = findViewById(R.id.getSerialNumberButton);
        getImeiNumberButton = findViewById(R.id.getImeiButton);
        imageView = findViewById(R.id.barcodeOutput);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }

        convertTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = generateBarcode(editText.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });

        getSerialNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(getSerialNumber());
                Bitmap bitmap = generateBarcode(editText.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });

        getImeiNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(getImeiNumber());
                Bitmap bitmap = generateBarcode(editText.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    private Bitmap generateBarcode(String s, BarcodeFormat bf, int width, int height) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(s, bf, width, height);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static String getSerialNumber() {
        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            // (?) Lenovo Tab (https://stackoverflow.com/a/34819027/1276306)
            serialNumber = (String) get.invoke(c, "gsm.sn1");

            if (serialNumber.equals("")) {
                // Samsung Galaxy S5 (SM-G900F) : 6.0.1
                // Samsung Galaxy S6 (SM-G920F) : 7.0
                // Samsung Galaxy Tab 4 (SM-T530) : 5.0.2
                // (?) Samsung Galaxy Tab 2 (https://gist.github.com/jgold6/f46b1c049a1ee94fdb52)
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            }

            if (serialNumber.equals("")) {
                // Archos 133 Oxygen : 6.0.1
                // Google Nexus 5 : 6.0.1
                // Hannspree HANNSPAD 13.3" TITAN 2 (HSG1351) : 5.1.1
                // Honor 5C (NEM-L51) : 7.0
                // Honor 5X (KIW-L21) : 6.0.1
                // Huawei M2 (M2-801w) : 5.1.1
                // (?) HTC Nexus One : 2.3.4 (https://gist.github.com/tetsu-koba/992373)
                serialNumber = (String) get.invoke(c, "ro.serialno");
            }

            if (serialNumber.equals("")) {
                // (?) Samsung Galaxy Tab 3 (https://stackoverflow.com/a/27274950/1276306)
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            }

            if (serialNumber.equals("")) {
                // Archos 133 Oxygen : 6.0.1
                // Hannspree HANNSPAD 13.3" TITAN 2 (HSG1351) : 5.1.1
                // Honor 9 Lite (LLD-L31) : 8.0
                // Xiaomi Mi 8 (M1803E1A) : 8.1.0
                serialNumber = Build.SERIAL;
            }

            // If none of the methods above worked
            if (serialNumber.equals(Build.UNKNOWN)) {
                serialNumber = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        return serialNumber;
    }

    private String getImeiNumber() {
        TelephonyManager telephonyManager = (TelephonyManager)
                getSystemService(this.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}