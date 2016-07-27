/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.checks.base;

import java.util.ArrayList;
import java.util.Set;

import org.drools.workbench.screens.guided.dtable.client.widget.analysis.CancellableRepeatingCommand;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.Status;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.ParameterizedCommand;

/**
 * This class handles processing the analysis itself. It supports "batched" processing
 * of different "chunks". State is a snapshot of the Checks at the time the RepeatingCommand
 * was instantiated.
 */
public class ChecksRepeatingCommand
        implements CancellableRepeatingCommand {

    private static final int BLOCK_SIZE = 50;

    private boolean isCancelled       = false;
    private int     currentStartIndex = 0;

    private ArrayList<Check> checksToRun = new ArrayList<Check>();

    private ParameterizedCommand<Status> onStatus;
    private Command                      onCompletion;

    public ChecksRepeatingCommand( final Set<Check> checksToRun,
                                   final ParameterizedCommand<Status> onStatus,
                                   final Command onCompletion ) {
        this.checksToRun.addAll( checksToRun );
        this.onStatus = onStatus;
        this.onCompletion = onCompletion;
    }

    @Override
    public boolean execute() {

        final int endIndex = Math.min( this.checksToRun.size(),
                                       currentStartIndex + BLOCK_SIZE );

        informAboutStatus( endIndex );

        for ( int index = this.currentStartIndex; index < endIndex; index++ ) {
            if ( isCancelled() ) {
                return false;
            }

            if ( isCancelled() ) {
                return false;
            }

            checksToRun.get( index ).check();
        }

        currentStartIndex += BLOCK_SIZE;

        if ( endIndex > checksToRun.size() - 1 ) {
            complete();
            return false;
        }

        return true;
    }

    private void informAboutStatus( final int endIndex ) {
        if ( onStatus != null ) {
            onStatus.execute( new Status( currentStartIndex,
                                          endIndex,
                                          checksToRun.size() ) );
        }
    }

    private boolean isCancelled() {
        if ( isCancelled ) {
            complete();
        }
        return isCancelled;
    }

    private void complete() {
        if ( onCompletion != null ) {
            onCompletion.execute();
        }
        checksToRun.clear();
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

}
