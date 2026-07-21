package com.rose.common.exception;

import com.rose.common.exception.donation.DonationAccessDeniedException;
import com.rose.common.exception.donation.SelfDonationNotAllowedException;
import com.rose.common.exception.donation.UserCannotReceiveDonationsException;
import com.rose.common.exception.payment.stripe.ConnectedStripeAccountRetrieveAccountException;
import com.rose.common.exception.payment.stripe.StripeIntegrationException;
import com.rose.common.exception.payment.stripe.StripeOnboardingLinkCreationException;
import com.rose.common.exception.payment.stripe.UserPaymentAccountNotFoundException;
import com.rose.common.exception.stripe.InvalidStripeSignatureException;
import com.rose.common.exception.user.EmailAlreadyExistsException;
import com.rose.common.exception.user.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException exception,
            HttpServletRequest request
    ) {
        log.warn("Email already exists: path={}, message={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(
            UsernameAlreadyExistsException exception,
            HttpServletRequest request
    ) {
        log.warn("Username already exists: path={}, message={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException exception,
            HttpServletRequest request
    ) {
        log.warn("Entity not found: path={}, message={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserPaymentAccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserPaymentAccountNotFoundException(
            UserPaymentAccountNotFoundException exception,
            HttpServletRequest request
    ) {
        log.warn("User payment account not found: path={}, message={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserCannotReceiveDonationsException.class)
    public ResponseEntity<ErrorResponse> handleUserCannotReceiveDonationsException(
            UserCannotReceiveDonationsException exception,
            HttpServletRequest request
    ) {
        log.warn("User cannot receive donations: path={}, message={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(StripeIntegrationException.class)
    public ResponseEntity<ErrorResponse> handleStripeIntegrationException(
            StripeIntegrationException exception,
            HttpServletRequest request
    ) {
        log.error("Stripe integration error: path={}, message={}", request.getRequestURI(), exception.getMessage(), exception);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while processing the payment. Please try again later.",
                request
        );
    }

    @ExceptionHandler(SelfDonationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleSelfDonationNotAllowedException(
            SelfDonationNotAllowedException exception,
            HttpServletRequest request
    ) {
        log.warn("Self donation not allowed: path={}, message={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(StripeOnboardingLinkCreationException.class)
    public ResponseEntity<ErrorResponse> handleStripeOnboardingLinkCreationException(
            StripeOnboardingLinkCreationException exception,
            HttpServletRequest request
    ) {
        log.error("Stripe onboarding link creation error: path={}, message={}", request.getRequestURI(), exception.getMessage(), exception);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while creating the Stripe onboarding link. Please try again later.",
                request
        );
    }

    @ExceptionHandler(ConnectedStripeAccountRetrieveAccountException.class)
    public ResponseEntity<ErrorResponse> handleConnectedStripeAccountRetriveAccountException(
            ConnectedStripeAccountRetrieveAccountException exception,
            HttpServletRequest request
    ) {
        log.error("Connected Stripe account retrieval error: path={}, message={}", request.getRequestURI(), exception.getMessage(), exception);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An error occurred while retrieving the connected Stripe account. Please try again later.",
                request
        );
    }

    @ExceptionHandler(InvalidStripeSignatureException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStripeSignatureException(
            InvalidStripeSignatureException exception,
            HttpServletRequest request
    ) {
        log.error("Invalid Stripe signature: path={}, message={}", request.getRequestURI(), exception.getMessage(), exception);
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid Stripe signature. Please check the webhook configuration.",
                request
        );
    }

    @ExceptionHandler(DonationAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleDonationAccessDeniedException(
            DonationAccessDeniedException exception,
            HttpServletRequest request
    ) {
        log.warn("Donation access denied: path={}, message={}", request.getRequestURI(), exception.getMessage());
        return buildResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                request
        );
    }


    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.of(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}