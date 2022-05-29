package com.example.camerastore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class visa extends AppCompatActivity {

    EditText inputVisaNumber,inputDate,inputCardName,inputCvv;
    TextView visaNumber, visaDate,VisaName,textcvv;
    String count = "4",countDate = "2";
    ImageButton buttoncvvimage;
    FirebaseFirestore fStore;
    String userId,email;
    Button orderBtn;
    CvvDialog loadingDialog = new CvvDialog(visa.this);
    boolean input1=false,input2=false,input3=false,input4=false;
    int priceAfter,quantitAfter;
    DatabaseReference rootdatabaseReference;
    int random = new Random().nextInt((180000 - 18000) + 1) + 18000;
    FirebaseAuth fAuth;

    String nameM,priceM;
    SimpleDateFormat datePatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        inputVisaNumber = findViewById(R.id.inputVisaNumber);
        inputDate = findViewById(R.id.inputDate);
        visaNumber = findViewById(R.id.visaNumber);
        visaDate = findViewById(R.id.visaDate);
        inputCardName = findViewById(R.id.inputCardName);
        VisaName = findViewById(R.id.VisaName);
        inputCvv = findViewById(R.id.inputCvv);
        textcvv = findViewById(R.id.textcvv);
        buttoncvvimage = findViewById(R.id.cvvinfo);
        orderBtn = findViewById(R.id.orderBtn);
        rootdatabaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        Intent recItems = getIntent();
        String imgM = recItems.getStringExtra("IMAGE");
        nameM = recItems.getStringExtra("NAME");
        priceM = recItems.getStringExtra("PRICE");
        String quantitM = recItems.getStringExtra("quantity");

        priceAfter = Integer.parseInt(priceM);
        quantitAfter = Integer.parseInt(quantitM);

        quantitAfter = quantitAfter - 1;

        final FirebaseUser mFirebaseUser = fAuth.getCurrentUser();

        if(mFirebaseUser != null){
            userId = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    email = value.getString("email");
                }
            });

        }


        inputVisaNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputVisaNumber.getText().toString().length() != 16){
                    Toast.makeText(getApplicationContext(),"Error in reception should be 16 numbers",Toast.LENGTH_SHORT).show();
                }else
                    visaNumber.setText(inputVisaNumber.getText().toString().replaceAll("(.{" + count + "})", "$1 ").trim());
                input1 = true;
            }
        });
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputDate.getText().toString().length() != 4){
                    Toast.makeText(getApplicationContext(),"Error in reception should be 4 numbers",Toast.LENGTH_SHORT).show();
                }else {
                    visaDate.setText(inputDate.getText().toString().replaceAll("(.{" + countDate + "})", "$1/").trim());
                    visaDate.setText(visaDate.getText().toString().substring(0,visaDate.length() -1));
                    input2 = true;
                }
            }
        });
        inputCardName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputCardName.getText().toString().contains(" ")){
                    Toast.makeText(getApplicationContext(),"you have to make a space between the name and the last name",Toast.LENGTH_SHORT).show();
                }else
                    VisaName.setText(inputCardName.getText().toString());
                input3 = true;
            }
        });
        inputCvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputCvv.getText().toString().length() != 3){
                    Toast.makeText(getApplicationContext(),"CVV is 3 digit number",Toast.LENGTH_SHORT).show();
                }else
                    textcvv.setText(inputCvv.getText().toString());
                input4 = true;
            }
        });
        // open dialog
        buttoncvvimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },3000);
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input1 && input2 && input3 && input4){
                    HashMap hashMap = new HashMap();
                    hashMap.put("quantity",quantitAfter);

                    rootdatabaseReference.child(nameM).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(visa.this, "Order placed", Toast.LENGTH_SHORT).show();

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(visa.this,"My Notification");
                            builder.setContentTitle("Reception");
                            builder.setContentText("Thanks for buying a camera " + nameM + " The price you bought the camera is " + priceM + " Reception number is " + random + "If there is any problem or delay you are welcome to contact us we will be happy to help");
                            builder.setSmallIcon(R.drawable.logo);
                            builder.setAutoCancel(true);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(visa.this);
                            managerCompat.notify(1,builder.build());
                            printPDF();
                        }
                    });
                }else{
                    Toast.makeText(visa.this, "finish the field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void printPDF() {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint forLinePaint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250,350,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0,50,250));

        canvas.drawText("Reception",20,20,paint);
        paint.setTextSize(8.5f);
        canvas.drawText("Reception number : " + random ,20,40,paint);
        canvas.drawText("Camera Store CM",20,55,paint);
        forLinePaint.setStyle(Paint.Style.STROKE);
        forLinePaint.setPathEffect(new DashPathEffect(new float[]{5,5}, 0));
        forLinePaint.setStrokeWidth(2);
        canvas.drawLine(20,65,230,65,forLinePaint);

        canvas.drawText("Customer Email: " + email ,20,80,paint);

        canvas.drawLine(20,90,230,90,forLinePaint);

        canvas.drawText("Purchase:",20,105,paint);
        canvas.drawText(nameM,20,135,paint);
        canvas.drawText("1 qty",120,135,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(priceM,230,135,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("+%",20,175,paint);
        canvas.drawText("Tax 0%",120,175,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("0.00",230,175,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(20,210,230,210,forLinePaint);

        paint.setTextSize(10f);
        canvas.drawText("Total",120,225,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(priceM,230,225,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(8.5f);
        canvas.drawText("Date "+datePatternFormat.format(new Date().getTime()),20,260,paint);
        canvas.drawText("Payment method Credit Card number : "+inputVisaNumber.getText().toString(),20,290,paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText("Thank you!",canvas.getWidth()/2,320,paint);

        myPdfDocument.finishPage(myPage);
        File file = new File(this.getExternalFilesDir("/"),"Reception.pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "The receipt was saved on your phone in a pdf file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();

    }

    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(this,MainStore.class);
        startActivity(intent3);
    }
}
