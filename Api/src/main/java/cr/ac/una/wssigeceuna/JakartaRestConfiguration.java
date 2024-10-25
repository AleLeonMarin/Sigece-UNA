package cr.ac.una.wssigeceuna;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.glassfish.jersey.server.ResourceConfig;
import cr.ac.una.wssigeceuna.controller.JsonbContextResolver;
import jakarta.ws.rs.ApplicationPath;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * 
 * @author Juneau
 */
@ApplicationPath("ws")
public class JakartaRestConfiguration extends ResourceConfig {

    public JakartaRestConfiguration() {
        super();
        packages("cr.ac.una.wssigeceuna.controller");
        packages("cr.ac.una.wssigeceuna.controller", "io.swagger.v3.jaxrs2.integration.resources");
        registerCustomProviders();

    }

    private void registerCustomProviders() {
        Set<String> providerClassNames = new HashSet() {
            {
                add(JsonbContextResolver.class.getName());
                // add(ConstraintViolationExceptionMapper.class.getName());
                // add(SecurityFilter.class.getName());
            }
        };

        Map<String, Object> properties = new HashMap() {
            {
                put("jersey.config.server.provider.classnames",
                        providerClassNames.stream().collect(Collectors.joining(",")));
            }
        };

        addProperties(Collections.unmodifiableMap(properties));
    }

}
