/*
 * Copyright (c) 2021. Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.appformer.kogito.bridge.client.ls.dmn;

import java.util.List;

import jsinterop.annotations.JsType;
import org.uberfire.workbench.model.bridge.LanguageServiceResult;

@JsType(isNative = true)
public interface DMNLanguageServiceApi {

    List<LanguageServiceResult> getClasses(String completion);

    List<LanguageServiceResult> getMethods(String fqdn, String completion);

    List<LanguageServiceResult> getAttributes(String fqdn, String completion);
}