/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.scenes.adminScenes.AdminBoardCtrl;
import client.scenes.adminScenes.AdminLoginCtrl;
import client.utils.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;


public class MyModule implements Module {

    @Override
    public void configure(final Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(TaskListUtils.class).in(Scopes.SINGLETON);
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(WebSocketUtils.class).in(Scopes.SINGLETON);
        binder.bind(CustomAlert.class).in(Scopes.SINGLETON);
        binder.bind(EditBoardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminBoardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminLoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardUtils.class).in(Scopes.SINGLETON);
        binder.bind(NetworkUtils.class).in(Scopes.SINGLETON);
    }
}