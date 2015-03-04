package freemarker.core;

import freemarker.core.ThreadInterruptionSupportTemplatePostProcessor.TemplateProcessingThreadInterruptedException;
import freemarker.template.Template;

/**
 * Functions that depend on unpublished FreeMarker functionality. Might need to be adjusted for new FreeMarker releases.
 * The relevant parts of the FreeMarker source code contains comments about keeping this in sync with that, so,
 * hopefully this won't be a problem.
 */
public final class FreeMarkerInternalsAccessor {

    /**
     * Ensures that the template will react to {@link #interruptTemplateProcessing(Thread)}. 
     */
    public static void makeTemplateInterruptable(Template template) {
        _CoreAPI.addThreadInterruptedChecks(template);
    }

    /**
     * Checks if the template processing has thrown exception because of a {@link #interruptTemplateProcessing(Thread)}
     * call.
     */
    public static boolean isTemplateProcessingInterruptedException(Throwable e) {
        return e instanceof TemplateProcessingThreadInterruptedException;
    }

    /**
     * Tells a template processing in another thread to abort; asynchronous.
     */
    public static void interruptTemplateProcessing(Thread t) {
        t.interrupt();
    }

    /**
     * Called from the thread where the interruptible template execution ran earlier, to clear any related thread state.
     */
    public static void clearAnyPendingTemplateProcessingInterruption() {
        Thread.interrupted();  // To clears the interruption flag 
    }
    
    private FreeMarkerInternalsAccessor() {
        // Not meant to be instantiated
    }

}
