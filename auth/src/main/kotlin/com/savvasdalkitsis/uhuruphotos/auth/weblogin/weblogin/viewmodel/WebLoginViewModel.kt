/*
Copyright 2022 Savvas Dalkitsis

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
package com.savvasdalkitsis.uhuruphotos.auth.weblogin.weblogin.viewmodel

import androidx.lifecycle.ViewModel
import com.savvasdalkitsis.uhuruphotos.auth.weblogin.weblogin.mvflow.WebLoginAction
import com.savvasdalkitsis.uhuruphotos.auth.weblogin.weblogin.mvflow.WebLoginEffect
import com.savvasdalkitsis.uhuruphotos.auth.weblogin.weblogin.mvflow.WebLoginMutation
import com.savvasdalkitsis.uhuruphotos.auth.weblogin.weblogin.view.WebLoginState
import com.savvasdalkitsis.uhuruphotos.viewmodel.ActionReceiver
import com.savvasdalkitsis.uhuruphotos.viewmodel.ActionReceiverHost
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebLoginViewModel @Inject constructor(
    handler: WebLoginHandler,
) : ViewModel(),
    ActionReceiverHost<WebLoginState, WebLoginEffect, WebLoginAction, WebLoginMutation> {

    override val actionReceiver = ActionReceiver(
        handler,
        webLoginReducer(),
        WebLoginState(""),
    )
}