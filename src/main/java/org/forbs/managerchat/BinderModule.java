package org.forbs.managerchat;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.forbs.managerchat.config.Configuration;
import org.forbs.managerchat.config.ConfigurationInterface;

public class BinderModule extends AbstractModule {

    private final ManagerChat plugin;

    public BinderModule(ManagerChat plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(ManagerChat.class).toInstance(this.plugin);
        this.bind(ConfigurationInterface.class).to(Configuration.class);
    }
}
