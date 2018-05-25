package ua.edu.ratos.service;

import lombok.NonNull;
import ua.edu.ratos.service.dto.BatchIn;
import ua.edu.ratos.service.dto.BatchOut;
import ua.edu.ratos.service.dto.Result;

/**
 * Generic batched session interface. Defines generic learning session operations in batches
 * @author Andrey P.
 */
public interface GenericSession {

    /**
     * Starts a new learning session
     * @param user
     * @param scheme
     * @return a newly generated key for im-memory storage
     */
    String start(String user, String scheme);

    /**
     * Provides a new batch of questions
     * @param batchIn batch with user's provided answers
     * @return next batchOut
     */
    BatchOut next(BatchIn batchIn);

    /**
     * Normal finish, provides results for session
     * @param batchIn last batch of questions
     * @return
     */
    Result finish(BatchIn batchIn);

    /**
     * Session cancelled by user, empty data
     * @param key
     */
    void cancel(String key);


    @Deprecated
    BatchOut proceed(@NonNull String key);


}
