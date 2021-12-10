# Android Library for the Vidinoti SDK

[![Release](https://jitpack.io/v/vidinoti/android-template.svg)](https://jitpack.io/#vidinoti/android-template)

## Installation

In your project's `build.gradle` file, add the following

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

And add the dependency in your app's `build.gradle` file:

```gradle
implementation 'com.github.vidinoti:android-template:<latest-release>'
```

## New project

* Create a new Android application
* Create a main Activity which extends `VidinotiBaseActivity`

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
        // You can call .synchronize() here if wanted.
    }
}

```

Then add it to your `AndroidManifest.xml`

``` xml
<application
    android:name=".MyApplication"
...
```

To handle the device orientation change, add

``` xml
android:configChanges="orientation|screenSize|keyboardHidden"
```

to your Activity in the `AndroidManifest.xml` file.

It might also be a good idea to set the launchMode to "singleTask".

``` xml
android:launchMode="singleTask"
```

### Locale

It is a good practice to enable only the locale corresponding to your application.
Edit the `app/build.gradle` and add the `resConfigs` attribute. For instance, like that:

```gradle
android { 
    ...
    defaultConfig {
        ...
        resConfigs 'en', 'es'
    }
}
```

### Push notification support

To support push notifications, create a new firebase project and configure the Android app: https://console.firebase.google.com/u/0/

Configure the push in [V-Director](https://armanager.vidinoti.com) too.

### Use the drawer layout template

See the `app` folder for an example. In summary, the steps are:

* Edit your main activity and make it extend the class `ScannerDrawerActivity`
* Override the method `public int getNavigationMenuId()` and return the id for your drawer menu.
* Override the method `public Map<Integer, DrawerEntry> getDrawerEntries()`. It allows configuring the default menu entry behaviors.
* Use the theme `android:theme="@style/VidinotiAppTheme.NoActionBar"` for your Activity
* Edit the following colors:
  * `vidinotiColorPrimary`
  * `vidinotiColorPrimaryDark`
  * `vidinotiColorPrimaryLight`
  * `vidinotiColorAccent`
* You can set the string `vidinoti_nav_header_subtitle` for the drawer subtitle.

## Development

### Create a new release

1. Update the version number in `vidinoti-library/build.gradle`.
2. Commit and push the changes.
3. Create a new release in GitHub. (see below for doing it with the CLI)

### Create a GitHub release with GitHub CLI

``` sh
gh release create <tag> --notes "<release notes>"
# For instance
gh release create 1.13.0 --notes "Vidinoti SDK 7.3.3"
```
