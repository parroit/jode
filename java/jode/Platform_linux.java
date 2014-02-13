package jode;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

interface CLibrary extends Library {
    CLibrary INSTANCE = (CLibrary) Native.loadLibrary("c", CLibrary.class);   
    int getpid ();
}

public enum Platform_linux {
    INSTANCE;

    public int pid(){
        return CLibrary.INSTANCE.getpid();
    }
}