package my.test.com.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class DrugActivity extends AppCompatActivity {

    private Button id_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug);
        id_btn = (Button) findViewById(R.id.id_btn);
        id_btn.setOnTouchListener(new View.OnTouchListener() {
            private int lastX, lastY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();  // 获取手指在Button上的位置。
                int y = (int) motionEvent.getY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;           // 保存手指按下时的位置。
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offsetX = x - lastX;
                        int offsetY = y - lastY;
                        // 调用layout方法更新View的位置。
                        id_btn.layout(id_btn.getLeft() + offsetX, id_btn.getTop() + offsetY,
                                id_btn.getRight() + offsetX, id_btn.getBottom() + offsetY);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
