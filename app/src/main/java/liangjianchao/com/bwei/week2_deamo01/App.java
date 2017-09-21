package liangjianchao.com.bwei.week2_deamo01;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by lenovo、 on 2017/8/11.
 */
public class App extends Application {

    {
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    private DbManager dbManager;
    private  UMShareAPI umshareAPI;



    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

      DbManager.DaoConfig config = new DbManager.DaoConfig()
              .setDbName("User")
              .setDbVersion(1)
              .setAllowTransaction(true);
      dbManager =  x.getDb(config);

      umshareAPI =  UMShareAPI.get(this);

    }

    public UMShareAPI getUmshareAPI() {
        return umshareAPI;
    }

    public DbManager getDbManager() {
        return dbManager;
    }
}
