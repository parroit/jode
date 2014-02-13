package jode;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import jdk.nashorn.internal.objects.*;

public enum PlatformCommon {
    INSTANCE;
    public String[] args;

    public Object wrapException(Throwable ex){
        return NativeError.constructor(true,null,ex.getClass().getSimpleName()+": "+ex.getMessage());
    }

}