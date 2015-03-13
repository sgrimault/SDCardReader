package sc.sn.android.commons;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.SdkConfig;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Properties;

/**
 * Custom implementation of {@code RobolectricTestRunner}.
 *
 * @author S. Grimault
 */
public class CustomRobolectricRunner
        extends RobolectricTestRunner {

    private static final String MODULE_NAME = "commons";

    public CustomRobolectricRunner(Class<?> testClass)
            throws
            InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        final StringBuilder androidManifestPathBuilder = new StringBuilder("src/main/AndroidManifest.xml");

        // Android Studio has a different execution root path for tests than pure gradle
        if (!(new File(androidManifestPathBuilder.toString())).exists()) {
            androidManifestPathBuilder.insert(
                    0,
                    MODULE_NAME + "/"
            );
        }

        config = updateConfig(
                config,
                "manifest",
                androidManifestPathBuilder.toString()
        );

        return super.getAppManifest(config);
    }

    @Override
    protected SdkConfig pickSdkVersion(
            AndroidManifest appManifest,
            Config config) {
        // Robolectric doesn't support the latest android SDK version
        config = updateConfig(
                config,
                "emulateSdk",
                "18"
        );

        return super.pickSdkVersion(
                appManifest,
                config
        );
    }

    private Config.Implementation updateConfig(
            Config config,
            String key,
            String value) {
        final Properties properties = new Properties();
        properties.setProperty(
                key,
                value
        );

        return new Config.Implementation(
                config,
                Config.Implementation.fromProperties(properties)
        );
    }
}
