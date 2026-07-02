package src.service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import src.model.AuditEvent;


public class AuditLogger {
    private  final List<AuditEvent> history = new CopyOnWriteArrayList<>();

    public void log(AuditEvent event) {
        history.add(event);
        System.out.println(event);
    }
}
