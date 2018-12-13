package com.tokopedia.track;

// Treck.getInstance().registerImplementation("GTM", SuicideGTM.class)
// mapOf("MOENGAGE", listOf(MOENGAGESuicide.class, "isExistingUser")

// configuration
// enable or not
// Treck.getInstance().get("GTM").sendEvent(Map<String,Object>)
// Treck.getInstance().get("MOENGAGE").get("isExistingUser")
// Treck.getInstance().get("MOENGAGE").set("isExistingUser", Object)
// Treck.getInstance().create(MOENGAGESuicide.class).saveAndBuild("MOENGAGE");
// Treck.get("MOENGAGE").isExistingUser();

import android.content.Context;
import android.support.annotation.GuardedBy;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;

import com.tokopedia.track.components.ContextAnalytics;
import com.tokopedia.track.components.GTMSuicide;

import java.util.HashMap;
import java.util.Map;

/**
 * @Moengage
 * @Type("Moengage")
 * class MOENGAGESuicide{
 *
 *   public void isExistingUser(){}
 * }
 *
 @Type("GTM")
  * class SuicideGTM{
  *
  *   public void sendEvent(Map<String,Object>){}
  * }
 */
public class TrackApp {
    private static final Object LOCK = new Object();

    private static final Object LOCK2 = new Object();

    @GuardedBy("LOCK")
    static final Map<String, TypedValue<? extends ContextAnalytics>> INSTANCES = new ArrayMap<>();

    @GuardedBy("LOCK2")
    static TrackApp trackApp;

    private static final Class<?>[] CONTEXT_ANALYTICS_CONSTRUCTOR_SIGNATURE =
            new Class[]{Context.class};
    private Context context;

    /**
     *
     *
     * @param context Application Context
     */
    private TrackApp(Context context){
        this.context = context;
    }

    /**
     * remove when necessary
     */
    public TrackApp(){

    }

    public static TrackApp initTrackApp(Context context){
        synchronized (LOCK){
            Context context1;
            if(context.getApplicationContext()==null){
                context1 = context;
            }else{
                context1 = context.getApplicationContext();
            }

           if(trackApp==null){
               trackApp = new TrackApp(context);
           }
           return trackApp;
        }
    }

    public static void registerImplementation(String TAG, Class<? extends ContextAnalytics> className){
        // determine if trackapp is initialized or not ??
        // create class using reflection
        // register to map
    }

    public <T> T getValue(String TAG, Class<T> type){
        Map<String, TypedValue<? extends ContextAnalytics>>
                result = new HashMap<>();
        result.put("GTM", new TypedValue(GTMSuicide.class, new GTMSuicide()));
        ContextAnalytics typedValue = result.get(TAG).value;

        return  type.cast(typedValue);
    }


    class TypedValue<T> {
        public final Class<T> type;
        public final T value;
        TypedValue(Class<T> type, T value) {
            this.type = type;
            this.value = value;
        }
    }


}
