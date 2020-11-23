package io.github.nosequel.core.util.synchronize.options;

import io.github.nosequel.core.util.database.options.DatabaseOption;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisDatabaseOption extends DatabaseOption {

    private final String hostname;
    private final int port;

    private final String username;
    private String password;
    private boolean authenticate;

    /**
     * Constructor for creating a new MongoDatabaseOption
     *
     * @param hostname               the ip address
     * @param username               the username
     * @param password               the passsword
     * @param port                   the port
     */
    public RedisDatabaseOption(String hostname, String username, String password, int port) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;

        if (!password.isEmpty()) {
            this.password = password;
            this.authenticate = true;
        }
    }

    @Override
    public Object[] getOptions() {
        return new Object[]{hostname, username, password, authenticate, port};
    }
}