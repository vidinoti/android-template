# Android Library for the Vidinoti SDK

## Installation

In your project's `build.gradle` file, add the following

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

And add the dependency in your app's `build.gradle` file:

```
implementation 'com.github.vidinoti:android-template:<latest-release>'
```

## Usage

``` java
import android.app.Application;

import com.vidinoti.vdarsdk.VidinotiAR;
import com.vidinoti.vdarsdk.VidinotiAROptions;
import com.vidinoti.vdarsdk.VidinotiLanguage;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String licenseKey = "Paste your V-Director License Key";
        VidinotiAROptions options = new VidinotiAROptions.Builder(licenseKey)
                .setCodeRecognitionEnabled(true)
                .setSynchronizationMode(VidinotiAROptions.SynchronizationMode.DEFAULT_TAG_WITH_ADDITIONAL_CONTENT)
                .setMultilingualEnabled(true)
                .setDefaultLanguage(VidinotiLanguage.EN)
                .setSupportedLanguages(VidinotiLanguage.EN, VidinotiLanguage.ES)
                .setDefaultTag("Default")
                .setNotificationSupport(true)
                .build();
        VidinotiAR.init(this, options);
    }
}

```
