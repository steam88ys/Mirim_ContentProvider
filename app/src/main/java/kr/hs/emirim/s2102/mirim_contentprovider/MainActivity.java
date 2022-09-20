package kr.hs.emirim.s2102.mirim_contentprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {
    EditText editWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnRead = findViewById(R.id.btn_read);
        EditText editWrite = findViewById(R.id.edit_write);
        if( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE}, 100);
        }
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editWrite.setText(getCallString());
            }
        });
    }

    public String getCallString() {
        String[] callSet = {CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER,
                            CallLog.Calls.DURATION};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet,
                                    null, null, null);
        if(c == null)
            return "통화기록 없음";
        
        StringBuffer callStr = new StringBuffer();
        callStr.append("\n날짜: 유형: 전화번호: 통화시간: \n\n");

        while(c.moveToNext()) {
            long date = c.getLong(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sdf.format(date);
            callStr.append(dateStr + ": ");
            if (c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
                callStr.append("착신: ");
            else
                callStr.append("발신: ");
            callStr.append(c.getString(2) + ": ");
            callStr.append(c.getString(3) + "초\n");
        }

        c.close();
        return callStr.toString();
    }

}