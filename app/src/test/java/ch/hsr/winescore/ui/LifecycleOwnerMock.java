package ch.hsr.winescore.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.annotation.NonNull;

public class LifecycleOwnerMock implements LifecycleOwner {

    final LifecycleRegistry registry = new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }
}
