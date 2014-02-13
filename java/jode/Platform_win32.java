package jode;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import jdk.nashorn.internal.objects.*;
import com.sun.jna.platform.win32.Kernel32;

public enum Platform_win32 {
    INSTANCE;

    public int pid(){
        return Kernel32.INSTANCE.GetCurrentProcessId(); 
    }
}