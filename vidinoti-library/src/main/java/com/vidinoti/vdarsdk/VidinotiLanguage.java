package com.vidinoti.vdarsdk;

@SuppressWarnings("unused")
public enum VidinotiLanguage {
    EN, FR, DE, IT, ES;

    public String getTagName() {
        return "lang_" + toString().toLowerCase();
    }
}
