package com.evolveum.polygon.connector.racf.rest;

import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;

import java.util.ArrayList;
import java.util.List;

public class ListResultHandler implements ResultsHandler {

    private List<ConnectorObject> objects = new ArrayList<>();

    @Override
    public boolean handle(ConnectorObject object) {
        objects.add(object);

        return false;
    }

    public List<ConnectorObject> getObjects() {
        return objects;
    }
}
