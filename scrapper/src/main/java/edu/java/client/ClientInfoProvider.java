package edu.java.client;

import edu.java.client.dto.LinkInfo;
import java.net.URI;
import java.util.List;

public interface ClientInfoProvider {
    List<LinkInfo> fetchData(URI url);

    boolean isValidated(URI url);
}
