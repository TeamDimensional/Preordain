package com.teamdimensional.preordain.library;

import java.util.function.Supplier;

public class RevertibleRegistry<T> {
    T old;
    T current;
    Supplier<T> supplier;

    public RevertibleRegistry(Supplier<T> supplier) {
        this.supplier = supplier;
        current = supplier.get();
    }

    public void beginTransaction() {
        old = current;
        current = supplier.get();
    }

    public void undo() {
        current = old;
        old = null;
    }

    public T get() {
        return current;
    }

}
