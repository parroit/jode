package jode;

public interface CallbackInvoker {
    Object invoke(Runnable cb);
}