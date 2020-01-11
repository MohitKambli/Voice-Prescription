package com.example.speechtotext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;

public class PrescriptionForm extends AppCompatActivity {

    TextView medText, doseText, diagnosisText, prescriptionText, adviceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_form);

        medText = findViewById(R.id.medRes);
        doseText = findViewById(R.id.doseRes);
        diagnosisText = findViewById(R.id.diagnosisRes);
        prescriptionText = findViewById(R.id.prescriptionRes);
        adviceText = findViewById(R.id.adviceRes);

        medText.setText(MainActivity.name.get(0));
        doseText.setText(Dosage.symptoms.get(0));
        diagnosisText.setText(Diagnosis.diagnosis.get(0));
        prescriptionText.setText(Prescription.prescriptions.get(0));
        adviceText.setText(Advice.advices.get(0));

    }

    private void createUpdateFile(String fileName, boolean update) {
        FileOutputStream outputStream;

        try {
            if (update) {
                outputStream = openFileOutput(fileName, Context.MODE_APPEND);
            } else {
                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            }
            //outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makePDF(View view) {
        ActivityCompat.requestPermissions(PrescriptionForm.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Paint myPaint = new Paint();

        String myString = "Name : " + medText.getText().toString() + "\nSymptoms : " + doseText.getText().toString()
                + "\nDiagnosis : " + diagnosisText.getText().toString() + "\nPrescriptions : " + prescriptionText.getText().toString()
                + "\nAdvices : " + adviceText.getText().toString();
        int x = 10, y=25;
        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }
        myPdfDocument.finishPage(myPage);
        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/myPDFFile.pdf";
        File myFile = new File(myFilePath);
        //createUpdateFile("myPDFFile.pdf", true);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        myPdfDocument.close();
    }
}
