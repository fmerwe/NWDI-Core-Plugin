/**
 *
 */
package org.arachna.netweaver.tools.dc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.arachna.netweaver.dc.types.DevelopmentComponent;
import org.arachna.netweaver.dc.types.DevelopmentConfiguration;
import org.arachna.netweaver.dc.types.JdkHomeAlias;

/**
 * Builder for 'builddc' commands for DC tool.
 * 
 * @author Dirk Weigenand
 */
final class BuildDevelopmentComponentsCommandBuilder extends AbstractDCToolCommandBuilder {
    /**
     * Template for generating dctool build commands.
     * 
     * @author Dirk Weigenand
     */
    private static enum BuildDevelopmentComponentsCommandTemplate {
        /**
         * template for NW 7.0 dctool build command.
         */
        V70("builddc -s %s -n %s -v %s -o;"),
        
	/**
	 * template for NW 7.1 dctool build command.
	 */
        V7x("builddc -s %s -n %s -v %s -o;"),

        /**
         * template for post NW 7.0 dctool build command.
         */
        V71("builddc -c %s -n %s -v %s");

        /**
         * mapping of {@link JdkHomeAlias}es to template matching the respective
         * NetWeaver version.
         */
        @SuppressWarnings("serial")
        private static final Map<JdkHomeAlias, BuildDevelopmentComponentsCommandTemplate> VALUES =
            new HashMap<JdkHomeAlias, BuildDevelopmentComponentsCommandTemplate>() {
                {
                    put(JdkHomeAlias.Jdk131Home, V70);
                    put(JdkHomeAlias.Jdk142Home, V70);
                    put(JdkHomeAlias.Jdk150Home, V7x);
                    put(JdkHomeAlias.Jdk160Home, V71);
                }
            };

        /**
         * Template for generating dctool build commands.
         */
        private final String buildCommandTemplate;

        /**
         * create new template instance.
         * 
         * @param buildCommandTemplate
         *            template to use.
         */
        private BuildDevelopmentComponentsCommandTemplate(final String buildCommandTemplate) {
            this.buildCommandTemplate = buildCommandTemplate;
        }

        /**
         * @return the buildCommandTemplate
         */
        String getBuildCommandTemplate() {
            return buildCommandTemplate;
        }

        /**
         * Create a template from the given {@link JdkHomeAlias}.
         * 
         * @param alias
         *            JDK home from which to map to template.
         * @return template for given alias.
         */
        static BuildDevelopmentComponentsCommandTemplate fromJdkHomeAlias(final JdkHomeAlias alias) {
            final BuildDevelopmentComponentsCommandTemplate template = VALUES.get(alias);

            if (template == null) {
                throw new IllegalStateException(String.format("Could not map JDK home alias %s to template!", alias));
            }

            return template;
        }
    }

    /**
     * list of development components to create build commands for.
     */
    private final List<DevelopmentComponent> components = new ArrayList<DevelopmentComponent>();

    /**
     * Template for generating dctool build commands.
     */
    private final BuildDevelopmentComponentsCommandTemplate template;

    /**
     * Creates a <code>DevelopmentComponentBuilder</code> instance for the given
     * development components.
     * 
     * @param config
     *            development configuration to use for executing dc tool
     *            commands.
     * @param components
     *            development components to create build dc commands for.
     */
    public BuildDevelopmentComponentsCommandBuilder(final DevelopmentConfiguration config,
        final Collection<DevelopmentComponent> components) {
        super(config);
        template = BuildDevelopmentComponentsCommandTemplate.fromJdkHomeAlias(config.getJdkHomeAlias());
        this.components.addAll(components);
    }

    /**
     * Create collection of builddc commands.
     * 
     * @return created list of 'builddc' commands.
     */
    @Override
    protected List<String> executeInternal() {
        final List<String> commands = new ArrayList<String>();

        for (final DevelopmentComponent component : components) {
            if (component.getCompartment() != null) {
                commands.add(createBuildDcCommand(component));
            }
            else {
                Logger.getLogger(getClass()).error(
                    String.format("%s/%s has no compartment set!", component.getVendor(), component.getName()));
            }
        }

        return commands;
    }

    /**
     * Create command for building a development component for the given DC.
     * 
     * @param component
     *            development component to create builddc command for.
     * @return the created builddc command
     */
    private String createBuildDcCommand(final DevelopmentComponent component) {
        return String.format(template.getBuildCommandTemplate(), component.getCompartment().getName(),
            component.getName(), component.getVendor());
    }
}