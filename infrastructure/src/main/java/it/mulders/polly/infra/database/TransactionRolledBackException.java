package it.mulders.polly.infra.database;

/**
 * Exception to signal a database transaction was rolled back completely or partially.
 */
public class TransactionRolledBackException extends Exception {}
