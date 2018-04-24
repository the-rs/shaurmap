#todo ensure it's ok
-dontobfuscate

-dontwarn durdinapps.rxfirebase2.**
-dontwarn com.bumptech.glide.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-dontwarn com.rightsoftware.shaurmap.data.**
-dontwarn org.androidannotations.api.**

#Toothpick
-keep class **$$Factory{ *; }
-adaptclassstrings **