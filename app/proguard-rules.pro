#-keep class io.realm.annotations.RealmModule
#-keep @io.realm.annotations.RealmModule class *-keep class io.realm.internal.Keep
#-keep @io.realm.internal.Keep class *
#-keepnames class * extends io.realm.RealmObject
#-keep class * extends io.realm.RealmObject { *; }