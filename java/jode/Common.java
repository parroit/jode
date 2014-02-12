package jode;

import jdk.nashorn.internal.objects.*;

public enum Common {
    INSTANCE;

    public Object wrapException(Throwable ex){
        return NativeError.constructor(true,null,ex.getClass().getSimpleName()+": "+ex.getMessage());
    }
}