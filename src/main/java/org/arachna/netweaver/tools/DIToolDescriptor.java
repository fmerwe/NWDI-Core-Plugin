package org.arachna.netweaver.tools;

import org.arachna.netweaver.dc.types.JdkHomeAlias;
import org.arachna.netweaver.dc.types.JdkHomePaths;

/**
 * Container for parameters relevant to DC tool execution.
 * 
 * @author Dirk Weigenand
 */
public final class DIToolDescriptor {
    /**
     * UME user for authentication against the NWDI.
     */
    private final String user;

    /**
     * password to use authenticating the user.
     */
    private final String password;

    /**
     * path to NWDI tool library folder.
     */
    private final String nwdiToolLibrary;

    /**
     * configured JDKs.
     */
    private final JdkHomePaths paths;

    /**
     * URL to CBS.
     */
    private final String cbsUrl;

    /**
     * Create an instance of a <code>DCToolDescriptor</code>.
     * 
     * @param user
     *            UME user for authentication against the NWDI.
     * @param password
     *            password to use authenticating the user.
     * @param nwdiToolLibrary
     *            path to NWDI tool library folder.
     * @param cbsUrl
     *            URL to CBS.
     * @param paths
     *            configured JDKs.
     */
    public DIToolDescriptor(final String user, final String password, final String nwdiToolLibrary, final String cbsUrl,
        final JdkHomePaths paths) {
        super();
        this.user = user;
        this.password = password;
        this.nwdiToolLibrary = nwdiToolLibrary;
        this.cbsUrl = cbsUrl;
        this.paths = paths;
    }

    /**
     * Return the NWDI user.
     * 
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Return the password for the NWDI user.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Path to the NWDI tools library folder (folder where the di_tools were extracted to).
     * 
     * @return the nwdiToolLibrary
     */
    public String getNwdiToolLibrary() {
        return nwdiToolLibrary;
    }

    /**
     * Return the JavaHome for the given {@link JdkHomeAlias}.
     * 
     * @param alias
     *            the <code>JdkHomeAlias</code> the JavaHome folder shall be returned for.
     * @return the registered JavaHome folder or the value of the system property <code>java.home</code> if no alias was registered.
     */
    String getJavaHome(final JdkHomeAlias alias) {
        return paths.get(alias);
    }

    /**
     * @return the cbsUrl
     */
    public String getCbsUrl() {
        return cbsUrl;
    }
}
