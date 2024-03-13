package edu.java.client;

import edu.java.client.dto.LinkInfo;
import java.net.URL;

public interface ClientInfoProvider {
    LinkInfo fetchData(URL url);

    boolean isValidated(URL url);
}
