package id.meyta.unify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by meyta on 2/22/18.
 */

public class ButtonActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ButtonActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
    }
}
