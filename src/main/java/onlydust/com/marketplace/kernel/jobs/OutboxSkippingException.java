package onlydust.com.marketplace.kernel.jobs;

public class OutboxSkippingException extends RuntimeException {
    public OutboxSkippingException(final String message) {
        super(message);
    }

    private OutboxSkippingException(final Exception e) {
        super(e);
    }

    public static OutboxSkippingException of(Exception e) {
        return new OutboxSkippingException(e);
    }
}
