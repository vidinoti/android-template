package com.vidinoti.vdarsdk;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class VidinotiAROptions {

    public enum SynchronizationMode {
        NO_TAG,
        DEFAULT_TAG,
        DEFAULT_TAG_WITH_ADDITIONAL_CONTENT,
        LANGUAGE_ONLY
    }

    private String licenseKey;
    private boolean codeRecognitionEnabled;

    /**
     * If true, open the URL when scanning a QR code containing a URL.
     */
    private boolean codeRecognitionOpenURL;

    private boolean multilingualEnabled;
    private VidinotiLanguage defaultLanguage;
    private Set<VidinotiLanguage> supportedLanguages;
    private String defaultTag;
    private SynchronizationMode synchronizationMode;
    private String customApiServer;
    private String customSdkServer;

    @SuppressWarnings("unused")
    public static class Builder {

        private final String licenseKey;
        private boolean codeRecognitionEnabled = true;
        private boolean codeRecognitionOpenURL = true;
        private SynchronizationMode synchronizationMode = SynchronizationMode.NO_TAG;
        private boolean multilingualEnabled = false;
        private VidinotiLanguage defaultLanguage = VidinotiLanguage.EN;
        private Set<VidinotiLanguage> supportedLanguages = Collections.emptySet();
        private String defaultTag = null;
        private String customApiServer = null;
        private String customSdkServer = null;

        public Builder(String licenseKey) {
            this.licenseKey = licenseKey;
        }

        public Builder setSynchronizationMode(SynchronizationMode synchronizationMode) {
            this.synchronizationMode = synchronizationMode;
            return this;
        }

        public Builder setDefaultLanguage(VidinotiLanguage defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
            return this;
        }

        public Builder setDefaultTag(String defaultTag) {
            this.defaultTag = defaultTag;
            return this;
        }

        public Builder setCodeRecognitionEnabled(boolean codeRecognitionEnabled) {
            this.codeRecognitionEnabled = codeRecognitionEnabled;
            return this;
        }

        public Builder setCodeRecognitionOpenURL(boolean codeRecognitionOpenURL) {
            this.codeRecognitionOpenURL = codeRecognitionOpenURL;
            return this;
        }

        public Builder setMultilingualEnabled(boolean multilingualEnabled) {
            this.multilingualEnabled = multilingualEnabled;
            return this;
        }

        public Builder setSupportedLanguages(VidinotiLanguage... supportedLanguages) {
            Set<VidinotiLanguage> set = new HashSet<>();
            Collections.addAll(set, supportedLanguages);
            this.supportedLanguages = set;
            return this;
        }

        public Builder setCustomApiServer(String url) {
            this.customApiServer = url;
            return this;
        }

        public Builder setCustomSdkServer(String url) {
            this.customSdkServer = url;
            return this;
        }

        public VidinotiAROptions build() {
            VidinotiAROptions options = new VidinotiAROptions();
            options.licenseKey = this.licenseKey;
            options.codeRecognitionEnabled = this.codeRecognitionEnabled;
            options.codeRecognitionOpenURL = this.codeRecognitionOpenURL;
            options.defaultLanguage = this.defaultLanguage;
            options.defaultTag = this.defaultTag;
            options.multilingualEnabled = this.multilingualEnabled;
            options.supportedLanguages = this.supportedLanguages;
            options.synchronizationMode = this.synchronizationMode;
            options.customApiServer = this.customApiServer;
            options.customSdkServer = this.customSdkServer;
            return options;
        }
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public boolean isCodeRecognitionEnabled() {
        return codeRecognitionEnabled;
    }

    public boolean isCodeRecognitionOpenURL() {
        return codeRecognitionOpenURL;
    }

    public SynchronizationMode getSynchronizationMode() {
        return synchronizationMode;
    }

    public boolean isMultilingualEnabled() {
        return multilingualEnabled;
    }

    public String getDefaultTag() {
        return defaultTag;
    }

    public Set<VidinotiLanguage> getSupportedLanguages() {
        return supportedLanguages;
    }

    public VidinotiLanguage getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getCustomApiServer() {
        return customApiServer;
    }

    public String getCustomSdkServer() {
        return customSdkServer;
    }
}
