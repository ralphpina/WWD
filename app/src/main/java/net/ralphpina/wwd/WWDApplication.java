package net.ralphpina.wwd;

import android.app.Application;

import net.ralphpina.wwd.api.MixpanelClient;
import net.ralphpina.wwd.api.MixpanelClientImpl;

public class WWDApplication extends Application {

    private static WWDApplication mInstance;
    private        MixpanelClient mClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mClient = new MixpanelClientImpl();
    }

    public MixpanelClient getClient() {
        return mClient;
    }

    public static WWDApplication get() {
        return mInstance;
    }
}
