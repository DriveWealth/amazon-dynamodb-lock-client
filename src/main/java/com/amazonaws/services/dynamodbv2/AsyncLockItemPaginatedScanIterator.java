/**
 * Copyright 2013-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * <p>
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * <p>
 * http://aws.amazon.com/asl/
 * <p>
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, express
 * or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.amazonaws.services.dynamodbv2;

import java.util.Objects;

import reactor.core.publisher.Flux;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

/**
 * Lazy-loaded. Not immutable. Not thread safe.
 */
final class AsyncLockItemPaginatedScanIterator {
    private final DynamoDbAsyncClient dynamoDB;
    private volatile ScanRequest scanRequest;
    private final AsyncLockItemFactory lockItemFactory;
    
    
    
    AsyncLockItemPaginatedScanIterator(final DynamoDbAsyncClient dynamoDB, final ScanRequest scanRequest, final AsyncLockItemFactory lockItemFactory) {
        this.dynamoDB = Objects.requireNonNull(dynamoDB, "dynamoDB must not be null");
        this.scanRequest = Objects.requireNonNull(scanRequest, "scanRequest must not be null");
        this.lockItemFactory = Objects.requireNonNull(lockItemFactory, "lockItemFactory must not be null");          
    }
    
    public Flux<AsyncLockItem> go() {
    	return Flux.from(dynamoDB.scanPaginator(scanRequest))
    		.flatMapIterable(r -> r.items())
    		.map(item -> lockItemFactory.create(item));
    	
    }
}
