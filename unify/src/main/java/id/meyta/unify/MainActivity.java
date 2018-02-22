package id.meyta.unify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.design.button.Button;
//import com.tokopedia.design.menu.ItemMenus;
//import com.tokopedia.design.menu.Menus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_buttonaction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonActivity.start(MainActivity.this);
            }
        });


//        final Menus menus = new Menus(this);
//        menus.setItemMenuList(data());
//        menus.setActionText("Action");
//        menus.setOnActionClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Test Yey", Toast.LENGTH_SHORT).show();
//            }
//        });
//        menus.setOnItemMenuClickListener(new Menus.OnItemMenuClickListener() {
//            @Override
//            public void onClick(ItemMenus itemMenus, int pos) {
//                Toast.makeText(MainActivity.this, "Test " + itemMenus.title + pos, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

//    private List<ItemMenus> data() {
//        List<ItemMenus> itemMenus = new ArrayList<>();
//        itemMenus.add(new ItemMenus("First Section Goes Here", R.drawable.ic_search_icon));
//        itemMenus.add(new ItemMenus("Second Section Goes Here", R.drawable.ic_search_icon));
//        itemMenus.add(new ItemMenus("Third Section Goes Here", R.drawable.ic_search_icon));
//        return itemMenus;
//    }
}
