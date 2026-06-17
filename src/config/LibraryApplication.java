package config;

import org.glassfish.jersey.server.ResourceConfig;

public class LibraryApplication
        extends ResourceConfig {

    public LibraryApplication() {

        packages("resource");

        register(ObjectMapperProvider.class);

        register(new DependencyBinder());
    }
}
