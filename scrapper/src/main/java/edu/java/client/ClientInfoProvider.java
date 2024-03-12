package edu.java.client;

import edu.java.client.dto.LinkInfo;
import java.net.URI;

public interface ClientInfoProvider {
    LinkInfo fetchData(URI url);

    boolean isValidated(URI url);
}
