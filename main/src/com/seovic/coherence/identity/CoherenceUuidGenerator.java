/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.coherence.identity;


import com.seovic.identity.IdentityGenerator;

import com.tangosol.util.UUID;
import java.io.Serializable;


/**
 * UUID-based {@link IdentityGenerator} implementation.
 *
 * @author Aleksandar Seovic  2009.05.27
 */
public class CoherenceUuidGenerator
        implements IdentityGenerator<UUID>, Serializable
    {
    /**
     * Generates a UUID-based identity.
     *
     * @return a new UUID value
     */
    public UUID generateIdentity()
        {
        return new UUID();
        }
    }
