/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author kenshin
 */
package org.foxbpm.engine.impl.task.cmd;

import org.foxbpm.engine.impl.entity.TaskEntity;
import org.foxbpm.engine.impl.identity.Authentication;
import org.foxbpm.engine.impl.interceptor.CommandContext;
import org.foxbpm.engine.impl.task.command.ReleaseTaskCommand;
import org.foxbpm.engine.impl.util.ExceptionUtil;

public class ReleaseTaskCmd extends AbstractExpandTaskCmd<ReleaseTaskCommand, Void>{

	private static final long serialVersionUID = 1L;

	public ReleaseTaskCmd(ReleaseTaskCommand abstractCustomExpandTaskCommand) {
		super(abstractCustomExpandTaskCommand);
	}

	 
	protected Void execute(CommandContext commandContext, TaskEntity task) {
		
		if (task.getAssignee() != null) {
			if (!task.getAssignee().equals(Authentication.getAuthenticatedUserId())) {
				// 当任务已经被另一个不是自己的用户占有，则抛出异常。
				throw ExceptionUtil.getException("10503004",taskId);
			}
			else{
				if(task.getIdentityLinks().size()>0){
					task.setAssignee(null);
					task.setClaimTime(null);
				}
				else{
					throw ExceptionUtil.getException("10503005",taskId);
				}
			}
		} else {
			throw ExceptionUtil.getException("10503006",taskId);
		}
		return null;
	}

}
