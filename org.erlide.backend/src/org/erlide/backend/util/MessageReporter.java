package org.erlide.backend.util;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.erlide.backend.BackendPlugin;

import com.google.common.collect.Lists;

public abstract class MessageReporter {
    public enum ReporterPosition {
        CENTER, CORNER, MODAL
    }

    public void displayMessage(final String message) {
        displayMessage(message, ReporterPosition.CORNER);
    }

    abstract public void displayMessage(String message, ReporterPosition style);

    public static void show(final String message) {
        show(message, ReporterPosition.CORNER);
    }

    public static void show(final String message, final ReporterPosition style) {
        final List<MessageReporter> reporters = getAllImplementors();
        for (final MessageReporter reporter : reporters) {
            reporter.displayMessage(message, style);
        }
        System.out.println("MSG::: " + message);
    }

    private static List<MessageReporter> getAllImplementors() {
        final List<MessageReporter> result = Lists.newArrayList();
        final IConfigurationElement[] elements = BackendPlugin
                .getMessageReporterConfigurationElements();
        for (final IConfigurationElement element : elements) {
            try {
                final MessageReporter provider = (MessageReporter) element
                        .createExecutableExtension("class");
                result.add(provider);
            } catch (final CoreException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
