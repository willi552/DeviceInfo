package ws.vika.deviceinfo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button convertTextButton;
    Button getSerialNumberButton;
    Button getImeiNumberButton;
    Button b1, b2, b3, b4, b5;
    ImageView imageView;

    public static final int REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.appOutput);
        getSerialNumberButton = findViewById(R.id.getSerialNumberButton);
        getImeiNumberButton = findViewById(R.id.getImeiButton);
        imageView = findViewById(R.id.barcodeOutput);

        b1 = findViewById(R.id.methodOneButton);
        b2 = findViewById(R.id.methodTwoButton);
        b3 = findViewById(R.id.methodThreeButton);
        b4 = findViewById(R.id.methodFourButton);
        b5 = findViewById(R.id.methodFiveButton);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }


        getSerialNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getSerialNumber());
                Bitmap bitmap = generateBarcode(textView.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });

        getImeiNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getImeiNumber());
                Bitmap bitmap = generateBarcode(textView.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getSerialNumber(1));
                Bitmap bitmap = generateBarcode(textView.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getSerialNumber(2));
                Bitmap bitmap = generateBarcode(textView.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getSerialNumber(3));
                Bitmap bitmap = generateBarcode(textView.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getSerialNumber(4));
                Bitmap bitmap = generateBarcode(textView.getText().toString(),
                        BarcodeFormat.CODE_128,
                        imageView.getWidth(),
                        imageView.getHeight());
                imageView.setImageBitmap(bitmap);
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getSerialNumber(5));
                Bitmap bitmap = generateBarcode(textView.getText().toString(),
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


    // below method from https://gist.github.com/flawyte/efd23dd520fc2320f94ba003b9aabfce
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

    // below method from https://gist.github.com/flawyte/efd23dd520fc2320f94ba003b9aabfce
    private static String getSerialNumber(int i) {

        String serialNumber = "nothing";

        try {

            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            if (i == 1) {
                // (?) Lenovo Tab (https://stackoverflow.com/a/34819027/1276306)
                serialNumber = (String) get.invoke(c, "gsm.sn1");
            }

            if (i == 2) {
                // Samsung Galaxy S5 (SM-G900F) : 6.0.1
                // Samsung Galaxy S6 (SM-G920F) : 7.0
                // Samsung Galaxy Tab 4 (SM-T530) : 5.0.2
                // (?) Samsung Galaxy Tab 2 (https://gist.github.com/jgold6/f46b1c049a1ee94fdb52)
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            }

            if (i == 3) {
                // Archos 133 Oxygen : 6.0.1
                // Google Nexus 5 : 6.0.1
                // Hannspree HANNSPAD 13.3" TITAN 2 (HSG1351) : 5.1.1
                // Honor 5C (NEM-L51) : 7.0
                // Honor 5X (KIW-L21) : 6.0.1
                // Huawei M2 (M2-801w) : 5.1.1
                // (?) HTC Nexus One : 2.3.4 (https://gist.github.com/tetsu-koba/992373)
                serialNumber = (String) get.invoke(c, "ro.serialno");
            }

            if (i == 4) {
                // Samsung Galaxy S5 (SM-G900F) : 6.0.1
                // Samsung Galaxy S6 (SM-G920F) : 7.0
                // Samsung Galaxy Tab 4 (SM-T530) : 5.0.2
                // (?) Samsung Galaxy Tab 2 (https://gist.github.com/jgold6/f46b1c049a1ee94fdb52)
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            }

            if (i == 5) {
                // (?) Samsung Galaxy Tab 3 (https://stackoverflow.com/a/27274950/1276306)
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            }

            if (i == 6) {
                // Archos 133 Oxygen : 6.0.1
                // Hannspree HANNSPAD 13.3" TITAN 2 (HSG1351) : 5.1.1
                // Honor 9 Lite (LLD-L31) : 8.0
                // Xiaomi Mi 8 (M1803E1A) : 8.1.0
                serialNumber = Build.SERIAL;
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